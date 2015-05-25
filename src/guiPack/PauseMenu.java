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
        final Window win = new Window(screen, "pausewin", new Vector2f(15, 15),
                new Vector2f(100, 300));
        screen.addElement(win);
        win.setIsResizable(false);
        win.setIsMovable(false);
        win.setIgnoreMouse(true);

        //resume button
        MyButton resumeGame = new MyButton(screen, "Resume",
                new Vector2f(15, 55), new Vector2f(100, 35)) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                resume();
            }
        };
        resumeGame.setFont("Interface/Fonts/Arial.fnt");
        resumeGame.setText("Resume");
        resumeGame.setTextAlign(BitmapFont.Align.Center);
        win.addChild(resumeGame);

        //controls
        MyButton controlMenuBtn = new MyButton(screen, "Controls",
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

        //options
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

        //exit button
        MyButton exitToMenu = new MyButton(screen, "ExitToMenu",
                new Vector2f(15, 205), new Vector2f(200, 35)) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                screen.removeElement(win);
                goToMM();
            }
        };
        exitToMenu.setFont("Interface/Fonts/Arial.fnt");
        exitToMenu.setText("Return to Main Menu");
        exitToMenu.setTextAlign(BitmapFont.Align.Center);
        win.addChild(exitToMenu);
    }

    public void goToMM() {
        asmr.detach(asmr.getState(BattleMain.class));
        mm.makeStartMenu();
    }

    private void goToControl() {
        mm.setPaused(true);
        mm.getCM().makeControlMenu();
    }

    private void goToOptions() {
        mm.setPaused(true);
        mm.getOM().makeOptionsMenu();
    }

    public void resume() {
        screen.removeElement(screen.getElementById("pausewin"));
        asmr.getState(BattleMain.class).setEnabled(true);
        mm.setPaused(false);
    }
}