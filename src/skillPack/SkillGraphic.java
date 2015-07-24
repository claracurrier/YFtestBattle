/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package skillPack;

import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.debug.WireBox;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;

/**
 *
 * @author Clara Currier
 */
public class SkillGraphic {

    private Node guiNode, rootNode;
    private InputManager input;
    private AssetManager assetManager;

    public SkillGraphic(Node gui, Node root, AssetManager aman, InputManager input) {
        /*
         * This class controls the way skills look
         * 
         * Eventually, it should be possible to pass in a collection of points,
         * and what kind of picture to display to put up the particles.
         * 
         * The mouse and GUI guide are also handled here based on the type of
         * attack that is passed in (changes the helper's look)
         */

        //set all the instance variables necessary to load graphics...
        guiNode = gui;
        rootNode = root;
        assetManager = aman;
        this.input = input;
    }

    public void removeTargetCursor() {
        if (guiNode.getChild("mouse") != null) {
            guiNode.detachChildNamed("mouse");
        }
    }

    protected void makeTargetCursor() {
        //attaches a little picture to the guiNode that follows the root
        //only one variety right now, will eventually accomodate aoe, sprays
        Node mouse = new Node("mouse");
        Geometry geom = new Geometry("mouse", new Quad(20f, 20f));
        Texture tex = assetManager.loadTexture("Textures/headshot.png"); //placeholder
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", tex);
        geom.setMaterial(mat);
        geom.setLocalTranslation(-10, -10, 0);
        mouse.attachChild(geom);

        guiNode.attachChild(mouse);
        mouse.addControl(new AbstractControl() { //mouse follower
            @Override
            protected void controlUpdate(float tpf) {
                if (!spatial.getLocalTranslation().equals(
                        new Vector3f(input.getCursorPosition().x,
                        input.getCursorPosition().y, 0))) {
                    spatial.setLocalTranslation(input.getCursorPosition().x,
                            input.getCursorPosition().y, 0);
                }
            }

            @Override
            protected void controlRender(RenderManager rm, ViewPort vp) {
            }
        });
    }

    protected Node addPictureEffect(String effectName, float timeShown,
            float picw, float pich, boolean setSize) {
        //loads a special picture and overlays it on the target
        Node effect = new Node("piceffect");
        Texture tex = assetManager.loadTexture("Textures/" + effectName + ".png");

        float w, h;
        if (setSize) {
            w = picw;
            h = pich;
        } else {
            w = tex.getImage().getWidth();
            h = tex.getImage().getHeight();
        }
        effect.setUserData("width", w);
        effect.setUserData("height", h);

        Geometry geom = new Geometry("effect",
                new Quad(w, h));
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", tex);
        geom.setMaterial(mat);
        geom.setLocalTranslation(-w / 2, -h / 2, 0);

        effect.attachChild(geom);
        effect.addControl(new timer(timeShown));
        return effect;
    }

    private class timer extends AbstractControl {

        private float counter = 0;
        private float length;

        public timer(float length) {
            this.length = length;
        }

        @Override
        protected void controlUpdate(float tpf) {
            if (counter < length) {
                counter += tpf;
            } else {
                spatial.removeFromParent();
            }
        }

        @Override
        protected void controlRender(RenderManager rm, ViewPort vp) {
        }
    }

    public Geometry tempWireBox(float width, float height) {
        //temp wirebox to see the hitboxes
        Geometry g = new Geometry("attackBox", new WireBox(width, height, 0));
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", ColorRGBA.Blue);
        g.setMaterial(mat);
        return g;
    }
}
