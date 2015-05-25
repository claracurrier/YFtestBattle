/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlestatepack.mobPack;

import battlestatepack.BattleMain;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author PC
 */
public class MobAS extends AbstractAppState {
    /*
     * Wrapper class for a mob. contains all relevant information and creation
     */

    private final String name;
    private final int id;
    private final Spatial mob, dan, ki;
    private float health;
    private MobSkill ms = MobSkill.mobSkill;
    private Spatial targ;

    public MobAS(Spatial mob, String name, int id, Spatial d, Spatial k) {
        this.name = name;
        this.id = id;
        this.mob = mob;
        dan = d;
        ki = k;

        health = 80f; //make this settable later
        Node mobatkbox = new Node("mobatkbox");

        mobatkbox.setLocalTranslation(mob.getLocalTranslation());
        mobatkbox.setUserData("halfwidth", 25f);
        mobatkbox.setUserData("halfheight", 40f);
        mobatkbox.setUserData("type", "attackbox");
        mobatkbox.setUserData("atkpower", 10f);
        mob.setUserData("type", "mob");

        BattleMain.ATKNODE.attachChild(mobatkbox);
        BattleMain.DEFNODE.attachChild(mob);

        mob.addControl(new MCollideCont(mobatkbox, this));
    }

    @Override
    public void initialize(AppStateManager asm, Application app) {
    }

    @Override
    public void update(float tpf) {
        pickTarget();

        if (mob.getNumControls() == 1) {
            pickSkill(targ);
        }
    }

    private void pickSkill(Spatial target) {
        //hardcoded mob right now. will need an abstraction later
        double rand = Math.random();
        if (rand >= 0 && rand < .3) {
            System.out.println("pursuing");
            ms.pursue(target, mob, 5);
        } else if (rand >= .3 && rand < .6) {
            System.out.println("wandering");
            ms.wander(mob, 5);
        } else if (rand >= .6 && rand < .7) {
            System.out.println("idling");
            ms.idle(mob, 3);
        } else if (rand >= .7 && rand < 1.0) {
            System.out.println("dashing");
            ms.testDash(target, mob);
        }
    }

    private void pickTarget() {
        if (health > 30f) {
            //which one is closer
            targ = (mob.getLocalTranslation().distance(dan.getLocalTranslation())
                    <= mob.getLocalTranslation().distance(ki.getLocalTranslation())
                    ? dan : ki);
        }
        else{
            targ = dan;
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (!enabled) {
            if (mob.getNumControls() > 1) {
                mob.removeControl(mob.getControl(1));
            }
        }
    }

    @Override
    public void cleanup() {
        super.cleanup();
    }

    public void reduceHealth(float damage) {
        health -= damage;
    }

    public float getHealth() {
        return health;
    }

    public Spatial getMobSpat() {
        return mob;
    }
}
