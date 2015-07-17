/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlestatepack;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import java.util.HashMap;
import playerPack.PSkills;
import playerPack.SkillMapper;

/**
 *
 * @author Clara Currier
 */
public class InputSystem implements ActionListener {

    InputManager inputManager;
    private HashMap<String, Integer> mappings = new HashMap<>();
    private SkillMapper skillmap; //just for maintaining skills

    public InputSystem(InputManager input, PSkills pskill) {
        inputManager = input;
        skillmap = new SkillMapper(input, pskill);

        defaultShortcuts();
    }

    public static enum Keys {
        //a list of every possible command that can be issued in battle

        leftclick, rightclick, pause, switchChar,
        dbuttonleft, dbuttonmid, dbuttonright,
        kbuttonleft, kbuttonmid, kbuttonright,
        up, down, right, left, space;
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            inputManager.addListener(this, "leftClick");
            inputManager.addListener(this, "rightClick");
            inputManager.addListener(this, "switchChar");
            inputManager.addListener(this, "pause");

            inputManager.addListener(this, "dbuttonleft");
            inputManager.addListener(this, "dbuttonmid");
            inputManager.addListener(this, "dbuttonright");
            inputManager.addListener(this, "kbuttonleft");
            inputManager.addListener(this, "kbuttonmid");
            inputManager.addListener(this, "kbuttonright");

            inputManager.addListener(this, "left");
            inputManager.addListener(this, "right");
            inputManager.addListener(this, "up");
            inputManager.addListener(this, "down");
            inputManager.addListener(this, "space");
        } else {
            inputManager.removeListener(this);
        }
    }

    public void setSkillMapping(String buttonName, PSkills.Skills skill) {
        skillmap.setMapping(buttonName, skill);
    }

    public void setMapping(String buttonName, Integer key) {
        mappings.put(buttonName, key);
        if (inputManager.hasMapping(buttonName)) {
            inputManager.deleteMapping(buttonName);
        }
        if (buttonName.equals("leftclick") || buttonName.equals("rightclick")) {
            inputManager.addMapping(buttonName, new MouseButtonTrigger(key));
        } else {
            inputManager.addMapping(buttonName, new KeyTrigger(key));
        }
    }

    public final void defaultShortcuts() {
        setMapping("leftclick", (MouseInput.BUTTON_LEFT));
        setMapping("rightclick", (MouseInput.BUTTON_RIGHT));

        setMapping("dbuttonleft", KeyInput.KEY_1);
        setMapping("dbuttonmid", KeyInput.KEY_2);
        setMapping("dbuttonright", KeyInput.KEY_3);
        setMapping("kbuttonleft", KeyInput.KEY_4);
        setMapping("kbuttonmid", KeyInput.KEY_5);
        setMapping("kbuttonright", KeyInput.KEY_6);

        setMapping("up", KeyInput.KEY_UP);
        setMapping("down", KeyInput.KEY_DOWN);
        setMapping("right", KeyInput.KEY_RIGHT);
        setMapping("left", KeyInput.KEY_LEFT);
        setMapping("space", KeyInput.KEY_SPACE);

        setMapping("switchChar", KeyInput.KEY_TAB);
        setMapping("pause", KeyInput.KEY_P);
    }

    public void manualFire(String name) {
        manualFire(name, false);
    }

    public void manualFire(String name, boolean isPressed) {
        switch (Keys.valueOf(name)) {
            case pause: //reach mainmenu
                break;
            case switchChar: //reach battleMain
                break;
            case dbuttonleft:
                break;
            case dbuttonmid:
                break;
            case dbuttonright:
                break;
            case kbuttonleft:
                break;
            case kbuttonmid:
                break;
            case kbuttonright:
                break;
            case up:
                break;
            case down:
                break;
            case right:
                break;
            case left:
                break;
            case space: //camoptions
                break;
            case leftclick: //depends
                break;
            case rightclick: //reach autoattack
                break;
        }
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        manualFire(name, isPressed);
    }
}
