/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlestatepack;

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

    public static CameraOptions options = new CameraOptions();
    private Camera cam;
    private CameraNode camNode;
    private Node camBox = new Node("camBox");
    private String camSetting = "AutoFollow";
    private final float frustumSize = 220f;
    private InputManager input;

    private CameraOptions() { //singleton
    }

    public void setup(Camera c, InputManager in) {
        cam = c;
        camNode = new CameraNode("Camera Node", cam);
        input = in;
    }

    public String getCamSetting() {
        return camSetting;
    }

    public Node getCamBox() {
        return camBox;
    }

    public void setCamSetting(String setting) {
        camSetting = setting;
        camBox.detachAllChildren(); //clears the Node
        camBox.attachChild(camNode);
        try {
            camBox.removeControl(camBox.getControl(0));
        } catch (Exception e) {
        };

        if (setting.equals("AutoFollow")) {
            makeAutoFollowCam();
            camBox.addControl(new CameraContAutoFollow());
        } else if (setting.equals("Manual")) {
            camBox.addControl(new CameraContManual(cam.getWidth(), cam.getHeight(), input));
        }
    }

    public void makeCamBox(Node rootNode) {
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

        camBox.attachChild(camNode);
        rootNode.attachChild(camBox);
    }

    private void makeAutoFollowCam() {
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

    public void setAutoFollowCam(Spatial character) {
        camBox.setLocalTranslation(character.getLocalTranslation());
    }
}
