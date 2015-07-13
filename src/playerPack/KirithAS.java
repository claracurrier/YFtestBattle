/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package playerPack;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author Clara Currier
 */
public class KirithAS extends Player {

    private final Node kirith;
    private float minAtkDist;

    public KirithAS(Node ki) {
        this.kirith = ki;
    }

    @Override
    public void update(float tpf) {
    }

    @Override
    public void autoAttack(Vector3f target) {
        //does a melee attack
    }

    @Override
    public void takeDamage() {
    }

    @Override
    public Node getNode() {
        return kirith;
    }
}