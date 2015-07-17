/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package menuPack;

import battlestatepack.BattleMain;
import battlestatepack.ReferenceRegistry;
import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import tonegod.gui.controls.windows.Window;
import tonegod.gui.core.Screen;

/**
 *
 * @author Clara Currier
 */
public class MainMenu extends SimpleApplication {

    public static void main(String[] args) {
        MainMenu app = new MainMenu();
        AppSettings newSettings = new AppSettings(true);
        newSettings.setFrameRate(60);
        app.setSettings(newSettings);
        app.start();
    }
    private static Screen screen;
    private PauseMenu pauseMenu;
    private ControlMenu controlMenu;
    private OptionsMenu optionsMenu;
    private EndGame endGame;
    private Credits credits;
    private Node tgGuiNode;
    private boolean ispaused = false;
    private ScheduledThreadPoolExecutor executor;

    @Override
    public void simpleInitApp() {
        getFlyByCamera().setEnabled(false); //enables mouse
        ReferenceRegistry.registry.register(MainMenu.class, this);

        tgGuiNode = new Node("tgGuiNode");
        screen = new Screen(this);
        guiNode.attachChild(tgGuiNode);
        tgGuiNode.addControl(screen);

        executor = new ScheduledThreadPoolExecutor(2);

        pauseMenu = new PauseMenu(screen, stateManager, this);
        controlMenu = new ControlMenu(screen, stateManager, this);
        optionsMenu = new OptionsMenu(screen, this, settings);
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

    public void pause() {
        BattleMain bM = stateManager.getState(BattleMain.class);
        if (bM != null) {
            bM.setEnabled(false);
            ispaused = true;
            pauseMenu.makePauseMenu();
        }
    }

    public boolean isPaused() {
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
    public void destroy() {
        super.destroy();
        executor.shutdown();
    }

    public ScheduledThreadPoolExecutor getExecutor() {
        return executor;
    }

    public EndGame getEndGameMenu() {
        return endGame;
    }
}