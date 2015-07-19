/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package playerPack;

import skillPack.PlayerSkills;
import com.jme3.input.InputManager;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 *
 * @author Clara Currier
 */
public class SkillMapper {

    private InputManager inputManager;
    private static boolean autoCast = false; //keep this global
    private HashMap<String, PlayerSkills.Skills> skillMap = new HashMap<>();
    private PlayerSkills.Skills waitingForSkill;
    private boolean waiting = false;
    private PlayerSkills pskills;

    public SkillMapper(InputManager input, PlayerSkills pskill) {
        inputManager = input;
        pskills = pskill;
    }

    public static void setAutoCast(boolean enabled) {
        autoCast = enabled;
    }

    public boolean skillIsWaiting() {
        return waiting;
    }

    public String getAssignedButton(PlayerSkills.Skills value) {
        for (Entry<String, PlayerSkills.Skills> entry : skillMap.entrySet()) {
            if (value.equals(skillMap.get(entry.getKey()))) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void setSkillMapping(String buttonName, PlayerSkills.Skills skill) {
        skillMap.put(buttonName, skill);
    }

    public void cancelSkill() {
        waiting = false;
        waitingForSkill = PlayerSkills.Skills.nothing;
        pskills.getGraphic().removeTargetCursor();
        //when user hits the skill button/shortcut twice, skill gets canceled
    }

    public void doSkill(String name) {
        if (autoCast) { //skip the graphic and fire
            pskills.useSkill(skillMap.get(name), inputManager.getCursorPosition(), true);
        } else if (waiting) { //removes the graphic, fires
            pskills.useSkill(waitingForSkill, inputManager.getCursorPosition(), true);
            waiting = false;
        } else {  //init the graphic, wait for second click
            pskills.useSkill(skillMap.get(name), inputManager.getCursorPosition(), false);
            waitingForSkill = skillMap.get(name);
            waiting = true;
        }
    }
}
