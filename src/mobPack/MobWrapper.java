/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mobPack;

import battlestatepack.BattleMain;
import battlestatepack.EntityWrapper;
import battlestatepack.GVars;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;
import skillPack.MobSkill;

/**
 *
 * @author Clara Currier
 */
public class MobWrapper extends EntityWrapper {

    private final String name;
    private final Node mobNode, dan, ki;
    private MobMoveBehavior mobBehavior;
    private MobSkill skill;
    private Node targ;
    private float atkcooldowntimer = 0f;

    public MobWrapper(Node mob, String name, Node d, Node k, MobMoveBehavior ms, MobSkill mskill) {
        this.name = name;
        this.mobNode = mob;
        dan = d;
        ki = k;
        health = GVars.gvars.mhealth;
        mobBehavior = ms;
        skill = mskill;

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
        if (atkcooldowntimer > 1.3f && (mobNode.getLocalTranslation().distance(dan.getLocalTranslation())
                < GVars.gvars.mminatkdistance
                || mobNode.getLocalTranslation().distance(ki.getLocalTranslation())
                < GVars.gvars.mminatkdistance)) {
            //tackle if within range
            skill.tackle(this, mobNode.getLocalTranslation().distance(dan.getLocalTranslation())
                    < GVars.gvars.mminatkdistance ? dan : ki);
            atkcooldowntimer = 0;
            //else if(in range to dash){ skill.dash(); }
        } else if (mobNode.getControl(MobMoveBehavior.MobMoveBehaviorCont.class) == null) {
            //otherwise move
            pickTarget();
            pickBehavior(targ);
        }

        if (!dan.getUserData("collided").equals("none")
                || !ki.getUserData("collided").equals("none")) {
            //stop doing stuff if attack connects
            if (mobNode.getControl(MobMoveBehavior.MobMoveBehaviorCont.class) != null) {
                mobNode.removeControl(mobNode.getControl(MobMoveBehavior.MobMoveBehaviorCont.class));
                mobBehavior.idle(mobNode, .2f);
            }
        }

        atkcooldowntimer += tpf;
    }

    private void pickBehavior(Node target) {
        //hardcoded mob right now. will need an abstraction later
        double rand = Math.random();
        if (rand >= 0 && rand < .3) {
            System.out.println("pursuing");
            mobBehavior.pursue(target, mobNode, 2f);
        } else if (rand >= .3 && rand < .6) {
            System.out.println("wandering");
            mobBehavior.wander(mobNode, 3f);
        } else if (rand >= .6 && rand < .7) {
            System.out.println("idling");
            mobBehavior.idle(mobNode, 2f);
        } else if (rand >= .7 && rand < 1.0) {
            System.out.println("dashing");
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
        if (mobNode.getControl(MobMoveBehavior.MobMoveBehaviorCont.class) != null) {
            mobNode.getControl(MobMoveBehavior.MobMoveBehaviorCont.class).setEnabled(enabled);
        }
    }

    @Override
    public Node getNode() {
        return mobNode;
    }
}
