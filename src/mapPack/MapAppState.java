/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mapPack;

import battlestatepack.BattleMain;
import com.jme3.app.state.AbstractAppState;
import com.jme3.scene.Spatial;
import java.util.ArrayList;

/**
 *
 * @author Clara Currier
 */
public class MapAppState extends AbstractAppState {

    private MapLayer layer;
    private ArrayList<Tile> tempClosed = new ArrayList<>();
    private Spatial activeChar;

    public MapAppState() {
        layer = Map.getTransparentLayer();
    }

    public void setActiveChar(Spatial player) {
        activeChar = player;
    }

    @Override
    public void update(float tpf) {
        while (!tempClosed.isEmpty()) { //reset and clear list
            tempClosed.get(0).setTempClosed(false);
            tempClosed.get(0).setClosedBecause("tile");
            tempClosed.remove(0);
        }

        //flips tiles that are occupied
        for (Spatial node : BattleMain.DEFNODE.getChildren()) {
            Tile centerTile = layer.getTile(node.getLocalTranslation().x, node.getLocalTranslation().y);
            for (Tile neighbor : centerTile.getNeighbors(true)) {
                neighbor.setTempClosed(true);
                neighbor.setClosedBecause(node.getName());
                tempClosed.add(neighbor);

            }
        }
    }
}
