/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package playerPack;

import battlestatepack.GVars;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author Clara Currier
 */
public class ArrowControl extends AbstractControl {

    private final float speed = GVars.gvars.arrowspeed;
    private final Vector3f direction;
    private boolean rotated = false;
    private final float aim;

    public ArrowControl(Vector3f direction) {
        this.direction = direction;
        aim = direction.angleBetween(Vector3f.ZERO);
    }

    @Override
    protected void controlUpdate(float tpf) {
        if ((Boolean) spatial.getUserData("collided")) {
            //arrow collided
            System.out.println("arrow collided!");
            spatial.removeFromParent();
            return;
        }

        if (!rotated) {
            spatial.rotate(0, 0, aim);
            rotated = true;
        }
        spatial.move(direction.mult(speed * tpf));
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}