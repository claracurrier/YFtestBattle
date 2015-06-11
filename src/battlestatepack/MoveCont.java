/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlestatepack;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import spriteProject.SpriteLibrary;

/**
 *
 * @author Clara Currier
 */
public class MoveCont extends AbstractControl {

    private Pathway path;
    private final float speed = GVars.gvars.pspeed;
    private int dir = 4;
    private SpriteLibrary spatSL;
    private float distanceCovered;
    private int dirsCovered;

    public MoveCont(Pathway p, String name) {
        path = p;
        spatSL = BattleMain.sEngine.getLibrary(name);
    }

    public int getDir() {
        return dir;
    }

    public void setDir(int d) {
        dir = d;
    }

    @Override
    public void controlUpdate(float tpf) {
        if (dirsCovered < path.getDirs().length) {
            if (distanceCovered <= path.getLengths()[dirsCovered]) {
                switch (path.getDirs()[dirsCovered]) {
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
                dirsCovered++;
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

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}