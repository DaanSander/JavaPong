package com.daansander.game.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.daansander.game.Game;
import com.daansander.game.net.packet.Packet;
import com.daansander.game.net.packet.Packet.PacketType;
import com.daansander.game.net.packet.Packet00Login;
import com.daansander.game.net.packet.Packet01Disconnect;
import com.daansander.game.net.packet.Packet02PlatformMove;
import com.daansander.game.net.packet.Packet03BallChangeDirection;
import com.daansander.game.net.packet.Packet04BallMove;
import com.daansander.game.tile.PlatformTile;
import com.daansander.game.tile.Tile;

public class GameClient extends Thread {

	private InetAddress ipAddress;
	private DatagramSocket socket;
	private Game game;

	public GameClient(Game game, String ipAddress) {
		this.game = game;
		try {
			this.socket = new DatagramSocket();
			this.ipAddress = InetAddress.getByName(ipAddress);
		} catch (UnknownHostException e) {
			e.printStackTrace();
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
			//
			// System.out.println("SERVER > " + new
			// String(packet.getData()).trim());
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
			handleLogin((Packet00Login) packet, address, port);
			break;
		case DISCONNECT:
			packet = new Packet01Disconnect(data);
			handleDisconnect((Packet01Disconnect) packet, address, port);
			break;
		case PLATFORM_MOVE:
			packet = new Packet02PlatformMove(data);
			handleMove((Packet02PlatformMove) packet);
			break;
		case BALL_CHANGE_DIRECTION:
			packet = new Packet03BallChangeDirection(data);
			handleBall((Packet03BallChangeDirection) packet);
		case BALL_MOVE:
			packet = new Packet04BallMove(data);
			handleBallPosistion((Packet04BallMove) packet);
		}
	}

	private void handleDisconnect(Packet01Disconnect packet, InetAddress address, int port) {
		System.out.println("[" + address.getHostAddress() + ":" + port + "] " + packet.getUsername() + " disconnected from the server");
	}

	private void handleLogin(Packet00Login packet, InetAddress address, int port) {
		System.out.println("[" + address.getHostAddress() + ":" + port + "] " + packet.getUsername().trim() + " has joined the game");
		game.reset();
	}

	public void sendData(byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 25565);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void handleMove(Packet02PlatformMove packet) {
//		Tile.AIPlatform.move(packet.getY());
		game.movePlayer(packet.getUsername(), packet.getY());
	}
	
	private void handleBall(Packet03BallChangeDirection packet) {
//		Tile.ballTile.getPosistion().add(0, -2);
		Tile.ballTile.movementX = packet.getX();
		Tile.ballTile.movementY = packet.getY();
	}
	
	private void handleBallPosistion(Packet04BallMove packet) {
		Tile.ballTile.getPosistion().set(packet.getX(), packet.getY());
	}
}