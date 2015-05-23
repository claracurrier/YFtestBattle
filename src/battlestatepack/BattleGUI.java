/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlestatepack;

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
            //change curPic
            curHealth.setCurrentValue(danHealth);
            altHealth.setCurrentValue(kiHealth);
            curChar = "dan";
        } else if (player.equals(ki)) {
            //change curPic
            curHealth.setCurrentValue(kiHealth);
            altHealth.setCurrentValue(danHealth);
            curChar = "kirith";
        }
    }

    private void makeHUD() {
        //for ref: screen, name, position, dimensions, resize, img
        curPic = new Panel(screen, "curpanel", new Vector2f(w - 50, h - 80),
                new Vector2f(50, 80),
                new Vector4f(14, 14, 14, 14), "Textures/danidle3.png");
        screen.addElement(curPic);
        curPic.setIsResizable(false);
        curPic.setIsMovable(false);
        curPic.setIgnoreMouse(true);


        //for ref: screen, name, position, dimentions, orientation
        curHealth = new Indicator(screen, "curindicator", new Vector2f(w - 150, h - 20),
                new Vector2f(100, 20),
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

        altPic = new Panel(screen, "altpanel", new Vector2f(w - 100, h - 100),
                new Vector2f(50, 80),
                new Vector4f(14, 14, 14, 14), "Textures/danidle6.png");
        screen.addElement(altPic);
        altPic.setIsResizable(false);
        altPic.setIsMovable(false);
        altPic.setIgnoreMouse(true);

        altHealth = new Indicator(screen, "altindicator", new Vector2f(w - 154, h - 42),
                new Vector2f(50, 14),
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