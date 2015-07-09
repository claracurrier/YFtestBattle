/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guiPack;

import battlestatepack.BattleGUI;
import cameraPack.CameraOptions;
import com.jme3.app.Application;
import com.jme3.font.BitmapFont;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import com.jme3.system.AppSettings;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import tonegod.gui.controls.lists.SelectBox;
import tonegod.gui.controls.windows.Window;
import tonegod.gui.core.Screen;

/**
 *
 * @author Clara Currier
 */
public class OptionsMenu {

    private final Screen screen;
    private final MainMenu mm;
    private AppSettings settings;

    public OptionsMenu(Screen screen, Application app, AppSettings s) {
        this.screen = screen;
        mm = (MainMenu) app;
        settings = s;
    }

    public void makeOptionsMenu() {
        final Window win = new Window(screen, "optionswin", new Vector2f(15, 15));
        screen.addElement(win);
        win.setIsResizable(false);
        win.setWindowIsMovable(false);
        win.setIgnoreMouse(true);

        final GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        final DisplayMode[] modes = device.getDisplayModes();

        //Resolution list
        SelectBox resList = new SelectBox(screen, "Resolutions", new Vector2f(15, 40)) {
            @Override
            public void onChange(int i, Object o) {
                if (o != null) {
                    int[] res = (int[]) o;
                    settings.setResolution(res[0], res[1]);
                    app.setSettings(settings);
                    app.restart();
                    win.setPosition(15, res[1] - 15 - win.getHeight());
                    if (mm.getStateManager().getState(BattleGUI.class) != null) {
                        mm.getStateManager().getState(BattleGUI.class).changeRes(res[0], res[1]);
                    }
                    if (CameraOptions.options.isActive()) {
                        CameraOptions.options.getCurrentCamera().refreshScreenDim(res[0], res[1]);
                    }
                }
            }
        };
        resList.addListItem("Resolution", null);
        ArrayList<String> resolutions = new ArrayList<>(modes.length);
        for (int i = 0; i < modes.length; i++) {
            int height = modes[i].getHeight();
            int width = modes[i].getWidth();
            if (width >= 800 && height >= 600) {
                String res = width + " x " + height;
                if (!resolutions.contains(res)) {
                    resolutions.add(res);
                    resList.addListItem(res, new int[]{width, height});
                }
            }
        }
        win.addChild(resList);

        //fullscreen box
        MyButton fullscreenBtn = new MyButton(screen, "fullscreen", new Vector2f(15, 70)) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                DisplayMode mode = device.getDisplayMode();
                settings.setResolution(mode.getWidth(), mode.getHeight());
                settings.setFrequency(mode.getRefreshRate());
                settings.setBitsPerPixel(mode.getBitDepth());
                settings.setFullscreen(toggled);
                app.setSettings(settings);
                app.restart();
                win.setPosition(15, mode.getHeight() - 15 - win.getHeight());
                if (mm.getStateManager().getState(BattleGUI.class) != null) {
                    mm.getStateManager().getState(BattleGUI.class).changeRes(mode.getWidth(), mode.getHeight());
                }
                if (CameraOptions.options.isActive()) {
                    CameraOptions.options.getCurrentCamera().refreshScreenDim(mode.getWidth(), mode.getHeight());
                }
            }
        };
        fullscreenBtn.setIsToggleButton(true);
        fullscreenBtn.setFont("Interface/Fonts/Arial.fnt");
        fullscreenBtn.setText("Fullscreen");
        fullscreenBtn.setTextAlign(BitmapFont.Align.Center);
        fullscreenBtn.setIsToggledNoCallback(settings.isFullscreen());
        win.addChild(fullscreenBtn);

        //Vsync button
        MyButton vsyncBtn = new MyButton(screen, "vsync", new Vector2f(15, 105)) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                settings.setVSync(toggled);
                app.setSettings(settings);
                app.restart();
            }
        };
        vsyncBtn.setIsToggleButton(true);
        vsyncBtn.setFont("Interface/Fonts/Arial.fnt");
        vsyncBtn.setText("VSync");
        vsyncBtn.setTextAlign(BitmapFont.Align.Center);
        vsyncBtn.setIsToggledNoCallback(settings.isVSync());
        win.addChild(vsyncBtn);

        //Camera list
        SelectBox camList = new SelectBox(screen, "Cameras", new Vector2f(15, 140)) {
            @Override
            public void onChange(int i, Object o) {
                if (o != null) {
                    CameraOptions.options.setCamSetting((String) o);
                }
            }
        };
        camList.addListItem("Camera", null);
        camList.addListItem("Manual", "Manual");
        camList.addListItem("Auto-follow (box)", "AutoFollowBox");
        camList.addListItem("Auto-follow (midpoint)", "AutoFollowMidPoint");
        camList.addListItem("Auto-follow (locked)", "AutoFollowLocked");
        win.addChild(camList);

        //go back button
        MyButton goBackBtn = new MyButton(screen, "GoBack",
                new Vector2f(15, 175)) {
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
        if (MainMenu.isPaused()) {
            mm.getPM().makePauseMenu();
        } else {
            mm.makeStartMenu();
        }
    }
}
