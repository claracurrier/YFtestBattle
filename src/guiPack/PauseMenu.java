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
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.windows.Window;
import tonegod.gui.core.Screen;

/**
 *
 * @author PC
 */
public class PauseMenu {

    private Screen screen;
    private final AppStateManager asmr;
    private final MainMenu mm;

    public PauseMenu(Screen screen, AppStateManager asm, Application app) {
        this.screen = screen;
        this.asmr = asm;
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
                new Vector2f(15, 155), new Vector2f(200, 35)) {
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

        //controls
        ButtonAdapter controlMenuBtn = new ButtonAdapter(screen, "Controls",
                new Vector2f(15, 105)) {
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
    }

    public void exitToMM() {
        asmr.detach(asmr.getState(BattleMain.class));
        mm.makeStartMenu();
    }

    private void goToControl() {
        mm.setPaused(true);
        mm.getCM().makeControlMenu();
    }

    public void resume() {
        asmr.getState(BattleMain.class).setEnabled(true);
        mm.setPaused(false);
    }
}