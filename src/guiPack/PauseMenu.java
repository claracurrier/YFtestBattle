/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guiPack;

import battlestatepack.BattleMain;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import com.jme3.scene.Node;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.windows.Window;
import tonegod.gui.core.Screen;

/**
 *
 * @author PC
 */
public class PauseMenu {
    
    private Screen screen;
    private Node guiNode;
    private AppStateManager asmr;
    private MainMenu mm;
    
    public PauseMenu(Node gui, Screen screen, AppStateManager asm, Application app) {
        guiNode = gui;
        this.screen = screen;
        this.asmr = asm;
        guiNode.addControl(screen);
        mm = (MainMenu) app;
    }
    
    public void makePauseMenu() {
        final Window win = new Window(screen, "win", new Vector2f(15, 15));
        screen.addElement(win);

        //resume button
        ButtonAdapter resumeGame = new ButtonAdapter(screen, "Resume",
                new Vector2f(15, 55), new Vector2f(100, 35)) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                screen.removeElement(win);
                resume();
            }
        };
        resumeGame.setFont("Interface/Fonts/Arial.fnt");
        resumeGame.setText("Resume");
        resumeGame.setTextAlign(BitmapFont.Align.Center);
        win.addChild(resumeGame);

        //exit button
        ButtonAdapter exitToMenu = new ButtonAdapter(screen, "ExitToMenu",
                new Vector2f(15, 105), new Vector2f(200, 35)) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                screen.removeElement(win);
                exitToMM();
            }
        };
        exitToMenu.setFont("Interface/Fonts/Arial.fnt");
        exitToMenu.setText("Return to Main Menu");
        exitToMenu.setTextAlign(BitmapFont.Align.Center);
        win.addChild(exitToMenu);
    }
    
    public void exitToMM() {
        asmr.detach(asmr.getState(BattleMain.class));
        mm.makeStartMenu();
    }
    
    public void resume() {
        asmr.getState(BattleMain.class).setEnabled(true);
        MainMenu.isPaused = false;
        
    }
}
