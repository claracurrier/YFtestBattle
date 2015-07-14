/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package playerPack;

import battlestatepack.GVars;
import com.jme3.app.state.AbstractAppState;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author Clara Currier
 */
public abstract class Player extends AbstractAppState {

    private float health = GVars.gvars.phealth;

    public Player() {
    }

    public abstract void autoAttack(Vector3f target);

    public abstract Node getNode();

    public void reduceHealth(float damage) {
        health -= damage;
    }

    public float getHealth() {
        return health;
    }
}