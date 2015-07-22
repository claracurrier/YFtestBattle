/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package skillPack;

import battlestatepack.EntityWrapper;
import com.jme3.scene.Node;

/**
 *
 * @author Clara Currier
 */
public class MobSkill {

    private SkillGraphic graphic;
    private SkillBehavior behavior;

    public MobSkill(SkillGraphic skillgraph, SkillBehavior behave) {
        /* all these skills are basic frameworks (ie. attack types)
         * the actual display and power and effects are to be determined via data
         * such as a mob config sheet.
         * Hardcoded for now.
         */
        graphic = skillgraph;
        behavior = behave;
    }

    public void dash(EntityWrapper mob, Node target) {
        //charges towards node in a straight line
        //needs to have the straight line path clear through picking
    }

    public void tackle(EntityWrapper mob, Node target) {
        //bangs into player in melee distance
        graphic.makeAttackBox(mob.getNode().getLocalTranslation(), 
                target.getLocalTranslation(), 15f, "mobtacklebox");
    }

    public void projectile(EntityWrapper mob, Node target) {
        //like dan's arrows
    }

    public void stream(EntityWrapper mob, Node target) {
        //a persistent ranged attack that acts like a wall
    }
    
    public void explosion(EntityWrapper mob, Node target){
        //area of effect in a circle
    }
    
    public void sweep(EntityWrapper mob, Node target){
        //melee swipe that hits a larger area than tackle
    }
}
