package gui;

public interface TileListener {
	public void onUpdate(long thread);
	public void onStart(int x0, int y0, int x1, int y1, long thread);
	public void onComplete();
}
