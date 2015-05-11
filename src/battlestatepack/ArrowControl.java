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
    private float rotation;

    public ArrowControl(Vector3f direction, int screenWidth, int screenHeight) {
        this.direction = direction;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        
    }

    @Override
    protected void controlUpdate(float tpf) {
        //        movement
        spatial.move(direction.mult(speed * tpf));

//        rotation
        float actualRotation = getAngleFromVector(direction);
        if (actualRotation != rotation) {
            spatial.rotate(0, 0, actualRotation - rotation);
            rotation = actualRotation;
        }

//        check boundaries
        Vector3f loc = spatial.getLocalTranslation();
        if (loc.x > screenWidth
                || loc.y > screenHeight
                || loc.x < 0
                || loc.y < 0) {
            spatial.removeFromParent();
        }
        
        if((Boolean)spatial.getUserData("collided")==true){
            //arrow collided
            System.out.println("arrow collided!");
            spatial.removeFromParent();
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    public static float getAngleFromVector(Vector3f vec) {
        Vector2f vec2 = new Vector2f(vec.x, vec.y);
        return vec2.getAngle();
    }

}
