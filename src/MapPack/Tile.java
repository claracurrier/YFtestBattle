/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MapPack;

import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture2D;
import com.jme3.texture.plugins.AWTLoader;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.Properties;

/**
 *
 * @author Clara Currier
 */
public class Tile extends Geometry implements Comparable {

    private int tileGID = -1;
    private int tilenumber = -1;
    private BufferedImage image;
    private TileSet tileSet;
    private Properties properties;
    private Vector2f location = new Vector2f();
    private int[][] arraylocation = new int[1][2];
    private MapLayer layer;
    //below are for pathfinding purposes
    public Tile pathParent;
    public float costFromStart;
    public float estimatedCostToGoal;
    private boolean isClosed;

    public Tile() {
        properties = new Properties();
    }

    public Tile(boolean newMesh) {
        mesh = new Quad(16, 16);
    }

    public Tile(Vector2f location, MapLayer layer) {
        //for navigational purposes, meant to be destroyed after use
        this.location = location;
        this.layer = layer;
    }

    public void setMesh(Geometry clone) {
        mesh = clone.getMesh();
    }

    public void setImage(BufferedImage img, AWTLoader loader) {
        if (material != null) {
            material.setTexture("ColorMap", new Texture2D(loader.load(img, true)));
        }
        image = img;
    }

    public void setImage(BufferedImage img) {
        image = img;
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getWidth() {
        return 16;
    }

    public int getHeight() {
        return 16;
    }

    public void setTileSet(TileSet ts) {
        this.tileSet = ts;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public int getNumber() {
        return tilenumber;
    }

    public void setNumber(int n) {
        this.tilenumber = n;
    }

    public int getId() {
        return tileGID;
    }

    public void setId(int Tn) {
        this.tileGID = Tn;
    }

    public void setLocVector(int x, int y) {
        location.set(x, y);
    }

    public void setLocArray(int x, int y) {
        arraylocation[0][1] = y;
        arraylocation[0][0] = x;
    }

    public Vector2f getLocVector() {
        return location;
    }

    public int[][] getLocArray() {
        return arraylocation;
    }

    public float[][] getTrueLocArray() {
        float[][] newint = new float[0][1];
        newint[0][1] = (float) location.y;
        newint[0][0] = (float) location.x;
        return newint;
    }

    public float distBetween(Tile other) {
        return this.getLocVector().distance(other.getLocVector());
    }

    public void setLayer(MapLayer l) {
        layer = l;
    }

    public MapLayer getLayer() {
        return layer;
    }

    @Override
    public String toString() {
        return "tile " + tilenumber + " at " + location + ", "
                + isClosed;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tile) {
            if (this.location.equals(((Tile) obj).getLocVector())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.location);
        return hash;
    }

    public float getCost(Tile end) {
        return this.distBetween(end);
    }

    public float getCost() {
        return costFromStart + estimatedCostToGoal;
    }

    public Tile[] getNeighbors(boolean sizeTesting) {
        Tile[] neighbors;
        if (sizeTesting) {
            neighbors = new Tile[11];
            //bottom additional 3
            neighbors[8] = layer.getTile(
                    location.x + 16,
                    location.y - 32);
            neighbors[9] = layer.getTile(
                    location.x,
                    location.y - 32);
            neighbors[10] = layer.getTile(
                    location.x - 16,
                    location.y - 32);
            //top additional 3
           /* neighbors[11] = layer.getTile(
             location.x + 16,
             location.y + 32);
             neighbors[12] = layer.getTile(
             location.x,
             location.y + 32);
             neighbors[13] = layer.getTile(
             location.x - 16,
             location.y + 32);
             * */
        } else {
            neighbors = new Tile[8];
        }
        //up
        neighbors[0] = layer.getTile(
                location.x,
                location.y + 16);
        //upright
        neighbors[1] = layer.getTile(
                location.x + 16,
                location.y + 16);
        //right
        neighbors[2] = layer.getTile(
                location.x + 16,
                location.y);
        //downright
        neighbors[3] = layer.getTile(
                location.x + 16,
                location.y - 16);
        //down
        neighbors[4] = layer.getTile(
                location.x,
                location.y - 16);
        //downleft
        neighbors[5] = layer.getTile(
                location.x - 16,
                location.y - 16);
        //left
        neighbors[6] = layer.getTile(
                location.x - 16,
                location.y);
        //upleft
        neighbors[7] = layer.getTile(
                location.x - 16,
                location.y + 16);

        return neighbors;
    }

    @Override
    public int compareTo(Object o) {
        float thisValue = this.getCost();
        float otherValue = ((Tile) o).getCost();

        float v = thisValue - otherValue;
        return (v > 0) ? 1 : (v < 0) ? -1 : 0; // sign function
    }
}