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
public class KnockbackCont extends AbstractControl {

    //gives a smooth pushing animation
    private final float distance;
    private final float intensity;
    private float dtimer;
    private int dir;

    public KnockbackCont(float d, float i, String lName, int spriteI, int dir) {
        distance = d;
        intensity = i;
        this.dir = dir;
        BattleMain.sEngine.getLibrary(lName).activateSprite(spriteI);
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (dtimer < Math.abs(distance)) {
            switch (dir) { //moves the node opposite of the direction it was it
                //right now SATtest only gives 4 cardinal directions
                case 1: //hit right
                    spatial.move(-tpf * intensity, 0, 0);
                    break;
                case 2: //hit left
                    spatial.move(tpf * intensity, 0, 0);
                    break;
                case 3: //hit below
                    spatial.move(0, tpf * intensity, 0);
                    break;
                case 4: //hit above
                    spatial.move(0, -tpf * intensity, 0);
                    break;
            }
            dtimer += tpf * intensity;
        } else {
            spatial.setUserData("knockback", false);
            spatial.removeControl(this);
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}
