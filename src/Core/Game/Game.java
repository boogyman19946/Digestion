package Core.Game;

import java.awt.event.KeyEvent;

import javax.swing.SwingUtilities;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.python.core.PyException;

import Core.Menu.MainMenu;
import Core.Menu.PauseMenu;
import Entity.EntityComponents;
import Entity.EntityFactory;
import Entity.Systems.AnimationSystem;
import Entity.Systems.ControlSystem;
import Entity.Systems.DrawingSystem;
import Entity.Systems.MotionSystem;
import Graphics.GameCanvas;
import Graphics.GameViewport;
import Graphics.GameWindow;
import Input.KeyManager;
import Level.EntityContainer;
import Level.Level;
import Util.ErrorLog;
import Util.GameTimer;
import Util.UnitConverter;

public class Game {
	private GameWindow mWindow;

	private MainMenu mMainMenu;
	private GameCanvas mGameCanvas;

	private boolean mQuit;

	private EntityFactory mEntityFactory;
	private Level mLevel;
	private World mBox2DWorld;
	private EntityContainer mWorld;

	public Game() {
		mMainMenu = new MainMenu(this);
		mGameCanvas = new GameCanvas();
	}

	public void startLevel(LevelLoadingScript loadingScript) {
		if(!loadLevel(loadingScript)) {
			return;
		}

		MotionSystem.resetTimer();

		mQuit = false;
		mWindow.switchTo(mGameCanvas);
		mWindow.update();
		execute();
	}

	private boolean loadLevel(LevelScript loadingScript) {
	}

	public void pause() { 
		mPaused = true;
		PauseMenu pauseMenu = new PauseMenu(this, mMenuStack);
		mMenuStack.pushScreen(pauseMenu);
		mWindow.switchTo(mMenuStack);
	}

	public void resume() {
		mPaused = false;
		mWorld.resetTimers();
		MotionSystem.resetTimer();
		mWindow.switchTo(mGameCanvas);
	}

	public void quitToMenu() {
		mQuit = true;
	}
	
	public void run() {
		showWindow();
		showTitleScreen();
	}
	
	private void showWindow() {
		SwingUtilities.invokeLater(() -> mWindow = new GameWindow("Digestion"));
	}
	
	private void showTitleScreen() {
		SwingUtilities.invokeLater(() -> mWindow.switchTo(mMainMenu));
	}
	
	private void execute() {
		mPaused = false;

		setFocusObject();

		GameTimer fpsUpdateTimer = new GameTimer();
		fpsUpdateTimer.setTimeInterval(5000);

		int frameCounter = 0;
		boolean escProcessed = false;
		while(!mQuit) {
			if(mPaused) {
				if(KeyManager.isKeyPressed(KeyEvent.VK_ESCAPE) && !escProcessed) {
					escProcessed = true;
					resume();
				}
			} else {
				ControlSystem.manipulate(mWorld);
				MotionSystem.move(mBox2DWorld, mWorld);
				AnimationSystem.animate(mWorld);
				DrawingSystem.draw(mWorld, mGameCanvas);

				mWindow.update();

				if(KeyManager.isKeyPressed(KeyEvent.VK_ESCAPE) && !escProcessed) {
					escProcessed = true;
					pause();
				}
			}

			if(!KeyManager.isKeyPressed(KeyEvent.VK_ESCAPE) && escProcessed)
				escProcessed = false;

			frameCounter++;
			if(fpsUpdateTimer.hasTimeIntervalPassed()) {
				updateFPS(frameCounter, fpsUpdateTimer.getElapsedTime());
				fpsUpdateTimer.reset();
				frameCounter = 0;
			}
		}
		performCleanUp();

		mWindow.setTitle("Digestion");

		mWindow.switchTo(mMenuStack);
	}

	private void performCleanUp() {
		mLevel = null;
		mWorld = null;
		mBox2DWorld = null;
	}

	private void setFocusObject() {
		for(int i = 0; i < EntityContainer.MAXIMUM_ENTITIES; i++) {
			int entityMask = mWorld.getEntityMask(i);
			if((entityMask & EntityContainer.ENTITY_FOCUSABLE) != 0) {
				EntityComponents components = mWorld.accessComponents(i);

				Vec2 px_levelSize = UnitConverter.metersToPixels(mLevel.m_size);
				GameViewport viewport = new GameViewport(px_levelSize, new Vec2(mWindow.getWidth(), mWindow.getHeight()));
				viewport.setFocusObject(components);

				mGameCanvas.setViewport(viewport);
				break;
			}
		}
	}

	private void updateFPS(int frameCount, long timePassed) {
		double fps = (double)frameCount / (timePassed/5000.0);
		mWindow.setTitle("Digestion - " + fps + " FPS");
	}
}
