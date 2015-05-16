/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlestatepack;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import com.jme3.scene.Node;
import guiPack.MainMenu;
import tonegod.gui.controls.extras.Indicator;
import tonegod.gui.controls.windows.Panel;
import tonegod.gui.core.Screen;

/**
 *
 * @author PC
 */
public class BattleGUI extends AbstractAppState {
    
    private Node activeChar;
    private Screen screen;
    private final int w;
    private final int h;
    
    public BattleGUI(int w, int h) {
        screen = MainMenu.getScreen();
        this.w = w;
        this.h = h;
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        makeHUD();
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }
    
    @Override
    public void update(float tpf) {
        //queries player's HP for updates
    }
    
    @Override
    public void cleanup() {
        //destroy the HUD
        screen.removeElement(screen.getElementById("curpanel"));
        screen.removeElement(screen.getElementById("altpanel"));
        screen.removeElement(screen.getElementById("curindicator"));
        screen.removeElement(screen.getElementById("altindicator"));
    }
    
    public void setActiveHUD(Node player) {
        activeChar = player;
        switchHUD(player);
    }
    
    private void makeHUD() {        
        //for ref: screen, name, position, dimensions, resize, img
        Panel curPPic = new Panel(screen, "curpanel", new Vector2f(w-50,h-80), 
                new Vector2f(50, 80),
                new Vector4f(14, 14, 14, 14), "Textures/danidle3.png");
        screen.addElement(curPPic);
        curPPic.setIsResizable(false);
        curPPic.setIsMovable(false);
        curPPic.setIgnoreMouse(true);
        
        
        //for ref: screen, name, position, dimentions, orientation
        Indicator curPHealth = new Indicator(screen, "curindicator", new Vector2f(w-150, h-20),
                new Vector2f(100,20),
                Indicator.Orientation.HORIZONTAL) {
            @Override
            public void onChange(float f, float f1) {
                //this is where you could add in changing portraits
            }
        };
        screen.addElement(curPHealth);
        curPHealth.setIsResizable(false);
        curPHealth.setIsMovable(false);
        curPHealth.setIgnoreMouse(true);
        
        Panel altPPic = new Panel(screen, "altpanel", new Vector2f(w-100, h-100),
                new Vector2f(50, 80),
                new Vector4f(14, 14, 14, 14), "Textures/danidle6.png");
        screen.addElement(altPPic);
        altPPic.setIsResizable(false);
        altPPic.setIsMovable(false);
        altPPic.setIgnoreMouse(true);
        
        Indicator altPHealth = new Indicator(screen, "altindicator", new Vector2f(w-154, h-42),
                new Vector2f(50,14),
                Indicator.Orientation.HORIZONTAL) {
            @Override
            public void onChange(float f, float f1) {
            }
        };
        screen.addElement(altPHealth);
        altPHealth.setIsResizable(false);
        altPHealth.setIsMovable(false);
        altPHealth.setIgnoreMouse(true);
    }
    
    private void switchHUD(Node player) {
        /*
         * switches the character portraits so the dominant one becomes
         * scaled down and the other one takes the dominant's space
         */
    }
}