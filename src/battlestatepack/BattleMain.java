package battlestatepack;

import MapPack.MapAppState;
import MapPack.MapLoader;
import playerPack.*;
import mobPack.MobWrapper;
import cameraPack.CameraOptions;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import menuPack.MainMenu;
import spriteProject.SpriteEngine;

/**
 *
 * @author Clara Currier
 */
public class BattleMain extends AbstractAppState {

    private final Node danNode, kiNode;
    private final EntityMaker maker;
    private final EntityWrapper danLogic, kiLogic;
    private final CollideAS collideAS;
    private final SimpleApplication app;
    private final AssetManager assetManager;
    private final AppStateManager stateManager;
    private final InputManager inputManager;
    private final PCollideCont danCC, kiCC;
    private CameraOptions camOps;
    private final BattleGUI battleGUI;
    private final Picker picker;
    public static AppSettings settings;
    public static final SpriteEngine sEngine = new SpriteEngine();
    public static final Node DEFNODE = new Node("defNode");
    public static final Node ATKNODE = new Node("atkNode");
    public static final Node BATTLENODE = new Node("battleNode");
    private MobWrapper mob;
    private InputSystem inputSystem;

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
        picker = new Picker(app.getCamera(), app.getRootNode(), this);


        //set up characters
        danNode = maker.createSpatial("Dan");
        danNode.move(settings.getWidth() / 2, settings.getHeight() / 2, 0);
        danLogic = new DanWrapper(danNode);
        danCC = new PCollideCont(danLogic);

        kiNode = maker.createSpatial("Kirith");
        kiNode.move(settings.getWidth() / 3, settings.getHeight() / 3, 0);
        kiLogic = new KirithWrapper(kiNode);
        kiCC = new PCollideCont(kiLogic);

        SkillBehavior behavior = new SkillBehavior();
        SkillGraphic graphic = new SkillGraphic(app.getGuiNode(), app.getRootNode(),
                assetManager, danLogic, kiLogic, input);
        SkillCooldown cooldown = new SkillCooldown();
        PSkills pskill = new PSkills(danLogic, kiLogic, app.getCamera(),
                graphic, cooldown, behavior);
        SkillMapper skillmap = new SkillMapper(input, pskill);

        inputSystem = new InputSystem(inputManager, skillmap);
        battleGUI = new BattleGUI(settings.getWidth(), settings.getHeight(),
                danLogic, kiLogic, inputSystem);

        register();
        makeCamera();
        makeMap();
        makeInput();

        ((DanWrapper) danLogic).setGraphic(graphic);
        cooldown.setSkillmap(skillmap);
        cooldown.setBattleGUI(battleGUI);
    }

    @Override
    public void initialize(AppStateManager asm, Application appl) {
        //spawn a MobWrapper
        Node mobSpat = maker.createSpatial("Wanderer");
        mob = new MobWrapper(mobSpat, "Wanderer", danNode, kiNode);
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

            camOps.setChar(kiNode);
            battleGUI.setActiveHUD(kiLogic);

        } else if (stateManager.hasState(kiLogic)) {
            //if dan is now in control
            collideAS.setMovingSpatial(danNode);
            stateManager.detach(kiLogic);
            stateManager.attach(danLogic);
            picker.setActiveChar(danLogic);
            stateManager.getState(MapAppState.class).setActiveChar(danNode);

            camOps.setChar(danNode);
            battleGUI.setActiveHUD(danLogic);
        }
    }

    public boolean getCurChar() {
        //true for dan, false for ki
        return stateManager.hasState(danLogic);
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

        inputSystem.setEnabled(enabled);
        collideAS.setEnabled(enabled);
        mob.setEnabled(enabled);
        stateManager.getState(MapAppState.class).setEnabled(enabled);
    }

    @Override
    public void cleanup() {
        app.getRootNode().detachAllChildren();
        ATKNODE.detachAllChildren();
        DEFNODE.detachAllChildren();
        stateManager.detach(stateManager.getState(MapAppState.class));
        stateManager.detach(kiLogic);
        stateManager.detach(danLogic);
        stateManager.detach(battleGUI);
        stateManager.detach(mob);
        sEngine.destroyEngine();
        inputSystem.setEnabled(false);
        app.getViewPort().setBackgroundColor(ColorRGBA.Black);
        camOps.setActive(false);
        super.cleanup();
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
        camOps = new CameraOptions(app.getCamera(), inputManager, danNode, app.getRootNode());
        camOps.setActive(true);
        camOps.setChar(danNode);
        camOps.setCamSetting(camOps.getCamSetting());
    }

    private void makeInput() {
        inputSystem.setEnabled(true);

        inputSystem.setSkillMapping("dbuttonleft", PSkills.Skills.tripleShot);
        inputSystem.setSkillMapping("dbuttonmid", PSkills.Skills.headshot);
        inputSystem.setSkillMapping("dbuttonright", PSkills.Skills.nothing);
        inputSystem.setSkillMapping("kbuttonleft", PSkills.Skills.stun);
        inputSystem.setSkillMapping("kbuttonmid", PSkills.Skills.push);
        inputSystem.setSkillMapping("kbuttonright", PSkills.Skills.nothing);
    }

    private void register() {
        ReferenceRegistry reg = ReferenceRegistry.registry;

        reg.register(CameraOptions.class, camOps);
        reg.register(BattleMain.class, this);
        reg.register(Picker.class, picker);
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
        ((MainMenu) ReferenceRegistry.registry.get(MainMenu.class))
                .getEndGameMenu().makeEndGameScreen(victory);
    }
}