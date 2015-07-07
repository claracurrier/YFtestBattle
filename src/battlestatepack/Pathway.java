/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlestatepack;

import MapPack.Tile;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import java.util.LinkedList;

/**
 *
 * @author Clara Currier
 */
public class Pathway {

    private LinkedList<Vector2f> coordPath = new LinkedList<>();
    private float closenessScore = 1000; //zero means end was reached

    public Pathway(LinkedList<Tile> path) {
        for (Tile t : path) {
            coordPath.add(t.getLocVector());
        }
    }

    public Pathway() {
        //does nothing, coordPath is empty
    }

    public float getClosenessScore() {
        return closenessScore;
    }

    public void setClosenessScore(float score) {
        closenessScore = score;
    }

    public Pathway(Vector3f curLoc, Vector3f goal) {
        //for paths without any need for pathfinding, ie testing, scripts
        Vector2f curVec = new Vector2f(curLoc.x, curLoc.y);
        Vector2f targVec = new Vector2f(goal.x, goal.y);
        coordPath.add(curVec);
        coordPath.add(targVec);
    }

    public LinkedList<Vector2f> getPath() {
        return coordPath;
    }
}