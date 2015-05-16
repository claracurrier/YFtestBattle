/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guiPack;

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
public class ControlMenu {

    private Screen screen;
    private final AppStateManager asmr;
    private final MainMenu mm;

    public ControlMenu(Screen screen, AppStateManager asm, Application app) {

        this.asmr = asm;
        this.screen = screen;
        mm = (MainMenu) app;
    }

    public void makeControlMenu() {
        final Window win = new Window(screen, "win", new Vector2f(15, 15));
        screen.addElement(win);

        //go back button
        ButtonAdapter goBackBtn = new ButtonAdapter(screen, "GoBack",
                new Vector2f(15, 105)) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                screen.removeElement(win);
                goBack();
            }
        };
        goBackBtn.setFont("Interface/Fonts/Arial.fnt");
        goBackBtn.setText("Back");
        goBackBtn.setTextAlign(BitmapFont.Align.Center);
        win.addChild(goBackBtn);
    }

    private void goBack() {
        if (mm.isPaused()) {
            mm.getPM().makePauseMenu();
        } else {
            mm.makeStartMenu();
        }
    }
}