/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package playerPack;

import battlestatepack.EntityWrapper;
import battlestatepack.GVars;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import pathfindingPack.MoveCont;
import pathfindingPack.Pathfinder;
import pathfindingPack.Picker;
import skillPack.SkillGraphic;

/**
 *
 * @author Clara Currier
 */
public class KirithWrapper extends EntityWrapper {

    private final Node kirith;
    private Vector3f playerPos;
    private SkillGraphic graphic;
    private Picker picker;

    public KirithWrapper(Node ki, Picker picker) {
        this.kirith = ki;
        health = GVars.gvars.khealth;
        this.picker = picker;
    }

    public void setGraphic(SkillGraphic graphic) {
        this.graphic = graphic;
    }

    @Override
    public void update(float tpf) {
        playerPos = kirith.getLocalTranslation();
    }

    @Override
    public void autoAttack(Vector3f target) {
        if (playerPos.distance(target) <= GVars.gvars.kminatkdist) {
            graphic.makeAttackBox(playerPos, target, 10f, "kiautoatkbox");
        } else if (kirith.getControl(MoveCont.class) == null && kirith.getControl(Pathfinder.class) == null) {
            //pick again, removes this control in method
            picker.handleAttack(target, this);
        }
    }

    @Override
    public Node getNode() {
        return kirith;
    }

    @Override
    public void initialize(AppStateManager asm, Application app) {
        kirith.setUserData("wrapper", this);
    }
}