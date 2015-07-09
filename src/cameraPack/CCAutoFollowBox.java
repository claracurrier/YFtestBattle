/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cameraPack;

import battlestatepack.GVars;
import com.jme3.scene.Node;

/**
 *
 * @author Clara Currier
 */
public class CCAutoFollowBox extends CameraControl {

    private Node camBox = CameraOptions.options.getCamBox();

    public CCAutoFollowBox(int w, int h) {
        width = w;
        height = h;
    }

    @Override
    public void setup() {
        for (int i = 0; i < 4; i++) {
            Node camBoxOutline = new Node("camBox" + i);
            camBox.attachChild(camBoxOutline);
            camBoxOutline.setUserData("halfwidth", 100f);
            camBoxOutline.setUserData("halfheight", 100f);
            switch (i) { //positioning
                case 0: //left
                    camBoxOutline.move(-200, 0, 0);
                    break;
                case 1: //right
                    camBoxOutline.move(200, 0, 0);
                    break;
                case 2: //down
                    camBoxOutline.move(0, -200, 0);
                    break;
                case 3: //up
                    camBoxOutline.move(0, 200, 0);
                    break;
            } // the spacing and positions should be based on screen dimensions
        }
        
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (camBox.getUserData("moveLeft")) {
            camBox.move(-tpf * GVars.gvars.camspeed, 0, 0);
        }
        if (camBox.getUserData("moveRight")) {
            camBox.move(tpf * GVars.gvars.camspeed, 0, 0);
        }
        if (camBox.getUserData("moveUp")) {
            camBox.move(0, tpf * GVars.gvars.camspeed, 0);
        }
        if (camBox.getUserData("moveDown")) {
            camBox.move(0, -tpf * GVars.gvars.camspeed, 0);
        }
    }
}
