/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guiPack;

import com.jme3.app.Application;
import com.jme3.font.BitmapFont;
import com.jme3.font.LineWrapMode;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import tonegod.gui.controls.text.Label;
import tonegod.gui.controls.windows.Window;
import tonegod.gui.core.Screen;

/**
 *
 * @author PC
 */
public class Credits {

    private final Screen screen;
    private final MainMenu mm;

    public Credits(Screen screen, Application app) {
        this.screen = screen;
        mm = (MainMenu) app;
    }

    public void makeCredits() {
        final Window win = new Window(screen, "creditswin", new Vector2f(15, 15));
        screen.addElement(win);
        win.setIsResizable(false);
        win.setIsMovable(false);
        win.setIgnoreMouse(true);

        Label txtbox = new Label(screen, "credittext", new Vector2f(15, 15),
                new Vector2f(300, 200));
        txtbox.setText("Copyright 2015 by Clara Currier and Yuri Kim."
                + " All rights reserved. See license for details."
                + " Special thanks to Jen, Autumn, and the JME3 Community for their support.");
        txtbox.setFont("Interface/Fonts/Arial.fnt");
        txtbox.setTextAlign(BitmapFont.Align.Left);
        txtbox.setTextWrap(LineWrapMode.Word);
        txtbox.setFontSize(16f);
        win.addChild(txtbox);

        //go back button
        MyButton goBackBtn = new MyButton(screen, "GoBack",
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
        mm.makeStartMenu();
    }
}