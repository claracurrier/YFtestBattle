package battlestatepack;

import battlestatepack.mobPack.Mob;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.scene.shape.Quad;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import spriteProject.SpriteEngine;

/**
 *
 * @author PC
 */
public class BattleMain extends AbstractAppState implements ActionListener {

    private Node dan, kirith;
    private EntityMaker maker;
    private DanAppState danAppState;
    private KirithAppState kiAppState;
    private PMoveAppState pMAppState;
    private CollideAS collideAS;
    private final SimpleApplication app;
    private final AppSettings settings;
    private AssetManager assetManager;
    private AppStateManager stateManager;
    private final InputManager inputManager;
    private Camera cam;
    private final float frustumSize = 220f;
    private CameraNode camNode;
    public static final SpriteEngine sEngine = new SpriteEngine();
    public static final Node DEFNODE = new Node("defNode");
    public static final Node ATKNODE = new Node("atkNode");
    public static final Node BATTLENODE = new Node("battleNode");

    public BattleMain(SimpleApplication appl, AppSettings set, InputManager input) {
        this.app = appl;
        settings = set;
        inputManager = input;
    }

    @Override
    public void initialize(AppStateManager asm, Application appl) {
        stateManager = asm;
        assetManager = app.getAssetManager();
        app.getRootNode().attachChild(BATTLENODE);

        BATTLENODE.attachChild(DEFNODE);
        BATTLENODE.attachChild(ATKNODE);
        collideAS = new CollideAS();
        stateManager.attach(collideAS);
        maker = new EntityMaker(assetManager, stateManager);
        
        //spawn and set up Dan
        dan = maker.createSpatial("Dan");
        dan.move(settings.getWidth() / 2, settings.getHeight() / 2, 0);
        danAppState = new DanAppState(dan, settings);
        stateManager.attach(danAppState);
        collideAS.setMovingSpatial(dan);

        //spawn Kirith
        kirith = maker.createSpatial("Kirith");
        kirith.move(settings.getWidth() / 3, settings.getHeight() / 3, 0);
        kiAppState = new KirithAppState(kirith);

        //spawn a Mob
        Spatial mobSpat = maker.createSpatial("Wanderer");
        Mob mob = new Mob(mobSpat, "Wanderer", 0, dan, kirith);
        mobSpat.move(500, 500, 0);

        //Set up Camera
        makeCam();
        
        //SwitchChar mapping
        if(!inputManager.hasMapping("switchChar")){
            inputManager.addMapping("switchChar", new KeyTrigger(KeyInput.KEY_G));
            inputManager.addListener(this, "switchChar");
            }

        //Map
        Geometry geom = new Geometry("Quad", new Quad(1500f, 1500f));
        Texture tex = assetManager.loadTexture("Textures/testBattle.png");
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", tex);
        geom.setMaterial(mat);
        geom.center();
        geom.move(470f, 550f, -2f);
        app.getRootNode().attachChild(geom);

        //set up movement
        pMAppState = new PMoveAppState(850f, 850f, inputManager);
        stateManager.attach(pMAppState);
        pMAppState.setSpatial(dan);

        DEFNODE.attachChild(dan);
        DEFNODE.attachChild(kirith);
    }

    @Override
    public void update(float tpf) {
        sEngine.update(tpf);
    }

    @Override
    public void render(RenderManager rm) {
    }

    private void switchChar() {
        if (stateManager.hasState(danAppState)) {
            collideAS.setMovingSpatial(kirith);
            pMAppState.setSpatial(kirith);
            stateManager.detach(danAppState);
            stateManager.attach(kiAppState);

            dan.detachChild(camNode);
            kirith.attachChild(camNode);
            look(kirith);

        } else if (stateManager.hasState(kiAppState)) {
            collideAS.setMovingSpatial(dan);
            pMAppState.setSpatial(dan);
            stateManager.detach(kiAppState);
            stateManager.attach(danAppState);

            kirith.detachChild(camNode);
            dan.attachChild(camNode);
            look(dan);
        }
    }

    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("switchChar") && !isPressed) {
            switchChar();
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (stateManager.hasState(danAppState)) {
            danAppState.setEnabled(enabled);
        } else if (stateManager.hasState(kiAppState)) {
            kiAppState.setEnabled(enabled);
        }

        collideAS.setEnabled(enabled);
        pMAppState.setEnabled(enabled);

        if (enabled) {
            if(!inputManager.hasMapping("switchChar")){
            inputManager.addMapping("switchChar", new KeyTrigger(KeyInput.KEY_G));
            inputManager.addListener(this, "switchChar");
            }
        } else {
            if(inputManager.hasMapping("switchChar")){
            inputManager.deleteMapping("switchChar");
            inputManager.removeListener(this);
            }
        }

    }

    @Override
    public void cleanup() {
        app.getRootNode().detachAllChildren();
        maker = null;
        inputManager.removeListener(this);
        stateManager.detach(pMAppState);
        stateManager.detach(kiAppState);
        stateManager.detach(danAppState);
        pMAppState = null;
        kiAppState = null;
        danAppState = null;
        
        ATKNODE.detachAllChildren();
        DEFNODE.detachAllChildren();
        
        sEngine.destroyEngine();
        super.cleanup();
    }

    private void makeCam() {
        //setup camera for 2D games
        cam = app.getCamera();
        //app.getRootNode().setCullHint(Spatial.CullHint.Never);
        float aspect = (float) cam.getWidth() / cam.getHeight();
        cam.setFrustum(-100, 100, -aspect * frustumSize, aspect * frustumSize, frustumSize, -frustumSize);
        //key: near, far, left, right, top, bottom
        cam.setLocation(new Vector3f(cam.getWidth() / 2, cam.getHeight() / 2, 10f));
        cam.setParallelProjection(true);
        camNode = new CameraNode("Camera Node", cam);
//This mode means that camera copies the movements of the target:
        camNode.setControlDir(ControlDirection.SpatialToCamera);
        camNode.setLocalTranslation(new Vector3f(0, 0, 10f));
        look(dan);
        //Attach the camNode to the target:
        dan.attachChild(camNode);


        /*
         * TODO: fix scaling issues w/ different resolutions
         */
    }

    private void look(Spatial s) {
        camNode.lookAt(s.getWorldTranslation(), Vector3f.UNIT_Y);
        camNode.rotate(camNode.getLocalRotation().fromAngleAxis(-FastMath.PI / 2, Vector3f.UNIT_Y));
    }
}
