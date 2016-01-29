package com.daansander.game.tile;

import com.daansander.game.Game;
import com.daansander.game.graphics.Screen;
import com.daansander.game.math.Vector2D;

public abstract class Tile {

	protected Vector2D posistion;
	
	public static PlatformTile playerPlatform;
	public static PlatformTile AIPlatform;
	public static BallTile ballTile;
	
	protected Tile(Vector2D posistion) {
		this.posistion = posistion;
	}
	
	public abstract void render(Screen screen);
	
	public abstract void tick();
	
	public abstract void reset();

	public static void renderTiles(Screen screen) {
		playerPlatform.render(screen);
		AIPlatform.render(screen);
		ballTile.render(screen);
	}
	
	public Vector2D getPosistion() {
		return posistion;
	}

	public static void tickTiles() {
		playerPlatform.tick();
		AIPlatform.tick();
		ballTile.tick();
	}
	
	public static void resetTiles() {
		playerPlatform.reset();
		AIPlatform.reset();
		ballTile.reset();
	}

	public static void init(boolean server) {
		ballTile = new BallTile(new Vector2D(Game.WIDTH / 2, Game.HEIGHT / 2), 2);
		AIPlatform = new PlatformTile(new Vector2D(10, 50), 30, 10, server == false);
		playerPlatform = new PlatformTile(new Vector2D(180, 50), 30, 10);
	}
	
}