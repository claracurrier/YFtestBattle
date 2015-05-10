/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlestatepack.mobPack;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author PC
 */
public class PursuitCont extends AbstractControl {

    private Node targ;
    private Spatial mob;
    private float speed = 200;

    public PursuitCont(Spatial m) {
        mob = m;
    }

    public PursuitCont(Spatial m, Node t) {
        mob = m;
        targ = t;
    }

    public void setTarg(Node t) {
        targ = t;
    }

    @Override
    protected void controlUpdate(float tpf) {
        Vector3f dir = getTargDir(targ);
        mob.move(dir.multLocal(tpf * speed));
        //1) get target position
        //2) get movement direction (normalize the difference in locations
        //3) move towards target
    }

    private Vector3f getTargDir(Node targ) {
        Vector3f targvel = new Vector3f(
                targ.getLocalTranslation().x - mob.getLocalTranslation().x,
                targ.getLocalTranslation().y - mob.getLocalTranslation().y, 0);
        return targvel.normalizeLocal();
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}
