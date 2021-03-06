/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cameraPack;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author Clara Currier
 */
public class CCAutoFollowLocked extends CameraControl {

    private Node activeChar, rootNode, camBox;
    private CameraOptions camOps;

    public CCAutoFollowLocked(int w, int h, Node firstChar, Node root, CameraOptions camops) {
        width = w;
        height = h;
        activeChar = firstChar;
        rootNode = root;
        camOps = camops;
        camBox = camops.getCamBox();
    }

    public void updateChar(Node newchar) {
        activeChar = newchar;
        if (camOps.getCamSetting().equals("AutoFollowLocked")) {
            activeChar.attachChild(camBox);
            camBox.setLocalTranslation(Vector3f.ZERO);
        }
    }

    @Override
    protected void controlUpdate(float tpf) {
    }

    @Override
    public void setup() {
        activeChar.attachChild(camBox);
        camBox.setLocalTranslation(Vector3f.ZERO);
        camOps.resetLocation();
    }

    @Override
    public void takedown() {
        rootNode.attachChild(camBox);
    }
}