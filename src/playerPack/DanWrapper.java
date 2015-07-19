/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package playerPack;

import skillPack.SkillGraphic;
import battlestatepack.EntityWrapper;
import battlestatepack.GVars;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

/**
 *
 * @author Clara Currier
 */
public class DanWrapper extends EntityWrapper {

    private final Node dan;
    private Vector3f playerPos;
    private SkillGraphic graphic;

    public DanWrapper(Node dan) {
        this.dan = dan;
        playerPos = dan.getLocalTranslation();
        health = GVars.gvars.dhealth;
    }

    @Override
    public void initialize(AppStateManager asm, Application app) {
        dan.setUserData("wrapper", this);
    }

    @Override
    public void update(float tpf) {
        playerPos = dan.getLocalTranslation();
    }

    public void setGraphic(SkillGraphic graphic) {
        this.graphic = graphic;
    }

    @Override
    public void autoAttack(Vector3f target) {
        if (playerPos.distance(target) <= GVars.gvars.dminatkdist) {
            graphic.makeArrow("arrow",
                    new Vector2f(target.x, target.y), GVars.gvars.dbaseatkpower);
        }
    }

    @Override
    public Node getNode() {
        return dan;
    }
}