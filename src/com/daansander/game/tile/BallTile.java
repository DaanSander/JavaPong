package com.daansander.game.tile;

import com.daansander.game.Game;
import com.daansander.game.graphics.Screen;
import com.daansander.game.math.Vector2D;
import com.daansander.game.net.packet.Packet04BallMove;

public class BallTile extends Tile {

	public int movementX = -1;
	public int movementY = 1;

	private int size;

	protected BallTile(Vector2D posistion, int size) {
		super(posistion);
		this.size = size;
	}

	@Override
	public void render(Screen screen) {
		screen.fillCircle(size, posistion, 0xffffff);
	}

	@Override
	public void tick() {
		if (posistion.getY() <= 0)
			bounceBall();
		else if (posistion.getY() >= 150)
			bounceBall();

		if (posistion.getX() < 20 && posistion.getX() > 10 && posistion.getY() >= Tile.AIPlatform.posistion.getY()
				&& posistion.getY() <= Tile.AIPlatform.posistion.getY() + Tile.AIPlatform.height
				|| posistion.getX() > 180 && posistion.getX() < 200 && posistion.getY() >= Tile.playerPlatform.posistion.getY()
						&& posistion.getY() <= Tile.playerPlatform.posistion.getY() + Tile.playerPlatform.height) {

			int direction = (int) Math.round(Math.random() * Math.PI);
			if (posistion.getX() < 20) {
				movementX = (direction > 0) ? direction *= -1 : direction;
			} else {
				movementX = (direction > 0) ? direction : direction * -1;
			}

			movementX = movementX * -1;
			if (Game.game.server && Game.game.hosting) {
				// Packet03BallChangeDirection packet = new
				// Packet03BallChangeDirection(movementX, movementY);
				// packet.writeData(Game.game.socketClient);
			}
		}

		if (posistion.getX() < 0) {
			posistion.set(Game.WIDTH / 2, Game.HEIGHT / 2);
			resetTiles();
			Game.updateScore(0, 1);
		} else if (posistion.getX() > 200) {
			posistion.set(Game.WIDTH / 2, Game.HEIGHT / 2);
			resetTiles();
			Game.updateScore(1, 0);
		}

		posistion.add(movementX, movementY);

		if (Game.game.server && Game.game.hosting) {
			Packet04BallMove packet = new Packet04BallMove(posistion.getX(), posistion.getY());
			packet.writeData(Game.game.socketClient);
		}
	}

	@Override
	public synchronized void reset() {
		movementX = -1;
		movementY = 1;
		posistion.set(Game.WIDTH / 2, Game.HEIGHT / 2);

		if (Game.game.server && Game.game.hosting) {
			Packet04BallMove packet = new Packet04BallMove(posistion.getX(), posistion.getY());
			packet.writeData(Game.game.socketClient);
		}
	}

	public void bounceBall() {
		// if (Game.game.server) {
		// if (Game.game.hosting) {
		// movementX = ((int) Math.round(Math.random() * Math.PI / movementX) ==
		// 0) ? movementX : movementX;
		//
		// movementY = -1 * movementY;
		// Packet03BallChangeDirection packet = new
		// Packet03BallChangeDirection(movementX, movementY);
		// packet.writeData(Game.game.socketClient);
		// }
		// } else {
		// movementX = ((int) Math.round(Math.random() * Math.PI / movementX) ==
		// 0) ? movementX : movementX;
		//
		// movementY = -1 * movementY;
		// }
		movementX = ((int) Math.round(Math.random() * Math.PI / movementX) == 0) ? movementX : movementX;

		movementY = -1 * movementY;
		if (Game.game.server && Game.game.hosting) {
			// Packet03BallChangeDirection packet = new
			// Packet03BallChangeDirection(movementX, movementY);
			// packet.writeData(Game.game.socketClient);
		}
	}

}