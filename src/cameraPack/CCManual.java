/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cameraPack;

import battlestatepack.GVars;
import battlestatepack.ReferenceRegistry;
import com.jme3.input.InputManager;

/**
 *
 * @author Clara Currier
 */
public class CCManual extends CameraControl {

    private boolean ku, kd, kl, kr, keying;
    private InputManager inputManager;

    public CCManual(int w, int h, InputManager in) {
        width = w;
        height = h;
        inputManager = in;
    }

    @Override
    public void setup() {
        ReferenceRegistry.registry.register(CCManual.class, this);
    }

    @Override
    public void takedown() {
        ReferenceRegistry.registry.remove(CCManual.class);
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (!keying) { //check for mouse movt on edge of screen if not using keys
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

        //check that camera isn't on the edge of the screen
        if (spatial.getLocalTranslation().x - width / 2 < 0) {
            kl = false;
        } else if (spatial.getLocalTranslation().x + width / 2 > GVars.gvars.mapwidth) {
            kr = false;
        }
        if (spatial.getLocalTranslation().y - height / 2 < 0) {
            kd = false;
        } else if (spatial.getLocalTranslation().y + height / 2 > GVars.gvars.mapheight) {
            ku = false;
        }

        //move camera if true
        //TODO: still not working...
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
        if (name.equals("up") && isPressed) {
            ku = true;
            keying = true;
        } else if (name.equals("up") && !isPressed) {
            ku = false;
            keying = false;
        }
        if (name.equals("down") && isPressed) {
            kd = true;
            keying = true;
        } else if (name.equals("down") && !isPressed) {
            kd = false;
            keying = false;
        }
        if (name.equals("left") && isPressed) {
            kl = true;
            keying = true;
        } else if (name.equals("left") && !isPressed) {
            kl = false;
            keying = false;
        }
        if (name.equals("right") && isPressed) {
            kr = true;
            keying = true;
        } else if (name.equals("right") && !isPressed) {
            kr = false;
            keying = false;
        }
    }
}
