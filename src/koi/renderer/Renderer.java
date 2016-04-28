package koi.renderer;

import gui.TileListener;

import java.util.ArrayList;

public abstract class Renderer {
	protected ArrayList<TileListener> updateListeners = new ArrayList<TileListener>();
	
	public abstract void render();

	public void addOnUpdateListener(TileListener listener) {
		updateListeners.add(listener);
	}
	
	
}
