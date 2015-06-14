/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MapPack;

import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;

/**
 *
 * @author Clara Currier
 */
public class Tile extends Geometry {

    private int tileGID = -1;
    private int tilenumber = -1;

    public Tile() {
        //change the size when you find out what size tiles you'll use
        mesh = new Quad(16, 16);
    }
    
    public void setGeom(Geometry clone){
        mesh = clone.getMesh();
        material = clone.getMaterial();
    }

    public int getNumber() {
        return tilenumber;
    }

    public void setNumber(int n) {
        this.tilenumber = n;
    }

    public int getTileID() {
        return tileGID;
    }

    public void setTileID(int Tn) {
        this.tileGID = Tn;
    }
}
