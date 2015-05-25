/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlestatepack;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import spriteProject.SpriteLibrary;

/**
 *
 * @author PC
 */
public class KiDizzyControl extends AbstractControl {

    private float time;
    private float timecount = 0;
    private KirithAppState kiApp;
    private PMoveAppState pmAS;
    private final SpriteLibrary sprites = BattleMain.sEngine.getLibrary("Kirith");

    public KiDizzyControl(float time, KirithAppState ki, PMoveAppState pmc) {
        this.time = time;
        this.kiApp = ki;
        this.pmAS = pmc;
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (timecount > time && kiApp.isInitialized() && kiApp.isEnabled()) {
            System.out.println("you can move again");
            kiApp.setEnabled(true);
            pmAS.setEnabled(true);
            spatial.removeControl(spatial.getControl(KiDizzyControl.class));
        } else if (timecount > time && !kiApp.isInitialized() && kiApp.isEnabled()) {
            System.out.println("you can move again");
            spatial.removeControl(spatial.getControl(KiDizzyControl.class));

        } else {
            //put the dizzy sprite activiation
            sprites.activateSprite((((int) Math.floor(timecount * 10)) % 8));
        }

        timecount += tpf;
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}
