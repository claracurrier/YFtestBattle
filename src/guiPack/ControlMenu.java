/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guiPack;

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
public class ControlMenu {

    private final Screen screen;
    private final AppStateManager asmr;
    private final MainMenu mm;

    public ControlMenu(Screen screen, AppStateManager asm, Application app) {

        this.asmr = asm;
        this.screen = screen;
        mm = (MainMenu) app;
    }

    public void makeControlMenu() {
        final Window win = new Window(screen, "controlwin", new Vector2f(15, 15));
        screen.addElement(win);
        win.setIsResizable(false);
        win.setIsMovable(false);
        win.setIgnoreMouse(true);

        Label txtbox = new Label(screen, "controltext", new Vector2f(15, 15),
                new Vector2f(300, 200));
        txtbox.setText(" To move: use the arrow keys or the WASD keys."
                + "\n To attack as Dan: use the mouse button. Hold down to charge."
                + "\n To attack as Kirith: use the i or o keys. Hold down to chage"
                + "\n To switch characters: use the g key"
                + "\n To pause the game: use the h key");
        txtbox.setFont("Interface/Fonts/Arial.fnt");
        txtbox.setTextAlign(BitmapFont.Align.Left);
        txtbox.setTextWrap(LineWrapMode.Word);
        txtbox.setFontSize(16f);
        win.addChild(txtbox);

        //go back button
        ButtonAdapter goBackBtn = new ButtonAdapter(screen, "GoBack",
                new Vector2f(15, 200)) {
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