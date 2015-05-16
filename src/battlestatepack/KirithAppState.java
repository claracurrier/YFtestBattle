/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlestatepack;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.WireBox;

/**
 *
 * @author PC
 */
public class KirithAppState extends AbstractAppState implements AnalogListener, ActionListener {

    private InputManager inputManager;
    private final Spatial kirith;
    private SimpleApplication app;
    private AppStateManager asm;
    private PMoveAppState pmc;
    private KiDizzyControl kdc;
    private float health;

    public KirithAppState(Spatial ki) {
        this.kirith = ki;

    }

    @Override
    public void initialize(AppStateManager asm, Application app) {
        this.app = (SimpleApplication) app;
        this.inputManager = this.app.getInputManager();
        this.asm = asm;

        kdc = kirith.getControl(KiDizzyControl.class);
        pmc = asm.getState(PMoveAppState.class);

        if (pmc.isEnabled() && !(kdc == null)) {
            //if ki is dizzy and has her move map enabled
            pmc.setEnabled(false);
        }

        setEnabled(true);
        System.out.println("Kirith is in control!");
    }

    @Override
    public void cleanup() {
        disableAttackMap();
        System.out.println("Kirith is no longer in control");
        super.cleanup();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (kdc == null) {
            if (enabled) {
                //if not dizzy
                enableAttackMap();
            } else {
                //if not dizzy
                disableAttackMap();
            }
        }
    }

    protected void enableAttackMap() {
        if (!inputManager.hasMapping("i")) {
            inputManager.addListener(this, "i");
            inputManager.addListener(this, "o");
            inputManager.addListener(this, "p");

            inputManager.addMapping("i", new KeyTrigger(KeyInput.KEY_I));
            inputManager.addMapping("o", new KeyTrigger(KeyInput.KEY_O));
            inputManager.addMapping("p", new KeyTrigger(KeyInput.KEY_P));
        }
    }

    protected void disableAttackMap() {
        if (inputManager.hasMapping("i")) {
            inputManager.removeListener(this);

            inputManager.deleteMapping("p");
            inputManager.deleteMapping("i");
            inputManager.deleteMapping("o");
        }
    }

    private void pushback(float charge) {
        System.out.println("pushed back with a power of " + charge);
        makeAttackBox("pushback", charge);
        attacking = true;
    }

    private void stun(float charge) {
        System.out.println("stunned with a power of " + charge);
        makeAttackBox("stun", charge);
        attacking = true;
    }

    private void spinattack(float spin) {
        kirith.rotate(0, 0, asm.getState(PMoveAppState.class).getLastRotation() + FastMath.PI * (spin / 50000));
        attacking = true;
    }

    private void makeAttackBox(String type, float power) {
        int dir = pmc.getDir(); //current direction

        Node node = new Node(type);

        node.setUserData("halfwidth", 10f);
        node.setUserData("halfheight", 10f);
        node.setUserData("type", "attackbox");
        node.setUserData("atkpower", power);

        float x = kirith.getWorldTranslation().x;
        float y = kirith.getWorldTranslation().y;

        //node.move(kirith.getWorldTranslation());
        switch (dir) { //moves the node to appropriate location relative to ki
            case 0:
                node.setLocalTranslation(x, y + 81f, 0);
                break;
            case 1:
                node.setLocalTranslation(x + 51f, y + 81f, 0);
                break;
            case 2:
                node.setLocalTranslation(x + 51f, y, 0);
                break;
            case 3:
                node.setLocalTranslation(x + 51f, y - 81f, 0);
                break;
            case 4:
                node.setLocalTranslation(x + 0, y - 81f, 0);
                break;
            case 5:
                node.setLocalTranslation(x - 51f, y - 81f, 0);
                break;
            case 6:
                node.setLocalTranslation(x - 51f, y, 0);
                break;
            case 7:
                node.setLocalTranslation(x - 51f, y + 81f, 0);
                break;
        }

        /*
         * temp wirebox for seeing the new hitbox
         */
        Geometry g = new Geometry("attackBox", new WireBox(20f, 20f, 0));
        Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", ColorRGBA.Blue);
        g.setMaterial(mat);
        // g.setLocalTranslation(kirith.getLocalTranslation());

        node.attachChild(g);

        BattleMain.ATKNODE.attachChild(node);

    }
    private float power;

    public void onAnalog(String name, float value, float tpf) {
        power += tpf * 250f;
        if (name.equals("p")) {
            spinattack(power);
            if (power > 1000) {
                System.out.println("you've spun too long");
                disableAttackMap();
                pmc.setEnabled(false);
                kirith.addControl(new KiDizzyControl(5, this, pmc));
                power = 0;
            }
        }
    }

    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("i") && !isPressed) {
            pushback(power);
            power = 0;
        } else if (name.equals("o") && !isPressed) {
            stun(power);
            power = 0;
        } else if (name.equals("p") && !isPressed) {
            power = 0;
        }
    }
    private boolean attacking = false;
    private float hbtimer = 0;

    @Override
    public void update(float tpf) {
        if (attacking) {
            if (hbtimer > .1f) {
                attacking = false;

                BattleMain.ATKNODE.detachChildNamed("pushback");
                BattleMain.ATKNODE.detachChildNamed("stun");
                hbtimer = 0;
            } else {
                hbtimer += tpf;
            }
        }
    }
}