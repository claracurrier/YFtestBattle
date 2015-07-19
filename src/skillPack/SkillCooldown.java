/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package skillPack;

import battlestatepack.BattleGUI;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import playerPack.SkillMapper;

/**
 *
 * @author Clara Currier
 */
public class SkillCooldown {

    private SkillMapper skillMap;
    private BattleGUI battleGUI;

    public SkillCooldown() {
    }

    public Cooldown newCooldown(int length, PlayerSkills.Skills skill) {
        battleGUI.displayButtonCooldown(skillMap.getAssignedButton(skill), length);
        return new Cooldown(length);
    }

    public void setBattleGUI(BattleGUI battleGUI) {
        this.battleGUI = battleGUI;
    }

    public void setSkillmap(SkillMapper skillmap) {
        this.skillMap = skillmap;
    }

    protected class Cooldown extends AbstractControl {

        private float counter = 0;
        private float length;

        public Cooldown(float length) {
            this.length = length;
        }

        @Override
        protected void controlUpdate(float tpf) {
            counter += tpf;
        }

        public boolean isReady() {
            return counter >= length;
        }

        @Override
        protected void controlRender(RenderManager rm, ViewPort vp) {
        }
    }
}
