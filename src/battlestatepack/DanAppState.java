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
import spriteProject.SpriteLibrary;

/**
 *
 * @author PC
 */
public class DanAppState extends AbstractAppState
        implements AnalogListener, ActionListener {

    private InputManager inputManager;
    private AssetManager assetManager;
    private Spatial dan;
    private SimpleApplication appl;
    private PMoveAppState pmc;
    private AppSettings settings;
    private SpriteLibrary spatSL = BattleMain.sEngine.getLibrary("Dan");
    private Vector2f mouse;
    private Geometry line1;
    private Vector3f playerPos;

    public DanAppState(Spatial dan, AppSettings settings) {
        this.dan = dan;
        this.settings = settings;
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
        Line l1 = new Line(Vector3f.ZERO,Vector3f.ZERO);
        l1.setLineWidth(2);
        line1 = new Geometry("line1", l1);
        Material blue = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        blue.setColor("Color", ColorRGBA.Blue);
        line1.setMaterial(blue);

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
        inputManager.addMapping("mousePick", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(this, "mousePick");
    }

    protected void disableAttackMap() {
        inputManager.deleteMapping("mousePick");
        inputManager.removeListener(this);
    }

    private void fireArrow(float accuracy) {
        Spatial arrow = makeArrow(accuracy);

        ((Node) ((Node) appl.getRootNode().getChild("battleNode"))
                .getChild("atkNode")).attachChild(arrow);

        arrow.addControl(new ArrowControl(getAimDirection(), 1500, 1500));
        System.out.println("Arrow fired, power=" + accuracy);
        power = 0;
    }

    private Node makeArrow(float power) {
        Node node = new Node("arrow");

        Geometry geom = new Geometry("Quad", new Quad(28f, 9f));
        Texture tex = assetManager.loadTexture("Textures/Arrow.png");
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", tex);
        geom.setMaterial(mat);
        geom.move(14f, 4.5f, 0f);
        //geom.center();


//        adjust picture
        float width = tex.getImage().getWidth();
        float height = tex.getImage().getHeight();

        node.setLocalTranslation(playerPos);

//        add a material to the picture
        Material picMat = new Material(assetManager, "Common/MatDefs/Gui/Gui.j3md");
        picMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.AlphaAdditive);
        node.setMaterial(picMat);

        node.setUserData("halfwidth", width / 2);
        node.setUserData("halfheight", height / 2);

        node.setUserData("collided", false);
        node.setUserData("atkpower", power);

        node.setUserData("type", "arrow");

        node.attachChild(geom);
        return node;
    }

    private Vector3f getAimDirection() {
        //aims bullets at the direction of the mouse click
        Vector3f dif = new Vector3f(mouse.x - playerPos.x, mouse.y - playerPos.y, 0f);
        return dif.normalizeLocal();
    }
    
    private float power;
    private boolean firing;

    public void onAnalog(String name, float value, float tpf) {
        power += tpf * 100f;

    }

    public void onAction(String name, boolean isPressed, float tpf) {

        if (name.equals("mousePick") && isPressed) {
            firing = true;
            //make the gui
            appl.getRootNode().attachChild(line1);
        }

        if (name.equals("mousePick") && !isPressed) {
            appl.getRootNode().detachChild(line1);
            fireArrow(power);
            firing = false;
        }
    }

    @Override
    public void update(float tpf) {
        if (firing && !pmc.isMoving()) {

            //update the gui
            playerPos = dan.getLocalTranslation();

        
            mouse = inputManager.getCursorPosition();
            mouse = mouse.addLocal(((playerPos.x) - (settings.getWidth() / 2)),
                (playerPos.y) - (settings.getHeight() / 2));
            ((Line) line1.getMesh()).updatePoints(
                    playerPos, new Vector3f(mouse.x, mouse.y, 0));

            //change dan's sprite based off of direction
            // use the idle ones for now, only 4 dir
            // voronoi region later
            // needs cleaning up for sprites
            float aim = ArrowControl.getAngleFromVector(getAimDirection());

            if (aim > FastMath.PI / 4 && aim < 3 * FastMath.PI / 4) {
                //facing up
                spatSL.activateSprite(8);
            } else if (aim > -FastMath.PI / 4 && aim < FastMath.PI / 4) {
                //facing right
                spatSL.activateSprite(10);
            } else if (aim > -3 * FastMath.PI / 4 && aim < -FastMath.PI / 4) {
                //facing down
                spatSL.activateSprite(12);
            } else {
                //facing left
                spatSL.activateSprite(14);
            }
        }
    }
}
