/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlestatepack;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import java.util.Random;

/**
 *
 * @author PC
 */
public class TempWC extends AbstractControl {

    private int screenWidth, screenHeight;
    private Vector3f velocity;
    private float directionAngle;
    

    public TempWC(Integer screenWidth, Integer screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        velocity = new Vector3f();
        directionAngle = new Random().nextFloat() * FastMath.PI * 2f;
        
    }

    
    public static Vector3f getVectorFromAngle(float angle) {
        return new Vector3f(FastMath.cos(angle), FastMath.sin(angle), 0);
    }

    public static float getAngleFromVector(Vector3f vec) {
        Vector2f vec2 = new Vector2f(vec.x, vec.y);
        return vec2.getAngle();
    }

    @Override
    protected void controlUpdate(float tpf) {
        
        //wandererBehavior(tpf);

    }
    
    private void idleBehavior(float tpf){
        //put in some idle movement w/ monkey brains later
    }
    
    private void pursueBehavior(float tpf){
        
    }
    
    private void wandererBehavior(float tpf){
        // change the directionAngle a bit
        directionAngle += (new Random().nextFloat() * 20f - 10f) * tpf;
        Vector3f directionVector = getVectorFromAngle(directionAngle);
        directionVector.multLocal(200f);
        velocity.addLocal(directionVector);

        // decrease the velocity a bit and move the wanderer
        velocity.multLocal(0.8f);
        spatial.move(velocity.mult(tpf * 0.1f));

        // make the wanderer bounce off the screen borders
        Vector3f loc = spatial.getLocalTranslation();
        if ((loc.x > screenWidth) || (loc.y > screenHeight)) {
            Vector3f newDirectionVector = new Vector3f(screenWidth / 2, screenHeight / 2, 0).subtract(loc);
            directionAngle = getAngleFromVector(newDirectionVector);
        }

    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}
