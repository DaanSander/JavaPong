package com.daansander.game.net.packet;

import com.daansander.game.net.GameClient;
import com.daansander.game.net.GameServer;

public class Packet03BallChangeDirection extends Packet {

	private int x, y;
	
	public Packet03BallChangeDirection(byte[] data) {
		super(03);
		String[] dataArray = new String(data).trim().substring(2).split(",");
		this.x = Integer.parseInt(dataArray[0]);
		this.y = Integer.parseInt(dataArray[1]);
	}
	
	public Packet03BallChangeDirection(int x, int y) {
		super(03);
		this.x = x;
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
		return ("03" + x + "," + y).getBytes();
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	
	
}