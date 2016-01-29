package com.daansander.game.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.daansander.game.Game;

public class KeyBoard implements KeyListener {

	private boolean[] keys = new boolean[256];
	
	public KeyBoard(Game game) {
		game.addKeyListener(this);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	public boolean isDown(int key) {
		return keys[key];
	}
	
}