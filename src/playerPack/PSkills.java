/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package playerPack;

import battlestatepack.BattleMain;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import com.jme3.system.AppSettings;
import java.util.HashMap;

/**
 *
 * @author Clara Currier
 */
public class PSkills {

    private HashMap<Skills, Cooldown> cooldowns = new HashMap<>();
    private Player dan, kirith;
    private AppSettings settings = BattleMain.settings; //lmao damn i need to clean this
    private Camera cam;

    public PSkills(Player d, Player k, Camera c) {
        dan = d;
        kirith = k;
        cam = c;
    }

    public static enum Skills {

        tripleShot, stun, push, headshot, nothing;
    }

    public void useSkill(Skills skill, Vector2f mousePos, boolean activate) {
        switch (skill) {
            case tripleShot:
                tripleShot(mousePos, activate);
                break;
            case headshot:
                headshot(mousePos, activate);
            case stun:
                stun(activate);
                break;
            case push:
                push(activate);
                break;
            case nothing: //empty skill button
                break;
        }
    }

    private Vector2f getTrueMouseLoc(Vector2f mouse) {
        //corrects the mouse location
        Vector3f mousePosition3d = cam.getWorldCoordinates(mouse, 0).clone();
        Vector3f direction = cam.getWorldCoordinates(mouse, 1f).subtractLocal(mousePosition3d).normalizeLocal();

        // 1. Reset results list.
        CollisionResults results = new CollisionResults();
        // 2. Aim the ray from cam loc to cam direction.
        Ray ray = new Ray(mousePosition3d, direction);
        // 3. Collect intersections between Ray and rootNode (where map is attached)
        BattleMain.BATTLENODE.getParent().getChild("Map").collideWith(ray, results); //Map node
        Vector3f trueloc3d = results.getClosestCollision().getGeometry().getLocalTranslation();
        return new Vector2f(trueloc3d.x, trueloc3d.y);
    }

    private class Cooldown extends AbstractControl {

        private float counter = 0;
        private float length;

        public Cooldown(float length) {
            this.length = length;
        }

        @Override
        protected void controlUpdate(float tpf) {
            counter += tpf;
        }

        public boolean isReady() {
            return counter >= length;
        }

        @Override
        protected void controlRender(RenderManager rm, ViewPort vp) {
        }
    }

    /**
     * **************
     * Dan's skills *
     */
    public void tripleShot(Vector2f mousePos, boolean activate) {
        if (cooldowns.get(Skills.tripleShot) == null
                || cooldowns.get(Skills.tripleShot).isReady()) {
            SkillGraphic graphic = SkillGraphicFactory.newInstance();
            if (activate) {
                graphic.fireArrow(getTrueMouseLoc(mousePos));
                dan.getNode().removeControl(Cooldown.class);

                Cooldown cooldown = new Cooldown(4f);
                dan.getNode().addControl(cooldown);
                cooldowns.put(Skills.tripleShot, cooldown);
            } else {
                graphic.makeGUIHelper();
            }
            graphic = null;
        } else {
            System.out.println("cooldown");
        }
    }

    public void headshot(Vector2f mousePos, boolean activate) {
    }

    /**
     * *****************
     * Kirith's skills *
     */
    public void stun(boolean activate) {
    }

    public void push(boolean activate) {
    }
}
