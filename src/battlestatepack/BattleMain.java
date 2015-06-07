package battlestatepack;

import playerPack.PAICont;
import playerPack.PMoveAppState;
import playerPack.PCollideCont;
import playerPack.KirithAppState;
import playerPack.DanAppState;
import mobPack.MobAS;
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
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.scene.shape.Quad;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import guiPack.MainMenu;
import spriteProject.SpriteEngine;

/**
 *
 * @author PC
 */
public class BattleMain extends AbstractAppState implements ActionListener {

    private final Node dan, kirith;
    private final EntityMaker maker;
    private final DanAppState danAppState;
    private final KirithAppState kiAppState;
    private final PMoveAppState pMAppState, pMAppStateAI;
    private final CollideAS collideAS;
    private final SimpleApplication app;
    private final AssetManager assetManager;
    private final AppStateManager stateManager;
    private final InputManager inputManager;
    private final PCollideCont danCC, kiCC;
    private final Camera cam;
    private final float frustumSize = 220f;
    private final CameraNode camNode;
    private final BattleGUI battleGUI;
    private final PAICont danAI, kiAI;
    public static AppSettings settings;
    public static final SpriteEngine sEngine = new SpriteEngine();
    public static final Node DEFNODE = new Node("defNode");
    public static final Node ATKNODE = new Node("atkNode");
    public static final Node BATTLENODE = new Node("battleNode");
    private MobAS mob;

    public BattleMain(SimpleApplication appl, AppSettings set, InputManager input, AppStateManager asm) {
        this.app = appl;
        settings = set;
        inputManager = input;
        stateManager = asm;
        assetManager = app.getAssetManager();
        cam = app.getCamera();
        camNode = new CameraNode("Camera Node", cam);
        app.getRootNode().attachChild(BATTLENODE);
        BATTLENODE.attachChild(DEFNODE);
        BATTLENODE.attachChild(ATKNODE);
        collideAS = new CollideAS();
        maker = new EntityMaker(assetManager, stateManager);
        pMAppState = new PMoveAppState(850f, 850f, inputManager);
        pMAppStateAI = new PMoveAppState(850f, 850f, inputManager);

        //set up characters
        dan = maker.createSpatial("Dan");
        dan.move(settings.getWidth() / 2, settings.getHeight() / 2, 0);
        danAppState = new DanAppState(dan, settings);
        danCC = new PCollideCont(pMAppState);
        danAI = new PAICont(pMAppStateAI);

        kirith = maker.createSpatial("Kirith");
        kirith.move(settings.getWidth() / 3, settings.getHeight() / 3, 0);
        kiAppState = new KirithAppState(kirith);
        kiCC = new PCollideCont(pMAppState);
        kiAI = new PAICont(pMAppStateAI);

        //set up movement
        battleGUI = new BattleGUI(settings.getWidth(), settings.getHeight(),
                danCC, kiCC);
    }

    @Override
    public void initialize(AppStateManager asm, Application appl) {
        //spawn a MobAS
        Spatial mobSpat = maker.createSpatial("Wanderer");
        mob = new MobAS(mobSpat, "Wanderer", dan, kirith);
        mobSpat.move(500, 500, -1);
        //temp disabling
        //mob.setEnabled(false);

        //SwitchChar mapping
        if (!inputManager.hasMapping("switchChar")) {
            inputManager.addMapping("switchChar", new KeyTrigger(KeyInput.KEY_G));
            inputManager.addListener(this, "switchChar");
        }

        makeCam();
        makeMap();

        //load all the states
        stateManager.attach(collideAS);
        stateManager.attach(danAppState);
        collideAS.setMovingSpatial(dan);
        stateManager.attach(pMAppState);
        stateManager.attach(pMAppStateAI);
        stateManager.attach(battleGUI);
        stateManager.attach(mob);

        pMAppState.setSpatial(dan);
        pMAppStateAI.setSpatial(kirith);
        danAI.setOtherChar(kirith);
        danAI.setEnabled(false);
        kiAI.setOtherChar(dan);
        dan.addControl(danCC);
        kirith.addControl(kiCC);
        dan.addControl(danAI);
        kirith.addControl(kiAI);
        DEFNODE.attachChild(dan);
        DEFNODE.attachChild(kirith);
    }

