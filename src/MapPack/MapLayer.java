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

public class MapLayer {

    private String name;
    private int width = 0;
    private int height = 0;
    private Tile[][] tiles;

    public MapLayer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Tile getTile(int x, int y) {
        return tiles[x][y];
    }

    public void setTileAt(int x, int y, Tile tile) {
        tiles[x][y] = tile;
    }

    public void setTiles(Tile[][] tiles) {
        this.tiles = tiles;
    }
}
