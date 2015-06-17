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
    private MapLayer layer;
    //below are for pathfinding purposes
    public Tile pathParent;
    public float costFromStart;
    public float estimatedCostToGoal;

    public Tile() {
        properties = new Properties();
    }

    public Tile(boolean newMesh) {
        //change the size when you find out what size tiles you'll use
        mesh = new Quad(16, 16);
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

    public void setLoc(int x, int y) {
        location.set(x, y);
    }

    public Vector2f getLocVec() {
        return location;
    }

    public int[][] getLocArray() {
        int[][] newint = new int[1][1];
        newint[0][1] = (int) location.y;
        newint[1][0] = (int) location.x;
        return newint;
    }

    public float distBetween(Tile other) {
        return this.getLocVec().distance(other.getLocVec());
    }

    public void setLayer(MapLayer l) {
        layer = l;
    }

    public MapLayer getLayer() {
        return layer;
    }

    @Override
    public String toString() {
        return "tile " + tilenumber + " at " + location;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tile) {
            if (this.getLocVec().equals(((Tile) obj).getLocVec())) {
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

    public float getCost(Tile goal) {
        //TODO
        //cost of moving from one tile to adjacent one
        return this.distBetween(goal);
    }

    public float getCost() {
        //TODO
        return costFromStart + estimatedCostToGoal;
    }

    public Tile[] getNeighbors() {
        Tile[] neighbors = new Tile[8];

        //up
        neighbors[0] = layer.getTile(
                (int) this.getLocVec().x,
                (int) this.getLocVec().y + 16);
        //upright
        neighbors[1] = layer.getTile(
                (int) this.getLocVec().x + 16,
                (int) this.getLocVec().y + 16);
        //right
        neighbors[2] = layer.getTile(
                (int) this.getLocVec().x + 16,
                (int) this.getLocVec().y);
        //downright
        neighbors[3] = layer.getTile(
                (int) this.getLocVec().x + 16,
                (int) this.getLocVec().y - 16);
        //down
        neighbors[4] = layer.getTile(
                (int) this.getLocVec().x,
                (int) this.getLocVec().y - 16);
        //downleft
        neighbors[5] = layer.getTile(
                (int) this.getLocVec().x - 16,
                (int) this.getLocVec().y - 16);
        //left
        neighbors[6] = layer.getTile(
                (int) this.getLocVec().x - 16,
                (int) this.getLocVec().y);
        //upleft
        neighbors[7] = layer.getTile(
                (int) this.getLocVec().x - 16,
                (int) this.getLocVec().y + 16);

        return neighbors;
    }

    @Override
    public int compareTo(Object o) {
        float thisValue = this.getCost();
        float otherValue = ((Tile) o).getCost();

        float v = thisValue - otherValue;
        return (v > 0) ? 1 : (v < 0) ? -1 : 0; // sign function
    }

    public float getEstimatedCost(Tile end) {
        //TODO
        return 0f;
    }
}
