/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlestatepack;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.scene.Node;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Line;
import com.jme3.scene.shape.Quad;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import java.util.Random;
import spriteProject.SpriteLibrary;

/**
 *
 * @author PC
 */
public class DanAppState extends AbstractAppState
        implements AnalogListener, ActionListener {

    private InputManager inputManager;
    private AssetManager assetManager;
    private final Spatial dan;
    private SimpleApplication appl;
    private PMoveAppState pmc;
    private final AppSettings settings;
    private SpriteLibrary spatSL;
    private Geometry line1, line2;
    private Vector3f playerPos;
    private float lsize = 140f;
    private float aimLimit = 35f;
    private float aim1, aim2, health;
    private boolean atkenabled;
    private final Random rand = new Random();

    public DanAppState(Spatial dan, AppSettings settings) {
        this.dan = dan;
        this.settings = settings;
        health = 100f;
    }

    @Override
    public void initialize(AppStateManager asm, Application app) {
        this.appl = (SimpleApplication) app;

        this.inputManager = appl.getInputManager();
        this.assetManager = appl.getAssetManager();

        pmc = asm.getState(PMoveAppState.class);

        if (!pmc.isEnabled()) {
            //if the movemap is disabled because kirith was dizzy
            pmc.setEnabled(true);
        }

        //set up lines
        Line l1 = new Line(Vector3f.ZERO, Vector3f.ZERO);
        l1.setLineWidth(2);
        line1 = new Geometry("line1", l1);
        Material color1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        color1.setColor("Color", ColorRGBA.Blue);
        line1.setMaterial(color1);

        Line l2 = new Line(Vector3f.ZERO, Vector3f.ZERO);
        l2.setLineWidth(2);
        line2 = new Geometry("line1", l2);
        Material color2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        color2.setColor("Color", ColorRGBA.Orange);
        line2.setMaterial(color2);

        spatSL = BattleMain.sEngine.getLibrary("Dan");
        setEnabled(true);
        System.out.println("Dan is in control!");
    }

    @Override
    public void cleanup() {
        disableAttackMap();
        System.out.println("Dan is no longer in control");
        super.cleanup();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        if (enabled) {
            enableAttackMap();
        } else {
            disableAttackMap();
        }
    }

    protected void enableAttackMap() {
        if (!inputManager.hasMapping("mousePick")) {
            inputManager.addMapping("mousePick", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
            inputManager.addListener(this, "mousePick");
        }
        atkenabled = true;
    }

    protected void disableAttackMap() {
        if (inputManager.hasMapping("mousePick")) {
            inputManager.deleteMapping("mousePick");
            inputManager.removeListener(this);
        }
        atkenabled = false;
    }

    private void fireArrow(float accuracy) {
        Spatial arrow = makeArrow(accuracy);
        BattleMain.ATKNODE.attachChild(arrow);
        float dir = ((rand.nextFloat()) * (aim1 - aim2) + aim2);
        arrow.addControl(new ArrowControl(lsize, 1500, 1500, dir, dan.getLocalTranslation()));
        System.out.println("arrow fired "+ accuracy);
    }

    private Node makeArrow(float accuracy) {
        Node node = new Node("arrow");

        Geometry geom = new Geometry("Quad", new Quad(28f, 9f));
        Texture tex = assetManager.loadTexture("Textures/Arrow.png");
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", tex);
        geom.setMaterial(mat);

//        adjust picture
        float width = tex.getImage().getWidth();
        float height = tex.getImage().getHeight();

        node.setLocalTranslation(playerPos);

        //add a material to the picture
        Material picMat = new Material(assetManager, "Common/MatDefs/Gui/Gui.j3md");
        picMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.AlphaAdditive);
        node.setMaterial(picMat);

        node.setUserData("halfwidth", width / 2);
        node.setUserData("halfheight", height / 2);
        node.setUserData("collided", false);
        node.setUserData("atkpower", accuracy);
        node.setUserData("type", "arrow");

        node.attachChild(geom);
        return node;
    }

    private Vector2f getAimDirection() {
        //aims bullets at the direction of the mouse click
        Vector2f mouse = inputManager.getCursorPosition();

        Vector2f newvec = new Vector2f((playerPos.x) - (settings.getWidth() / 2),
                (playerPos.y) - (settings.getHeight() / 2));

        mouse = mouse.add(newvec);

        Vector2f dif = new Vector2f(mouse.x - playerPos.x, mouse.y - playerPos.y);
        return dif.normalizeLocal();
    }
    private float power = 0f;
    private static boolean firing;

    public static boolean isfiring() {
        return firing;
    }

    public void onAnalog(String name, float value, float tpf) {
        if (power <= aimLimit / 2) {
            power += tpf * 10;
            lsize += tpf * 60;
        }
    }

    public void onAction(String name, boolean isPressed, float tpf) {

        if (name.equals("mousePick") && isPressed && !pmc.isMoving()) {
            firing = true;
            //make the gui
            appl.getRootNode().attachChild(line1);
            appl.getRootNode().attachChild(line2);
        }

        if (name.equals("mousePick") && !isPressed && !pmc.isMoving()) {
            appl.getRootNode().detachChild(line1);
            appl.getRootNode().detachChild(line2);
            if (firing) {
                fireArrow(power);
            }
            firing = false;
            power = 0;
            lsize = 140;
        }
    }

    private void updateLines(float aim, float range) {
        aim1 = -(FastMath.PI / 6) + (aim + range);
        Vector3f newvec = new Vector3f(lsize * FastMath.cos(aim1), lsize * FastMath.sin(aim1), 0f);
        newvec.addLocal(playerPos);
        ((Line) line1.getMesh()).updatePoints(playerPos, newvec);

        aim2 = (FastMath.PI / 6) + (aim - range);
        newvec = new Vector3f(lsize * FastMath.cos(aim2), lsize * FastMath.sin(aim2), 0f);
        newvec.addLocal(playerPos);
        ((Line) line2.getMesh()).updatePoints(playerPos, newvec);
    }

    @Override
    public void update(float tpf) {

        if (pmc.isMoving()) {
            if (atkenabled) {
                disableAttackMap();
            }
        } else {
            if (!atkenabled) {
                enableAttackMap();
            }
        }

        if (firing && pmc.isMoving()) {
            appl.getRootNode().detachChild(line1);
            appl.getRootNode().detachChild(line2);
            power = 0;
            firing = false;
        } else if (firing && !pmc.isMoving()) {
            //update the gui
            playerPos = dan.getLocalTranslation();
            float aim = getAimDirection().getAngle();
            updateLines(aim, power / aimLimit);


            //change dan's sprite based off of direction
            // use the idle ones for now, only 4 dir
            // voronoi region later
            if (aim > FastMath.PI / 4 && aim < 3 * FastMath.PI / 4) {
                //facing up
                spatSL.activateSprite(8);
                pmc.setDir(0);
            } else if (aim > -FastMath.PI / 4 && aim < FastMath.PI / 4) {
                //facing right
                spatSL.activateSprite(10);
                pmc.setDir(2);
            } else if (aim > -3 * FastMath.PI / 4 && aim < -FastMath.PI / 4) {
                //facing down
                spatSL.activateSprite(12);
                pmc.setDir(4);
            } else {
                //facing left
                spatSL.activateSprite(14);
                pmc.setDir(6);
            }
        }
    }

    public void reduceHealth(float damage) {
        health -= damage;
    }

    public float getHealth() {
        return health;
    }
}