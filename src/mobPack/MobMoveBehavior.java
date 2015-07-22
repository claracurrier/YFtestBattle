/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mobPack;

import battlestatepack.GVars;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import java.util.Random;
import pathfindingPack.Pathfinder;

/**
 *
 * @author Clara Currier
 */
public class MobMoveBehavior {

    public MobMoveBehavior() {
        //these are specific pathfinding behaviors implemented as controls
    }

    public abstract class MobMoveBehaviorCont extends AbstractControl {
        //for hierarchy

        protected float length;
        protected float timer = 0;

        public MobMoveBehaviorCont(float length) {
            this.length = length;
        }

        @Override
        protected abstract void controlUpdate(float tpf);

        @Override
        protected void controlRender(RenderManager rm, ViewPort vp) {
        }
    }

    public void pursue(final Node target, final Node mob, float time) {

        //actively runs towards target after "picking" them
        mob.addControl(new MobMoveBehaviorCont(time) {
            @Override
            protected void controlUpdate(float tpf) {
                if (timer < length) {
                    if (mob.getControl(MobMoveCont.class) == null
                            && mob.getControl(Pathfinder.class) == null) {
                        Vector2f mobLoc = new Vector2f(
                                mob.getLocalTranslation().x,
                                mob.getLocalTranslation().y);
                        Vector2f targLoc = new Vector2f(
                                target.getLocalTranslation().x,
                                target.getLocalTranslation().y);
                        mob.addControl(new Pathfinder(mobLoc, targLoc, 35));
                    }
                    timer += tpf;
                } else {
                    mob.removeControl(this);
                }
            }
        });
    }

    public void wander(final Node mob, float length) {
        //move around randomly
        mob.addControl(new MobMoveBehaviorCont(length) {
            private final Random rand = new Random();
            Vector3f dir = new Vector3f();
            int dirint = 0;
            private float changeDirTimer = 5;

            @Override
            protected void controlUpdate(float tpf) {
                if (timer < length) {
                    if (changeDirTimer >= 1.3f) {
                        dirint = (int) (rand.nextFloat() * 8);
                        switch (dirint) {
                            case 0:
                                dir.set(0, 1f, 0);
                                break;
                            case 1:
                                dir.set(.707f, .707f, 0);
                                break;
                            case 2:
                                dir.set(1f, 0, 0);
                                break;
                            case 3:
                                dir.set(.707f, -.707f, 0);
                                break;
                            case 4:
                                dir.set(0, -1f, 0);
                                break;
                            case 5:
                                dir.set(-.707f, -.707f, 0);
                                break;
                            case 6:
                                dir.set(-1f, 0, 0);
                                break;
                            case 7:
                                dir.set(-.707f, .707f, 0);
                                break;
                        }
                        changeDirTimer = 0;
                    } else {
                        changeDirTimer += tpf;
                        switch (dirint) {
                            case 0:
                                if (spatial.getUserData("canU")) {
                                    spatial.move(dir.mult(tpf * GVars.gvars.mspeed));
                                } else {
                                    changeDirTimer = 10;
                                }
                                break;
                            case 1:
                                if ((boolean) spatial.getUserData("canU") && (boolean) spatial.getUserData("canR")) {
                                    spatial.move(dir.mult(tpf * GVars.gvars.mspeed));
                                } else {
                                    changeDirTimer = 10;
                                }
                                break;
                            case 2:
                                if (spatial.getUserData("canR")) {
                                    spatial.move(dir.mult(tpf * GVars.gvars.mspeed));
                                } else {
                                    changeDirTimer = 10;
                                }
                                break;
                            case 03:
                                if ((boolean) spatial.getUserData("canD") && (boolean) spatial.getUserData("canR")) {
                                    spatial.move(dir.mult(tpf * GVars.gvars.mspeed));
                                } else {
                                    changeDirTimer = 10;
                                }
                                break;
                            case 04:
                                if (spatial.getUserData("canD")) {
                                    spatial.move(dir.mult(tpf * GVars.gvars.mspeed));
                                } else {
                                    changeDirTimer = 10;
                                }
                                break;
                            case 05:
                                if ((boolean) spatial.getUserData("canD") && (boolean) spatial.getUserData("canL")) {
                                    spatial.move(dir.mult(tpf * GVars.gvars.mspeed));
                                } else {
                                    changeDirTimer = 10;
                                }
                                break;
                            case 06:
                                if (spatial.getUserData("canL")) {
                                    spatial.move(dir.mult(tpf * GVars.gvars.mspeed));
                                } else {
                                    changeDirTimer = 10;
                                }
                                break;
                            case 07:
                                if ((boolean) spatial.getUserData("canU") && (boolean) spatial.getUserData("canL")) {
                                    spatial.move(dir.mult(tpf * GVars.gvars.mspeed));
                                } else {
                                    changeDirTimer = 10;
                                }
                                break;
                        }
                    }
                    timer += tpf;
                } else {
                    mob.removeControl(this);
                }
            }
        });
    }

    public void idle(final Node mob, float length) {
        mob.addControl(new MobMoveBehaviorCont(length) {
            @Override
            protected void controlUpdate(float tpf) {
                if (timer < length) {
                    //do nothing
                    timer += tpf;
                } else {
                    mob.removeControl(this);
                }
            }
        });
    }
}
