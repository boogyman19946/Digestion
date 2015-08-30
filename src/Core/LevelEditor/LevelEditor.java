package Core.LevelEditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Queue;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import Core.Events.Event;
import Core.Game.Game;
import Core.Game.GameState;
import Core.LevelEditor.Components.EditorToolbar;
import Core.LevelEditor.Models.LevelModel;
import Core.LevelEditor.Settings.EditorSettings;
import Graphics.CanvasInterface;

public class LevelEditor extends JPanel implements GameState {   
   private Game mGame;
   
   private ContentPanel mContentPanel;
   
   private EditorSettings mSettings;
   private LevelModel mLevelModel;
   
   public LevelEditor(Game game) {
      mGame = game;
      mSettings = new EditorSettings();
      mLevelModel = new LevelModel();
      
      createWidgets();
   }
   
   /* ********************************************************************** */
   /*                               VIEW                                     */
   /* ********************************************************************** */
   private void createWidgets() {
      setLayout(new BorderLayout());
      createToolBar();
      createEditorPanel();
   }
   
   private void createToolBar() {
      EditorToolbar toolbar = new EditorToolbar(mGame, mLevelModel);
      add(toolbar, BorderLayout.NORTH);
   }
   
   private void createEditorPanel() {
      JSplitPane editorPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
      createSettingsPanel(editorPanel);
      createContentPanel(editorPanel);
      add(editorPanel, BorderLayout.CENTER);
   }
   
   private void createSettingsPanel(JSplitPane parent) {
      SettingsPanel settingsPanel = new SettingsPanel(mSettings);
      parent.add(settingsPanel);
   }
   
   private void createContentPanel(JSplitPane parent) {
      mContentPanel = new ContentPanel(mSettings, mLevelModel);
      parent.add(mContentPanel);
   }

   @Override
   public String stateName() {
      return "LEVEL EDITOR";
   }

   @Override
   public void beforeSwitch(Dimension screenSize) {
      mGame.switchCard("LEVEL EDITOR");
   }

   @Override
   public void handleEvents(Queue<Event> eventQueue) {
      // Events are received from the GameCanvas which is not 
      // visible when the level editor is being show.
   }

   @Override
   public void update() {
      // Literally nothing to do here. Leave this empty! 
   }

   @Override
   public void draw(CanvasInterface canvas) {
      // The canvas is not visible when this function is called!
   }

   @Override
   public void onSwitch() { 
      mGame.switchCard("GAME CANVAS");
   }
}
