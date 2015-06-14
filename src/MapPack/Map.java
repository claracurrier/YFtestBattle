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
    private ArrayList<TileSet> tileSets = new ArrayList<>();
    private ArrayList<ObjectGroup> objectGroups = new ArrayList<>();
    private ArrayList<MapLayer> mapLayers = new ArrayList<>();
    private HashMap<String, String> properties = new HashMap<>();

    public Map() {
    }

    public MapLayer getLayer(String name) {
        for (int i = 0; i < mapLayers.size(); i++) {
            if (this.mapLayers.get(i).getName().equals(name)) {
                return this.mapLayers.get(i);
            }
        }
        return null;
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

    public void add(ObjectGroup objectGroup) {
        objectGroups.add(objectGroup);
    }

    public ObjectGroup getObjectGroup(String name) {
        for (int i = 0; i < objectGroups.size(); i++) {
            if (this.objectGroups.get(i).getName().equals(name)) {
                return this.objectGroups.get(i);
            }
        }
        return null;
    }

    public ObjectGroup getObjectGroup(int index) {
        return objectGroups.get(index);
    }

    public int getNumLayers() {
        return mapLayers.size();
    }

    public int getObjectGroups() {
        return objectGroups.size();
    }

    public void setProperties(HashMap<String, String> properties) {
        this.properties = properties;
    }
}