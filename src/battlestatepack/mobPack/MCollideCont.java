/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlestatepack.mobPack;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author PC
 */
public class MCollideCont extends AbstractControl {
    
    private Node atkNode;
    private Vector3f loc;
    
    public MCollideCont(){
    }
    
    public MCollideCont(Node atk){
        atkNode = atk;
    }
    
    public void setAtkNode(Node atk){
        atkNode = atk;
    }
    
    @Override
    protected void controlUpdate(float tpf) {
        loc = spatial.getLocalTranslation();
        
        if(!atkNode.getLocalTranslation().equals(loc)){
            atkNode.setLocalTranslation(loc);
            //make sure box follows mob 
        }      
        
        if (loc.x > 1500
                || loc.y > 1500
                || loc.x < 0
                || loc.y < 0) {
            //if monster is outside of bounds
        }
        
        //place to poll for collisions
        {
            if (!spatial.getUserData("collided").equals("none")) {
                //collision
                if (spatial.getUserData("collided").equals("arrow")) {
                    movedir((Integer) spatial.getUserData("atkdirection"));
                    
                } else if (spatial.getUserData("collided").equals("pushback")) {
                    movedir((Integer) spatial.getUserData("atkdirection"));
                    
                } else if (spatial.getUserData("collided").equals("stun")) {
                    movedir((Integer) spatial.getUserData("atkdirection"));
                    
                } else if (spatial.getUserData("collided").equals("spin")) {
                    movedir((Integer) spatial.getUserData("atkdirection"));
                }
                spatial.setUserData("collided", "none");
            }
        }
    }
    
    private void movedir(int dir) {
        switch (dir) { //moves the node opposite of the direction it was it
            case 0:
                spatial.move(0, -81f, 0);
                break;
            case 1:
                spatial.move(-51f, -81f, 0);
                break;
            case 2:
                spatial.move(-51f, 0, 0);
                break;
            case 3:
                spatial.move(-51f, 81f, 0);
                break;
            case 4:
                spatial.move(0, 81f, 0);
                break;
            case 5:
                spatial.move(51f, 81f, 0);
                break;
            case 6:
                spatial.move(51f, 0, 0);
                break;
            case 7:
                spatial.move(51f, -81f, 0);
                break;
        }
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}