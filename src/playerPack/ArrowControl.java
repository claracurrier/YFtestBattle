/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package playerPack;

import com.jme3.math.FastMath;
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
    private final Vector3f direction, initloc;
    private boolean rotated = false;
    private final float aim;
    private final float distance;

    public ArrowControl(float distance, int screenWidth, int screenHeight, float accuracy, Vector3f il) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        aim = accuracy;
        this.direction = new Vector3f(FastMath.cos(aim), FastMath.sin(aim), 0f);
        this.distance = distance;
        initloc = il;
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
        if ((loc.x > screenWidth || loc.y > screenHeight
                || loc.x < 0 || loc.y < 0) || (Math.abs(loc.x - initloc.x) > Math.abs(direction.x * distance)
                || Math.abs(loc.y - initloc.y) > Math.abs(direction.y * distance))) {
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