package com.daansander.game.tile;

import com.daansander.game.Game;
import com.daansander.game.graphics.Screen;
import com.daansander.game.math.Vector2D;

public class PlatformTile extends Tile {

	protected boolean AI = false;
	protected int height;
	protected int width;

	protected PlatformTile(Vector2D posistion, int height, int width) {
		super(posistion);
		this.height = height;
		this.width = width;
	}

	protected PlatformTile(Vector2D posistion, int height, int width, boolean AI) {
		super(posistion);
		this.height = height;
		this.width = width;
		this.AI = AI;
	}

	public void move(int y) {
		if (y < 0) {
			if (posistion.getY() - y < 0)
				return;
		} else {
			if (posistion.getY() + y > (Game.HEIGHT - height))
				return;
		}
		
		posistion.add(0, y);
	}

	@Override
	public void render(Screen screen) {
		screen.fillRectangle(width, height, posistion, 0xffffff);
	}

	@Override
	public void tick() {
		if (AI) {
			if (Tile.ballTile.getPosistion().getX() < Game.WIDTH / 2) {
				int direction = Tile.ballTile.posistion.getY() - (height / 2) > posistion.getY() ? 2 : -2;
				move(direction);
			}
		} 
//		else {
//			if (Tile.ballTile.getPosistion().getX() > (Game.WIDTH + 20) / 2) {
//				int direction = Tile.ballTile.posistion.getY() - height / 2 > posistion.getY() ? 2 : -2;
//				move(direction);
//			}
//		}
	}

	@Override
	public void reset() {
		Tile.AIPlatform.posistion.set(10, 50);
		Tile.playerPlatform.posistion.set(180, 50);
//		if (AI) {
//			posistion.set(10, 50);
//		} else {
//			posistion.set(180, 50);
//		}
	}

}