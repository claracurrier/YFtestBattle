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
import tonegod.gui.controls.buttons.ButtonAdapter;
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
    private Screen screen;
    private PauseMenu pauseMenu;
    private Node tgGuiNode;

    @Override
    public void simpleInitApp() {
        getFlyByCamera().setEnabled(false); //enables mouse

        tgGuiNode = new Node("tgGuiNode");
        screen = new Screen(this);
        guiNode.attachChild(tgGuiNode);
        tgGuiNode.addControl(screen);

        pauseMenu = new PauseMenu(guiNode, screen, stateManager, this);

        makeStartMenu();

    }

    public void makeStartMenu() {
        //set up the mapping for the pause button
        inputManager.addMapping("pause", new KeyTrigger(KeyInput.KEY_H));
        inputManager.addListener(this, "pause");

        final Window win = new Window(screen, "win", new Vector2f(15, 15));
        screen.addElement(win);

        ButtonAdapter startGame = new ButtonAdapter(screen, "Start", new Vector2f(15, 55)) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                BattleMain battleMain = new BattleMain((SimpleApplication) app, settings, inputManager);
                stateManager.attach(battleMain);
                screen.removeElement(win);

            }
        };
        startGame.setFont("Interface/Fonts/Arial.fnt");
        startGame.setText("Start");
        startGame.setTextAlign(BitmapFont.Align.Center);

        win.addChild(startGame);
        isPaused = false;
    }
    public static boolean isPaused = false;

    private void pause() {
        BattleMain bM = stateManager.getState(BattleMain.class);
        if (bM != null) {
            bM.setEnabled(false);
            pauseMenu.makePauseMenu();
            isPaused = true;
        }
    }

    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("pause") && !isPressed && !isPaused) {
            pause();
        }
        if (name.equals("pause") && !isPressed && isPaused) {
            pauseMenu.resume();
        }
    }
}
