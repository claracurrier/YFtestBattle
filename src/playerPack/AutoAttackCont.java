/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package playerPack;

import battlestatepack.EntityWrapper;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author Clara Currier
 */
public class AutoAttackCont extends AbstractControl {

    private float cooldown;
    private float counter = 50; //always start
    private EntityWrapper activeChar;
    private Vector3f target;

    public AutoAttackCont(float cooldown, EntityWrapper activeChar, Vector3f target) {
        this.cooldown = cooldown;
        this.activeChar = activeChar;
        this.target = target;
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (counter < cooldown) {
            counter += tpf;
        } else {
            //perform attack
            activeChar.autoAttack(target);
            counter = 0;
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}
