package com.daansander.game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.daansander.game.graphics.Screen;
import com.daansander.game.input.KeyBoard;
import com.daansander.game.input.Mouse;
import com.daansander.game.net.GameClient;
import com.daansander.game.net.GameServer;
import com.daansander.game.net.packet.Packet00Login;
import com.daansander.game.net.packet.Packet02PlatformMove;
import com.daansander.game.player.Player;
import com.daansander.game.tile.PlatformTile;
import com.daansander.game.tile.Tile;

public class Game extends Canvas implements Runnable {

	public static final int WIDTH = 200;
	public static final int HEIGHT = WIDTH / 4 * 3;
	public static final int SCALE = 3;
	public static final String NAME = "Game";

	public static Game game;

	private static final long serialVersionUID = 1L;

	private JFrame frame;
	private Screen screen;
	private Mouse mouse;
	private KeyBoard keyBoard;
	public GameClient socketClient;
	public GameServer socketServer;
	public Player player;
	public List<Player> players = new ArrayList<>();

	private static int playerScore = 0;
	private static int AI = 0;

	private boolean running = false;
	public volatile boolean hosting = false;
	public volatile boolean server = false;

	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	public Game() {
		game = this;
		Dimension dimension = new Dimension(SCALE * WIDTH, SCALE * HEIGHT);

		setMinimumSize(dimension);
		setPreferredSize(dimension);
		setMaximumSize(dimension);

		frame = new JFrame();
		screen = new Screen(WIDTH, HEIGHT);
		mouse = new Mouse(this);
		keyBoard = new KeyBoard(this);

		frame.setResizable(false);
		frame.setTitle(NAME);
		frame.add(this);

		frame.pack();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);

		frame.setVisible(true);

		requestFocus();

		start();
	}

	public synchronized void init() {
		hosting = JOptionPane.showConfirmDialog(this, "Do want to run a server") == 0;

		if (hosting) {
			socketServer = new GameServer(this);
			socketServer.start();
		}

		if (!hosting) {
			if (JOptionPane.showConfirmDialog(this, "Do you want to join a server?") == 0) {
				Tile.init(true);

				player = new Player(Tile.AIPlatform, "alt");

//				socketClient = new GameClient(this, "*********");
//				socketClient = new GameClient(this, "localhost");
				socketClient = new GameClient(this, "*********");
				socketClient.start();

				Packet00Login packet = new Packet00Login(player.getUsername());
				packet.writeData(socketClient);

				players.add(player);

				server = true;
			} else {
				Tile.init(false);

				player = new Player(Tile.playerPlatform, "player");

				server = false;
			}
		} else {
			socketClient = new GameClient(this, "localhost");
			socketClient.start();

			socketClient.sendData("ping".getBytes());

			Tile.init(true);
			player = new Player(Tile.playerPlatform, "player");

			Packet00Login packet = new Packet00Login(player.getUsername());
			packet.writeData(socketClient);

			players.add(player);

			server = true;
		}

	}

	public synchronized void start() {
		init();

		running = true;
		new Thread(this, "Game").start();
	}

	public synchronized void stop() {
		running = false;
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 50.0;

		double delta = 0;

		int frames = 0;
		int ticks = 0;

		while (running) {
			long currentTime = System.nanoTime();

			delta += (currentTime - lastTime) / ns;
			lastTime = currentTime;

			if (delta >= 1) {
				tick();

				ticks++;
				delta--;
			}

			render();
			frames++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;

				frame.setTitle(NAME + ": fps " + frames + " , tps " + ticks);

				frames = 0;
				ticks = 0;
			}
		}
	}

	private void tick() {
		int speed = 2;

		Tile.tickTiles();

		if (server) {
			if (keyBoard.isDown(KeyEvent.VK_UP)) {
				Packet02PlatformMove packet = new Packet02PlatformMove(player.getUsername(), -1 * speed);
				packet.writeData(socketClient);
			}
			if (keyBoard.isDown(KeyEvent.VK_DOWN)) {
				Packet02PlatformMove packet = new Packet02PlatformMove(player.getUsername(), speed);
				packet.writeData(socketClient);
			}
		} else {
			if (keyBoard.isDown(KeyEvent.VK_UP)) {
				 player.move(-1 * speed);
			}
			if (keyBoard.isDown(KeyEvent.VK_DOWN)) {
				 player.move(speed);
			}
		}

	}

	private void render() {
		BufferStrategy bf = getBufferStrategy();

		if (bf == null) {
			createBufferStrategy(3);
			return;
		}

		Graphics graphics = bf.getDrawGraphics();

		screen.clear();

		Tile.renderTiles(screen);
		screen.render();

		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}

		graphics.drawImage(image, 0, 0, getWidth(), getHeight(), null);

		graphics.setColor(Color.WHITE);
		graphics.setFont(getFont().deriveFont(20.0F));
		graphics.drawString(AI + ":" + playerScore, WIDTH / 2, 20);

		graphics.dispose();
		bf.show();
	}

	public void reset() {
		playerScore = 0;
		AI = 0;
		Tile.resetTiles();
	}

	public void movePlayer(String platform, int y) {
		if (platform.equalsIgnoreCase("player")) {
			Tile.playerPlatform.move(y);
		} else if (platform.equalsIgnoreCase("alt")) {
			Tile.AIPlatform.move(y);
		}
	}

	public PlatformTile getTile(String platform) {
		if (platform.equalsIgnoreCase("player"))
			return Tile.playerPlatform;
		else if (platform.equalsIgnoreCase("alt"))
			return Tile.AIPlatform;
		return null;
	}

	public static void updateScore(int ai, int pl) {
		AI = AI + ai;
		playerScore = playerScore + pl;
	}

	public static void main(String[] args) {
		new Game();
	}
}