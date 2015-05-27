/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlestatepack;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector2f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author PC
 */
public class CollideAS extends AbstractAppState {

    private final Node atkNode, defNode;
    private final Vector2f xaxis = new Vector2f(1f, 0);
    private final Vector2f yaxis = new Vector2f(0, 1f);
    private boolean collided = false;
    private Spatial atkchild, defchild, spatial, curChild;
    private int val;

    public CollideAS() {
        atkNode = BattleMain.ATKNODE;
        defNode = BattleMain.DEFNODE;
    }

    @Override
    public void initialize(AppStateManager asm, Application app) {
    }

    @Override
    public void cleanup() {
        super.cleanup();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    public void setMovingSpatial(Spatial spat) {
        spatial = spat;
    }

    @Override
    public void update(float tpf) {
        if (isEnabled()) {
            moveCheck();
            attackCheck();
        }
    }

    private void attackCheck() {
        for (int i = 0; i < atkNode.getQuantity(); i++) {
            for (int j = 0; j < defNode.getQuantity(); j++) {
                atkchild = atkNode.getChild(i); //attacker
                defchild = defNode.getChild(j); //reciver


                if (noException(atkchild, defchild)) {
                    val = satTest(atkchild, defchild);
                    if (val > 0) {
                        atkchild.setUserData("collided", true);
                        defchild.setUserData("collided", atkchild.getName());
                        defchild.setUserData("atkpower", atkchild.getUserData("atkpower"));
                        defchild.setUserData("atkdirection", val);

                        System.out.println(atkchild + " collided into " + defchild);

                    }
                }
            }
        }
    }

    private boolean noException(Spatial atkchild, Spatial defchild) {
        if (defchild.getName().equals("Dan")
                && atkchild.getUserData("type").equals("arrow")) {
            //dan and arrow case
            return false;
        }
        if (atkchild.getName().equals("mobatkbox")
                && defchild.getUserData("type").equals("mob")) {
            //mob and its atkbox case
            return false;
        }
        if (atkchild.getUserData("type").equals("kiatkbox")
                && defchild.getName().equals("Kirith")) {
            //ki and her attacks
            return false;
        }

        return true;
    }

    private void moveCheck() {

        collided = false;
        //collision loop
        for (int i = 0; i < defNode.getQuantity(); i++) {

            curChild = defNode.getChild(i);

            if (!curChild.equals(spatial)) {

                val = satTest(spatial, curChild);
                if (val > 0) {
                    switch (val) {
                        case 1: //no left
                            spatial.setUserData("canL", false);
                            collided = true;
                            break;
                        case 2: //no right
                            spatial.setUserData("canR", false);
                            collided = true;
                            break;
                        case 3: //no down
                            spatial.setUserData("canU", false);
                            collided = true;
                            break;
                        case 4: //no up
                            spatial.setUserData("canD", false);
                            collided = true;

                            break;
                    }
                }

            } else if (!collided) {
                spatial.setUserData("canU", true);
                spatial.setUserData("canD", true);
                spatial.setUserData("canR", true);
                spatial.setUserData("canL", true);
            }

        }
    }

    private int satTest(Spatial mover, Spatial targ) {
        //Separating Axis Theorem application
        //x axis check
        // project both shapes onto the axis
        Vector2f xmoverp = project(xaxis, mover);
        Vector2f xtargp = project(xaxis, targ);

        //y axis check
        Vector2f ymoverp = project(yaxis, mover);
        Vector2f ytargp = project(yaxis, targ);
        // do the projections overlap?
        if (overlap(xmoverp, xtargp) && overlap(ymoverp, ytargp)) {
            //if they both overlap, then we have collision
            //calculates the direction of collision by comparing the smallest overlap
            if (Math.max(0, Math.min(xmoverp.y, xtargp.y) - Math.max(xmoverp.x, xtargp.x))
                    < Math.max(0, Math.min(ymoverp.y, ytargp.y) - Math.max(ymoverp.x, ytargp.x))) {
                //if distance of x direction is shorter and therefore colliding left or right...
                if (xmoverp.x > xtargp.x) {

                    return 1;//mover is left of targ
                } else {

                    return 2; //mover is right of targ
                }
            } else {
                //y dir is shorter and therefore colliding up or down
                if (ymoverp.x > ytargp.x) {

                    return 4; //mover is below targ
                } else {

                    return 3; //mover is above targ
                }
            }
        } else {
            return 0; //no collision
        }

    }

    private Vector2f project(Vector2f axis, Spatial shape) {
        //this takes in the rectangles hitboxes, and finds its projection

        //widths and heights
        float w = shape.getUserData("halfwidth");
        float h = shape.getUserData("halfheight");
        float x = shape.getWorldTranslation().x;
        float y = shape.getWorldTranslation().y;

        Vector2f botleft = new Vector2f(x - w, y - h);

        if (axis.equals(yaxis)) {
            Vector2f upleft = new Vector2f(x - w, y + h);
            return new Vector2f(axis.dot(botleft), axis.dot(upleft));
            //both of these return statements give the projection coordinates
            //in order of min, max (they are 1d axis-aligned) line segments
            //so ([0][0], [0][1])

        } else { //xaxis
            Vector2f botright = new Vector2f(x + w, y - h);
            return new Vector2f(axis.dot(botleft), axis.dot(botright));
            // ([0,0], [1][0])
        }

    }

    private boolean overlap(Vector2f mover, Vector2f targ) {
        //compares locations of vertices
        //if the greater vertex of mover is less than the smaller
        //vertex of targ, then no overlap. Same for reverse
        //x is smaller, y is greater

        if (mover.x > targ.y || mover.y < targ.x) {
            return false;
        } else {
            return true;
        }
    }
}