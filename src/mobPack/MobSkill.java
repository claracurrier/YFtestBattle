/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mobPack;

import battlestatepack.GVars;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import java.util.Random;

/**
 *
 * @author Clara Currier
 */
public class MobSkill {

    public static final MobSkill mobSkill = new MobSkill();
    /*
     * TODO: this class needs a major overhaul
     */
    private float speed = GVars.gvars.mspeed;

    private MobSkill() {
    }

    public abstract class mSkillCont extends AbstractControl {
        //for hierarchy

        @Override
        protected abstract void controlUpdate(float tpf);

        @Override
        protected void controlRender(RenderManager rm, ViewPort vp) {
        }
    }

    public void pursue(Spatial t, Spatial m, float ti) {
        final Spatial targ = t;
        final Spatial mob = m;
        final float time = ti;

        mob.addControl(new mSkillCont() {
            float timer = 0;

            @Override
            protected void controlUpdate(float tpf) {
                if (timer < time) {
                    Vector3f dir = getTargDir(targ);
                    mob.move(dir.multLocal(tpf * speed));
                    timer += tpf;
                } else {
                    mob.removeControl(this);
                }
            }

            private Vector3f getTargDir(Spatial targ) {
                Vector3f targvel = new Vector3f(
                        targ.getLocalTranslation().x - mob.getLocalTranslation().x,
                        targ.getLocalTranslation().y - mob.getLocalTranslation().y, 0);
                return targvel.normalizeLocal();
            }
        });
    }

    public void wander(Spatial m, float ti) {
        final Spatial mob = m;
        final float time = ti;

        mob.addControl(new mSkillCont() {
            private final Random rand = new Random();
            private Vector3f dir = new Vector3f(rand.nextFloat() * 2 - 1f, rand.nextFloat() * 2 - 1f, 0);
            private float changeDirTimer = 0;
            private float timer = 0;

            @Override
            protected void controlUpdate(float tpf) {
                if (timer <= time) {
                    if (changeDirTimer >= 1.3f) {
                        dir = new Vector3f(rand.nextFloat() * 2 - 1f, rand.nextFloat() * 2 - 1f, 0);
                        dir.normalizeLocal();
                        changeDirTimer = 0;
                    } else {
                        changeDirTimer += tpf;
                        spatial.move(dir.mult(tpf * speed));
                    }
                    timer += tpf;
                } else {
                    mob.removeControl(this);
                }
            }
        });
    }

    public void idle(Spatial m, float ti) {
        final Spatial mob = m;
        final float time = ti;

        mob.addControl(new mSkillCont() {
            float timer = 0;

            @Override
            protected void controlUpdate(float tpf) {
                if (timer <= time) {
                    timer += tpf;
                } else {
                    mob.removeControl(this);
                }
            }
        });
    }

    public void testDash(Spatial t, Spatial m) {
        final Spatial targ = t;
        final Spatial mob = m;

        mob.addControl(new mSkillCont() {
            private float growtimer = 0;
            private float chargetimer = 0;
            private boolean setdir = false;
            private Vector3f dir = new Vector3f();

            @Override
            protected void controlUpdate(float tpf) {
                if (growtimer <= 1) {
                    mob.setLocalScale(1f + growtimer);
                    growtimer += tpf / 2;
                } else {
                    mob.setLocalScale(1f);
                    if (!setdir) {
                        dir = new Vector3f(
                                targ.getLocalTranslation().x - mob.getLocalTranslation().x,
                                targ.getLocalTranslation().y - mob.getLocalTranslation().y, 0);
                        dir.normalizeLocal().multLocal(4f);
                        setdir = true;
                    }
                    if (chargetimer <= .3) {
                        mob.move(dir);
                        chargetimer += tpf;
                    } else {
                        mob.removeControl(this);
                    }
                }
            }
            /*
             @Override
             public void setEnabled(boolean enabled) {
             super.setEnabled(enabled);
             if (enabled) {
             if (!menuPack.MainMenu.isPaused()) {
             mob.removeControl(this);
             }
             }
             }
             */
        });
    }
}