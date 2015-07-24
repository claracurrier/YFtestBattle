/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package skillPack;

import battlestatepack.BattleMain;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import java.util.ArrayList;
import mapPack.Map;
import mapPack.MapLayer;
import mapPack.Tile;
import playerPack.ArrowControl;

/**
 *
 * @author Clara Currier
 */
public class SkillBehavior {

    public SkillBehavior() {
        /* 
         * This class contains basic skill frameworks (ie. attack types)
         * 
         * Hitboxes should be made here with their attack type and who made it
         * The combination of these behaviors should allow for a wide possibility of skills
         */
    }

    private Node makeAttackBox(Node source, float power, String name, final float time, float boxw, float boxh) {
        Node node = new Node(name);

        node.setUserData("collided", "none");
        node.setUserData("atkpower", power);
        node.setUserData("halfwidth", boxw / 2);
        node.setUserData("halfheight", boxh / 2);
        node.setLocalTranslation(source.getLocalTranslation());
        node.setUserData("source", source.getName());

        node.addControl(new AbstractControl() {
            float hbtimer = 0;

            @Override
            protected void controlUpdate(float tpf) {
                if (hbtimer > time || !spatial.getUserData("collided").equals("none")) {
                    spatial.removeFromParent();
                } else {
                    hbtimer += tpf;
                }
            }

            @Override
            protected void controlRender(RenderManager rm, ViewPort vp) {
            }
        });
        BattleMain.ATKNODE.attachChild(node);
        return node;
    }

    private void positionAttackBox(Node node, int dir, float y, float x) {
        switch (dir) {
            //moves the node to appropriate location
            case 0: //up
                node.move(0, y, 0);
                break;
            case 1:
                node.move(x, y, 0);
            case 2: //right
                node.move(x, 0, 0);
                break;
            case 3:
                node.move(x, -y, 0);
                break;
            case 4: //down
                node.move(0, -y, 0);
                break;
            case 5:
                node.move(-x, -y, 0);
                break;
            case 6: //left
                node.move(-x, 0, 0);
                break;
            case 7:
                node.move(-x, y, 0);
                break;
        }
    }

    private int findDirectionBetween(Vector3f targ, Vector3f cur) {
        Vector2f newvec = new Vector2f(cur.x, cur.y).subtractLocal(targ.x, targ.y);
        float aim = newvec.normalizeLocal().getAngle();

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

    public void dash(Node source, final Node target) {
        //charges towards node in a straight line
        MapLayer mapLayer = Map.getTransparentLayer();
        ArrayList<Tile> closedTiles = new ArrayList<>();

        Tile mobPosTile = mapLayer.getTile(source.getLocalTranslation().x,
                source.getLocalTranslation().y);
        Tile playerPosTile = mapLayer.getTile(target.getLocalTranslation().x,
                target.getLocalTranslation().y);

        for (int i = (mobPosTile.getLocArray()[0][0] < playerPosTile.getLocArray()[0][0]
                ? mobPosTile.getLocArray()[0][0] : playerPosTile.getLocArray()[0][0]);
                i < (mobPosTile.getLocArray()[0][0] > playerPosTile.getLocArray()[0][0]
                ? mobPosTile.getLocArray()[0][0] : playerPosTile.getLocArray()[0][0]); i++) {
            //runs from the "smaller" horizontal tile position to other entity's position
            for (int j = (mobPosTile.getLocArray()[0][1] < playerPosTile.getLocArray()[0][1]
                    ? mobPosTile.getLocArray()[0][1] : playerPosTile.getLocArray()[0][1]);
                    j < (mobPosTile.getLocArray()[0][1] > playerPosTile.getLocArray()[0][1]
                    ? mobPosTile.getLocArray()[0][1] : playerPosTile.getLocArray()[0][1]); j++) {
                //vertical smaller tile to other position 
                //Makes sure tiles are assessed from top-left to bot-right
                if (mapLayer.getTile(i, j).isClosedIgnoreChars()) {
                    closedTiles.add(mapLayer.getTile(i, j));
                }
            }
        }

        if (!closedTiles.isEmpty()) {
            ArrayList<Vector2f> poly = new ArrayList<>();
            poly.add(new Vector2f(source.getLocalTranslation().x,
                    source.getLocalTranslation().y - 16));
            poly.add(new Vector2f(source.getLocalTranslation().x,
                    source.getLocalTranslation().y + 16));
            poly.add(new Vector2f(target.getLocalTranslation().x,
                    target.getLocalTranslation().y + 16));
            poly.add(new Vector2f(target.getLocalTranslation().x,
                    target.getLocalTranslation().y - 16));
            poly.add(new Vector2f(source.getLocalTranslation().x,
                    source.getLocalTranslation().y - 16));
            //TODO: find a better way to get the corners of the testing rectangle
            //last point repeated
            WindingPointTest tester = new WindingPointTest();

            for (Tile tile : closedTiles) {
                int winds = tester.Vector2fInPoly(tile.getLocVector(), poly);
                if (winds > 0) {
                    System.out.println("can't dash: " + winds
                            + "\n" + tile);
                    return; //don't dash, failure
                }
            }
        }
        //execute dash
        source.addControl(new AbstractControl() {
            int steps = 0;

            @Override
            protected void controlUpdate(float tpf) {
                if (steps < 10) {
                    spatial.move(target.getLocalTranslation().divide(10));
                    steps++;
                } else {
                    spatial.removeControl(this);
                }
            }

            @Override
            protected void controlRender(RenderManager rm, ViewPort vp) {
            }
        });
    }

    public Node tackle(Node source, Vector3f target, float width, float height, float power) {
        //bangs into player in melee distance
        Node tacklebox = makeAttackBox(source, power, "tackle", .4f, width, height);

        positionAttackBox(tacklebox,
                findDirectionBetween(target, source.getLocalTranslation()),
                height / (1.5f), width / (1.5f));

        return tacklebox;
    }

    public Node projectile(Node source, Vector2f target, float width, float height, float power) {
        //the hitbox moves

        Node projectile = makeAttackBox(source, power, "projectile", 10f, width, height);

        projectile.addControl(new ArrowControl(new Vector2f(
                target.x, target.y).subtract(
                source.getLocalTranslation().x,
                source.getLocalTranslation().y).normalizeLocal()));

        return projectile;
    }

    public void stream(Node source, Node target) {
        //a persistent ranged attack that acts like a wall
    }

    public void explosion(Node source, Node target) {
        //area of effect in a circle
    }

    public void sweep(Node source, Node target) {
        //melee swipe that hits a larger area than tackle
    }
}
