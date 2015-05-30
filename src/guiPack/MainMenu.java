/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guiPack;

import battlestatepack.BattleMain;
import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import tonegod.gui.controls.windows.Window;
import tonegod.gui.core.Screen;

/**
 *
 * @author PC
 */
public class MainMenu extends SimpleApplication implements ActionListener {

    public static void main(String[] args) {
        // BattleMain app = new BattleMain();
        MainMenu app = new MainMenu();
        AppSettings newSettings = new AppSettings(true);
        newSettings.setFrameRate(200);
        app.setSettings(newSettings);
        app.start();
    }
    private static Screen screen;
    private PauseMenu pauseMenu;
    private ControlMenu controlMenu;
    private OptionsMenu optionsMenu;
    public static EndGame endGame;
    private Credits credits;
    private Node tgGuiNode;
    private static boolean ispaused = false;

    @Override
    public void simpleInitApp() {
        getFlyByCamera().setEnabled(false); //enables mouse

        tgGuiNode = new Node("tgGuiNode");
        screen = new Screen(this);
        guiNode.attachChild(tgGuiNode);
        tgGuiNode.addControl(screen);

        pauseMenu = new PauseMenu(screen, stateManager, this);
        controlMenu = new ControlMenu(screen, stateManager, this);
        optionsMenu = new OptionsMenu(screen, this);
        credits = new Credits(screen, this);
        endGame = new EndGame(screen, stateManager, this);
        makeStartMenu();
    }

    public static Screen getScreen() {
        return screen;
    }

    public PauseMenu getPM() {
        return pauseMenu;
    }

    public OptionsMenu getOM() {
        return optionsMenu;
    }

    public ControlMenu getCM() {
        return controlMenu;
    }

    public void makeStartMenu() {
        //set up the mapping for the pause button
        if (!inputManager.hasMapping("pause")) {
            inputManager.addMapping("pause", new KeyTrigger(KeyInput.KEY_H));
            inputManager.addListener(this, "pause");
        }

        final Window win = new Window(screen, "win", new Vector2f(15, 15),
                new Vector2f(settings.getWidth() - 30, settings.getHeight() - 30));
        screen.addElement(win);
        win.setIsResizable(false);
        win.setWindowIsMovable(false);
        win.setIgnoreMouse(true);

        //start
        MyButton startGameBtn = new MyButton(screen, "Start", new Vector2f(15, 55)) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                BattleMain battleMain = new BattleMain((SimpleApplication) app,
                        settings, inputManager, stateManager);
                stateManager.attach(battleMain);
                screen.removeElement(win);

            }
        };
        startGameBtn.setFont("Interface/Fonts/Arial.fnt");
        startGameBtn.setText("Start");
        startGameBtn.setTextAlign(BitmapFont.Align.Center);
        win.addChild(startGameBtn);

        //controls
        MyButton controlMenuBtn = new MyButton(screen, "Controls", new Vector2f(15, 105)) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                screen.removeElement(win);
                goToControl();
            }
        };
        controlMenuBtn.setFont("Interface/Fonts/Arial.fnt");
        controlMenuBtn.setText("Controls");
        controlMenuBtn.setTextAlign(BitmapFont.Align.Center);
        win.addChild(controlMenuBtn);

        //Options
        MyButton optionMenuBtn = new MyButton(screen, "Options",
                new Vector2f(15, 155)) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                screen.removeElement(win);
                goToOptions();
            }
        };
        optionMenuBtn.setFont("Interface/Fonts/Arial.fnt");
        optionMenuBtn.setText("Options");
        optionMenuBtn.setTextAlign(BitmapFont.Align.Center);
        win.addChild(optionMenuBtn);

        //Credits
        MyButton creditBtn = new MyButton(screen, "Credits",
                new Vector2f(15, 205)) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                screen.removeElement(win);
                goToCredits();
            }
        };
        creditBtn.setFont("Interface/Fonts/Arial.fnt");
        creditBtn.setText("Credits");
        creditBtn.setTextAlign(BitmapFont.Align.Center);
        win.addChild(creditBtn);

        //Exit
        MyButton exitBtn = new MyButton(screen, "Exit",
                new Vector2f(15, 255)) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                screen.removeElement(win);
                app.stop();
            }
        };
        exitBtn.setFont("Interface/Fonts/Arial.fnt");
        exitBtn.setText("Exit");
        exitBtn.setTextAlign(BitmapFont.Align.Center);
        win.addChild(exitBtn);

        ispaused = false;
    }

    private void pause() {
        BattleMain bM = stateManager.getState(BattleMain.class);
        if (bM != null) {
            bM.setEnabled(false);
            ispaused = true;
            pauseMenu.makePauseMenu();
        }
    }

    public static boolean isPaused() {
        return ispaused;
    }

    public void setPaused(boolean p) {
        ispaused = p;
    }

    private void goToControl() {
        controlMenu.makeControlMenu();
    }

    private void goToOptions() {
        optionsMenu.makeOptionsMenu();
    }

    private void goToCredits() {
        credits.makeCredits();
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("pause") && !isPressed && !ispaused) {
            pause();
        }
    }
}