/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlestatepack;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author Clara Currier
 */
public class CameraContManual extends AbstractControl implements ActionListener {

    private int width, height;
    private boolean up, down, left, right, keying;
    private InputManager inputManager;

    public CameraContManual(int w, int h, InputManager in) {
        width = w;
        height = h;
        inputManager = in;

        enableCharMapping();
    }

    public void refreshScreenDim(int w, int h) {
        width = w;
        height = h;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled) {
            enableCharMapping();
        } else {
            disableCharMapping();
        }
    }

    private void enableCharMapping() {
        if (!inputManager.hasMapping("LEFT")) {
            inputManager.addListener(this, "LEFT");
            inputManager.addListener(this, "RIGHT");
            inputManager.addListener(this, "UP");
            inputManager.addListener(this, "DOWN");

            inputManager.addMapping("LEFT", new KeyTrigger(KeyInput.KEY_LEFT));
            inputManager.addMapping("RIGHT", new KeyTrigger(KeyInput.KEY_RIGHT));
            inputManager.addMapping("UP", new KeyTrigger(KeyInput.KEY_UP));
            inputManager.addMapping("DOWN", new KeyTrigger(KeyInput.KEY_DOWN));
        }
    }

    private void disableCharMapping() {
        if (inputManager.hasMapping("LEFT")) {
            inputManager.removeListener(this);

            inputManager.deleteMapping("LEFT");
            inputManager.deleteMapping("RIGHT");
            inputManager.deleteMapping("UP");
            inputManager.deleteMapping("DOWN");
        }
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (!keying) { //check for mouse panning on edge of screen if not using keys
            if (inputManager.getCursorPosition().y > (height - 15)) {
                up = true;
            } else if (inputManager.getCursorPosition().y < (height - 15)) {
                up = false;
            }
            if (inputManager.getCursorPosition().y < 15) {
                down = true;
            } else if (inputManager.getCursorPosition().y > 15) {
                down = false;
            }
            if (inputManager.getCursorPosition().x > (width - 15)) {
                right = true;
            } else if (inputManager.getCursorPosition().x < (width - 15)) {
                right = false;
            }
            if (inputManager.getCursorPosition().x < 15) {
                left = true;
            } else if (inputManager.getCursorPosition().x > 15) {
                left = false;
            }
        }

        //move camera if true
        if (up) {
            spatial.move(0, tpf * GVars.gvars.camspeed, 0);
        }
        if (down) {
            spatial.move(0, -tpf * GVars.gvars.camspeed, 0);
        }
        if (left) {
            spatial.move(-tpf * GVars.gvars.camspeed, 0, 0);
        }
        if (right) {
            spatial.move(tpf * GVars.gvars.camspeed, 0, 0);
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (isPressed) {
            if (name.equals("UP")) {
                up = true;
            }
            if (name.equals("DOWN")) {
                down = true;
            }
            if (name.equals("LEFT")) {
                left = true;
            }
            if (name.equals("RIGHT")) {
                right = true;
            }
            keying = true;
        } else {
            if (name.equals("UP")) {
                up = false;
            }
            if (name.equals("LEFT")) {
                left = false;
            }
            if (name.equals("DOWN")) {
                down = false;
            }
            if (name.equals("RIGHT")) {
                right = false;
            }
            keying = false;
        }
    }
}
