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
public class CatmullRomSpline {

    private float p0, p1, p2, p3;

    public CatmullRomSpline(float p0, float p1, float p2, float p3) {
        this.p0 = p0;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public float q(float t) {
        return 0.5f * ((2 * p1)
                + (p2 - p0) * t
                + (2 * p0 - 5 * p1 + 4 * p2 - p3) * t * t
                + (3 * p1 - p0 - 3 * p2 + p3) * t * t * t);
    }

    /**
     * @return the p0
     */
    public float getP0() {
        return p0;
    }

    /**
     * @param p0 the p0 to set
     */
    public void setP0(float p0) {
        this.p0 = p0;
    }

    /**
     * @return the p1
     */
    public float getP1() {
        return p1;
    }

    /**
     * @param p1 the p1 to set
     */
    public void setP1(float p1) {
        this.p1 = p1;
    }

    /**
     * @return the p2
     */
    public float getP2() {
        return p2;
    }

    /**
     * @param p2 the p2 to set
     */
    public void setP2(float p2) {
        this.p2 = p2;
    }

    /**
     * @return the p3
     */
    public float getP3() {
        return p3;
    }

    /**
     * @param p3 the p3 to set
     */
    public void setP3(float p3) {
        this.p3 = p3;
    }

    /**
     * Creates catmull spline curves between the points array.
     *
     * @param points The current 2D points array
     * @param subdivisions The number of subdivisions to add between each of the
     * points.
     *
     * @return A larger array with the points subdivided.
     */
    public static Vector2f[] subdividePoints(Vector2f[] points, int subdivisions) {
        assert points != null;
        assert points.length >= 3;

        Vector2f[] subdividedPoints = new Vector2f[((points.length - 1) * subdivisions) + 1];

        float increments = 1f / (float) subdivisions;

        for (int i = 0; i < points.length - 1; i++) {
            Vector2f p0 = i == 0 ? points[i] : points[i - 1];
            Vector2f p1 = points[i];
            Vector2f p2 = points[i + 1];
            Vector2f p3 = (i + 2 == points.length) ? points[i + 1] : points[i + 2];

            CatmullRomSpline2D crs = new CatmullRomSpline2D(p0, p1, p2, p3);

            for (int j = 0; j <= subdivisions; j++) {
                subdividedPoints[(i * subdivisions) + j] = crs.q(j * increments);
            }
        }

        return subdividedPoints;
    }
}
