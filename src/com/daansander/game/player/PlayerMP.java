package com.daansander.game.player;

import java.net.InetAddress;

import com.daansander.game.tile.PlatformTile;

public class PlayerMP extends Player {

	// TODO InetAddress toevoegen
	public InetAddress ipAddress;
	public int port;

	public PlayerMP(PlatformTile tile, String username, InetAddress ipAddress, int port) {
		super(tile, username);
		this.ipAddress = ipAddress;
		this.port = port;
	}

}