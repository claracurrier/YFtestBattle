/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package skillPack;

import battlestatepack.EntityWrapper;
import battlestatepack.BattleMain;
import battlestatepack.GVars;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import java.util.HashMap;
import skillPack.SkillCooldown.Cooldown;

/**
 *
 * @author Clara Currier
 */
public class PlayerSkills {

    private HashMap<Skills, Cooldown> cooldowns = new HashMap<>();
    private EntityWrapper dan, kirith;
    private Camera cam;
    private SkillGraphic graphic;
    private SkillCooldown skillCooldown;
    private SkillEffects effects;
    private SkillBehavior behavior;

    public PlayerSkills(EntityWrapper d, EntityWrapper k, Camera c, SkillGraphic skillgraph,
            SkillCooldown cooldown, SkillEffects effect, SkillBehavior behave) {
        dan = d;
        kirith = k;
        cam = c;
        graphic = skillgraph;
        skillCooldown = cooldown;
        effects = effect;
        behavior = behave;
    }

    public enum Skills {

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
                stun(mousePos, activate);
                break;
            case push:
                push(mousePos, activate);
                break;
            case nothing: //empty skill button
                break;
        }
    }

    public SkillGraphic getGraphic() {
        return graphic;
    }

    private CollisionResults collide(Vector2f mouse) {
        //corrects the mouse location
        Vector3f mousePosition3d = cam.getWorldCoordinates(mouse, 0).clone();
        Vector3f direction = cam.getWorldCoordinates(mouse, 1f).subtractLocal(mousePosition3d).normalizeLocal();

        // 1. Reset results list.
        CollisionResults results = new CollisionResults();
        // 2. Aim the ray from cam loc to cam direction.
        Ray ray = new Ray(mousePosition3d, direction);
        // 3. Collect intersections between Ray and rootNode
        BattleMain.BATTLENODE.getParent().collideWith(ray, results); //rootNode

        return results;
    }

    private Vector2f getTrueMouseLoc(Vector2f mouse) {
        Geometry hit = collide(mouse).getClosestCollision().getGeometry();
        if (!hit.getName().contains("tile")) {
            Vector3f trueloc3d = hit.getParent().getParent().getLocalTranslation(); //node of entity
            return new Vector2f(trueloc3d.x, trueloc3d.y);
        } else {
            Vector3f trueloc3d = hit.getLocalTranslation(); //tile
            return new Vector2f(trueloc3d.x, trueloc3d.y);
        }
    }

    private EntityWrapper checkHitEntity(Vector2f mouse) {
        Geometry hit = collide(mouse).getClosestCollision().getGeometry();
        if (!hit.getName().contains("tile")) {
            return hit.getParent().getParent().getUserData("wrapper"); //should be the node
        }
        return null;
    }

    public void autoAttack(Vector3f target, EntityWrapper player) {
        if (player.equals(dan)) {
            Node effectpic = graphic.addPictureEffect("arrow", 10, 0, 0, false);
            Node arrow = behavior.projectile(dan.getNode(),
                    new Vector2f(target.x, target.y),
                    (float) effectpic.getUserData("width"),
                    (float) effectpic.getUserData("height"),
                    GVars.gvars.dbaseatkpower);
            arrow.attachChild(effectpic);
        } else {
            behavior.tackle(kirith.getNode(), target,
                    40f, 40f, GVars.gvars.kbaseatkpower);
        }
    }

    /**
     * **************
     * Dan's skills *
     */
    public void tripleShot(final Vector2f mousePos, boolean activate) {
        if (cooldowns.get(Skills.tripleShot) == null
                || cooldowns.get(Skills.tripleShot).isReady()) {
            graphic.removeTargetCursor();
            if (activate) {
                dan.getNode().addControl(new AbstractControl() { //timer control
                    float counter = 0;
                    float numarrows = 0;

                    @Override
                    protected void controlUpdate(float tpf) {
                        if (numarrows <= 2) {
                            if (counter > .1f) {
                                Node effectpic = graphic.addPictureEffect("arrow", 10, 0, 0, false);
                                Node arrow = behavior.projectile(dan.getNode(), getTrueMouseLoc(mousePos),
                                        (float) effectpic.getUserData("width"),
                                        (float) effectpic.getUserData("height"),
                                        GVars.gvars.dbaseatkpower * .8f);
                                arrow.attachChild(effectpic);
                                counter = 0;
                                numarrows++;
                            } else {
                                counter += tpf;
                            }
                        } else {
                            dan.getNode().removeControl(this);
                        }
                    }

                    @Override
                    protected void controlRender(RenderManager rm, ViewPort vp) {
                    }
                });
                dan.getNode().removeControl(cooldowns.get(Skills.tripleShot));

                Cooldown cooldown = skillCooldown.newCooldown(4, Skills.tripleShot);
                dan.getNode().addControl(cooldown);

                cooldowns.put(Skills.tripleShot, cooldown);
            } else {
                graphic.makeTargetCursor();
            }
        }
    }

    public void headshot(Vector2f mousePos, boolean activate) {
        if (cooldowns.get(Skills.headshot) == null
                || cooldowns.get(Skills.headshot).isReady()) {
            graphic.removeTargetCursor();
            if (activate) {
                Node effectpic = graphic.addPictureEffect("arrow", 10, 0, 0, false);
                Node arrow = behavior.projectile(dan.getNode(), getTrueMouseLoc(mousePos),
                        (float) effectpic.getUserData("width"),
                        (float) effectpic.getUserData("height"),
                        GVars.gvars.dbaseatkpower * 1.5f);
                arrow.attachChild(effectpic);

                dan.getNode().removeControl(cooldowns.get(Skills.headshot));
                Cooldown cooldown = skillCooldown.newCooldown(6, Skills.headshot);
                dan.getNode().addControl(cooldown);

                cooldowns.put(Skills.headshot, cooldown);
            } else {
                graphic.makeTargetCursor();
            }
        }
    }

    /**
     * *****************
     * Kirith's skills *
     */
    public void stun(Vector2f mousePos, boolean activate) {
        if (cooldowns.get(Skills.stun) == null
                || cooldowns.get(Skills.stun).isReady()) {
            graphic.removeTargetCursor();
            if (activate) {
                EntityWrapper entity = checkHitEntity(mousePos);
                if (entity != null) {
                    Node effectpic = graphic.addPictureEffect("kiidle4", 10, 0, 0, false);
                    Node tackle = behavior.tackle(kirith.getNode(), entity.getNode().getLocalTranslation(),
                            40f, 40f, GVars.gvars.kbaseatkpower * .6f);
                    tackle.attachChild(effectpic);

                    effects.addStun(entity, 4);
                    effects.addMovementModifier(entity, 6.3f, 6);

                    kirith.getNode().removeControl(cooldowns.get(Skills.stun));
                    Cooldown cooldown = skillCooldown.newCooldown(6, Skills.stun);
                    kirith.getNode().addControl(cooldown);
                    cooldowns.put(Skills.stun, cooldown);
                }
            } else {
                graphic.makeTargetCursor();
            }
        }
    }

    public void push(Vector2f mousePos, boolean activate) {
        if (cooldowns.get(Skills.push) == null
                || cooldowns.get(Skills.push).isReady()) {
            graphic.removeTargetCursor();
            if (activate) {
                EntityWrapper entity = checkHitEntity(mousePos);
                if (entity != null) {
                    Node effectpic = graphic.addPictureEffect("kiidle6", 10, 0, 0, false);
                    Node tackle = behavior.tackle(kirith.getNode(), entity.getNode().getLocalTranslation(),
                            40f, 40f, GVars.gvars.kbaseatkpower * .5f);
                    tackle.attachChild(effectpic);

                    effects.addDisplacement(entity, kirith, 40f, 8f);
                    effects.addMovementModifier(entity, 4f, 8);

                    kirith.getNode().removeControl(cooldowns.get(Skills.push));
                    Cooldown cooldown = skillCooldown.newCooldown(6, Skills.push);
                    kirith.getNode().addControl(cooldown);
                    cooldowns.put(Skills.push, cooldown);
                }
            } else {
                graphic.makeTargetCursor();
            }
        }
    }
}
