package Core.LevelEditor.Control.Commands;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import Core.LevelEditor.Models.DrawerSettings;
import Core.LevelEditor.Models.EditorSettings;
import Core.LevelEditor.Models.Entity;
import Core.LevelEditor.Models.LevelModel;
import Util.Size;

public class ChangeEntityCommand extends LevelModelCommand {

   private Entity mActiveEntity;
   
   private Point mStartPosition;
   
   public ChangeEntityCommand(LevelModel level,
         EditorSettings editor, DrawerSettings drawer) {
      super(level, editor, drawer);
      mStartPosition = new Point();
   }
   
   @Override
   public void perform(MouseEvent event) {
      Point mouseCoords = snapToGrid(event.getPoint());
      
      Rectangle bounds = getBounds(mStartPosition, mouseCoords);
      mActiveEntity.setRect(bounds);
   }
   
   private Rectangle getBounds(Point p1, Point p2) {
      int xLeft = (p1.x < p2.x) ? p1.x : p2.x;
      int xRight = (p1.x >= p2.x) ? p1.x : p2.x;
      int yTop = (p1.y < p2.y) ? p1.y : p2.y;
      int yBottom = (p1.y >= p2.y) ? p1.y : p2.y;
      Size gridSize = mDrawer.getGridSize();
      
      return new Rectangle(
            xLeft, yTop,
            xRight - xLeft + gridSize.width,
            yBottom - yTop + gridSize.height);
   }
   
   protected void setActiveEntity(Entity entity) {
      mActiveEntity = entity;
      Rectangle bounds = entity.getRect();
      mStartPosition.x = bounds.x;
      mStartPosition.y = bounds.y;
   }

}