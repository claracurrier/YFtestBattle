/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlestatepack.mobPack;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author PC
 */
public class MobBehavior {
    /*
     * Single framework class to store all the possible movement behaviors
     * Each mob has an instantiation of this class to keep update loops separate
     */

    private Spatial mob;
    private float speed = 200;

    public MobBehavior(Spatial m) {
        mob = m;
    }

    private Vector3f getTargDir(Node targ) {
        Vector3f targvel = new Vector3f(
                targ.getLocalTranslation().x - mob.getLocalTranslation().x,
                targ.getLocalTranslation().y - mob.getLocalTranslation().y, 0);
        return targvel.normalizeLocal();
    }

    public void pursuit(float tpf, Node targ) {
        Vector3f dir = getTargDir(targ);
        mob.move(dir.multLocal(tpf*speed));
        //1) get target position
        //2) get movement direction (normalize the difference in locations
        //3) move towards target
    }
    
    public void behaviorUpdate(){
        //calls pursuit or any number of behaviors based on which behaviors are set
    }
}
