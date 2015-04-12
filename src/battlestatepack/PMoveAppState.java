/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlestatepack;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.scene.Spatial;
import spriteProject.SpriteLibrary;

/**
 *
 * @author PC
 */
public class PMoveAppState extends AbstractAppState implements ActionListener {

    private float screenWidth, screenHeight;
    private InputManager inputManager;
//    speed of the player
    private float speed = 350f;
//    lastRotation of the player
    private float lastRotation;
    private boolean ku, kd, kl, kr;
    private int dir = 4;
    private Spatial spatial;
    
    private boolean moving;

    public PMoveAppState(float width, float height, InputManager inManager) {
        screenWidth = width;
        screenHeight = height;
        inputManager = inManager;
    }

    @Override
    public void initialize(AppStateManager asm, Application app) {
        super.initialize(asm, app);
        System.out.println("Hey");
       setEnabled(true);
    }
    
    @Override
    public void setEnabled(boolean enabled){
        super.setEnabled(enabled);
        if(enabled){
            enableCharMapping();
        }else{
            disableCharMapping();
        }
    }

    @Override
    public void cleanup() {
        super.cleanup();
        disableCharMapping();
    }

    public void setSpatial(Spatial s) {
        spatial = s;
    }

    public Spatial getSpatial() {
        return spatial;
    }
    
    public boolean isMoving(){
        return moving;
    }
    
    public int getDir(){
        return dir;
    }

