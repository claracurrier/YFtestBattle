/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package skillPack;

import com.jme3.math.Vector2f;

/* PointInPoly.cpp
 * Dijkstras on Bijkstras Programming Competition Notebook 2014
 * Point in Polygon Test
 * Computational Geometry
 *
 * Tests if point is in polygon or on its edge
 * Originally in C++
 * Edited 2015 by Clara Currier
 */
public class WindingPointTest {

    // Returns the winding number for the Vector2f test, or INT_MIN if the Vector2f is exactly on the edge of the polygon
    public int Vector2fInPoly(Vector2f test, java.util.ArrayList<Vector2f> poly) {
        final int INT_MIN = -1;

        if (poly.isEmpty()) {
            return 0;
        }
        if (poly.size() == 1) {
            if (poly.get(0).x == test.x && poly.get(0).y == test.y) {
                return INT_MIN;
            } else {
                return 0;
            }
        }

        Vector2f newpt = new Vector2f();
        Vector2f oldpt = new Vector2f();
        int winds = 0; // HALF winds

//ORIGINAL LINE: newpt = poly[poly.size()-1];
        newpt = copyFrom(poly.get(poly.size() - 1));

        for (int i = 0; i < poly.size(); ++i) {
//ORIGINAL LINE: oldpt = newpt;
            oldpt = copyFrom(newpt);
//ORIGINAL LINE: newpt = poly[i];
            newpt = copyFrom(poly.get(i));
            if (newpt.x == test.x && newpt.y == test.y) {
                return INT_MIN; // If newpt is exactly test Vector2f, return on edge
            }

            float dx;
            float dy;
            float dotparr;
            float dotperp;
            float xinter;
            dx = newpt.x - oldpt.x;
            dy = newpt.y - oldpt.y;
            dotparr = dx * (test.x - oldpt.x) + dy * (test.y - oldpt.y);
            dotperp = dy * (test.x - oldpt.x) - dx * (test.y - oldpt.y);

            if ((dotperp == 0) && (dotparr > 0) && (dotparr < (dx * dx + dy * dy))) {
                return INT_MIN; // If test Vector2f is on (newpt-oldpt), return on edge
            }

            if (dy == 0) {
                continue; // If (oldpt-newpt) is parallel to x-axis, it cannot change the winding, so skip it
            }
            if ((oldpt.y > test.y) && (newpt.y > test.y) || (oldpt.y < test.y) && (newpt.y < test.y)) {
                continue; // If edge has no x intercept, continue
            }
            if (oldpt.y == test.y) {
                xinter = oldpt.x; // Tiny, hardly worthwhile optimization
            } else if (newpt.y == test.y) {
                xinter = newpt.x;
            } else {
                xinter = oldpt.x + dx * (test.y - oldpt.y) / dy; 
                // Find where (oldpt-newpt) intersects with the x-axis. WARNING: This may overflow
            }

            if (xinter > test.x) { // If the intersection is to the right of the test Vector2f
                if (newpt.y > test.y) { // If newpt is above x-axis, add one half wind
                    ++winds;
                } else if (newpt.y < test.y) { // If below, subtract one
                    --winds;
                }

                if (oldpt.y > test.y) { // If oldpt is below x-axis, add one half wind
                    --winds;
                } else if (oldpt.y < test.y) { // If above, subtract one
                    ++winds;
                }
            }
        }

        assert winds % 2 == 0;
        return winds / 2;
    }

    private Vector2f copyFrom(Vector2f toclone) {
        return toclone.clone();
    }
}
