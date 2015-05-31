/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package playerPack;

import battlestatepack.GBalanceVars;
import battlestatepack.KnockbackCont;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author PC
 */
public class PCollideCont extends AbstractControl {

    private float health = GBalanceVars.gbal.phealth;
    private final PMoveAppState pmas;

    public PCollideCont(PMoveAppState p) {
        pmas = p;
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (!spatial.getUserData("collided").equals("none")) {
            //collision
            float atkpower = (Float) spatial.getUserData("atkpower");
            reduceHealth(atkpower);

            spatial.setUserData("knockback", true);
            spatial.addControl(new KnockbackCont(GBalanceVars.gbal.mminmovement + atkpower,
                    atkpower * GBalanceVars.gbal.mintensitymovemod + GBalanceVars.gbal.mminintensity,
                    spatial.getName(), pmas.getDir() + 8, (Integer) spatial.getUserData("atkdirection")));

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