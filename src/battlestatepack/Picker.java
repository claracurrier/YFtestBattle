/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlestatepack;

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

    public Picker(Camera c, InputManager in, AppSettings s) {
        cam = c;
        input = in;
        settings = s;
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

            

            Vector3f playerPos = activeChar.getLocalTranslation();
            Vector2f newvec = new Vector2f((playerPos.x) - (settings.getWidth() / 2),
                    (playerPos.y) - (settings.getHeight() / 2));
            mouse = mouse.add(newvec);

            Vector3f dif3 = new Vector3f(mouse.x - playerPos.x, mouse.y - playerPos.y, 3);
            Vector2f dif2 = new Vector2f(mouse.x - playerPos.x, mouse.y - playerPos.y);
            Vector2f dist = new Vector2f(playerPos.x, playerPos.y);
            
            Vector3f vec1 = new Vector3f(mouse.x, mouse.y, 3);


            // 1. Reset results list.
            CollisionResults results = new CollisionResults();
            // 2. Aim the ray from cam loc to cam direction.
            Ray ray = new Ray(dif3, Vector3f.UNIT_Z.mult(-1));
            // 3. Collect intersections between Ray and Shootables in results list.
            BattleMain.BATTLENODE.collideWith(ray, results);
            // 4. Use the results (we mark the hit object)
            if (results.size() > 0) {
                // The closest collision point is what was truly hit:
                CollisionResult closest = results.getClosestCollision();
                System.out.println(closest.getGeometry().getName());
                System.out.println(cam.getLocation() + " " + dif3);
                activeChar.addControl(new MoveCont(
                        new Pathway(mouseAngle(dif2), mouse.distance(dist)),
                        activeChar.getName()));
            } else {
                // No hits? 
                System.out.println("nothing");
                System.out.println(cam.getLocation() + " " + vec1);
            }
        }
    }

    private int mouseAngle(Vector2f mouseloc) {
        float aim = mouseloc.normalize().getAngle();

        if (aim <= 5 * FastMath.PI / 6 && aim > 2 * FastMath.PI / 3) {
            //up left
            return 7;
        } else if (aim <= 2 * FastMath.PI / 3 && aim > FastMath.PI / 3) {
            //facing up
            return 0;
        } else if (aim <= FastMath.PI / 3 && aim > FastMath.PI / 6) {
            //up right
            return 1;
        } else if (aim <= FastMath.PI / 6 && aim > -FastMath.PI / 6) {
            //facing right
            return 2;
        } else if (aim <= -FastMath.PI / 6 && aim > -FastMath.PI / 3) {
            //down right
            return 3;
        } else if (aim <= -FastMath.PI / 3 && aim > -2 * FastMath.PI / 3) {
            //facing down
            return 4;
        } else if (aim <= -2 * FastMath.PI / 3 && aim > -5 * FastMath.PI / 6) {
            //down left
            return 5;
        } else {
            //facing left
            return 6;
        }
    }
}