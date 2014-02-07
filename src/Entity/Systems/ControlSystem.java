package Entity.Systems;

import Entity.Components.Controllable;
import Entity.EntityComponents;
import Input.KeyManager;
import Input.KeyMapping;
import Level.EntityContainer;

public class ControlSystem {
   public static void manipulate(EntityContainer world) {
      for(int i = 0; i < EntityContainer.MAXIMUM_ENTITIES; i++) {
         int entity = world.getEntityMask(i);
         if(isControllable(entity)) {
            manipulateEntity(world.accessComponents(i));
         }
      }  
   }
   
   private static boolean isControllable(int entity) {
      return (entity & EntityContainer.ENTITY_CONTROLLABLE) != 0;
   }
   
   private static void manipulateEntity(EntityComponents entity) {
      Controllable controlComp = entity.controllable;
      for(KeyMapping keyMapping: controlComp.keyMappings) {
         if(KeyManager.isKeyPressed(keyMapping.keyCode) && !keyMapping.pressProcessed) {
            keyMapping.keyFunction.keyPressed(entity);
            keyMapping.pressProcessed = true;
            keyMapping.releaseProcessed = false;
         } else if(!KeyManager.isKeyPressed(keyMapping.keyCode) && !keyMapping.releaseProcessed) {
            keyMapping.keyFunction.keyReleased(entity);
            keyMapping.releaseProcessed = true;
            keyMapping.pressProcessed = false;
         }
      }
      
   }
}
