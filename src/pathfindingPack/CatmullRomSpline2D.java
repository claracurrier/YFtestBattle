/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pathfindingPack;

import com.jme3.math.Vector2f;

/**
 *
 * @author Richard Hawkes EDITED 2015 by Clara Currier
 */
public class CatmullRomSpline2D {

    private CatmullRomSpline splineXVals, splineYVals;

    public CatmullRomSpline2D(Vector2f p0, Vector2f p1, Vector2f p2, Vector2f p3) {
        assert p0 != null : "p0 cannot be null";
        assert p1 != null : "p1 cannot be null";
        assert p2 != null : "p2 cannot be null";
        assert p3 != null : "p3 cannot be null";

        splineXVals = new CatmullRomSpline(p0.getX(), p1.getX(), p2.getX(), p3.getX());
        splineYVals = new CatmullRomSpline(p0.getY(), p1.getY(), p2.getY(), p3.getY());
    }

    public Vector2f q(float t) {
        return new Vector2f(splineXVals.q(t), splineYVals.q(t));
    }
}
