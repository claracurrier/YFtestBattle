/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlestatepack.mobPack;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import java.util.Random;

/**
 *
 * @author PC
 */
public class MobSkill {

    public static final MobSkill mobSkill = new MobSkill();
    /*
     * singleton class containing all hard-coded skills and abilities
     * whether a mob uses these skills or not depends on the mob at instantiation
     * every skill is an update()-able loop
     */

    private MobSkill() {
    }

    public void pursue(Spatial t, Spatial m, float ti) {
        final Spatial targ = t;
        final Spatial mob = m;
        final float time = ti;

        mob.addControl(new AbstractControl() {
            float timer = 0;

            @Override
            protected void controlUpdate(float tpf) {
                if (timer < time) {
                    Vector3f dir = getTargDir(targ);
                    mob.move(dir.multLocal(tpf * 200));
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

            @Override
            protected void controlRender(RenderManager rm, ViewPort vp) {
            }
        });
    }

    public void wander(Spatial m, float ti) {
        final Spatial mob = m;
        final float time = ti;

        mob.addControl(new AbstractControl() {
            private final Random rand = new Random();
            private Vector3f dir = new Vector3f(rand.nextFloat() * 2 - 1f, rand.nextFloat() * 2 - 1f, 0);
            private float changeDirTimer = 0;
            private float timer = 0;

            @Override
            protected void controlUpdate(float tpf) {
                if (timer <= time) {
                    if (changeDirTimer >= 1.25f) {
                        dir = new Vector3f(rand.nextFloat() * 2 - 1f, rand.nextFloat() * 2 - 1f, 0);
                        dir.normalizeLocal();
                        changeDirTimer = 0;
                    } else {
                        changeDirTimer += tpf;
                        spatial.move(dir.multLocal(tpf * 200));
                    }
                    timer += tpf;
                } else {
                    mob.removeControl(this);
                }
            }

            @Override
            protected void controlRender(RenderManager rm, ViewPort vp) {
            }
        });
    }

    public void idle(Spatial m, float ti) {
        final Spatial mob = m;
        final float time = ti;

        mob.addControl(new AbstractControl() {
            float timer = 0;

            @Override
            protected void controlUpdate(float tpf) {
                if (timer <= time) {
                    timer += tpf;
                } else {
                    mob.removeControl(this);
                }
            }

            @Override
            protected void controlRender(RenderManager rm, ViewPort vp) {
            }
        });
    }

    public void testDash(Spatial t, Spatial m) {
        final Spatial targ = t;
        final Spatial mob = m;

        mob.addControl(new AbstractControl() {
            float timer = 0;

            @Override
            protected void controlUpdate(float tpf) {
                Vector3f dir = new Vector3f(
                        targ.getLocalTranslation().x - mob.getLocalTranslation().x,
                        targ.getLocalTranslation().y - mob.getLocalTranslation().y, 0);

                dir.normalizeLocal().multLocal(200f);
                if (timer <= 1) {
                    mob.setLocalScale(1.3f);
                    timer += tpf / 2;
                } else {
                    mob.setLocalScale(.75f);
                    mob.move(dir);
                    mob.removeControl(this);
                }
            }

            @Override
            protected void controlRender(RenderManager rm, ViewPort vp) {
            }
        });
    }
}