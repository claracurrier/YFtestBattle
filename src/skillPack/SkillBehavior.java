/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package skillPack;

import battlestatepack.EntityWrapper;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author Clara Currier
 */
public class SkillBehavior {

    public SkillBehavior() {
    }

    protected void addDisplacement(EntityWrapper target) {
        //moves the target a certain amount in the given direction if possible
        //needs to check that there are no tiles in the way
    }

    protected void addMovementModifier(EntityWrapper target) {
        //slows or speeds up the target
        //will need to integrate with MoveCont somehow
        // * MoveCont is unfriendly towards speed modifiers right now
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
