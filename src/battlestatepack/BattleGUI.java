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
import playerPack.Player;
import tonegod.gui.controls.extras.Indicator;
import tonegod.gui.controls.windows.Panel;
import tonegod.gui.core.Screen;

/**
 *
 * @author Clara Currier
 */
public class BattleGUI extends AbstractAppState {

    private final Player dan, ki;
    private float danHealth, kiHealth, aspect;
    private final Screen screen;
    private int w, h;
    private String curChar = "dan";
    private Indicator curHealth, altHealth;
    private Panel curPic, altPic;
    private final ColorRGBA green = new ColorRGBA(50f / 255f, 143f / 255f, 50f / 255f, 1f);

    public BattleGUI(int w, int h, Player dan, Player ki) {
        screen = MainMenu.getScreen();
        this.w = w;
        this.h = h;
        this.dan = dan;
        this.ki = ki;
        aspect = 1;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        makeHUD();
        setActiveHUD(dan);
    }

    public void changeRes(int w, int h) {
        this.w = w;
        this.h = h;
        curPic.setPosition(w - (150 * aspect + 5), 5);
        curHealth.setPosition(w - (190 * aspect + 5 + curPic.getWidth()), 5);
        altPic.setPosition(w - (100 * aspect + 5 + curPic.getWidth()), 5 + curHealth.getHeight());
        altHealth.setPosition(w - (110 * aspect + altPic.getWidth() + curPic.getWidth()),
                10 + curHealth.getHeight());
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

    public void setActiveHUD(Player player) {
        if (player.equals(dan)) {
            curPic.setColorMap("Textures/danPortrait.png");
            altPic.setColorMap("Textures/kiPortrait.png");
            curHealth.setIndicatorColor(green);
            altHealth.setIndicatorColor(ColorRGBA.Red);
            curHealth.setCurrentValue(danHealth);
            altHealth.setCurrentValue(kiHealth);
            curChar = "dan";
        } else if (player.equals(ki)) {
            curPic.setColorMap("Textures/kiPortrait.png");
            altPic.setColorMap("Textures/danPortrait.png");
            curHealth.setIndicatorColor(ColorRGBA.Red);
            altHealth.setIndicatorColor(green);
            curHealth.setCurrentValue(kiHealth);
            altHealth.setCurrentValue(danHealth);
            curChar = "kirith";
        }
    }

    private void makeHUD() {
        //for ref: screen, name, position, dimensions, resize, img
        curPic = new Panel(screen, "curpanel",
                new Vector2f(w - (150 * aspect + 5), h - (150 * aspect + 5)),
                new Vector2f(150 * aspect, 150 * aspect),
                new Vector4f(1, 1, 1, 1), "Textures/danPortrait.png");
        screen.addElement(curPic);
        curPic.setIsResizable(false);
        curPic.setIsMovable(false);
        curPic.setIgnoreMouse(true);

        //for ref: screen, name, position, dimentions, orientation
        curHealth = new Indicator(screen, "curindicator",
                new Vector2f(w - (190 * aspect + 5 + curPic.getWidth()), h - (35 * aspect + 5)),
                new Vector2f(190 * aspect, 35 * aspect),
                Indicator.Orientation.HORIZONTAL) {
            @Override
            public void onChange(float f, float f1) {
                //this is where you could add in changing portraits
            }
        };
        curHealth.setBaseImage(screen.getStyle("Window").getString("defaultImg"));
        curHealth.setIndicatorColor(green);
        curHealth.setAlphaMap(screen.getStyle("Indicator").getString("alphaImg"));
        curHealth.setMaxValue(100f);
        screen.addElement(curHealth);
        curHealth.setIsResizable(false);
        curHealth.setIsMovable(false);
        curHealth.setIgnoreMouse(true);

        altPic = new Panel(screen, "altpanel",
                new Vector2f(w - (100 * aspect + 5 + curPic.getWidth()), h - (100 * aspect + 5 + curHealth.getHeight())),
                new Vector2f(100 * aspect, 100 * aspect),
                new Vector4f(1, 1, 1, 1), "Textures/kiPortrait.png");
        screen.addElement(altPic);
        altPic.setIsResizable(false);
        altPic.setIsMovable(false);
        altPic.setIgnoreMouse(true);

        altHealth = new Indicator(screen, "altindicator",
                new Vector2f(w - (110 * aspect + altPic.getWidth() + curPic.getWidth()),
                h - (25 * aspect + 5 + curHealth.getHeight())),
                new Vector2f(100 * aspect, 22 * aspect),
                Indicator.Orientation.HORIZONTAL) {
            @Override
            public void onChange(float f, float f1) {
            }
        };
        altHealth.setBaseImage(screen.getStyle("Window").getString("defaultImg"));
        altHealth.setIndicatorColor(ColorRGBA.Red);
        altHealth.setAlphaMap(screen.getStyle("Indicator").getString("alphaImg"));
        altHealth.setMaxValue(100f);
        screen.addElement(altHealth);
        altHealth.setIsResizable(false);
        altHealth.setIsMovable(false);
        altHealth.setIgnoreMouse(true);
    }
}