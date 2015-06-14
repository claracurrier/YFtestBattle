/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MapPack;

import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture2D;
import com.jme3.texture.plugins.AWTLoader;
import java.awt.image.BufferedImage;
import java.util.Properties;

/**
 *
 * @author Clara Currier
 */
public class Tile extends Geometry {

    private int tileGID = -1;
    private int tilenumber = -1;
    private BufferedImage image;
    private TileSet tileSet;
    private Properties properties;

    public Tile() {
        properties = new Properties();
    }

    public Tile(boolean newMesh) {
        //change the size when you find out what size tiles you'll use
        mesh = new Quad(16, 16);
    }

    public void setGeom(Geometry clone) {
        mesh = clone.getMesh();
        material = clone.getMaterial();
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
}
