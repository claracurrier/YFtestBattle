/*
 * Copyright [2012] [Sergey Mukhin]
 *
 * Licensed under the Apache License, Version 2.0 (the License); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an AS IS BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
 * ANY KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 * 
 * EDITED 2015 by Clara Currier
 */
package MapPack;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class TMXReader {

    private DefaultHandler currentHandler = new TMXHandler();
    private Map map = null;
    private HashMap<Integer, Integer> tileSetsGids = new HashMap<>();
    private int tileSetIndex = 0;

    // SAX library don't have SAXParser.setHandler(DefaultHandler) method, thats why we will use this workaround stub to parse hierarchy 
    private class SAXStub extends DefaultHandler {

        @Override
        public void startElement(String uri, String name, String qName, Attributes attributes) throws SAXException {
            currentHandler.startElement(uri, name, qName, attributes);
        }

        @Override
        public void endElement(String uri, String name, String qName) throws SAXException {
            currentHandler.endElement(uri, name, qName);
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            currentHandler.characters(ch, start, length);
        }
    }

    private class TMXHandler extends DefaultHandler {

        @Override
        public void startElement(String uri, String name, String qName, Attributes atts) throws SAXException {
            if (qName.equalsIgnoreCase("map")) {
                map = new Map();
                map.setWidth(getInteger(atts, "width"));
                map.setHeight(getInteger(atts, "height"));
                map.setTileWidth(getInteger(atts, "tilewidth"));
                map.setTileHeight(getInteger(atts, "tileheight"));
                currentHandler = new MapHandler(this, map);
            }
        }
    }

    private class MapHandler extends DefaultHandler {

        private DefaultHandler previousHandler;

        public MapHandler(DefaultHandler previousHandler, Map map) {
            this.previousHandler = previousHandler;
        }

        @Override
        public void startElement(String uri, String name, String qName, Attributes atts) throws SAXException {
            if (qName.equalsIgnoreCase("tileset")) {
                TileSet tileSet = new TileSet();
                tileSet.setName(getString(atts, "name"));
                tileSet.setTileWidth(getInteger(atts, "tilewidth"));
                tileSet.setTileHeight(getInteger(atts, "tileheight"));
                tileSet.setSpacing(getInteger(atts, "spacing"));
                tileSet.setMargin(getInteger(atts, "margin"));
                map.add(tileSet);
                currentHandler = new TileSetHandler(this, tileSet, getInteger(atts, "firstgid"));

            } else if (qName.equalsIgnoreCase("layer")) {
                MapLayer mapLayer = new MapLayer(getString(atts, "name"));
                mapLayer.setWidth(getInteger(atts, "width"));
                mapLayer.setHeight(getInteger(atts, "height"));
                map.add(mapLayer);
                currentHandler = new MapLayerHandler(this, mapLayer);
            } else {
                throw new SAXException("unknown tag:" + qName);
            }
        }

        @Override
        public void endElement(String uri, String name, String qName) throws SAXException {
            if (qName.equalsIgnoreCase("map")) {
                currentHandler = previousHandler;
            }
        }
    }

    private class LayerDataHandler extends DefaultHandler {

        private DefaultHandler previousHandler;
        private MapLayer mapLayer;
        private String encoding;
        private String compression;
        private String data = "";

        public LayerDataHandler(DefaultHandler previousHandler, MapLayer mapLayer, String encoding, String compression) {
            this.previousHandler = previousHandler;
            this.mapLayer = mapLayer;
            this.encoding = encoding;
            this.compression = compression;
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            data = data + new String(ch, start, length);
        }

        @Override
        public void endElement(String uri, String name, String qName) throws SAXException {
            try {
                mapLayer.setTiles(data2tilesArray(data, encoding, compression, mapLayer));
            } catch (IOException e) {
                throw new SAXException("unknown tag:" + qName);
            }
            if (qName.equalsIgnoreCase("data")) {
                currentHandler = previousHandler;
            }
        }
    }

    private class MapLayerHandler extends DefaultHandler {

        private DefaultHandler previousHandler;
        private MapLayer mapLayer;

        public MapLayerHandler(DefaultHandler previousHandler, MapLayer mapLayer) {
            this.previousHandler = previousHandler;
            this.mapLayer = mapLayer;
        }

        @Override
        public void startElement(String uri, String name, String qName, Attributes atts) throws SAXException {
            if (qName.equalsIgnoreCase("data")) {
                currentHandler = new LayerDataHandler(this, mapLayer, getString(atts, "encoding"), getString(atts, "compression"));
            } else {
                throw new SAXException("unknown tag:" + qName);
            }
        }

        @Override
        public void endElement(String uri, String name, String qName) throws SAXException {
            if (qName.equalsIgnoreCase("layer")) {
                currentHandler = previousHandler;
            }
        }
    }

    private class TileSetHandler extends DefaultHandler {

        private DefaultHandler previousHandler;
        private TileSet tileSet;
        private int firstgid;

        public TileSetHandler(DefaultHandler previousHandler, TileSet tileSet, int firstgid) {
            this.previousHandler = previousHandler;
            this.tileSet = tileSet;
            this.firstgid = firstgid;
        }

        @Override
        public void startElement(String uri, String name, String qName, Attributes atts) throws SAXException {
            if (qName.equalsIgnoreCase("image")) {
                tileSet.setSource(getString(atts, "source"));
                tileSet.setTilesPerRow((getInteger(atts, "width") - 2 * tileSet.getMargin() + tileSet.getSpacing()) / (tileSet.getTileWidth() + tileSet.getSpacing()));
                tileSetsGids.put(firstgid, tileSetIndex);
                tileSetIndex++;
            } else {
                throw new SAXException("unknown tag:" + qName);
            }
        }

        @Override
        public void endElement(String uri, String name, String qName) throws SAXException {
            if (qName.equalsIgnoreCase("tileset")) {
                currentHandler = previousHandler;
            }
        }
    }

    private int getInteger(Attributes atts, String name) {
        String value = atts.getValue(name);
        if (value != null) {
            return (Integer.valueOf(value));
        }
        return 0;
    }

    private String getString(Attributes atts, String name) {
        String value = atts.getValue(name);
        if (value == null) {
            return ("");
        }
        return value;
    }

    public Map readMap(InputStream IS) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        saxParser.parse(IS, new SAXStub());
        return (map);
    }

    private Tile[][] data2tilesArray(String data, String encoding, String compression, MapLayer mapLayer) throws IOException {
        Tile[][] tiles = new Tile[mapLayer.getWidth()][mapLayer.getHeight()];

        if (encoding != null && "base64".equalsIgnoreCase(encoding)) {
            if (data != null) {
                char[] enc = data.trim().toCharArray();
                byte[] dec = Base64.decode(enc);
                ByteArrayInputStream bais = new ByteArrayInputStream(dec);
                InputStream is;

                if ("gzip".equalsIgnoreCase(compression)) {
                    is = new GZIPInputStream(bais);
                } else if ("zlib".equalsIgnoreCase(compression)) {
                    is = new InflaterInputStream(bais);
                } else {
                    is = bais;
                }

                for (int y = 0; y < mapLayer.getHeight(); y++) {
                    for (int x = 0; x < mapLayer.getWidth(); x++) {
                        int tiletypeId = 0;
                        tiletypeId |= is.read();
                        tiletypeId |= is.read() << 8;
                        tiletypeId |= is.read() << 16;
                        tiletypeId |= is.read() << 24;

                        int TGID = -1;
                        Iterator<Integer> i = tileSetsGids.keySet().iterator();
                        while (i.hasNext()) {
                            int gid = i.next();
                            if ((gid <= tiletypeId) && (gid >= TGID)) {
                                TGID = gid;
                            }
                        }
                        Tile tile = new Tile();

                        if (TGID != -1) {
                            tile.setId(tileSetsGids.get(TGID));
                            tile.setNumber(tiletypeId - TGID);
                        } else {
                            tile.setId(-1);
                            tile.setNumber(-1);
                        }

                        tile.setLocVector(x * 16, (mapLayer.getHeight() - y) * 16);
                        tile.setLayer(mapLayer);
                        tile.setLocArray(x, y);
                        tiles[x][y] = tile;

                        if (!mapLayer.getName().equals("closed_tiles")) {
                            if (map.getLayer("closed_tiles").getTile(x, y).getId() > -1) {
                                tile.setClosed(true);
                            } else {
                                tile.setClosed(false);
                            }
                        }
                    }
                }
            }
        }
        return (tiles);
    }
}