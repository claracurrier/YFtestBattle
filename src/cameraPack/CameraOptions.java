/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cameraPack;

import com.jme3.input.InputManager;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl.ControlDirection;

/**
 *
 * @author Clara Currier
 */
public class CameraOptions {

    private Camera cam;
    private CameraNode camNode;
    private Node camBox = new Node("camBox");
    private Node activeChar;
    private String camSetting = "Manual";
    private final float frustumSize = 220f;
    private Node rootNode;
    private boolean active = false;
    private CameraControl afBox, afMidpoint, afLocked, manual;

    public CameraOptions(Camera c, InputManager in, Node firstChar, Node root) {
        cam = c;
        rootNode = root;
        camNode = new CameraNode("Camera Node", cam);
        makeCamBox();

        int w = cam.getWidth();
        int h = cam.getHeight();
        afLocked = new CCAutoFollowLocked(w, h, firstChar, root, this);
        manual = new CCManual(w, h, in);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        if (!active) {
            cleanup();
        }
    }

    public Camera getCamera() {
        return cam;
    }

    public void cleanup() {
        camBox.detachAllChildren();
        camNode = null;
        camBox.removeFromParent();
    }

    public CameraControl getCameraControl(String c) {
        if (c.equals("AutoFollowBox")) {
            return afBox;
        } else if (c.equals("AutoFollowMidpoint")) {
            return afMidpoint;
        } else if (c.equals("AutoFollowLocked")) {
            return afLocked;
        } else {
            return manual;
        }
    }

    public CameraControl getCurrentCamera() {
        return getCameraControl(camSetting);
    }

    public String getCamSetting() {
        return camSetting;
    }

    public Node getCamBox() {
        return camBox;
    }

    public void setCamSetting(String setting) {
        if (!active) {
            camSetting = setting;
            return;
        }

        camBox.removeControl(getCameraControl(camSetting));
        getCameraControl(camSetting).takedown();

        camSetting = setting;
        getCameraControl(setting).setup();
        camBox.addControl(getCameraControl(setting));
    }

    private void makeCamBox() {
        float aspect = (float) cam.getWidth() / cam.getHeight();
        cam.setFrustum(-100, 100, -aspect * frustumSize, aspect * frustumSize, frustumSize, -frustumSize);
        //key: near, far, left, right, top, bottom
        cam.setLocation(new Vector3f(cam.getWidth() / 2, cam.getHeight() / 2, 5f));
        cam.setParallelProjection(true);
        //This mode means that camera copies the movements of the target:
        camNode.setControlDir(ControlDirection.SpatialToCamera);
        camNode.setLocalTranslation(new Vector3f(0, 0, 10f));
        camNode.lookAt(camBox.getWorldTranslation(), Vector3f.UNIT_Y);
        camNode.rotate(camNode.getLocalRotation().fromAngleAxis(-FastMath.PI / 2, Vector3f.UNIT_Y));

        camBox.setUserData("moveLeft", false);
        camBox.setUserData("moveRight", false);
        camBox.setUserData("moveUp", false);
        camBox.setUserData("moveDown", false);

        camBox.attachChild(camNode);
        rootNode.attachChild(camBox);
    }

    public void setChar(Node character) {
        activeChar = character;
        ((CCAutoFollowLocked) afLocked).updateChar(character);
    }

    public Spatial getChar() {
        return activeChar;
    }

    public void resetLocation() {
        camBox.setLocalTranslation(activeChar.getLocalTranslation());
    }
}
