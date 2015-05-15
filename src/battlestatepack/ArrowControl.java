/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlestatepack;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author PC
 */
public class ArrowControl extends AbstractControl {

    private final int screenWidth, screenHeight;
    private final float speed = 1100f;
    public Vector3f direction;
    private boolean rotated = false;
    private final float aim;

    public ArrowControl(Vector2f direction, int screenWidth, int screenHeight) {
        this.direction = new Vector3f(direction.x, direction.y, 0f);
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        aim = direction.getAngle();

    }

    @Override
    protected void controlUpdate(float tpf) {

        if (!rotated) {
            spatial.rotate(0, 0, aim);
            rotated = true;
        }

        //        movement
        spatial.move(direction.mult(speed * tpf));

//        check boundaries
        Vector3f loc = spatial.getLocalTranslation();
        if (loc.x > screenWidth
                || loc.y > screenHeight
                || loc.x < 0
                || loc.y < 0) {
            spatial.removeFromParent();
        }

        if ((Boolean) spatial.getUserData("collided") == true) {
            //arrow collided
            System.out.println("arrow collided!");
            spatial.removeFromParent();
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}