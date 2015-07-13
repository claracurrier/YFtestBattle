/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mobPack;

import battlestatepack.BattleMain;
import battlestatepack.GVars;
import mobPack.MobSkill.mSkillCont;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.io.IOException;

/**
 *
 * @author Clara Currier
 */
public class MobAS extends AbstractAppState implements Savable{

    private final String name;
    private final Spatial mob, dan, ki;
    private float health = GVars.gvars.mhealth;
    private MobSkill ms = MobSkill.mobSkill;
    private Spatial targ;

    public MobAS(Spatial mob, String name, Spatial d, Spatial k) {
        this.name = name;
        this.mob = mob;
        dan = d;
        ki = k;

        Node mobatkbox = new Node("mobatkbox");

        mobatkbox.setLocalTranslation(mob.getLocalTranslation());
        mobatkbox.setUserData("halfwidth", 25f);
        mobatkbox.setUserData("halfheight", 40f);
        mobatkbox.setUserData("type", "attackbox");
        mobatkbox.setUserData("atkpower", GVars.gvars.matkpwr);
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
        if (mob.getControl(mSkillCont.class) == null) {
            pickTarget();
            pickSkill(targ);
        }

        if (!dan.getUserData("collided").equals("none")
                || !ki.getUserData("collided").equals("none")) {
            //stop doing stuff if attack connects
            if (mob.getControl(mSkillCont.class) != null) {
                mob.removeControl(mob.getControl(mSkillCont.class));
                ms.idle(mob, .2f);
            }
        }
    }

    private void pickSkill(Spatial target) {
        //hardcoded mob right now. will need an abstraction later
        double rand = Math.random();
        if (rand >= 0 && rand < .3) {
            System.out.println("pursuing");
            ms.pursue(target, mob, 3);
        } else if (rand >= .3 && rand < .6) {
            System.out.println("wandering");
            ms.wander(mob, 4.2f);
        } else if (rand >= .6 && rand < .7) {
            System.out.println("idling");
            ms.idle(mob, 3.5f);
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
        } else {
            targ = dan;
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (mob.getControl(mSkillCont.class) != null) {
            mob.getControl(mSkillCont.class).setEnabled(enabled);
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
    
    @Override
    public void write(JmeExporter ex) throws IOException {
    }

    @Override
    public void read(JmeImporter im) throws IOException {
    }
}
