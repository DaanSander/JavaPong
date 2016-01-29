package com.daansander.game.graphics;

import java.util.HashMap;
import java.util.Map;

import com.daansander.game.math.Vector2D;

public class Screen {

	private Map<Vector2D, Integer> posistions = new HashMap<>();
	private int width, height;
	public int[] pixels;

	public Screen(int width, int height) {
		this.width = width;
		this.height = height;

		pixels = new int[width * height];

	}

	public void clear() {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
	}

	public void fillCube(int size, Vector2D posistion, int color) {
		for (int x = posistion.getX(); x < (size + posistion.getX()); x++) {
			for (int y = posistion.getY(); y < (size + posistion.getY()); y++) {

				int xp = x;
				int yp = y;

				if (x < 0 || x >= width)
					xp = 0;
				if (y < 0 || y >= height)
					yp = 0;

				pixels[xp + yp * width] = color;
			}
		}
	}

	public void fillRectangle(int xx, int yy, Vector2D posistion, int color) {
		for (int y = posistion.getY(); y < (yy + posistion.getY()); y++) {

			for (int x = posistion.getX(); x < (xx + posistion.getX()); x++) {

				int xp = x;
				int yp = y;

				if (x < 0 || x >= width)
					xp = 0;
				if (y < 0 || y >= height)
					yp = 0;

				pixels[xp + yp * width] = color;
			}
		}
	}

	public void drawCircle(int size, Vector2D posistion, int color) {
		for (int i = 0; i < 360; i++) {
			int x = (int) Math.round(posistion.getX() + size * Math.cos(i));
			int y = (int) Math.round(posistion.getY() + size * Math.sin(i));

			if (x < 0 || x >= width)
				x = 0;
			if (y < 0 || y >= height)
				y = 0;

			pixels[x + y * width] = color;
		}
	}

	public void fillCircle(int size, Vector2D posistion, int color) {
		for (int i = 0; i < 360; i++) {
			for (int a = size; a > -1; a--) {
				int x = (int) Math.round(posistion.getX() + a * Math.cos(i));
				int y = (int) Math.round(posistion.getY() + a * Math.sin(i));

				if (x < 0 || x >= width)
					x = 0;
				if (y < 0 || y >= height)
					y = 0;

				pixels[x + y * width] = color;
			}
		}
	}

	public void render() {

	}

	public void addPosistion(Vector2D posistion, int color) {
		posistions.put(posistion, color);
	}
}