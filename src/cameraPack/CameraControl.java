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

    int width = 0;
    int height = 0;

    public abstract void setup();
    //Makes the camera when the options change

    public void refreshScreenDim(int w, int h) {
        //Resets dimensions and relevant nodes when options change.
        width = w;
        height = h;
        setup();
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //not used
    }
}
