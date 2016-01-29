package com.daansander.game.math;

public class Vector2D {

	private int x, y;
	
	public Vector2D(int x, int y) {
		this.x = x;
		this.y = y;
		
	}

	public void set(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void add(int x, int y) {
		this.x += x;
		this.y += y;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}