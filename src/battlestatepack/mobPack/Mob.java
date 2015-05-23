/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlestatepack.mobPack;

import battlestatepack.BattleMain;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author PC
 */
public class Mob {
    /*
     * Wrapper class for a mob. contains all relevant information and creation
     */

    private final String name;
    private final int id;
    private final Spatial mob;
    private float health;

    public Mob(Spatial mob, String name, int id) {
        this.name = name;
        this.id = id;
        this.mob = mob;

        health = 100f; //make this settable later
        Node mobatkbox = new Node("mobatkbox");

        mobatkbox.setLocalTranslation(mob.getLocalTranslation());
        mobatkbox.setUserData("halfwidth", 25f);
        mobatkbox.setUserData("halfheight", 40f);
        mobatkbox.setUserData("type", "attackbox");
        mobatkbox.setUserData("atkpower", 50);

        mob.setUserData("atkbox", mobatkbox);
        mob.setUserData("type", "mob");

        BattleMain.ATKNODE.attachChild(mobatkbox);
        BattleMain.DEFNODE.attachChild(mob);

        mob.addControl(new MCollideCont(mobatkbox, this));
        //mob.addControl(new PursuitCont(mob, dan)); //for now it's hard coded
        //temp disabled
    }

    public void reduceHealth(float damage) {
        health -= damage;
    }
    
    public float getHealth(){
        return health;
    }

    public Spatial getMobSpat() {
        return mob;
    }
}
