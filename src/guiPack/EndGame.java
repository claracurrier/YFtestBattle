/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guiPack;

import battlestatepack.BattleMain;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.LineWrapMode;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.text.Label;
import tonegod.gui.controls.windows.Window;
import tonegod.gui.core.Screen;

/**
 *
 * @author PC
 */
public class EndGame {

    private Screen screen;
    private final AppStateManager asmr;
    private final MainMenu mm;

    public EndGame(Screen screen, AppStateManager asm, Application app) {
        this.screen = screen;
        this.asmr = asm;
        mm = (MainMenu) app;
    }

    public void makeEndGameScreen(boolean victory) {
        final Window win = new Window(screen, "win", new Vector2f(15, 15),
                new Vector2f(100, 300));
        screen.addElement(win);
        win.setIsResizable(false);
        win.setIsMovable(false);
        win.setIgnoreMouse(true);

        Label txtbox = new Label(screen, "text", new Vector2f(15, 15),
                new Vector2f(300, 200));
        txtbox.setFont("Interface/Fonts/Arial.fnt");
        txtbox.setTextAlign(BitmapFont.Align.Left);
        txtbox.setTextWrap(LineWrapMode.Word);
        txtbox.setFontSize(16f);
        win.addChild(txtbox);
        if (victory) {
            txtbox.setText("You win!");
        } else {
            txtbox.setText("You lost.");
        }
        
        //exit button
        ButtonAdapter exitToMenu = new ButtonAdapter(screen, "ExitToMenu",
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
}