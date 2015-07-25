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
import skillPack.SkillBehavior;
import skillPack.SkillEffects;
import skillPack.SkillGraphic;

/**
 *
 * @author Clara Currier
 */
public class MobWrapper extends EntityWrapper {

    private final String name;
    private final Node mobNode, dan, ki;
    private MobMoveBehavior movebehavior;
    private SkillBehavior behavior;
    private SkillGraphic graphic;
    private SkillEffects effect;
    private Node targ;
    private float atkcooldowntimer = 0f;
    private float dashcooldown = 0f;

    public MobWrapper(Node mob, String name, Node d, Node k, MobMoveBehavior ms,
            SkillBehavior behavior, SkillGraphic graphic, SkillEffects effect) {
        this.name = name;
        this.mobNode = mob;
        dan = d;
        ki = k;
        health = GVars.gvars.mhealth;
        movebehavior = ms;
        speed = 5;
        this.behavior = behavior;
        this.graphic = graphic;
        this.effect = effect;

        Node mobatkbox = new Node("mobatkbox");

        mobatkbox.setLocalTranslation(mob.getLocalTranslation());
        mobatkbox.setUserData("halfwidth", 25f);
        mobatkbox.setUserData("halfheight", 40f);
        mobatkbox.setUserData("type", "attackbox");
        mobatkbox.setUserData("atkpower", GVars.gvars.matkpwr);
        mobatkbox.setUserData("source", name);
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
        float mobDistanceFromKi = mobNode.getLocalTranslation().distance(ki.getLocalTranslation());
        float mobDistanceFromDan = mobNode.getLocalTranslation().distance(dan.getLocalTranslation());

        if (atkcooldowntimer > 1.3f && (mobDistanceFromDan < GVars.gvars.mminatkdistance
                || mobDistanceFromKi < GVars.gvars.mminatkdistance)) {
            //tackle if within range
            Node tackle = behavior.tackle(mobNode, mobDistanceFromDan < GVars.gvars.mminatkdistance
                    ? dan.getLocalTranslation() : ki.getLocalTranslation(),
                    40f, 40f, GVars.gvars.matkpwr);
            tackle.attachChild(graphic.tempWireBox(40, 40));
            atkcooldowntimer = 0;

        } else if (dashcooldown > 5f && ((mobDistanceFromDan < 350 && mobDistanceFromDan > 150)
                || (mobDistanceFromKi < 350 && mobDistanceFromKi > 150))) {
            //else dash if the range is right
            behavior.dash(mobNode, (mobDistanceFromDan < mobDistanceFromKi) ? dan : ki);
            Node tackle = behavior.tackle(mobNode, mobDistanceFromDan < GVars.gvars.mminatkdistance
                    ? dan.getLocalTranslation() : ki.getLocalTranslation(),
                    40f, 40f, GVars.gvars.matkpwr * 1.8f);
            tackle.attachChild(graphic.tempWireBox(40, 40));
            dashcooldown = 0;

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
                movebehavior.idle(mobNode, 1.2f);
            }
        }
        dashcooldown += tpf;
        atkcooldowntimer += tpf;
    }

    private void pickBehavior(Node target) {
        //hardcoded mob right now. will need an abstraction later
        double rand = Math.random();
        if (rand >= 0 && rand < .3) {
            System.out.println("pursuing");
            movebehavior.pursue(target, this, 2f);
        } else if (rand >= .3 && rand < .6) {
            System.out.println("wandering");
            movebehavior.wander(mobNode, 3f);
        } else if (rand >= .6 && rand < 1) {
            System.out.println("idling");
            movebehavior.idle(mobNode, 2f);
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
