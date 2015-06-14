/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MapPack;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.system.AppSettings;
import com.jme3.texture.plugins.AWTLoader;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    private final AppSettings s;
    private AWTLoader loader = new AWTLoader();

    public JMEMap2d(Node root, AssetManager asman, AppSettings settings) {
        rootNode = root;
        assetManager = asman;
        s = settings;
    }

    public void makeMap(String name) {
        this.name = name;
        TMXReader tmxReader = new TMXReader();
        InputStream is = null;
        Map map = null;
        try {
            is = new BufferedInputStream(new FileInputStream("assets/Scenes/" + name + ".tmx"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JMEMap2d.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            map = tmxReader.readMap(is);
        } catch (Exception ex) {
            Logger.getLogger(JMEMap2d.class.getName()).log(Level.SEVERE, null, ex);
        }

        TileSet tileSet = map.getTileSet(0);
        TileCutter tileCutter = new TileCutter(16, 16, tileSet.getSpacing(), tileSet.getMargin());
        try {
            tileSet.importTileBitmap("assets/Scenes/" + tileSet.getSource(), tileCutter);
        } catch (IOException ex) {
            Logger.getLogger(JMEMap2d.class.getName()).log(Level.SEVERE, null, ex);
        }

        Node mapNode = new Node("Map");
        //for each mapLayer, get the tiles, add tiles to the mapLayer node, add each mapLayer node to map node
        for (int i = 0; i < map.getNumLayers(); i++) {
            mapNode.attachChild(makeTileNodes(map.getLayer(i), map.getLayer(i).getName(), tileSet, i));
        }
        
        rootNode.attachChild(mapNode);
    }

    private Node makeTileNodes(MapLayer layer, String name, TileSet tileSet, int zorder) {
        Node tileGeoms = new Node(name);

        Tile firstTile = layer.getTile(0, 0);
        firstTile.setMesh(new Quad(16, 16));
        firstTile.setMaterial(new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"));
        for (int j = 0; j < layer.getHeight(); j++) {
            for (int i = 0; i < layer.getWidth(); i++) {
                if (layer.getTile(i, j).getNumber() >= 0) {
                    Tile tile = layer.getTile(i, j);
                    tile.setGeom(firstTile);
                    tile.setImage(tileSet.getTile(tile.getNumber()).getImage(), loader);
                    tileGeoms.attachChild(tile);
                    tile.setLocalTranslation(i* 16, (layer.getHeight()-j)*16, zorder-3);
                }
            }
        }

        return tileGeoms;
    }
}