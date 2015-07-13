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
    private int dir = 4;
    private SpriteLibrary spatSL;
    private Vector2f curCoord;
    private Vector2f targCoord;

    public MoveCont(Pathway p, String name) {
        path = p.getSmoothPath(p.getPath().size());

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
        spatial.move(targCoord.x - curCoord.x, targCoord.y - curCoord.y, 0);
        switch (findMoveDir(curCoord, targCoord)) {
            case 0: //up
                spatSL.activateSprite(0);
                dir = 0;
                break;
            case 1://upright
                spatSL.activateSprite(1);
                dir = 1;
                break;
            case 2://right
                spatSL.activateSprite(2);
                dir = 2;
                break;
            case 3: //downright
                spatSL.activateSprite(3);
                dir = 3;
                break;
            case 4://down
                spatSL.activateSprite(4);
                dir = 4;
                break;
            case 5://downleft
                spatSL.activateSprite(5);
                dir = 5;
                break;
            case 6: //left
                spatSL.activateSprite(6);
                dir = 6;
                break;
            case 7: //upleft
                spatSL.activateSprite(7);
                dir = 7;
                break;
        }
        if (!path.isEmpty()) {
            curCoord = targCoord;
            targCoord = path.removeFirst();
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