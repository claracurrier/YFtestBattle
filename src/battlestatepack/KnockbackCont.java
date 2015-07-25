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
 * @author Clara Currier
 */
public class KnockbackCont extends AbstractControl {

    //gives a smooth pushing animation
    private final float distance;
    private final float intensity;
    private float dtimer;
    private String library;
    private int dir;

    public KnockbackCont(float distance, float intensity, String libraryName, int dir) {
        this.distance = distance;
        this.intensity = intensity;
        this.dir = dir;
        this.library = libraryName;
        BattleMain.sEngine.getLibrary(libraryName).activateSprite(0);
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (dtimer < Math.abs(distance)) {
            switch (dir) { //moves the node opposite of the direction it was it
                //right now SATtest only gives 4 cardinal directions
                case 1: //hit right
                    if (spatial.getUserData("canL")) {
                        spatial.move(-tpf * intensity, 0, 0);
                    }
                    break;
                case 2: //hit left
                    if (spatial.getUserData("canR")) {
                        spatial.move(tpf * intensity, 0, 0);
                    }
                    break;
                case 3: //hit below
                    if (spatial.getUserData("canU")) {
                        spatial.move(0, tpf * intensity, 0);
                    }
                    break;
                case 4: //hit above
                    if (spatial.getUserData("canD")) {
                        spatial.move(0, -tpf * intensity, 0);
                    }
                    break;
            }
            dtimer += tpf * intensity;
        } else {
            if (!library.equals("Wanderer")) {
                BattleMain.sEngine.getLibrary(library).activateSprite(dir + 8);
            }
            spatial.setUserData("knockback", false);
            spatial.removeControl(this);
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}
