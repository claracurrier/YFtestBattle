/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlestatepack.mobPack;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author PC
 */
public class MStunnedCont extends AbstractControl {

    private float timecount = 0;
    private final float atkpower;
    private final MobAS mob;
    private final Spatial spat;

    public MStunnedCont(float ap, MobAS m, Spatial s) {
        atkpower = ap;
        mob = m;
        spat = s;
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (timecount < atkpower) {
            timecount += tpf;
            spat.setLocalScale(.5f);
        } else {
            spat.setLocalScale(1f);
            mob.setEnabled(true);
            spat.removeControl(this);
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}
