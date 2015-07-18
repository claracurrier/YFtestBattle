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
 * @author Clara Currier
 */
public class MCollideCont extends AbstractControl {

    private Node atkNode;
    private Vector3f loc;
    private final MobWrapper mob;
    private final float width = GVars.gvars.mapwidth;
    private final float height = GVars.gvars.mapheight;

    public MCollideCont(MobWrapper m) {
        mob = m;
    }

    public MCollideCont(Node atk, MobWrapper m) {
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
                    //movedir(dir, 0);
                } else if (spatial.getUserData("collided").equals("kiautoattack")) {
                    //movedir(dir, 0);
                }

                mob.reduceHealth((Float) spatial.getUserData("atkpower"));

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