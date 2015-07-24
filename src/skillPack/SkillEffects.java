/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package skillPack;

import battlestatepack.EntityWrapper;
import battlestatepack.GVars;
import battlestatepack.KnockbackCont;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author Clara Currier
 */
public class SkillEffects {

    public SkillEffects() {
        /*
         * This class contains the components that can change a status
         * 
         * Characterized by a reversable "Command" executed after a timer
         */
    }

    protected void addDisplacement(EntityWrapper target, EntityWrapper source, float distance, float intensity) {
        //moves the target a certain amount in the given direction if possible

        target.getNode().addControl(new KnockbackCont(GVars.gvars.mminmovement + distance,
                intensity * GVars.gvars.mintensitymovemod + GVars.gvars.mminintensity,
                target.getNode().getName(), findAimDir(
                new Vector2f(source.getNode().getLocalTranslation().x, source.getNode().getLocalTranslation().y),
                new Vector2f(target.getNode().getLocalTranslation().x, target.getNode().getLocalTranslation().y))));

    }

    private int findAimDir(Vector2f cur, Vector2f targ) {
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

    protected void addMovementModifier(final EntityWrapper target, float length, int speed) {
        //slows or speeds up the target
        target.setSpeed(speed);
        target.getNode().addControl(new Timer(length, new Command() {
            @Override
            public void execute() {
                target.setSpeed(3);
            }
        }));
    }

    protected void addStun(final EntityWrapper target, float length) {
        //completely stops the target
        target.setEnabled(false);
        addDamageModifier(target, 1.5f, length); //stuns always add a basic modifier
        target.getNode().addControl(new Timer(length, new Command() {
            @Override
            public void execute() {
                target.setEnabled(true);
            }
        }));
    }

    protected void addDamageModifier(final EntityWrapper target, float dmgmod, float length) {
        //makes target more or less susceptible to damage
        target.setDamageModifier(dmgmod);
        target.getNode().addControl(new Timer(length, new Command() {
            @Override
            public void execute() {
                target.setDamageModifier(1);
            }
        }));
    }

    private interface Command {
        //used for resetting a status after a timer

        public void execute();
    }

    private class Timer extends AbstractControl {

        private float counter = 0;
        private float length;
        private Command command;

        public Timer(float length, Command command) {
            this.length = length;
            this.command = command;
        }

        @Override
        protected void controlUpdate(float tpf) {
            if (counter < length) {
                counter += tpf;
            } else {
                command.execute();
                spatial.removeControl(this);
            }
        }

        @Override
        protected void controlRender(RenderManager rm, ViewPort vp) {
        }
    }
}
