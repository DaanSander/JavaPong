package com.daansander.game.player;

import com.daansander.game.tile.PlatformTile;
import com.daansander.game.tile.Tile;

public class Player {

	public PlatformTile tile;
	private String username;

	public Player(Tile tile, String username) {
		if (!(tile instanceof PlatformTile))
			throw new IllegalArgumentException("Tile is not instance of a platform");
		this.tile = (PlatformTile) tile;
		this.username = username;
	}

	public void move(int y) {
		tile.move(y);
	}

	public String getUsername() {
		return username;
	}
	
	public PlatformTile getTile() {
		return tile;
	}

}