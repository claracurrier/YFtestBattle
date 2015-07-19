/*
 * Copyright 2004-2010, Thorbjørn Lindeijer <thorbjorn@lindeijer.nl>
 * Copyright 2004-2006, Adam Turk <aturk@biggeruniverse.com>
 *
 * This file is part of libtiled-java.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE CONTRIBUTORS ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package mapPack;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class TileSet {

    private String name = "";
    private String source = "";
    private int tilesPerRow = 0;
    private int tileWidth = 0;
    private int tileHeight = 0;
    private int spacing = 0;
    private int margin = 0;
    final private ArrayList<Tile> tiles = new ArrayList<>();
    private File tilebmpFile;
    private Color transparentColor = Color.white;
    private Image tileSetImage;
    private Rectangle tileDimensions;
    private long tilebmpFileLastModified;

    public TileSet() {
    }

    /**
     * Creates a tileset from a tileset image file.
     *
     * @param imgFilename
     * @param cutter
     * @throws IOException
     * @see TileSet#importTileBitmap(BufferedImage, TileCutter)
     */
    public void importTileBitmap(String imgFilename, TileCutter cutter)
            throws IOException {
        setTilesetImageFilename(imgFilename);

        Image image = ImageIO.read(new File(imgFilename));
        if (image == null) {
            throw new IOException("Failed to load " + tilebmpFile);
        }

        Toolkit tk = Toolkit.getDefaultToolkit();

        if (transparentColor != null) {
            int rgb = transparentColor.getRGB();
            image = tk.createImage(
                    new FilteredImageSource(image.getSource(),
                    (ImageFilter) new TransparentImageFilter(rgb)));
        }

        BufferedImage buffered = new BufferedImage(
                image.getWidth(null),
                image.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);
        buffered.getGraphics().drawImage(image, 0, 0, null);

        importTileBitmap(buffered, cutter);
    }

    private class TransparentImageFilter extends RGBImageFilter {

        int trans;

        /**
         * @param col the color to make transparent
         */
        public TransparentImageFilter(int col) {
            trans = col;

            // The filter doesn't depend on pixel location
            canFilterIndexColorModel = true;
        }

        /**
         * Filters the given pixel. It returns a transparent pixel for pixels
         * that match the transparency color, or the existing pixel for anything
         * else.
         */
        @Override
        public int filterRGB(int x, int y, int rgb) {
            if (rgb == trans) {
                return 0;
            } else {
                return rgb;
            }
        }
    }

    /**
     * Creates a tileset from a buffered image. Tiles are cut by the passed
     * cutter.
     *
     * @param tileBitmap the image to be used, must not be null
     * @param cutter the tile cutter, must not be null
     */
    private void importTileBitmap(BufferedImage tileBitmap, TileCutter cutter) {
        assert tileBitmap != null;
        assert cutter != null;

        tileSetImage = tileBitmap;

        cutter.setImage(tileBitmap);

        tileDimensions = new Rectangle(cutter.getTileDimensions());
        if (cutter instanceof TileCutter) {
            TileCutter basicTileCutter = (TileCutter) cutter;
            spacing = basicTileCutter.getTileSpacing();
            margin = basicTileCutter.getTileMargin();
            tilesPerRow = basicTileCutter.getTilesPerRow();
        }

        BufferedImage tileImage = cutter.getNextTile();
        while (tileImage != null) {
            Tile tile = new Tile();
            tile.setImage(tileImage);
            addNewTile(tile);
            tileImage = cutter.getNextTile();
        }
    }

    /**
     * Sets the filename of the tileset image. Doesn't change the tileset in any
     * other way.
     *
     * @param name
     */
    public void setTilesetImageFilename(String name) {
        if (name != null) {
            tilebmpFile = new File(name);
            tilebmpFileLastModified = tilebmpFile.lastModified();
        } else {
            tilebmpFile = null;
        }
    }

    /**
     * Adds the tile to the set, setting the id of the tile only if the current
     * value of id is -1.
     *
     * @param t the tile to add
     * @return int The <b>local</b> id of the tile
     */
    public int addTile(Tile t) {
        if (t.getId() < 0) {
            t.setId(tiles.size());
        }

        if (tileDimensions.width < t.getWidth()) {
            tileDimensions.width = t.getWidth();
        }

        if (tileDimensions.height < t.getHeight()) {
            tileDimensions.height = t.getHeight();
        }

        tiles.add(t);
        t.setTileSet(this);

        return t.getId();
    }

    /**
     * This method takes a new Tile object as argument, and in addition to the
     * functionality of
     * <code>addTile()</code>, sets the id of the tile to -1.
     *
     * @see TileSet#addTile(Tile)
     * @param t the new tile to add.
     */
    public void addNewTile(Tile t) {
        t.setId(-1);
        addTile(t);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getTilesPerRow() {
        return tilesPerRow;
    }

    public void setTilesPerRow(int tilesPerRow) {
        this.tilesPerRow = tilesPerRow;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public void setTileWidth(int tileWidth) {
        this.tileWidth = tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public void setTileHeight(int tileHeight) {
        this.tileHeight = tileHeight;
    }

    public int getSpacing() {
        return spacing;
    }

    public void setSpacing(int spacing) {
        this.spacing = spacing;
    }

    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    public Tile getTile(int index) {
        return tiles.get(index);
    }
}