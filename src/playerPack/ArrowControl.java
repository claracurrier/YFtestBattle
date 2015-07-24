/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package playerPack;

import battlestatepack.GVars;
import com.jme3.math.Vector2f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author Clara Currier
 */
public class ArrowControl extends AbstractControl {

    private final float speed = GVars.gvars.arrowspeed;
    private final Vector2f direction;
    private boolean rotated = false;
    private final float aim;

    public ArrowControl(Vector2f direction) {
        this.direction = direction;
        aim = new Vector2f(direction.x, direction.y).getAngle();
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (!spatial.getUserData("collided").equals("none")) {
            //arrow collided
            System.out.println("arrow collided!");
            spatial.removeFromParent();
            return;
        }

        if (!rotated) {
            spatial.rotate(0, 0, aim);
            rotated = true;
        }
        spatial.move(direction.x * (speed * tpf),
                direction.y * (speed * tpf), 0);
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}