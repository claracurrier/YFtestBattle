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

    private String name;
    private int id;
    private Node dan;
    private Node ki;
    private Spatial mob;

    public Mob(Spatial mob, String name, int id, Node d, Node k) {
        this.name = name;
        this.id = id;
        dan = d;
        ki = k;
        this.mob = mob;

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

        mob.addControl(new MCollideCont(mobatkbox));
        //mob.addControl(new PursuitCont(mob, dan)); for now it's hard coded
        //temp disabled
    }
    
    public Spatial getMobSpat() {
        return mob;
    }
}
