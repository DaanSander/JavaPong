package com.daansander.game.net.packet;

import com.daansander.game.net.GameClient;
import com.daansander.game.net.GameServer;

public class Packet04BallMove extends Packet {

	private int x, y;

	public Packet04BallMove(byte[] data) {
		super(04);
		String[] dataArray = new String(data).trim().substring(2).split(",");
		this.x = Integer.parseInt(dataArray[0]);
		this.y = Integer.parseInt(dataArray[1]);
	}

	public Packet04BallMove(int x, int y) {
		super(04);
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
		return ("04" + x + "," + y).getBytes();
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}