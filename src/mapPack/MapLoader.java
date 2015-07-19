/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mapPack;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
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
public class MapLoader {

    private Node rootNode;
    private final AssetManager assetManager;
    private AWTLoader loader = new AWTLoader();

    public MapLoader(Node root, AssetManager asman) {
        rootNode = root;
        assetManager = asman;
    }

    public void makeImageMap(String name, int dimx, int dimy, int numRows, int numCols) {
        //loads in pre-segmented pngs
        Node imageMapNode = new Node("imageMap");

        for (int j = 0; j < numRows; j++) {
            for (int i = 0; i < numCols; i++) {
                Geometry imgTile = new Geometry("imgTile" + i + ", " + j);
                imgTile.setMesh(new Quad(dimx / numCols, dimy / numRows));
                Material tileMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                tileMat.setTexture("ColorMap", assetManager.loadTexture("Scenes/"
                        + name + " [www.imagesplitter.net]-" + j + "-" + i + ".png"));
                imgTile.setMaterial(tileMat);
                imgTile.setLocalTranslation(i * dimx / numCols, (numRows - 1 - j) * dimy / numRows + 16, -3);
                imageMapNode.attachChild(imgTile);
            }
        }
        rootNode.attachChild(imageMapNode);
    }

    public Map makeTiledMap(String name) {
        //sax parsing for tiled maps - might only be used for collision
        TMXReader tmxReader = new TMXReader();
        InputStream is = null;
        Map map = null;
        Map.clearLayers();
        try {
            is = new BufferedInputStream(new FileInputStream("assets/Scenes/" + name + ".tmx"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MapLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            map = tmxReader.readMap(is);
        } catch (Exception ex) {
            Logger.getLogger(MapLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

        TileSet tileSet = map.getTileSet(0);
        TileCutter tileCutter = new TileCutter(16, 16, tileSet.getSpacing(), tileSet.getMargin());
        try {
            tileSet.importTileBitmap("assets/Scenes/" + tileSet.getSource(), tileCutter);
        } catch (IOException ex) {
            Logger.getLogger(MapLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

        Node mapNode = new Node("Map");
        //for each mapLayer, get the tiles, add tiles to the mapLayer node, add each mapLayer node to map node
        for (int i = 0; i < map.getNumLayers(); i++) {
            if (!map.getLayer(i).getName().equals("closed_tiles")) {
                mapNode.attachChild(makeTileNodes(map.getLayer(i), map.getLayer(i).getName(), tileSet, i));
            }
        }
        rootNode.attachChild(mapNode);
        return map;
    }

    private Spatial makeTileNodes(MapLayer layer, String name, TileSet tileSet, int zorder) {
        Node tileGeoms = new Node(name);
        tileGeoms.setCullHint(Spatial.CullHint.Always);
        Tile firstTile = layer.getTile(0, 0);
        firstTile.setMesh(new Quad(16, 16));

        for (int j = 0; j < layer.getHeight(); j++) {
            for (int i = 0; i < layer.getWidth(); i++) {
                if (layer.getTile(i, j).getId() >= 0) {
                    Tile tile = layer.getTile(i, j);
                    Material tileMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                    tileMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
                    tileMat.getAdditionalRenderState().setAlphaTest(true);
                    tileMat.getAdditionalRenderState().setAlphaFallOff(1);

                    tile.setMesh(firstTile.getMesh());
                    tile.setMaterial(tileMat);
                    tile.setLocalTranslation(i * 16, (layer.getHeight() - j) * 16, zorder - 2);
                    tile.setImage(tileSet.getTile(tile.getNumber()).getImage(), loader);
                    tile.setName("tile");

                    tileGeoms.attachChild(tile);
                }
            }
        }
        return tileGeoms;
    }
}