/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlestatepack;

import com.jme3.app.state.AbstractAppState;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.io.IOException;

/**
 *
 * @author Clara Currier
 */
public abstract class EntityWrapper extends AbstractAppState implements Savable {

    public float health;
    private float damageModifier = 1f;

    public EntityWrapper() {
    }

    public void autoAttack(Vector3f target) {
        //expected to override
    }

    public abstract Node getNode();

    public void setDamageModifier(float dmgmod) {
        damageModifier = dmgmod;
    }

    public void reduceHealth(float damage) {
        health -= damage * damageModifier;
    }

    public float getHealth() {
        return health;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
    }

    @Override
    public void read(JmeImporter im) throws IOException {
    }
}