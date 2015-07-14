/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package playerPack;

import battlestatepack.GVars;
import battlestatepack.KnockbackCont;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author Clara Currier
 */
public class PCollideCont extends AbstractControl {

    private Player player;

    public PCollideCont(Player player) {
        this.player = player;
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (!spatial.getUserData("collided").equals("none")) {
            //collision
            float atkpower = (Float) spatial.getUserData("atkpower");
            player.reduceHealth(atkpower);

            spatial.setUserData("knockback", true);
            spatial.addControl(new KnockbackCont(GVars.gvars.mminmovement + atkpower,
                    atkpower * GVars.gvars.mintensitymovemod + GVars.gvars.mminintensity,
                    spatial.getName(), 8, (Integer) spatial.getUserData("atkdirection")));

            spatial.setUserData("collided", "none");
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}