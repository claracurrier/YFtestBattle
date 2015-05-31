/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mobPack;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author PC
 */
public class MStunnedCont extends AbstractControl {

    private float timecount = 0;
    private final float atkpower;
    private final MobAS mob;

    public MStunnedCont(float ap, MobAS m) {
        atkpower = ap;
        mob = m;
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (timecount < atkpower) {
            timecount += tpf;
            spatial.setLocalScale(.5f);
        } else {
            spatial.setLocalScale(1f);
            mob.setEnabled(true);
            spatial.removeControl(this);
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}
