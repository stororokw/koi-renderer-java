package koi.renderer;

import gui.TileListener;

import java.util.ArrayList;

public abstract class Renderer {
	protected ArrayList<TileListener> updateListeners = new ArrayList<TileListener>();
	
	public abstract void render(int x0, int y0, int x1, int y1);

	public void addOnUpdateListener(TileListener listener) {
		updateListeners.add(listener);
	}
	
	
}
