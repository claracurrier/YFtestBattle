/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cameraPack;

import battlestatepack.GVars;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;

/**
 *
 * @author Clara Currier
 */
public class CCManual extends CameraControl implements ActionListener {

    private boolean ku, kd, kl, kr, keying;
    private InputManager inputManager;

    public CCManual(int w, int h, InputManager in) {
        width = w;
        height = h;
        inputManager = in;

        enableCharMapping();
    }

    @Override
    public void setup() {
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
                ku = true;
            } else if (inputManager.getCursorPosition().y < (height - 15)) {
                ku = false;
            }
            if (inputManager.getCursorPosition().y < 15) {
                kd = true;
            } else if (inputManager.getCursorPosition().y > 15) {
                kd = false;
            }
            if (inputManager.getCursorPosition().x > (width - 15)) {
                kr = true;
            } else if (inputManager.getCursorPosition().x < (width - 15)) {
                kr = false;
            }
            if (inputManager.getCursorPosition().x < 15) {
                kl = true;
            } else if (inputManager.getCursorPosition().x > 15) {
                kl = false;
            }
        }

        //move camera if true
        if (ku && kl && !kr) {//upleft
            spatial.move(-tpf * GVars.gvars.camspeed, tpf * GVars.gvars.camspeed, 0);

        } else if (ku && kr && !kl) {//upright
            spatial.move(tpf * GVars.gvars.camspeed, tpf * GVars.gvars.camspeed, 0);

        } else if (ku && !kr && !kl) {//up
            spatial.move(0, tpf * GVars.gvars.camspeed, 0);

        } else if (kd && kl && !kr) {//downleft
            spatial.move(-tpf * GVars.gvars.camspeed, -tpf * GVars.gvars.camspeed, 0);

        } else if (kd && kr && !kl) {//downright
            spatial.move(tpf * GVars.gvars.camspeed, -tpf * GVars.gvars.camspeed, 0);

        } else if (kd && !kl && !kr) {//down
            spatial.move(0, -tpf * GVars.gvars.camspeed, 0);

        } else if (kl && !ku && !kd) {//left
            spatial.move(-tpf * GVars.gvars.camspeed, 0, 0);

        } else if (kr && !ku && !kd) {//right
            spatial.move(tpf * GVars.gvars.camspeed, 0, 0);
        }
    }

    public void move(String name, boolean isPressed) {
        //this whole method is an elaborate way of checking for key combos
        //ultimately setting the value of keyPressed will tell update which way to go
        if (name.equals("UP") && isPressed) {
            ku = true;
        } else if (name.equals("UP") && !isPressed) {
            ku = false;
        }
        if (name.equals("DOWN") && isPressed) {
            kd = true;
        } else if (name.equals("DOWN") && !isPressed) {
            kd = false;
        }
        if (name.equals("LEFT") && isPressed) {
            kl = true;
        } else if (name.equals("LEFT") && !isPressed) {
            kl = false;
        }
        if (name.equals("RIGHT") && isPressed) {
            kr = true;
        } else if (name.equals("RIGHT") && !isPressed) {
            kr = false;
        }
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        move(name, isPressed);
        if (isPressed) {
            keying = true;
        } else {
            keying = false;
        }
    }
}
