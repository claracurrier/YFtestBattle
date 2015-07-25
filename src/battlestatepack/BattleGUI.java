/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlestatepack;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import java.util.HashMap;
import menuPack.MainMenu;
import menuPack.MyButton;
import tonegod.gui.controls.extras.Indicator;
import tonegod.gui.controls.windows.Panel;
import tonegod.gui.core.Element;
import tonegod.gui.core.Screen;
import tonegod.gui.effects.Effect;

/**
 *
 * @author Clara Currier
 */
public class BattleGUI extends AbstractAppState {

    private final EntityWrapper dan, ki;
    private float danHealth, kiHealth, aspect;
    private final Screen screen;
    private HashMap<String, MyButton> buttons = new HashMap<>();
    private int w, h;
    private Element HUDNode;
    private Indicator danIndicator, kiIndicator;
    private InputSystem inputSystem;
    private final ColorRGBA green = new ColorRGBA(50f / 255f, 143f / 255f, 50f / 255f, 1f);

    public BattleGUI(int w, int h, EntityWrapper dan, EntityWrapper ki, InputSystem input) {
        screen = MainMenu.getScreen();
        this.w = w;
        this.h = h;
        this.dan = dan;
        this.ki = ki;
        aspect = 1;
        inputSystem = input;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        makeHUD();
        setActiveHUD(dan);
    }

    public void changeRes(int w, int h) {
        this.w = w;
        this.h = h;

        HUDNode.setPosition(w / 2, 90);
    }

    @Override
    public void update(float tpf) {
        if (dan.getHealth() != danIndicator.getCurrentValue()) {
            danHealth = dan.getHealth();
            danIndicator.setCurrentValue(danHealth * 100 / GVars.gvars.dhealth);
        }
        if (ki.getHealth() != kiIndicator.getCurrentValue()) {
            kiHealth = ki.getHealth();
            kiIndicator.setCurrentValue(kiHealth * 100 / GVars.gvars.khealth);
        }
    }

    @Override
    public void cleanup() {
        //destroy the HUD
        screen.removeElement(HUDNode);
    }

    public void setActiveHUD(EntityWrapper player) {
        //TODO: make some kind of indicator for when the player is active
        if (player.equals(dan)) {
        } else if (player.equals(ki)) {
        }
    }

