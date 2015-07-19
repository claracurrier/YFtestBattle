/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mobPack;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import java.util.LinkedList;
import pathfindingPack.Pathway;

/**
 *
 * @author Clara Currier
 */
public class MobMoveCont extends AbstractControl {
    //specific for the alpha mob until mobs get their full sprite sheets

    private LinkedList<Vector2f> path;
    private int dir = 4;
    private Vector2f curCoord;
    private Vector2f targCoord;

    public MobMoveCont(Pathway p, String name) {
        path = p.getSmoothPath(p.getPath().size());

        curCoord = path.removeFirst();
        targCoord = path.removeFirst();
    }

    public int getDir() {
        return dir;
    }

    public void setDir(int d) {
        dir = d;
    }

    @Override
    public void controlUpdate(float tpf) {
        spatial.move(targCoord.x - curCoord.x, targCoord.y - curCoord.y, 0);
        dir = findMoveDir(curCoord, targCoord);
        if (!path.isEmpty()) {
            curCoord = targCoord;
            targCoord = path.removeFirst();
        } else {
            //stop moving
            spatial.removeControl(this);
        }
    }

    private int findMoveDir(Vector2f cur, Vector2f targ) {
        Vector2f newvec = targ.subtract(cur);
        float aim = newvec.normalize().getAngle();

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

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}
