/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlestatepack;

import MapPack.Map;
import MapPack.Tile;
import com.jme3.math.Vector2f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author Clara Currier
 */
public class KnockbackCont extends AbstractControl {

    //gives a smooth pushing animation
    private final float distance;
    private final float intensity;
    private float dtimer;
    private int dir;

    public KnockbackCont(float distance, float intensity, String libraryName, int spriteIndex, int dir) {
        this.distance = distance;
        this.intensity = intensity;
        this.dir = dir;
        BattleMain.sEngine.getLibrary(libraryName).activateSprite(spriteIndex);
    }

    @Override
    protected void controlUpdate(float tpf) {
        //tests for closed tiles
        Tile curTile = new Tile(new Vector2f(
                spatial.getLocalTranslation().x,
                spatial.getLocalTranslation().y),
                Map.getTransparentLayer());
        for (Tile neighbor : curTile.getNeighbors(true)) {
            if (neighbor.isClosed()) {
                spatial.setUserData("knockback", false);
                spatial.removeControl(this);
                return;
            }
        }

        if (dtimer < Math.abs(distance)) {
            switch (dir) { //moves the node opposite of the direction it was it
                //right now SATtest only gives 4 cardinal directions
                case 1: //hit right
                    spatial.move(-tpf * intensity, 0, 0);
                    break;
                case 2: //hit left
                    spatial.move(tpf * intensity, 0, 0);
                    break;
                case 3: //hit below
                    spatial.move(0, tpf * intensity, 0);
                    break;
                case 4: //hit above
                    spatial.move(0, -tpf * intensity, 0);
                    break;
            }
            dtimer += tpf * intensity;
        } else {
            spatial.setUserData("knockback", false);
            spatial.removeControl(this);
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}
