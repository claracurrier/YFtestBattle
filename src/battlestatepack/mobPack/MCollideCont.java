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
    private final MobAS mob;
    private final float width = 1000f;
    private final float height = 1000f;
    private float stunThreshold = .5f;
    //TODO: make the bounds either built into the map or change this

    public MCollideCont(MobAS m) {
        mob = m;
    }

    public MCollideCont(Node atk, MobAS m) {
        atkNode = atk;
        mob = m;
    }

    public void setAtkNode(Node atk) {
        atkNode = atk;
    }

    @Override
    protected void controlUpdate(float tpf) {
        if(mob.getHealth()<=0){
            spatial.removeFromParent();
            atkNode.removeFromParent();
            return;
        }
        
        loc = spatial.getLocalTranslation();

        if (!atkNode.getLocalTranslation().equals(loc)) {
            atkNode.setLocalTranslation(loc);
            //make sure box follows mob 
        }

        //check bounds
        if (loc.x > width) {
            spatial.move(width - loc.x, 0, 0);
        }
        if (loc.y > height) {
            spatial.move(0, height - loc.y, 0);
        }
        if (loc.x < 0) {
            spatial.move(0 - loc.x, 0, 0);
        }
        if (loc.y < 0) {
            spatial.move(0, 0 - loc.y, 0);
        }

        //place to poll for collisions
        {
            if (!spatial.getUserData("collided").equals("none")) {
                //collision
                int dir = (Integer) spatial.getUserData("atkdirection");
                if (spatial.getUserData("collided").equals("arrow")) {
                    movedir(dir);

                } else if (spatial.getUserData("collided").equals("pushback")) {
                    movedir(dir);

                } else if (spatial.getUserData("collided").equals("stun")) {
                    movedir(dir);
                    float atkpower = (Float)spatial.getUserData("atkpower");
                    if(atkpower>stunThreshold){
                        mob.setEnabled(false);
                        spatial.addControl(new MStunnedCont(atkpower, mob, spatial));
                    }

                } else if (spatial.getUserData("collided").equals("spin")) {
                    movedir(dir);
                }
                
                mob.reduceHealth((Float)spatial.getUserData("atkpower"));
                //modifers can be added here depending on mob
                //refactoring of health mechanism may be necessary
                //especially if considering overhaul of inheritance
                spatial.setUserData("collided", "none");
            }
        }
    }

    private void movedir(int dir) {
        switch (dir) { //moves the node opposite of the direction it was it
            //right now SATtest only gives 4 cardinal directions
            case 1: //hit right
                spatial.move(-10f, 0, 0);
                break;
            case 2: //hit left
                spatial.move(10f, 0, 0);
                break;
            case 3: //hit below
                spatial.move(0, 10f, 0);
                break;
            case 4: //hit above
                spatial.move(0, -10f, 0);
                break;
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}