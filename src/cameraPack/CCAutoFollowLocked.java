/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cameraPack;

import com.jme3.scene.Node;

/**
 *
 * @author Clara Currier
 */
public class CCAutoFollowLocked extends CameraControl {

    private Node activeChar;
    private Node camBox = CameraOptions.options.getCamBox();

    public CCAutoFollowLocked(int w, int h, Node firstChar) {
        width = w;
        height = h;
        activeChar = firstChar;
    }

    public void updateChar(Node newchar) {
        if (activeChar.hasChild(camBox)) {
            activeChar.detachChild(camBox);
        }
        activeChar = newchar;
        if (CameraOptions.options.getCamSetting().equals("AutoFollowLocked")) {
            activeChar.attachChild(camBox);
        }

    }

    @Override
    protected void controlUpdate(float tpf) {
        if (CameraOptions.options.getCamSetting().equals("AutoFollowLocked")) {
            if (!camBox.getLocalTranslation().equals(activeChar.getLocalTranslation())) {
                camBox.setLocalTranslation(activeChar.getLocalTranslation());
            }
        }
    }

    @Override
    public void setup() {
        activeChar.attachChild(camBox);
    }
}