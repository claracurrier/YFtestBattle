/*
 * Copyright [2012] [Sergey Mukhin]
 *
 * Licensed under the Apache License, Version 2.0 (the �License�); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an �AS IS� BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
 * ANY KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 * 
 * EDITED 2015 by Clara Currier
 */
package MapPack;

import java.util.ArrayList;

public class Polygon {

    private ArrayList<Vertex> vertices = new ArrayList<>();

    public Polygon() {
    }

    public Polygon(ArrayList<Vertex> vertices) {
        this.vertices = vertices;
    }

    public boolean isCCW() {
        return (getArea() > 0.0f);
    }

    private float getArea() {
        // TODO: fix up the areaIsSet caching so that it can be used
        //if (areaIsSet) return area;
        float area = 0.0f;
        Vertex v_last = vertices.get(vertices.size() - 1);
        Vertex v_first = vertices.get(0);

        area += v_last.getX() * v_first.getY() - v_first.getX() * v_last.getY();

        for (int i = 0; i < vertices.size() - 1; ++i) {
            Vertex v = vertices.get(i);
            Vertex v_next = vertices.get(i + 1);
            area += v.getX() * v_next.getY() - v_next.getX() * v.getY();
        }
        area *= .5f;
        return area;
    }

    public ArrayList<Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(ArrayList<Vertex> vertices) {
        this.vertices = vertices;
    }
}