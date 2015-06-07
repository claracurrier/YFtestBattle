/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mobPack;

import battlestatepack.KnockbackCont;
import battlestatepack.GVars;
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
    private float stunThreshold = GVars.gvars.mstunthreshold;
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
        if (mob.getHealth() <= 0) {
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
            spatial.setLocalTranslation(width, loc.y, 0);
        }
        if (loc.y > height) {
            spatial.setLocalTranslation(loc.x, height, 0);
        }
        if (loc.x < 0) {
            spatial.setLocalTranslation(0, loc.y, 0);
        }
        if (loc.y < 0) {
            spatial.setLocalTranslation(loc.x, 0, 0);
        }

        //place to poll for collisions
        {
            if (!spatial.getUserData("collided").equals("none")) {
                //collision
                float atkpower = (Float) spatial.getUserData("atkpower");
                int dir = (Integer) spatial.getUserData("atkdirection");

                if (spatial.getUserData("collided").equals("arrow")) {
                    movedir(dir, 0);

                } else if (spatial.getUserData("collided").equals("pushback")) {
                    movedir(dir, atkpower * GVars.gvars.mpushbackmod);
                    mob.setEnabled(false);
                    spatial.addControl(new MStunnedCont(atkpower
                            * GVars.gvars.mpushstunmod, mob));

                } else if (spatial.getUserData("collided").equals("stun")) {
                    movedir(dir, 0);
                    if (atkpower > stunThreshold) {
                        mob.setEnabled(false);
                        spatial.addControl(new MStunnedCont(atkpower
                                * GVars.gvars.mstunmod, mob));
                    }

                } else if (spatial.getUserData("collided").equals("spin")) {
                    movedir(dir, atkpower * GVars.gvars.mspinpushmod);
                    mob.setEnabled(false);
                    spatial.addControl(new MStunnedCont(
                            atkpower * GVars.gvars.mspinstunmod, mob));
                }

                mob.reduceHealth((Float) spatial.getUserData("atkpower"));
                //modifers can be added here depending on mob
                //refactoring of health mechanism may be necessary
                //especially if considering overhaul of inheritance
                spatial.setUserData("collided", "none");
            }
        }
    }

    private void movedir(int dir, float atkpower) {
        spatial.setUserData("knockback", true);
        spatial.addControl(new KnockbackCont(GVars.gvars.mminmovement + atkpower,
                atkpower * GVars.gvars.mintensitymovemod + GVars.gvars.mminintensity,
                spatial.getName(), 0, dir));
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}