package com.daansander.game.net.packet;

import com.daansander.game.net.GameClient;
import com.daansander.game.net.GameServer;

public class Packet01Disconnect extends Packet {

	private String username;
	
	public Packet01Disconnect(byte[] data) {
		super(01);
		this.username = new String(data).substring(2).trim();
	}

	public Packet01Disconnect(String username) {
		super(01);
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
		return ("01" + username).getBytes();
	}

	public String getUsername() {
		return username;
	}
	
}