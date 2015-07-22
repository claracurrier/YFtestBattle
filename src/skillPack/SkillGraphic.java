/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package skillPack;

import battlestatepack.EntityWrapper;
import battlestatepack.BattleMain;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.debug.WireBox;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import playerPack.ArrowControl;

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

    public void makeArrow(String arrowName, Vector2f target, float power) {
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

    private Geometry tempWireBox(float width, float height) {
        //temp wirebox to see the hitboxes
        Geometry g = new Geometry("attackBox", new WireBox(width, height, 0));
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", ColorRGBA.Blue);
        g.setMaterial(mat);
        return g;
    }

    public void makeAttackBox(Vector3f source, Vector3f target, float power, String name) {
        int dir = findDirectionBetween(source, target);

        Node node = new Node(name);

        float x = source.x;
        float y = source.y;
        float boxw = 0;
        float boxh = 0;

        switch (dir) {
            //moves the node to appropriate location relative to ki
            case 0: //up
                node.setLocalTranslation(x, y + 50f, 0);
                boxw = 15;
                boxh = 50;
                break;
            case 1:
                node.setLocalTranslation(x + 30f, y + 50f, 0);
                boxw = 30;
                boxh = 30;
                break;
            case 2: //right
                node.setLocalTranslation(x + 30f, y, 0);
                boxw = 50;
                boxh = 15;
                break;
            case 3:
                node.setLocalTranslation(x + 30f, y - 50f, 0);
                boxw = 30;
                boxh = 30;
                break;
            case 4: //down
                node.setLocalTranslation(x, y - 50f, 0);
                boxw = 15;
                boxh = 50;
                break;
            case 5:
                node.setLocalTranslation(x - 30f, y - 50f, 0);
                boxw = 30;
                boxh = 30;
                break;
            case 6: //left
                node.setLocalTranslation(x - 30f, y, 0);
                boxw = 50;
                boxh = 15;
                break;
            case 7:
                node.setLocalTranslation(x - 30f, y + 50f, 0);
                boxw = 30;
                boxh = 30;
                break;
        }

        node.setUserData("type", name);
        node.setUserData("collided", "none");
        node.setUserData("atkpower", power);
        node.setUserData("halfwidth", boxw / 2);
        node.setUserData("halfheight", boxh / 2);
        node.attachChild(tempWireBox(boxw, boxh));

        node.addControl(new AbstractControl() {
            float hbtimer = 0;

            @Override
            protected void controlUpdate(float tpf) {
                if (hbtimer > .2f || !spatial.getUserData("collided").equals("none")) {
                    spatial.removeFromParent();
                } else {
                    hbtimer += tpf;
                }
            }

            @Override
            protected void controlRender(RenderManager rm, ViewPort vp) {
            }
        });

        BattleMain.ATKNODE.attachChild(node);
    }

    private int findDirectionBetween(Vector3f targ, Vector3f cur) {
        Vector2f newvec = new Vector2f(cur.x, cur.y).subtractLocal(targ.x, targ.y);
        float aim = newvec.normalizeLocal().getAngle();

        if (aim <= 5 * FastMath.PI / 6 && aim > 2 * FastMath.PI / 3) {
            //up left
            return 7;
        } else if (aim <= 2 * FastMath.PI / 3 && aim > FastMath.PI / 3) {
            //facing up
            return 0;
        } else if (aim <= FastMath.PI / 3 && aim > FastMath.PI / 6) {
            //up right
            return 1;
        } else if (aim <= FastMath.PI / 6 && aim > -FastMath.PI / 6) {
            //facing right
            return 2;
        } else if (aim <= -FastMath.PI / 6 && aim > -FastMath.PI / 3) {
            //down right
            return 3;
        } else if (aim <= -FastMath.PI / 3 && aim > -2 * FastMath.PI / 3) {
            //facing down
            return 4;
        } else if (aim <= -2 * FastMath.PI / 3 && aim > -5 * FastMath.PI / 6) {
            //down left
            return 5;
        } else {
            //facing left
            return 6;
        }
    }
}
