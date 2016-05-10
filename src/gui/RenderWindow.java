package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.SwingWorker;
import javax.swing.SwingWorker.StateValue;
import javax.swing.filechooser.FileFilter;

import koi.Scene;
import koi.renderer.Renderer;

public class RenderWindow extends JFrame implements ActionListener{
	private RenderPanel panel = null;
	private Renderer renderer = null;
	private Scene scene = null;
	private SwingWorker<Void, Void> worker;
	
	private JToolBar toolbar = new JToolBar();
	public RenderWindow(Scene scene, Renderer renderer) {
		this.renderer = renderer;
		this.scene = scene;
		panel = new RenderPanel(scene);
		renderer.addOnUpdateListener(panel);
		toolbar.setFloatable(false);
		JButton button = new JButton();
		button.addActionListener(this);
		worker = new SwingWorker<Void, Void>() {
			
			@Override
			protected Void doInBackground() throws Exception {
				return null;
			}
		};
		JButton stop = new JButton();
		stop.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				worker.cancel(true);
				panel.onStop();
			}
		});
		JButton save = new JButton();
		save.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				FileFilter filter = new FileFilter() {
					
					@Override
					public String getDescription() {
						return "PNG (*.PNG)";
					}
					
					@Override
					public boolean accept(File f) {
						if(f.isDirectory())
						{
							return true;
						}
						String extension = null;
						String name = f.getName();
						int index = name.lastIndexOf('.');
						if(index > 0 && index < name.length() - 1)
						{
							extension = name.substring(index + 1).toLowerCase();
						}
						if(extension.equals("png"))
						{
							return true;
						}
						return false;
					}
				};
				fileChooser.addChoosableFileFilter(filter);
				fileChooser.setFileFilter(filter);
				int returnVal = fileChooser.showSaveDialog(RenderWindow.this);
				if(returnVal == JFileChooser.APPROVE_OPTION)
				{
					File f = fileChooser.getSelectedFile();
					String name = f.getName();
					String extension = null;
					int index = name.lastIndexOf('.');
					if(index > 0 && index < name.length() - 1)
					{
						extension = name.substring(index + 1).toLowerCase();
					}
					
					if(extension.equals("png"))
					{
						try {
							RenderWindow.this.scene.getBitmap().save(f.getAbsolutePath());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
						
				}
			}
		});
		toolbar.add(button);
		toolbar.add(stop);
		toolbar.addSeparator();
		toolbar.add(save);
		try {
			this.setIconImage(ImageIO.read(new FileInputStream(getClass().getClassLoader().getResource("app-icon.png").getFile())));
			button.setIcon(new ImageIcon(
					ImageIO.read(new FileInputStream(getClass().getClassLoader().getResource("render-icon.png").getFile()))
					.getScaledInstance(35, 35, Image.SCALE_SMOOTH)));
			stop.setIcon(new ImageIcon(
					ImageIO.read(new FileInputStream(getClass().getClassLoader().getResource("stop-icon.png").getFile()))
					.getScaledInstance(35, 35, Image.SCALE_SMOOTH)));
			save.setIcon(new ImageIcon(
					ImageIO.read(new FileInputStream(getClass().getClassLoader().getResource("save-icon.png").getFile()))
					.getScaledInstance(35, 35, Image.SCALE_SMOOTH)));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		this.setTitle("Koi Renderer Viewer");
	    this.add(toolbar, BorderLayout.PAGE_START);
		this.add(panel);
		this.pack();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(worker.getState() != StateValue.STARTED)
		{
			worker = new SwingWorker<Void, Void>() {
				@Override
				protected Void doInBackground() throws Exception {
					long start = System.currentTimeMillis();
					panel.clear();
					renderer.render();
					long end = System.currentTimeMillis() - start;
					System.out.println(end / 1000.0);
					return null;
				}
			};

			worker.execute();
		}
	}
}
