package com.daansander.game.input;

import java.awt.MouseInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.daansander.game.Game;
import com.daansander.game.math.Vector2D;

public class Mouse implements MouseListener, MouseMotionListener, MouseWheelListener {

	private boolean[] mouse = new boolean[3];
	private Vector2D posistion = new Vector2D(0, 0);
	
	public Mouse(Game game) {
		game.addMouseListener(this);
		game.addMouseMotionListener(this);
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		posistion = new Vector2D(e.getPoint().x, e.getPoint().y);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
//		mouseMoved(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouse[e.getButton() - 1] = true;
		posistion = new Vector2D(e.getPoint().x, e.getPoint().y);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouse[e.getButton() - 1] = false;
	}

	public boolean isDown(int button) {
		return mouse[button - 1];
	}
	
	public Vector2D getPosistion() {
		return posistion;
	}
	
	public void tick() {
//		posistion = new Vector2D(MouseInfo.getPointerInfo().getLocation().getX(), MouseInfo.getPointerInfo().getLocation().getY());
	}
	
}