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

    public Pathway(Vector3f curLoc, Vector3f goal) {
        //for paths without any need for pathfinding, ie testing, scripts
        Vector2f curVec = new Vector2f(curLoc.x, curLoc.y);
        Vector2f targVec = new Vector2f(goal.x, goal.y);
        coordPath.add(curVec);
        coordPath.add(targVec);
    }

    public float getClosenessScore() {
        return closenessScore;
    }

    public void setClosenessScore(float score) {
        closenessScore = score;
    }

    public LinkedList<Vector2f> getPath() {
        return coordPath;
    }

    public LinkedList<Vector2f> getSmoothPath(int size) {
        LinkedList<Vector2f> newPath = new LinkedList<>();
        newPath.add(coordPath.getFirst());

        if (size < 3) { //not long enough
            CatmullRomSpline2D crs2Dfirst = new CatmullRomSpline2D(
                    coordPath.get(0), coordPath.get(0),
                    coordPath.get(1), coordPath.get(1));
            newPath.add(crs2Dfirst.q(.333f));
            newPath.add(crs2Dfirst.q(.667f));
            newPath.add(coordPath.get(1));
            return newPath;
        }

        CatmullRomSpline2D crs2D;
        //calculate special first point
        crs2D = new CatmullRomSpline2D(
                coordPath.get(0), coordPath.get(0),
                coordPath.get(1), coordPath.get(2));
        newPath.add(crs2D.q(.25f));
        newPath.add(crs2D.q(.5f));
        newPath.add(crs2D.q(.75f));
        newPath.add(coordPath.get(1));

        //What CRS does is calculate a new "in between" points for p1 and p2
        //for every point in coordPath, calculate t = .25, .5, .75
        //if character appears to walk too fast, just increase sample size
        for (int i = 1; i < coordPath.size() - 2; i++) {
            crs2D = new CatmullRomSpline2D(
                    coordPath.get(i - 1), coordPath.get(i),
                    coordPath.get(i + 1), coordPath.get(i + 2));
            newPath.add(crs2D.q(.25f));
            newPath.add(crs2D.q(.5f));
            newPath.add(crs2D.q(.75f));
            newPath.add(coordPath.get(i + 1));
        }

        //last point case
        crs2D = new CatmullRomSpline2D(
                coordPath.get(coordPath.size() - 3), coordPath.get(coordPath.size() - 2),
                coordPath.getLast(), coordPath.getLast());
        newPath.add(crs2D.q(.25f));
        newPath.add(crs2D.q(.5f));
        newPath.add(crs2D.q(.75f));
        newPath.add(coordPath.getLast());

        return newPath;
    }
}