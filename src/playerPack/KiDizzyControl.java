/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package playerPack;

import battlestatepack.BattleMain;
import battlestatepack.GBalanceVars;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import spriteProject.SpriteLibrary;

/**
 *
 * @author PC
 */
public class KiDizzyControl extends AbstractControl {

    private float time = GBalanceVars.gbal.kdizzytimer;
    private float timecount = 0;
    private final KirithAppState kiApp;
    private final PMoveAppState pmAS;
    private final SpriteLibrary sprites = BattleMain.sEngine.getLibrary("Kirith");

    public KiDizzyControl(KirithAppState ki, PMoveAppState pmc) {
        this.kiApp = ki;
        this.pmAS = pmc;
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (timecount > time && kiApp.isEnabled()) {
            System.out.println("you can move again");
            kiApp.enableAttackMap();
            pmAS.setEnabled(true);
            sprites.activateSprite(12);
            spatial.removeControl(this);
        } else if (timecount > time && kiApp.isEnabled()) {
            System.out.println("you can move again");
            spatial.removeControl(this);
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