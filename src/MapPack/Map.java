/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MapPack;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Clara Currier
 */
public class Map {

    private int width = 0;
    private int height = 0;
    private int tileWidth = 0;
    private int tileHeight = 0;
    private int closedTileGID = -1;
    private ArrayList<TileSet> tileSets = new ArrayList<>();
    private static ArrayList<MapLayer> mapLayers = new ArrayList<>();
    private HashMap<String, String> properties = new HashMap<>();

    public Map() {
    }

    public MapLayer getLayer(String name) {
        for (int i = 0; i < mapLayers.size(); i++) {
            if (mapLayers.get(i).getName().equals(name)) {
                return mapLayers.get(i);
            }
        }
        return null;
    }

    public static MapLayer getTransparentLayer() {
        return mapLayers.get(1); //always the transparent layer
    }
    
    public static void clearLayers(){
        mapLayers.removeAll(mapLayers);
    }

    public MapLayer getLayer(int index) {
        return mapLayers.get(index);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public void setTileHeight(int tileHeight) {
        this.tileHeight = tileHeight;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public void setTileWidth(int tileWidth) {
        this.tileWidth = tileWidth;
    }

    public void add(MapLayer mapLayer) {
        mapLayers.add(mapLayer);
    }

    public void add(TileSet tileSet) {
        tileSets.add(tileSet);
    }

    public TileSet getTileSet(int index) {
        return tileSets.get(index);
    }

    public int getNumTileSets() {
        return tileSets.size();
    }

    public int getNumLayers() {
        return mapLayers.size();
    }

    public void setProperties(HashMap<String, String> properties) {
        this.properties = properties;
    }

    public void setClosedTileGID(int tileGID) {
        closedTileGID = tileGID;
    }

    public int getClosedTileGID() {
        return closedTileGID;
    }
}