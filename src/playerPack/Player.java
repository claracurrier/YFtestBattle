/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package playerPack;

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
public abstract class Player extends AbstractAppState implements Savable {

    /*
     * Inheritance for Dan and Ki's common methods and for general player control
     * 
     * Essentially, the child classes will only have skills and the behavior for 
     * those skills in addition to any unique modifers 
     */
    public Player() {
    }

    public abstract void autoAttack(Vector3f target);

    public abstract void takeDamage();
    //reduceHealth() in PCollideCont handles this

    public abstract Node getNode();

    @Override
    public void write(JmeExporter ex) throws IOException {
    }

    @Override
    public void read(JmeImporter im) throws IOException {
    }
}