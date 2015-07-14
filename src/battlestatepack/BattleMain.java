package battlestatepack;

import MapPack.MapAppState;
import MapPack.MapLoader;
import playerPack.*;
import mobPack.MobAS;
import cameraPack.CameraOptions;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import guiPack.MainMenu;
import spriteProject.SpriteEngine;

/**
 *
 * @author Clara Currier
 */
public class BattleMain extends AbstractAppState implements ActionListener {

    private final Node danNode, kiNode;
    private final EntityMaker maker;
    private final Player danLogic, kiLogic;
    private final CollideAS collideAS;
    private final SimpleApplication app;
    private final AssetManager assetManager;
    private final AppStateManager stateManager;
    private final InputManager inputManager;
    private final PCollideCont danCC, kiCC;
    private final BattleGUI battleGUI;
    private final Picker picker;
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
        app.getRootNode().attachChild(BATTLENODE);
        BATTLENODE.attachChild(DEFNODE);
        BATTLENODE.attachChild(ATKNODE);
        collideAS = new CollideAS();
        maker = new EntityMaker(assetManager);
        picker = new Picker(app.getCamera(), inputManager, app.getRootNode(), this);

        //set up characters
        danNode = maker.createSpatial("Dan");
        danNode.move(settings.getWidth() / 2, settings.getHeight() / 2, 0);
        danLogic = new DanAS(danNode, settings);
        danCC = new PCollideCont(danLogic);

        kiNode = maker.createSpatial("Kirith");
        kiNode.move(settings.getWidth() / 3, settings.getHeight() / 3, 0);
        kiLogic = new KirithAS(kiNode);
        kiCC = new PCollideCont(kiLogic);


        battleGUI = new BattleGUI(settings.getWidth(), settings.getHeight(),
                danLogic, kiLogic);
    }

    @Override
    public void initialize(AppStateManager asm, Application appl) {
        makeCamera();
        makeMap();
        switchCharKey(true);
        picker.enableMouseMapping(true);

        //spawn a MobAS
        Spatial mobSpat = maker.createSpatial("Wanderer");
        mob = new MobAS(mobSpat, "Wanderer", danNode, kiNode);
        mobSpat.setLocalTranslation(500, 240, 0);
        //disabled mob for now
        mob.setEnabled(false);

        //load all the states
        stateManager.attach(collideAS);
        stateManager.attach(danLogic);
        stateManager.attach(battleGUI);
        stateManager.attach(mob);

        stateManager.getState(MapAppState.class).setActiveChar(danNode);
        picker.setActiveChar(danLogic);
        collideAS.setMovingSpatial(danNode);

        danNode.addControl(danCC);
        kiNode.addControl(kiCC);
        DEFNODE.attachChild(danNode);
        DEFNODE.attachChild(kiNode);
    }

    @Override
    public void update(float tpf) {
        sEngine.update(tpf);
        checkComplete();
    }

    public void switchChar() {
        if (stateManager.hasState(danLogic)) {
            //if kirith is now in control
            collideAS.setMovingSpatial(kiNode);
            stateManager.detach(danLogic);
            stateManager.attach(kiLogic);
            picker.setActiveChar(kiLogic);
            stateManager.getState(MapAppState.class).setActiveChar(kiNode);

            CameraOptions.options.setChar(kiNode);
            battleGUI.setActiveHUD(kiLogic);

        } else if (stateManager.hasState(kiLogic)) {
            //if dan is now in control
            collideAS.setMovingSpatial(danNode);
            stateManager.detach(kiLogic);
            stateManager.attach(danLogic);
            picker.setActiveChar(danLogic);
            stateManager.getState(MapAppState.class).setActiveChar(danNode);

            CameraOptions.options.setChar(danNode);
            battleGUI.setActiveHUD(danLogic);
        }
    }

    public boolean getCurChar() {
        //true for dan, false for ki
        return stateManager.hasState(danLogic);
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
        if (stateManager.hasState(danLogic)) {
            danLogic.setEnabled(enabled);
        } else if (stateManager.hasState(kiLogic)) {
            kiLogic.setEnabled(enabled);
        }

        if (danNode.getControl(AutoAttackCont.class) != null) {
            danNode.getControl(AutoAttackCont.class).setEnabled(enabled);
        }
        if (kiNode.getControl(AutoAttackCont.class) != null) {
            kiNode.getControl(AutoAttackCont.class).setEnabled(enabled);
        }

        collideAS.setEnabled(enabled);
        mob.setEnabled(enabled);
        CameraOptions.options.enableCharMapping(enabled);
        CameraOptions.options.getCurrentCamera().setEnabled(enabled);
        stateManager.getState(MapAppState.class).setEnabled(enabled);
        switchCharKey(enabled);
        picker.enableMouseMapping(enabled);
    }

    @Override
    public void cleanup() {
        app.getRootNode().detachAllChildren();
        ATKNODE.detachAllChildren();
        DEFNODE.detachAllChildren();
        picker.enableMouseMapping(false);
        inputManager.removeListener(this);
        stateManager.detach(stateManager.getState(MapAppState.class));
        stateManager.detach(kiLogic);
        stateManager.detach(danLogic);
        stateManager.detach(battleGUI);
        stateManager.detach(mob);
        sEngine.destroyEngine();
        app.getViewPort().setBackgroundColor(ColorRGBA.Black);
        CameraOptions.options.setActive(false);
        super.cleanup();
    }

    protected void switchCharKey(boolean enabled) {
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

    private void makeMap() {
        GVars.gvars.mapheight = 90 * 16; //modified when loading map
        GVars.gvars.mapwidth = 120 * 16;

        MapLoader mapMaker = new MapLoader(app.getRootNode(), assetManager);
        mapMaker.makeTiledMap("test_large_collision");
        mapMaker.makeImageMap("test_large", GVars.gvars.mapwidth, GVars.gvars.mapheight, 15, 20);
        stateManager.attach(new MapAppState());
        app.getViewPort().setBackgroundColor(ColorRGBA.Brown);
    }

    private void makeCamera() {
        CameraOptions camOps = CameraOptions.options;
        camOps.setActive(true);
        camOps.setup(app.getCamera(), inputManager, danNode, app.getRootNode());
        camOps.makeCamBox();
        camOps.setChar(danNode);
        camOps.setCamSetting(camOps.getCamSetting());
    }

    private void checkComplete() {
        //checks the health of dan, kirith, or the monster to see if it's below
        //threshold and if so, launch a victory or defeat screen
        if (danLogic.getHealth() <= 0 || kiLogic.getHealth() <= 0) {
            endGame(false);
        } else if (ATKNODE.getQuantity() == 0) {
            //this method would change depending on the script being loaded
            endGame(true);
        }
    }

    private void endGame(boolean victory) {
        inputManager.deleteMapping("pause");
        setEnabled(false);
        MainMenu.endGame.makeEndGameScreen(victory);
    }
}