    private void enableCharMapping() {

        inputManager.addListener(this, "LEFT");
        inputManager.addListener(this, "RIGHT");
        inputManager.addListener(this, "UP");
        inputManager.addListener(this, "DOWN");

        inputManager.addMapping("LEFT", new KeyTrigger(KeyInput.KEY_LEFT),new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("RIGHT", new KeyTrigger(KeyInput.KEY_RIGHT),new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("UP", new KeyTrigger(KeyInput.KEY_UP),new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("DOWN", new KeyTrigger(KeyInput.KEY_DOWN), new KeyTrigger(KeyInput.KEY_S));

    }

    private void disableCharMapping() {
        inputManager.removeListener(this);

        inputManager.deleteMapping("LEFT");
        inputManager.deleteMapping("RIGHT");
        inputManager.deleteMapping("UP");
        inputManager.deleteMapping("DOWN");

    }

    public float getLastRotation() {
        return lastRotation;
    }

    public void onAction(String name, boolean isPressed, float tpf) {
        //this whole method is an elaborate way of checking for key combos
        //ultimately setting the value of keyPressed will tell update which way to go
        if (name.equals("UP") && isPressed) {
            ku = true;
            moving = true;
        } else if (name.equals("UP") && !isPressed) {
            ku = false;
            moving = false;
        }
        if (name.equals("DOWN") && isPressed) {
            kd = true;
            moving = true;
        } else if (name.equals("DOWN") && !isPressed) {
            kd = false;
            moving = false;
        }
        if (name.equals("LEFT") && isPressed) {
            kl = true;
            moving = true;
        } else if (name.equals("LEFT") && !isPressed) {
            kl = false;
            moving = false;
        }
        if (name.equals("RIGHT") && isPressed) {
            kr = true;
            moving = true;
        } else if (name.equals("RIGHT") && !isPressed) {
            kr = false;
            moving = false;
        }

    }

    @Override
    public void update(float tpf) {
        SpriteLibrary spatSL = BattleMain.sEngine.getLibrary(spatial.getName());

        spatSL.deactivateSprite(dir+8);


        if (ku && kl && !kr) {//upleft
            if (spatial.getWorldTranslation().y < screenHeight - (Float) spatial.getUserData("halfheight")
                    && spatial.getLocalTranslation().x > (Float) spatial.getUserData("halfwidth")
                    && (Boolean) spatial.getUserData("canL")
                    && (Boolean) spatial.getUserData("canU")) {
                spatial.move(tpf * -speed / 1.47f, tpf * speed / 1.47f, 0);
            }
            spatSL.deactivateSprite(dir);
            spatSL.activateSprite(7);
            dir = 7;

        } else if (ku && kr && !kl) {//upright
            if (spatial.getWorldTranslation().y < screenHeight - (Float) spatial.getUserData("halfheight")
                    && spatial.getWorldTranslation().x < screenWidth - (Float) spatial.getUserData("halfwidth")
                    && (Boolean) spatial.getUserData("canR")
                    && (Boolean) spatial.getUserData("canU")) {
                spatial.move(tpf * speed / 1.47f, tpf * speed / 1.47f, 0);
            }
            spatSL.deactivateSprite(dir);
            spatSL.activateSprite(1);
            dir = 1;

        } else if (ku && !kr && !kl) {//up
            if (spatial.getWorldTranslation().y < screenHeight - (Float) spatial.getUserData("halfheight")
                    && (Boolean) spatial.getUserData("canU")) {
                spatial.move(0, tpf * speed, 0);
            }
            spatSL.deactivateSprite(dir);
            spatSL.activateSprite(0);
            dir = 0;

        } else if (kd && kl && !kr) {//downleft
            if (spatial.getWorldTranslation().y > (Float) spatial.getUserData("halfheight")
                    && spatial.getWorldTranslation().x > (Float) spatial.getUserData("halfwidth")
                    && (Boolean) spatial.getUserData("canD")
                    && (Boolean) spatial.getUserData("canL")) {
                spatial.move(tpf * -speed / 1.47f, tpf * -speed / 1.47f, 0);
            }
            spatSL.deactivateSprite(dir);
            spatSL.activateSprite(5);
            dir = 5;

        } else if (kd && kr && !kl) {//downright
            if (spatial.getLocalTranslation().y > (Float) spatial.getUserData("halfheight")
                    && spatial.getLocalTranslation().x < screenWidth - (Float) spatial.getUserData("halfwidth")
                    && (Boolean) spatial.getUserData("canR")
                    && (Boolean) spatial.getUserData("canD")) {
                spatial.move(tpf * speed / 1.47f, tpf * -speed / 1.47f, 0);  //needs to be fixed
            }
            spatSL.deactivateSprite(dir);
            spatSL.activateSprite(3);
            dir = 3;

        } else if (kd && !kl && !kr) {//down
            if (spatial.getWorldTranslation().y > (Float) spatial.getUserData("halfheight")
                    && (Boolean) spatial.getUserData("canD")) {
                spatial.move(0, tpf * -speed, 0);
            }
            spatSL.deactivateSprite(dir);
            spatSL.activateSprite(4);
            dir = 4;

        } else if (kl && !ku && !kd) {//left
            if (spatial.getWorldTranslation().x > (Float) spatial.getUserData("halfwidth")
                    && (Boolean) spatial.getUserData("canL")) {
                spatial.move(tpf * -speed, 0, 0);
            }
            spatSL.deactivateSprite(dir);
            spatSL.activateSprite(6);
            dir = 6;

        } else if (kr && !ku && !kd) {//right
            if (spatial.getWorldTranslation().x < screenWidth - (Float) spatial.getUserData("halfwidth")
                    && (Boolean) spatial.getUserData("canR")) {
                spatial.move(tpf * speed, 0, 0);
            }
            spatSL.deactivateSprite(dir);
            spatSL.activateSprite(2);
            dir = 2;

        } else { //stop moving

            spatSL.deactivateSprite(dir);

            switch (dir) { //activite proper idle sprite
                case 0:
                    spatSL.activateSprite(8);
                    break;
                case 1:
                    spatSL.activateSprite(9);
                    break;
                case 2:
                    spatSL.activateSprite(10);
                    break;
                case 3:
                    spatSL.activateSprite(11);
                    break;
                case 4:
                    spatSL.activateSprite(12);
                    break;
                case 5:
                    spatSL.activateSprite(13);
                    break;
                case 6:
                    spatSL.activateSprite(14);
                    break;
                case 7:
                    spatSL.activateSprite(15);
                    break;
            }

        }

    }
}