/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package playerPack;

import com.jme3.scene.Spatial;

/**
 *
 * @author Clara Currier
 */
public class KirithAS extends Player{

    private final Spatial kirith;

    public KirithAS(Spatial ki) {
        this.kirith = ki;
    }

    private void pushback(float charge) {
        System.out.println("pushed back with a power of " + charge);
        
    }

    private void stun(float charge) {
        System.out.println("stunned with a power of " + charge);
        
    }

    private void spin(){
        
    }

    @Override
    public void update(float tpf) {
        
    }
}