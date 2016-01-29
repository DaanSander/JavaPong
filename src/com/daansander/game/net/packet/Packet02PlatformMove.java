package com.daansander.game.net.packet;

import com.daansander.game.net.GameClient;
import com.daansander.game.net.GameServer;

public class Packet02PlatformMove extends Packet {

	private String username;
	private int y;

	public Packet02PlatformMove(byte[] data) {
		super(02);
		String[] dataArray = new String(data).substring(2).trim().split(",");
		this.username = dataArray[0];
		this.y = Integer.parseInt(dataArray[1]);
	}

	public Packet02PlatformMove(String username, int y) {
		super(02);
		this.username = username;
		this.y = y;
	}
	
	@Override
	public void writeData(GameClient client) {
		client.sendData(getData());
	}

	@Override
	public void writeData(GameServer server) {
		server.sendToAllClients(getData());
	}

	@Override
	public byte[] getData() {
		return ("02" + username + "," + y).getBytes();
	}

	public String getUsername() {
		return username;
	}

	public int getY() {
		return y;
	}
	
}