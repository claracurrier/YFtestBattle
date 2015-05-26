/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlestatepack;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author PC
 */
public class PCollideCont extends AbstractControl {

    private float health = GBalanceVars.gbal.phealth;

    public PCollideCont() {
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (!spatial.getUserData("collided").equals("none")) {
            //collision
            reduceHealth((Float) spatial.getUserData("atkpower"));

            switch ((Integer) spatial.getUserData("atkdirection")) { //moves the node opposite of the direction it was it
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
            spatial.setUserData("collided", "none");
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    public void reduceHealth(float damage) {
        health -= damage;
    }

    public float getHealth() {
        return health;
    }
}