    @Override
    public void update(float tpf) {
        sEngine.update(tpf);
        checkComplete();
    }

    private void switchChar() {
        if (stateManager.hasState(danAppState)) {
            //if kirith is now in control
            collideAS.setMovingSpatial(kirith);
            pMAppState.setSpatial(kirith);
            stateManager.detach(danAppState);
            stateManager.attach(kiAppState);
            kiAI.setEnabled(false);

            pMAppStateAI.setSpatial(dan);
            dan.detachChild(camNode);
            kirith.attachChild(camNode);
            battleGUI.setActiveHUD(kiCC);
            danAI.setEnabled(true);
            look(kirith);

        } else if (stateManager.hasState(kiAppState)) {
            //if dan is now in control
            collideAS.setMovingSpatial(dan);
            pMAppState.setSpatial(dan);
            stateManager.detach(kiAppState);
            stateManager.attach(danAppState);
            danAI.setEnabled(false);

            pMAppStateAI.setSpatial(kirith);
            kirith.detachChild(camNode);
            dan.attachChild(camNode);
            battleGUI.setActiveHUD(danCC);
            kiAI.setEnabled(true);
            look(dan);
        }
    }

    @Override
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
        danAI.setEnabled(enabled);
        kiAI.setEnabled(enabled);
        pMAppStateAI.setEnabled(enabled);
        mob.setEnabled(enabled);

        if (enabled) {
            if (!inputManager.hasMapping("switchChar")) {
                inputManager.addMapping("switchChar", new KeyTrigger(KeyInput.KEY_G));
                inputManager.addListener(this, "switchChar");
            }
        } else {
            if (inputManager.hasMapping("switchChar")) {
                inputManager.deleteMapping("switchChar");
                inputManager.removeListener(this);
            }
        }

    }

    @Override
    public void cleanup() {
        app.getRootNode().detachAllChildren();
        ATKNODE.detachAllChildren();
        DEFNODE.detachAllChildren();
        inputManager.removeListener(this);
        stateManager.detach(pMAppState);
        stateManager.detach(pMAppStateAI);
        stateManager.detach(kiAppState);
        stateManager.detach(danAppState);
        stateManager.detach(battleGUI);
        stateManager.detach(mob);
        sEngine.destroyEngine();
        super.cleanup();
    }

    private void makeCam() {
        //app.getRootNode().setCullHint(Spatial.CullHint.Never);
        float aspect = (float) cam.getWidth() / cam.getHeight();
        cam.setFrustum(-100, 100, -aspect * frustumSize, aspect * frustumSize, frustumSize, -frustumSize);
        //key: near, far, left, right, top, bottom
        cam.setLocation(new Vector3f(cam.getWidth() / 2, cam.getHeight() / 2, 10f));
        cam.setParallelProjection(true);
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

    private void makeMap() {
        //at some point this will read a real map
        Geometry geom = new Geometry("Quad", new Quad(1500f, 1500f));
        Texture tex = assetManager.loadTexture("Textures/testBattle.png");
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", tex);
        geom.setMaterial(mat);
        geom.center();
        geom.move(470f, 550f, -2f);
        app.getRootNode().attachChild(geom);
    }

    private void look(Spatial s) {
        camNode.lookAt(s.getWorldTranslation(), Vector3f.UNIT_Y);
        camNode.rotate(camNode.getLocalRotation().fromAngleAxis(-FastMath.PI / 2, Vector3f.UNIT_Y));
    }

    private void checkComplete() {
        //checks the health of dan, kirith, or the monster to see if it's below
        //threshold and if so, launch a victory or defeat screen
        if (danCC.getHealth() <= 0 || kiCC.getHealth() <= 0) {
            endGame(false);
        } else if (ATKNODE.getQuantity() == 0) {
            //this method would change depending on the script being loaded
            endGame(true);
        }
    }

    public Spatial getDan() {
        return dan;
    }

    public Spatial getKi() {
        return kirith;
    }

    private void endGame(boolean victory) {
        inputManager.deleteMapping("pause");
        setEnabled(false);
        MainMenu.endGame.makeEndGameScreen(victory);
    }
}