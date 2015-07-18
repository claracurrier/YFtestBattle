/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package playerPack;

import battlestatepack.EntityWrapper;
import battlestatepack.BattleMain;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
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
    private EntityWrapper dan, kirith;

    public SkillGraphic(Node gui, Node root, AssetManager aman,
            EntityWrapper dan, EntityWrapper kirith, InputManager input) {
        //set all the instance variables necessary to load graphics...
        //eg. rootNode, guiNode, inputManager, assetManager, etc.
        guiNode = gui;
        rootNode = root;
        assetManager = aman;
        this.dan = dan;
        this.kirith = kirith;
        this.input = input;
    }

    protected void removeTargetCursor() {
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

    protected void addPictureEffect(String effectName, Vector2f target, float timeShown) {
        //loads a special picture and overlays it on the target
        Node effect = new Node("piceffect");
        Texture tex = assetManager.loadTexture("Textures/" + effectName + ".png"); //placeholder
        int picw = tex.getImage().getWidth();
        int pich = tex.getImage().getHeight();
        Geometry geom = new Geometry("effect",
                new Quad(picw, pich));
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", tex);
        geom.setMaterial(mat);
        geom.setLocalTranslation(-picw / 2, -pich / 2, 0);

        effect.attachChild(geom);
        guiNode.attachChild(effect);
        effect.setLocalTranslation(target.x, target.y, 0);
        effect.addControl(new timer(timeShown));
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

    protected void makeArrow(String arrowName, Vector2f target, float power) {
        Node arrow = new Node("arrow");

        Geometry geom = new Geometry("Quad", new Quad(28f, 9f));
        Texture tex = assetManager.loadTexture("Textures/" + arrowName + ".png");
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", tex);
        geom.setMaterial(mat);
        float width = tex.getImage().getWidth();
        float height = tex.getImage().getHeight();

        arrow.setLocalTranslation(dan.getNode().getLocalTranslation());
        arrow.setUserData("halfwidth", width / 2);
        arrow.setUserData("halfheight", height / 2);
        arrow.setUserData("collided", false);
        arrow.setUserData("atkpower", power);
        arrow.setUserData("type", "arrow");

        arrow.attachChild(geom);
        arrow.addControl(new ArrowControl(target.subtract(
                dan.getNode().getLocalTranslation().x,
                dan.getNode().getLocalTranslation().y).normalizeLocal()));
        BattleMain.ATKNODE.attachChild(arrow);
    }
}
