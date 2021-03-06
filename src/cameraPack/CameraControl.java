/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cameraPack;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author Clara Currier
 */
public abstract class CameraControl extends AbstractControl {

    protected int width = 0;
    protected int height = 0;

    public abstract void setup();
    //Makes the camera when the options change

    public abstract void takedown();
    //removes all presence of the camera

    public void refreshScreenDim(int w, int h) {
        //Resets dimensions and relevant nodes when options change.
        width = w;
        height = h;
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //not used
    }
}
