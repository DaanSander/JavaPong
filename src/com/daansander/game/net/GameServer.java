package com.daansander.game.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import com.daansander.game.Game;
import com.daansander.game.net.packet.Packet;
import com.daansander.game.net.packet.Packet.PacketType;
import com.daansander.game.net.packet.Packet00Login;
import com.daansander.game.net.packet.Packet01Disconnect;
import com.daansander.game.net.packet.Packet02PlatformMove;
import com.daansander.game.net.packet.Packet03BallChangeDirection;
import com.daansander.game.net.packet.Packet04BallMove;
import com.daansander.game.player.PlayerMP;
import com.daansander.game.tile.Tile;

public class GameServer extends Thread {

	private List<PlayerMP> connectedPlayers = new ArrayList<>();
	private DatagramSocket socket;
	private Game game;

	public GameServer(Game game) {
		this.game = game;
		try {
			this.socket = new DatagramSocket(25565);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			// String message = new String(packet.getData());
			// message = message.trim();
			// System.out.println("CLIENT [" +
			// packet.getAddress().getHostAddress() + ":" + packet.getPort() +
			// "]> " + message);
			//
			// if (message.equalsIgnoreCase("ping")) {
			// System.out.println("Returning pong");
			// sendData("pong".getBytes(), packet.getAddress(),
			// packet.getPort());
			// }
			parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
		}
	}

	private void parsePacket(byte[] data, InetAddress address, int port) {
		String message = new String(data).trim();
		PacketType type = Packet.lookupPacket(message.substring(0, 2));
		Packet packet = null;
		switch (type) {
		case INVALID:
			break;
		case LOGIN:
			packet = new Packet00Login(data);
			System.out.println("[" + address.getHostAddress() + ":" + port + "] " + ((Packet00Login) packet).getUsername().trim() + " has connected");
			PlayerMP player = new PlayerMP(game.getTile(((Packet00Login) packet).getUsername().trim()), ((Packet00Login) packet).getUsername(), address, port);
			this.addConnection(player, (Packet00Login) packet);
			// Tile.
			break;
		case DISCONNECT:
			packet = new Packet01Disconnect(data);
			PlayerMP pl = getConnectedPlayer(((Packet01Disconnect) packet).getUsername());
			// this.removeConnection(pl, (Player01Disconnect)packet);/
			this.removeConnection(pl, (Packet01Disconnect) packet);
			break;
		case PLATFORM_MOVE:
			packet = new Packet02PlatformMove(data);
			this.handleMove((Packet02PlatformMove) packet);
			break;
		case BALL_CHANGE_DIRECTION:
			packet = new Packet03BallChangeDirection(data);
			handleBall((Packet03BallChangeDirection) packet);
			break;
		case BALL_MOVE:
			packet = new Packet04BallMove(data);
			handleBallPosistion((Packet04BallMove) packet);
			break;
		}
	}

	public void sendToAllClients(byte[] data) {
		for (PlayerMP player : connectedPlayers) {
			sendData(data, player.ipAddress, player.port);
		}
	}

	public void addConnection(PlayerMP player, Packet00Login packet) {
		// TODO decide to send packet to player himself
		packet.writeData(this);
		this.connectedPlayers.add(player);
	}

	public void removeConnection(PlayerMP player, Packet01Disconnect packet) {
		// TODO decide to send packet to player himself
		packet.writeData(this);
		this.connectedPlayers.remove(player);

	}

	public PlayerMP getConnectedPlayer(String name) {
		for (PlayerMP player : connectedPlayers) {
			if (player.getUsername().equals(name)) {
				return player;
			}
		}
		return null;
	}

	public void sendData(byte[] data, InetAddress ipAddress, int port) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
		try {
			this.socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void handleBall(Packet03BallChangeDirection packet) {
		packet.writeData(this);
	}

	public void handleBallPosistion(Packet04BallMove packet) {
		packet.writeData(this);
	}

	public void handleMove(Packet02PlatformMove packet) {
		PlayerMP player = getConnectedPlayer(packet.getUsername());
		if (player != null) {
			// player.getTile().getPosistion().add(0, packet.getY());
			packet.writeData(this);
		}
	}
}