package Graphics;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;

import javax.swing.JFrame;

import Core.Events.EventPump;
import Util.ErrorLog;

public class GameWindow {
	private JFrame mWindow;
	private GameCanvas mCanvas;
   
	private GraphicsDevice mFullscreenDevice;
	
	private boolean mFullscreen;
	private int mWidth;
	private int mHeight;
   
	public GameWindow(String title) {
      setupWindow(title);
      mCanvas = new GameCanvas(this);
      loadSettings();
	}
   
   private void setupWindow(String title) {
		mWindow = new JFrame(title);
      mWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mWindow.setVisible(true);
		mWindow.getContentPane().setLayout(new CardLayout());
   }
   
   private void loadSettings() {
		mFullscreenDevice = null;
		mFullscreen = false;
      setSize(1024, 768);
   }
   
   public void addEventPump(EventPump pump) {
      mCanvas.addComponentListener(pump);
      mCanvas.addFocusListener(pump);
      mCanvas.addKeyListener(pump);
      mCanvas.addMouseListener(pump);
      mCanvas.addMouseMotionListener(pump);
   }
   
   public void setTitle(String title) {
      mWindow.setTitle(title);
   }
	
	public void switchFullscreen() {
      mFullscreen = !mFullscreen;
      
		if(mFullscreen) {
         switchToFullscreen();
		} else {
         switchToWindow();
		}
	}
   
   private void switchToFullscreen() {
      GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();

      mFullscreenDevice = env.getDefaultScreenDevice();
      if(mFullscreenDevice.isFullScreenSupported()) {
         mFullscreenDevice.setFullScreenWindow(mWindow);
         mFullscreen = true;
      }

      if(!mFullscreen) {
         ErrorLog errorLog = ErrorLog.getInstance();
         errorLog.displayMessageDialog("It seems there are no screens that support fullscreen mode.\nRemaining in Windowed mode");
      }
   }
	
   private void switchToWindow() {
      mFullscreenDevice.setFullScreenWindow(null);
      mFullscreenDevice = null;

      mFullscreen = false;
   }
   
	public boolean isFullscreen() {
		return mFullscreen;
	}
	
	public int getWidth() {
	   return mWindow.getWidth();
	}
	
	public int getHeight() {
	   return mWindow.getHeight();
	}
	
	public Dimension getSize() {
	   return new Dimension(mWidth, mHeight);
	}
	
	public Dimension getCanvasSize() {
	   return mCanvas.getSize();
	}
	
	public void setSize(int width, int height) {
		if(mFullscreen) {
         setFullscreenSize(width, height);
		} else {
         setWindowedSize(width, height);
		}
		mCanvas.setSize(width, height);
	}
	
	public void setSize(Dimension size) {
	   setSize(size.width, size.height);
	}
   
   private void setFullscreenSize(int width, int height) {
      mWidth = width;
      mHeight = height;
      mWindow.setSize(width, height);
   }
   
   private void setWindowedSize(int width, int height) {
      Insets insets = mWindow.getInsets();

      mWidth = width;
      mHeight = height;
      mWindow.setSize(width + insets.left + insets.right, 
            height + insets.top + insets.bottom);
   }
   
   public void addCard(Component component, String cardName) {
      mWindow.getContentPane().add(component, cardName);
   }
   
   public void switchCard(String cardName) {
      Container container = mWindow.getContentPane();
      CardLayout layout = (CardLayout)container.getLayout();
      layout.show(container, cardName);
   }
   
   public CanvasInterface getCanvas() {
      return mCanvas;
   }
   
   public void draw() {
      mCanvas.draw();
   }
}
