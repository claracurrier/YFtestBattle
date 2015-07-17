/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlestatepack;

import cameraPack.CCManual;
import cameraPack.CameraOptions;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import java.util.HashMap;
import menuPack.MainMenu;
import playerPack.PSkills;
import playerPack.SkillMapper;

/**
 *
 * @author Clara Currier
 */
public class InputSystem implements ActionListener {

    private InputManager inputManager;
    private HashMap<String, Integer> mappings = new HashMap<>();
    private SkillMapper skillmap; //just for maintaining skills
    private ReferenceRegistry reg = ReferenceRegistry.registry;

    public InputSystem(InputManager input, PSkills pskill) {
        inputManager = input;
        skillmap = new SkillMapper(input, pskill);

        defaultShortcuts();
    }

    public enum Keys {
        //a list of every possible command that can be issued in battle

        leftclick, rightclick, pause, switchChar,
        dbuttonleft, dbuttonmid, dbuttonright,
        kbuttonleft, kbuttonmid, kbuttonright,
        up, down, right, left, space;
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            inputManager.addListener(this, "leftclick");
            inputManager.addListener(this, "rightclick");
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
        skillmap.setSkillMapping(buttonName, skill);
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
        setMapping("switchChar", KeyInput.KEY_TAB);
        setMapping("pause", KeyInput.KEY_P);
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
    }

    public void manualFire(String name) {
        //for the buttons
        manualFire(name, false);
    }

    public void manualFire(String name, boolean isPressed) {
        if (!isPressed) { //the abstraction is in the shortcut
            //calling pause will pause regardless of key
            switch (Keys.valueOf(name)) {
                case pause:
                    ((MainMenu) reg.get(MainMenu.class)).pause();
                    break;
                case switchChar:
                    ((BattleMain) reg.get(BattleMain.class)).switchChar();
                    break;
                case dbuttonleft:
                    if (skillmap.skillIsWaiting()) {
                        skillmap.cancelSkill();
                    } else {
                        skillmap.doSkill(name);
                    }
                    break;
                case dbuttonmid:
                    if (skillmap.skillIsWaiting()) {
                        skillmap.cancelSkill();
                    } else {
                        skillmap.doSkill(name);
                    }
                    break;
                case dbuttonright:
                    if (skillmap.skillIsWaiting()) {
                        skillmap.cancelSkill();
                    } else {
                        skillmap.doSkill(name);
                    }
                    break;
                case kbuttonleft:
                    if (skillmap.skillIsWaiting()) {
                        skillmap.cancelSkill();
                    } else {
                        skillmap.doSkill(name);
                    }
                    break;
                case kbuttonmid:
                    if (skillmap.skillIsWaiting()) {
                        skillmap.cancelSkill();
                    } else {
                        skillmap.doSkill(name);
                    }
                    break;
                case kbuttonright:
                    if (skillmap.skillIsWaiting()) {
                        skillmap.cancelSkill();
                    } else {
                        skillmap.doSkill(name);
                    }
                    break;
                case up:
                    if (reg.hasRegistry(CCManual.class)) {
                        ((CCManual) reg.get(CCManual.class)).move(name, isPressed);
                    }
                    break;
                case down:
                    if (reg.hasRegistry(CCManual.class)) {
                        ((CCManual) reg.get(CCManual.class)).move(name, isPressed);
                    }
                    break;
                case right:
                    if (reg.hasRegistry(CCManual.class)) {
                        ((CCManual) reg.get(CCManual.class)).move(name, isPressed);
                    }
                    break;
                case left:
                    if (reg.hasRegistry(CCManual.class)) {
                        ((CCManual) reg.get(CCManual.class)).move(name, isPressed);
                    }
                    break;
                case space: //switch between AutoFollowLocked and Manual cameras
                    CameraOptions camOps = ((CameraOptions) reg.get(CameraOptions.class));
                    if (camOps.getCamSetting().equals("Manual")) {
                        camOps.setCamSetting("AutoFollowLocked");
                    } else {
                        camOps.setCamSetting("Manual");
                    }
                    break;
                case rightclick: //auto-attacks (cancels waiting skills too)
                    if (skillmap.skillIsWaiting()) {
                        skillmap.cancelSkill();
                    }
                    ((Picker) reg.get(Picker.class)).pick(name, inputManager.getCursorPosition());
                    break;
            }
        }
        if (name.equals("leftclick")) {
            //dragging may want to listen for isPressed
            if (skillmap.skillIsWaiting() && !isPressed) {
                skillmap.doSkill(name);
            } else if (!isPressed) { //handle regular picking (picker class takes over actions)
                ((Picker) reg.get(Picker.class)).pick(name, inputManager.getCursorPosition());
            }
        }
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        manualFire(name, isPressed);
    }
}
