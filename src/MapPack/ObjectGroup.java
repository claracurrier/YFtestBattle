/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MapPack;

import java.util.ArrayList;

/**
 *
 * @author Clara Currier
 */
public class ObjectGroup {

    //objects will need to be handled differently as collision groups
    private String name;
    private ArrayList<MapObject> objects = new ArrayList<>();

    public ObjectGroup(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void add(ArrayList<MapObject> objects) {
        this.objects = objects;
    }

    public void add(MapObject mapObject) {
        objects.add(mapObject);
    }
}