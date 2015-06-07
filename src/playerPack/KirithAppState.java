/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package playerPack;

import battlestatepack.BattleMain;
import battlestatepack.GVars;
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
    private PMoveAppState pmc;
    private KiDizzyControl kdc;
    private float power = 0;
    private float spintimer = 0;
    private boolean attacking = false;
    private float hbtimer = 0;
    private boolean spinning = false;

    public KirithAppState(Spatial ki) {
        this.kirith = ki;
    }

    @Override
    public void initialize(AppStateManager asm, Application app) {
        this.app = (SimpleApplication) app;
        this.inputManager = this.app.getInputManager();

        kdc = kirith.getControl(KiDizzyControl.class);
        pmc = asm.getState(PMoveAppState.class);

        if (pmc.isEnabled() && !(kdc == null)) {
            //if ki is dizzy and has her move map enabled
            pmc.setEnabled(false);
        }

        setEnabled(true);
    }

    @Override
    public void cleanup() {
        disableAttackMap();
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
        } else {
            kdc.setEnabled(enabled);
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

    private void makeSpinAtkBox() {
        Node node = new Node("spin");

        node.setUserData("halfwidth", 80f);
        node.setUserData("halfheight", 80f);
        node.setUserData("type", "kiatkbox");
        node.setUserData("collided", "none");
        node.setUserData("atkpower", GVars.gvars.kspinpower);

        float x = kirith.getWorldTranslation().x;
        float y = kirith.getWorldTranslation().y;

        node.setLocalTranslation(x, y, 0);
        node.attachChild(tempWireBox(80, 80));
        BattleMain.ATKNODE.attachChild(node);
    }

    private void makeAttackBox(String type, float power) {
        int dir = pmc.getDir(); //current direction

        Node node = new Node(type);

        node.setUserData("type", "kiatkbox");
        node.setUserData("collided", "none");
        node.setUserData("atkpower", power);

        float x = kirith.getWorldTranslation().x;
        float y = kirith.getWorldTranslation().y;
        float boxw = 0;
        float boxh = 0;

        switch (dir) {
            //moves the node to appropriate location relative to ki
            case 0: //up
                node.setLocalTranslation(x, y + 81f, 0);
                boxw = 15;
                boxh = 50;
                break;
            case 1:
                node.setLocalTranslation(x + 51f, y + 81f, 0);
                boxw = 30;
                boxh = 30;
                break;
            case 2: //right
                node.setLocalTranslation(x + 51f, y, 0);
                boxw = 50;
                boxh = 15;
                break;
            case 3:
                node.setLocalTranslation(x + 51f, y - 81f, 0);
                boxw = 30;
                boxh = 30;
                break;
            case 4: //down
                node.setLocalTranslation(x + 0, y - 81f, 0);
                boxw = 15;
                boxh = 50;
                break;
            case 5:
                node.setLocalTranslation(x - 51f, y - 81f, 0);
                boxw = 30;
                boxh = 30;
                break;
            case 6: //left
                node.setLocalTranslation(x - 51f, y, 0);
                boxw = 50;
                boxh = 15;
                break;
            case 7:
                node.setLocalTranslation(x - 51f, y + 81f, 0);
                boxw = 30;
                boxh = 30;
                break;
        }

        node.setUserData("halfwidth", boxw / 2);
        node.setUserData("halfheight", boxh / 2);
        node.attachChild(tempWireBox(boxw, boxh));
        BattleMain.ATKNODE.attachChild(node);
    }

    private Geometry tempWireBox(float width, float height) {
        //temp wirebox to see the hitboxes
        Geometry g = new Geometry("attackBox", new WireBox(width, height, 0));
        Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", ColorRGBA.Blue);
        g.setMaterial(mat);
        return g;
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
        if ((name.equals("i") || name.equals("o")) && !spinning) {
            if (power < GVars.gvars.kmaxchargepower) {
                power += tpf * GVars.gvars.kpowerincrement;
            }
        } else {
            if (spintimer > GVars.gvars.kmaxspintime) {
                System.out.println("you've spun too long");
                BattleMain.ATKNODE.detachChildNamed("spin");
                disableAttackMap();
                pmc.setEnabled(false);
                kirith.addControl(new KiDizzyControl(this, pmc));
                attacking = false;
                spinning = false;
                spintimer = 0;
            } else {
                spintimer += tpf;
            }
        }
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("i") && !isPressed) {
            pushback(power);
            power = 0;
        } else if (name.equals("o") && !isPressed) {
            stun(power);
            power = 0;
        } else if (name.equals("p") && isPressed) {
            makeSpinAtkBox();
            spinning = true;
            attacking = true;
        } else if (name.equals("p") && !isPressed) {
            BattleMain.ATKNODE.detachChildNamed("spin");
            spinning = false;
            attacking = false;
        }
    }

    @Override
    public void update(float tpf) {
        if (attacking) {
            if (spinning) {
                //update location of spin
                if (!BattleMain.ATKNODE.getChild("spin").getLocalTranslation().
                        equals(kirith.getLocalTranslation())) {
                    BattleMain.ATKNODE.getChild("spin").setLocalTranslation(
                            kirith.getLocalTranslation());
                }
            } else {
                //pushing or stunning
                //remove whichever is not null
                Spatial box = (BattleMain.ATKNODE.getChild("pushback") == null
                        ? BattleMain.ATKNODE.getChild("stun")
                        : BattleMain.ATKNODE.getChild("pushback"));
                if (hbtimer > .02f || !box.getUserData("collided").equals("none")) {
                    attacking = false;
                    BattleMain.ATKNODE.detachChildNamed(box.getName());
                    hbtimer = 0;
                } else {
                    hbtimer += tpf;
                }
            }
        }
    }
}