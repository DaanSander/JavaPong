package com.daansander.game.net.packet;

import com.daansander.game.net.GameClient;
import com.daansander.game.net.GameServer;

public class Packet00Login extends Packet {

	private String username;
	
	public Packet00Login(byte[] data) {
		super((byte) 00);
//		String[] dataArray = new String(data).split(",");
		this.username = new String(data).substring(2).trim();
	}

	public Packet00Login(String username) {
		super(00);
		this.username = username;
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
		return ("00" + username).getBytes();
	}

	public String getUsername() {
		return username;
	}
	
}