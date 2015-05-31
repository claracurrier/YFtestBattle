/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlestatepack;

import playerPack.PCollideCont;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import guiPack.MainMenu;
import tonegod.gui.controls.extras.Indicator;
import tonegod.gui.controls.windows.Panel;
import tonegod.gui.core.Screen;

/**
 *
 * @author PC
 */
public class BattleGUI extends AbstractAppState {

    private final PCollideCont dan, ki;
    private float danHealth, kiHealth;
    private final Screen screen;
    private final int w;
    private final int h;
    private String curChar = "dan";
    private Indicator curHealth, altHealth;
    private Panel curPic, altPic;

    public BattleGUI(int w, int h, PCollideCont d, PCollideCont k) {
        screen = MainMenu.getScreen();
        this.w = w;
        this.h = h;
        dan = d;
        ki = k;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        makeHUD();
        setActiveHUD(dan);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    @Override
    public void update(float tpf) {
        if (curChar.equals("dan")) {
            if (dan.getHealth() != curHealth.getCurrentValue()) {
                danHealth = dan.getHealth();
                curHealth.setCurrentValue(danHealth);
            }
            if (ki.getHealth() != altHealth.getCurrentValue()) {
                kiHealth = ki.getHealth();
                altHealth.setCurrentValue(kiHealth);
            }
        } else if (curChar.equals("kirith")) {
            if (dan.getHealth() != altHealth.getCurrentValue()) {
                danHealth = dan.getHealth();
                altHealth.setCurrentValue(danHealth);
            }
            if (ki.getHealth() != curHealth.getCurrentValue()) {
                kiHealth = ki.getHealth();
                curHealth.setCurrentValue(kiHealth);
            }
        }
    }

    @Override
    public void cleanup() {
        //destroy the HUD
        screen.removeElement(curHealth);
        screen.removeElement(altHealth);
        screen.removeElement(curPic);
        screen.removeElement(altPic);
    }

    public void setActiveHUD(PCollideCont player) {
        if (player.equals(dan)) {
            curPic.setColorMap("Textures/danPortrait.png");
            altPic.setColorMap("Textures/kiPortrait.png");
            curHealth.setIndicatorColor(ColorRGBA.Red);
            altHealth.setIndicatorColor(ColorRGBA.Blue);
            curHealth.setCurrentValue(danHealth);
            altHealth.setCurrentValue(kiHealth);
            curChar = "dan";
        } else if (player.equals(ki)) {
            curPic.setColorMap("Textures/kiPortrait.png");
            altPic.setColorMap("Textures/danPortrait.png");
            curHealth.setIndicatorColor(ColorRGBA.Blue);
            altHealth.setIndicatorColor(ColorRGBA.Red);
            curHealth.setCurrentValue(kiHealth);
            altHealth.setCurrentValue(danHealth);
            curChar = "kirith";
        }
    }

    private void makeHUD() {
        /*
         * TODO: make the size and positions of HUD dependent on resolution
         */
        
        //for ref: screen, name, position, dimensions, resize, img
        curPic = new Panel(screen, "curpanel", new Vector2f(w - 105, h - 105),
                new Vector2f(100, 100),
                new Vector4f(14, 14, 14, 14), "Textures/danPortrait.png");
        screen.addElement(curPic);
        curPic.setIsResizable(false);
        curPic.setIsMovable(false);
        curPic.setIgnoreMouse(true);


        //for ref: screen, name, position, dimentions, orientation
        curHealth = new Indicator(screen, "curindicator", new Vector2f(w - (185+105), h - 35),
                new Vector2f(180, 30),
                Indicator.Orientation.HORIZONTAL) {
            @Override
            public void onChange(float f, float f1) {
                //this is where you could add in changing portraits
            }
        };
        curHealth.setBaseImage(screen.getStyle("Window").getString("defaultImg"));
        curHealth.setIndicatorColor(ColorRGBA.Red);
        curHealth.setAlphaMap(screen.getStyle("Indicator").getString("alphaImg"));
        curHealth.setMaxValue(100f);
        screen.addElement(curHealth);
        curHealth.setIsResizable(false);
        curHealth.setIsMovable(false);
        curHealth.setIgnoreMouse(true);

        altPic = new Panel(screen, "altpanel", new Vector2f(w - (110+70), h - (35+75)),
                new Vector2f(70, 70),
                new Vector4f(14, 14, 14, 14), "Textures/kiPortrait.png");
        screen.addElement(altPic);
        altPic.setIsResizable(false);
        altPic.setIsMovable(false);
        altPic.setIgnoreMouse(true);

        altHealth = new Indicator(screen, "altindicator", new Vector2f(w - (185+110), h - (35+25)),
                new Vector2f(110, 20),
                Indicator.Orientation.HORIZONTAL) {
            @Override
            public void onChange(float f, float f1) {
            }
        };
        altHealth.setBaseImage(screen.getStyle("Window").getString("defaultImg"));
        altHealth.setIndicatorColor(ColorRGBA.Blue);
        altHealth.setAlphaMap(screen.getStyle("Indicator").getString("alphaImg"));
        altHealth.setMaxValue(100f);
        screen.addElement(altHealth);
        altHealth.setIsResizable(false);
        altHealth.setIsMovable(false);
        altHealth.setIgnoreMouse(true);
    }
}