    private void makeHUD() {
        //remember that tonegodgui orientation is top-left = 0,0
        HUDNode = new Element(screen, "danHUDNode",
                new Vector2f(w / 2, h - 90), new Vector2f(0, 0), new Vector4f(0, 0, 0, 0),
                "Textures/transparent.png");
        screen.addElement(HUDNode);

        /*
         * Dan's side
         */
        //for ref: screen, name, position, dimensions, resize, img
        Panel danPic = new Panel(screen, "danpanel",
                new Vector2f(-330, -30),
                new Vector2f(120 * aspect, 120 * aspect),
                new Vector4f(1, 1, 1, 1), "Textures/danPortrait.png");
        HUDNode.addChild(danPic);
        danPic.setIsResizable(false);
        danPic.setIsMovable(false);
        danPic.setIgnoreMouse(true);

        //for ref: screen, name, position, dimentions, orientation
        danIndicator = new Indicator(screen, "danindicator",
                new Vector2f(-200, 70),
                new Vector2f(190 * aspect, 20 * aspect),
                Indicator.Orientation.HORIZONTAL) {
            @Override
            public void onChange(float f, float f1) {
                //this is where you could add in changing portraits
            }
        };
        danIndicator.setBaseImage(screen.getStyle("Window").getString("defaultImg"));
        danIndicator.setIndicatorColor(green);
        danIndicator.setAlphaMap(screen.getStyle("Indicator").getString("alphaImg"));
        danIndicator.setMaxValue(100f);
        HUDNode.addChild(danIndicator);
        danIndicator.setIsResizable(false);
        danIndicator.setIsMovable(false);
        danIndicator.setIgnoreMouse(true);

        MyButton dbuttonleft = new MyButton(screen, "dbuttonleft",
                new Vector2f(-190, 0), new Vector2f(55, 55),
                new Vector4f(1, 1, 1, 1), "Textures/tripleShot.png") {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                inputSystem.manualFire("dbuttonleft");
            }
        };
        MyButton dbuttonmid = new MyButton(screen, "dbuttonmid",
                new Vector2f(-130, 0), new Vector2f(55, 55),
                new Vector4f(1, 1, 1, 1), "Textures/headshot.png") {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                inputSystem.manualFire("dbuttonmid");
            }
        };
        MyButton dbuttonright = new MyButton(screen, "dbuttonright",
                new Vector2f(-70, 0), new Vector2f(55, 55),
                new Vector4f(1, 1, 1, 1), "Textures/greybox.png") {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                inputSystem.manualFire("dbuttonright");
            }
        };
        HUDNode.addChild(dbuttonleft);
        HUDNode.addChild(dbuttonmid);
        HUDNode.addChild(dbuttonright);

        /*
         * Kirith's side
         */
        //for ref: screen, name, position, dimensions, resize, img
        Panel kiPic = new Panel(screen, "kipanel",
                new Vector2f(200, -30),
                new Vector2f(120 * aspect, 120 * aspect),
                new Vector4f(1, 1, 1, 1), "Textures/kiPortrait.png");
        HUDNode.addChild(kiPic);
        kiPic.setIsResizable(false);
        kiPic.setIsMovable(false);
        kiPic.setIgnoreMouse(true);

        kiIndicator = new Indicator(screen, "kiindicator",
                new Vector2f(10, 70),
                new Vector2f(190 * aspect, 20 * aspect),
                Indicator.Orientation.HORIZONTAL) {
            @Override
            public void onChange(float f, float f1) {
            }
        };
        kiIndicator.setBaseImage(screen.getStyle("Window").getString("defaultImg"));
        kiIndicator.setIndicatorColor(ColorRGBA.Red);
        kiIndicator.setAlphaMap(screen.getStyle("Indicator").getString("alphaImg"));
        kiIndicator.setMaxValue(100f);
        HUDNode.addChild(kiIndicator);
        kiIndicator.setIsResizable(false);
        kiIndicator.setIsMovable(false);
        kiIndicator.setIgnoreMouse(true);

        MyButton kbuttonleft = new MyButton(screen, "kbuttonleft",
                new Vector2f(15, 0), new Vector2f(55, 55),
                new Vector4f(1, 1, 1, 1), "Textures/stun.png") {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                inputSystem.manualFire("kbuttonleft");
            }
        };
        MyButton kbuttonmid = new MyButton(screen, "kbuttonmid",
                new Vector2f(75, 0), new Vector2f(55, 55),
                new Vector4f(1, 1, 1, 1), "Textures/pushback.png") {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                inputSystem.manualFire("kbuttonmid");
            }
        };
        MyButton kbuttonright = new MyButton(screen, "kbuttonright",
                new Vector2f(135, 0), new Vector2f(55, 55),
                new Vector4f(1, 1, 1, 1), "Textures/greybox.png") {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                inputSystem.manualFire("kbuttonright");
            }
        };
        HUDNode.addChild(kbuttonleft);
        HUDNode.addChild(kbuttonmid);
        HUDNode.addChild(kbuttonright);

        buttons.put("dbuttonleft", dbuttonleft);
        buttons.put("dbuttonmid", dbuttonmid);
        buttons.put("dbuttonright", dbuttonright);
        buttons.put("kbuttonleft", kbuttonleft);
        buttons.put("kbuttonmid", kbuttonmid);
        buttons.put("kbuttonright", kbuttonright);
    }

    public void displayButtonCooldown(String buttonName, int time) {
        //changes the button appearance to reflect cooldown
        buttons.get(buttonName).addControl(
                new buttonCooldown(time, buttons.get(buttonName)));
    }

    private class buttonCooldown extends AbstractControl {

        private float countdown;
        private MyButton button;
        private Effect buttondim;

        public buttonCooldown(int length, MyButton button) {
            this.countdown = length;
            this.button = button;
            button.setFontColor(ColorRGBA.White);
            button.setFontSize(36);
            buttondim = new Effect(Effect.EffectType.ColorSwap, Effect.EffectEvent.Show, countdown);
            buttondim.setColor(ColorRGBA.Black);
            buttondim.setElement(button);
            screen.getEffectManager().applyEffect(buttondim);
        }

        @Override
        protected void controlUpdate(float tpf) {
            if (countdown > 0) {
                countdown -= tpf;
                button.setText(new Integer((int) countdown + 1).toString());
            } else {
                String name = button.getName().split(":")[0];

                MyButton replacebutton = new MyButton(screen, name,
                        button.getPosition(), button.getDimensions(),
                        new Vector4f(1, 1, 1, 1), button.getElementTexture().getName()) {
                    @Override
                    public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                        inputSystem.manualFire(name.split(":")[0]);
                    }
                };
                replacebutton.setY(0f);
                buttons.put(name, replacebutton);
                HUDNode.removeChild(HUDNode.getChildElementById(name));
                HUDNode.addChild(replacebutton);
                spatial.removeControl(this);
            }
        }

        @Override
        protected void controlRender(RenderManager rm, ViewPort vp) {
        }
    }
}