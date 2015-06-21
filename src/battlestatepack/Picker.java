/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlestatepack;

import MapPack.Tile;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;

/**
 *
 * @author Clara Currier
 */
public class Picker implements ActionListener {

    private final Camera cam;
    private final InputManager input;
    private Vector2f mouse;
    private AppSettings settings;
    private Spatial activeChar;
    private Node rootNode;

    public Picker(Camera c, InputManager in, AppSettings s, Node rootNode) {
        cam = c;
        input = in;
        settings = s;
        this.rootNode = rootNode;
    }

    public void setActiveChar(Spatial player) {
        activeChar = player;
    }

    protected void mouseKey(boolean enabled) {
        if (enabled) {
            if (!input.hasMapping("click")) {
                input.addMapping("click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
                input.addListener(this, "click");
            }
        } else {
            if (input.hasMapping("click")) {
                input.deleteMapping("click");
                input.removeListener(this);
            }
        }
    }

    @Override
    public void onAction(String name, boolean keyPressed, float tpf) {
        if (name.equals("click") && !keyPressed) {
            mouse = input.getCursorPosition();

            //to get an accurate location of the mouse
            //subtract half the screenwidth and screenheight from mouse
            //add camera's position to the recalculated mouse
            Vector3f mouseLoc3d = new Vector3f(mouse.x - settings.getWidth() / 2,
                    mouse.y - settings.getHeight() / 2, 2f);
            mouseLoc3d.addLocal(cam.getLocation());

            Vector2f mouseLoc2d = new Vector2f(mouseLoc3d.x, mouseLoc3d.y);

            // 1. Reset results list.
            CollisionResults results = new CollisionResults();
            // 2. Aim the ray from cam loc to cam direction.
            Ray ray = new Ray(mouseLoc3d, Vector3f.UNIT_Z.mult(-1));
            // 3. Collect intersections between Ray and Shootables in results list.
            rootNode.collideWith(ray, results);
            // 4. Use the results (we mark the hit object)
            if (results.size() > 0) {
                // The closest collision point is what was truly hit:
                CollisionResult closest = results.getClosestCollision();
                System.out.println(closest.getGeometry().toString());
                if (closest.getGeometry() instanceof Tile) {
                    activeChar.addControl(new MoveCont(
                            new Pathway(
                            activeChar.getLocalTranslation(),
                            mouseLoc3d),
                            activeChar.getName()));
                } else {
                    System.out.println("not a tile");
                }
            } else {
                // No hits? 
                System.out.println("nothing");

            }
        }
    }
}