/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package playerPack;

import com.jme3.input.InputManager;
import java.util.HashMap;

/**
 *
 * @author Clara Currier
 */
public class SkillMapper {

    private InputManager inputManager;
    private static boolean autoCast = false; //keep this global
    private HashMap<String, PSkills.Skills> skillMap = new HashMap<>();
    private PSkills.Skills waitingForSkill;
    private boolean waiting = false;
    private PSkills pskills;

    public SkillMapper(InputManager input, PSkills pskill) {
        inputManager = input;
        pskills = pskill;
    }

    public static void setAutoCast(boolean enabled) {
        autoCast = enabled;
    }

    public void setMapping(String buttonName, PSkills.Skills skill) {
        skillMap.put(buttonName, skill);
    }

    public void setSkillMapping(String buttonName, PSkills.Skills skill) {
        skillMap.put(buttonName, skill);
    }

    public void doSkill(String name) {
        if (autoCast) { //skip the graphic and fire
            pskills.useSkill(skillMap.get(name), inputManager.getCursorPosition(), true);
        } else if (waiting) { //init the graphic, wait for second click
            pskills.useSkill(waitingForSkill, inputManager.getCursorPosition(), true);
            waiting = false;
        } else { //removes the graphic, fires
            pskills.useSkill(skillMap.get(name), inputManager.getCursorPosition(), false);
            waitingForSkill = skillMap.get(name);
            waiting = true;
        }
    }
}
