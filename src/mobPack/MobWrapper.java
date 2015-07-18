/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mobPack;

import battlestatepack.BattleMain;
import battlestatepack.EntityWrapper;
import battlestatepack.GVars;
import mobPack.MobSkill.mSkillCont;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author Clara Currier
 */
public class MobWrapper extends EntityWrapper {

    private final String name;
    private final Node mobNode, dan, ki;
    private MobSkill ms = MobSkill.mobSkill;
    private Spatial targ;

    public MobWrapper(Node mob, String name, Node d, Node k) {
        this.name = name;
        this.mobNode = mob;
        dan = d;
        ki = k;
        health = GVars.gvars.mhealth;

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
        mobNode.setUserData("wrapper", this);
    }

    @Override
    public void update(float tpf) {
        if (mobNode.getControl(mSkillCont.class) == null) {
            pickTarget();
            pickSkill(targ);
        }

        if (!dan.getUserData("collided").equals("none")
                || !ki.getUserData("collided").equals("none")) {
            //stop doing stuff if attack connects
            if (mobNode.getControl(mSkillCont.class) != null) {
                mobNode.removeControl(mobNode.getControl(mSkillCont.class));
                ms.idle(mobNode, .2f);
            }
        }
    }

    private void pickSkill(Spatial target) {
        //hardcoded mob right now. will need an abstraction later
        double rand = Math.random();
        if (rand >= 0 && rand < .3) {
            System.out.println("pursuing");
            ms.pursue(target, mobNode, 3);
        } else if (rand >= .3 && rand < .6) {
            System.out.println("wandering");
            ms.wander(mobNode, 4.2f);
        } else if (rand >= .6 && rand < .7) {
            System.out.println("idling");
            ms.idle(mobNode, 3.5f);
        } else if (rand >= .7 && rand < 1.0) {
            System.out.println("dashing");
            ms.testDash(target, mobNode);
        }
    }

    private void pickTarget() {
        if (health > 30f) {
            //which one is closer
            targ = (mobNode.getLocalTranslation().distance(dan.getLocalTranslation())
                    <= mobNode.getLocalTranslation().distance(ki.getLocalTranslation())
                    ? dan : ki);
        } else {
            targ = dan;
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (mobNode.getControl(mSkillCont.class) != null) {
            mobNode.getControl(mSkillCont.class).setEnabled(enabled);
        }
    }

    @Override
    public Node getNode() {
        return mobNode;
    }
}
