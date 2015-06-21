/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlestatepack;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import java.util.LinkedList;
import spriteProject.SpriteLibrary;

/**
 *
 * @author Clara Currier
 */
public class MoveCont extends AbstractControl {

    private LinkedList<Vector2f> path;
    private final float speed = GVars.gvars.pspeed;
    private int dir = 4;
    private SpriteLibrary spatSL;
    private float distanceCovered;
    private Vector2f curCoord;
    private Vector2f targCoord;
    boolean runOnce = true;

    public MoveCont(Pathway p, String name) {
        path = p.getPath();
        spatSL = BattleMain.sEngine.getLibrary(name);
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
        if (runOnce) {
            if (!path.isEmpty()) {
                curCoord = targCoord;
                targCoord = path.removeFirst();
            }

            if (distanceCovered < curCoord.distance(targCoord)) {
                switch (findMoveDir(curCoord, targCoord)) {
                    case 0: //up
                        spatial.move(0, tpf * speed, 0);
                        spatSL.activateSprite(0);
                        dir = 0;
                        distanceCovered += tpf * speed;
                        break;
                    case 1://upright
                        spatial.move(tpf * speed / 1.414f, tpf * speed / 1.414f, 0);
                        spatSL.activateSprite(1);
                        dir = 1;
                        distanceCovered += tpf * speed / 2.818f;
                        break;
                    case 2://right
                        spatial.move(tpf * speed, 0, 0);
                        spatSL.activateSprite(2);
                        dir = 2;
                        distanceCovered += tpf * speed;
                        break;
                    case 3: //downright
                        spatial.move(tpf * speed / 1.414f, tpf * -speed / 1.414f, 0);  //needs to be fixed
                        spatSL.activateSprite(3);
                        dir = 3;
                        distanceCovered += tpf * speed / 2.818f;
                        break;
                    case 4://down
                        spatial.move(0, tpf * -speed, 0);
                        spatSL.activateSprite(4);
                        dir = 4;
                        distanceCovered += tpf * speed;
                        break;
                    case 5://downleft
                        spatial.move(tpf * -speed / 1.414f, tpf * -speed / 1.414f, 0);
                        spatSL.activateSprite(5);
                        dir = 5;
                        distanceCovered += tpf * speed / 2.818f;
                        break;
                    case 6: //left
                        spatial.move(tpf * -speed, 0, 0);
                        spatSL.activateSprite(6);
                        dir = 6;
                        distanceCovered += tpf * speed;
                        break;
                    case 7: //upleft
                        spatial.move(tpf * -speed / 1.414f, tpf * speed / 1.414f, 0);
                        spatSL.activateSprite(7);
                        dir = 7;
                        distanceCovered += tpf * speed / 2.818f;
                        break;
                }

            } else {
                distanceCovered = 0;
                if (path.isEmpty()) {
                    runOnce = false;
                }
            }
        } else {
            //stop moving
            switch (dir) { //activite proper idle sprite
                case 0:
                    spatSL.activateSprite(8);
                    break;
                case 1:
                    spatSL.activateSprite(9);
                    break;
                case 2:
                    spatSL.activateSprite(10);
                    break;
                case 3:
                    spatSL.activateSprite(11);
                    break;
                case 4:
                    spatSL.activateSprite(12);
                    break;
                case 5:
                    spatSL.activateSprite(13);
                    break;
                case 6:
                    spatSL.activateSprite(14);
                    break;
                case 7:
                    spatSL.activateSprite(15);
                    break;
            }
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