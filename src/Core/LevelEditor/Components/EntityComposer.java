package Core.LevelEditor.Components;

import javax.swing.JSplitPane;

import Core.LevelEditor.Models.EntityModelList;
import Core.LevelEditor.Settings.EditorSettings;

public class EntityComposer extends JSplitPane {
   private EditorSettings mEditorSettings;
   private EntityModelList mEntityModelList;
   
   public EntityComposer(EditorSettings editorSettings, EntityModelList entityModelList) {
      super(HORIZONTAL_SPLIT);
      
      mEditorSettings = editorSettings;
      mEntityModelList = entityModelList;
      
      createWidgets();
      
      setResizeWeight(0.5);
   }
   
   private void createWidgets() {
      createComponentListPanel();
      createComponentComposer();
   }
   
   private void createComponentListPanel() {
      ComponentList compList = new ComponentList(mEditorSettings);
      add(compList);
   }
   
   private void createComponentComposer() {
      
   }
}
