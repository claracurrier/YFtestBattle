/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MapPack;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.scene.Node;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Clara Currier
 */
public class JMEMap2d {

    private String name;
    private Node rootNode;
    private final AssetManager assetManager;

    public JMEMap2d(String name, Node root, AssetManager asman) {
        this.name = name;
        rootNode = root;
        assetManager = asman;
    }

    public void makeMap() {
        TMXReader tmxReader = new TMXReader();
        InputStream is = null;
        Map map = null;
        try {
            is = new BufferedInputStream(new FileInputStream(name + ".tmx"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JMEMap2d.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            map = tmxReader.readMap(is);
        } catch (Exception ex) {
            Logger.getLogger(JMEMap2d.class.getName()).log(Level.SEVERE, null, ex);
        }

        Node mapNode = new Node("Map");
        //for each mapLayer, get the tiles, add tiles to the mapLayer node, add each mapLayer node to map node
        for (int i = 0; i < map.getNumLayers(); i++) {
            mapNode.attachChild(makeTileNodes(map.getLayer(i), map.getLayer(i).getName()));
        }

        rootNode.attachChild(mapNode);
    }

    private Node makeTileNodes(MapLayer layer, String name) {
        Node tiles = new Node(name);

        Tile firstTile = layer.getTile(0, 0);
        firstTile.setMaterial(new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"));
        for (int i = 0; i < layer.getWidth(); i++) {
            for (int j = 0; j < layer.getHeight(); j++) {
                if (layer.getTile(i, j) != null) {
                    Tile tile = layer.getTile(i, j);
                    tile.setGeom(firstTile);
                    tiles.attachChild(tile);
                }
            }
        }

        return tiles;
    }
}
