package spriteProject;

import com.jme3.scene.Node;
import java.util.ArrayList;

/**
 * @author Nicholas Mamo - Nyphoon Games
 *
 * edited by Clara Currier
 */
public class SpriteLibrary {

    public static Node l_baseNode;
    private boolean l_staticLibrary;
    private Node l_node;
    private ArrayList<Sprite> l_library = new ArrayList<Sprite>();
    public Sprite curSprite;

    public SpriteLibrary(String name, boolean staticLibrary, Node attach) {
        l_node = attach;
        this.l_staticLibrary = staticLibrary;
    }

    public void setStatic(boolean staticLibrary) {
        this.l_staticLibrary = staticLibrary;
    }

    //editor: Clara
    public void addSprite(Sprite sprite) {
        l_library.add(sprite);
        sprite.move(-(Float) l_node.getUserData("halfwidth"), -(Float) l_node.getUserData("halfheight"));
    }

    //author: Clara
    public void activateSprite(int index) {
        Sprite s = getSprite(index);
        l_node.detachChild(curSprite.getNode());
        curSprite = s;
        l_node.attachChild(s.getNode());
    }

    public void setCurSprite(int index) {
        curSprite = getSprite(index);
        l_node.attachChild(curSprite.getNode());
    }

    public void removeSprite(int index) {
        l_library.remove(index);
    }

    public void removeSprite(String name) {
        for (int i = 0; i < l_library.size(); i++) {
            if (l_library.get(i).getName().equals(name)) {
                l_library.remove(i);
            }
        }
    }

    public boolean getStatic() {
        return l_staticLibrary;
    }

    public Node getNode() {
        return l_node;
    }

    public ArrayList<Sprite> getLibrary() {
        return l_library;
    }

    public Sprite getSprite(int index) {
        return l_library.get(index);
    }

    public Sprite getSprite(String name) {
        for (int i = 0; i < l_library.size(); i++) {
            if (this.l_library.get(i).getName().equals(name)) {
                return this.l_library.get(i);
            }
        }
        return null;
    }

    public String getName() {
        return l_node.getName();
    }

    public void purge() {
        for (int i = l_library.size() - 1; i >= 0; i--) {
            removeSprite(i);
        }
    }
}