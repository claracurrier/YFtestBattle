/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package playerPack;

import battlestatepack.BattleMain;
import battlestatepack.CollideAS;
import battlestatepack.GVars;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import java.util.ArrayList;

/**
 *
 * @author PC
 */
public class PAICont extends AbstractControl {
    //only one AI for now, will need to make seperate behaviors later

    private float activatetimer = 0;
    private float charDistanceThreshold = GVars.gvars.pdistancefromchar;
    private float mobDistanceThreshold = GVars.gvars.pdistancefrommob;
    private final PMoveAppState pmas;
    private final CollideAS cas;
    private ArrayList<Spatial> nearMobs = new ArrayList<>();
    private Spatial otherchar;
    
    public PAICont(PMoveAppState p, CollideAS c) {
        pmas = p;
        cas = c;
    }
    
    public void setOtherChar(Spatial c) {
        otherchar = c;
    }
    
    @Override
    protected void controlUpdate(float tpf) {
        if (activatetimer < GVars.gvars.pAIactivate) {
            activatetimer += tpf;
        } else {
            for (int i = 0; i < BattleMain.ATKNODE.getQuantity(); i++) {
                if (BattleMain.ATKNODE.getChild(i).getName().equals("mobatkbox")) {
                    if (spatial.getLocalTranslation().distance(
                            BattleMain.ATKNODE.getChild(i).getLocalTranslation())
                            <= mobDistanceThreshold) {
                        //check for any near mobs, made for multiple mobs

                        nearMobs.add(BattleMain.ATKNODE.getChild(i));
                    }
                }
            }
            if (!nearMobs.isEmpty()) {
                //run away
                mobDistanceThreshold = GVars.gvars.pdistancefromsafety;
                run(aimDir(spatial.getLocalTranslation(), nearMobs.get(0).getLocalTranslation()));
            } else if (spatial.getLocalTranslation().distance(otherchar.getLocalTranslation())
                    >= charDistanceThreshold) {
                //run to other character
                charDistanceThreshold = GVars.gvars.pdistancefromrest;
                run(aimDir(otherchar.getLocalTranslation(), spatial.getLocalTranslation()));
            } else {
                mobDistanceThreshold = GVars.gvars.pdistancefrommob;
                charDistanceThreshold = GVars.gvars.pdistancefromchar;
                pmas.stopMoving();
            }
            nearMobs.clear();
        }
    }
    
    private float aimDir(Vector3f targ, Vector3f spatLoc) {
        Vector2f dif = new Vector2f(targ.x - spatLoc.x,
                targ.y - spatLoc.y);
        
        return dif.normalize().getAngle();
    }
    
    private void run(float aim) {
        if (aim <= 5 * FastMath.PI / 6 && aim > 2 * FastMath.PI / 3) {
            //up left
            pmas.move("RIGHT", false);
            pmas.move("UP", true);
            pmas.move("DOWN", false);
            pmas.move("LEFT", true);
        } else if (aim <= 2 * FastMath.PI / 3 && aim > FastMath.PI / 3) {
            //facing up
            pmas.move("RIGHT", false);
            pmas.move("UP", true);
            pmas.move("DOWN", false);
            pmas.move("LEFT", false);
        } else if (aim <= FastMath.PI / 3 && aim > FastMath.PI / 6) {
            //up right
            pmas.move("RIGHT", true);
            pmas.move("UP", true);
            pmas.move("DOWN", false);
            pmas.move("LEFT", false);
        } else if (aim <= FastMath.PI / 6 && aim > -FastMath.PI / 6) {
            //facing right
            pmas.move("RIGHT", true);
            pmas.move("UP", false);
            pmas.move("DOWN", false);
            pmas.move("LEFT", false);
            pmas.setDir(2);
        } else if (aim <= -FastMath.PI / 6 && aim > -FastMath.PI / 3) {
            //down right
            pmas.move("RIGHT", true);
            pmas.move("UP", false);
            pmas.move("DOWN", true);
            pmas.move("LEFT", false);
        } else if (aim <= -FastMath.PI / 3 && aim > -2 * FastMath.PI / 3) {
            //facing down
            pmas.move("RIGHT", false);
            pmas.move("UP", false);
            pmas.move("DOWN", true);
            pmas.move("LEFT", false);
        } else if (aim <= -2 * FastMath.PI / 3 && aim > -5 * FastMath.PI / 6) {
            //down left
            pmas.move("RIGHT", false);
            pmas.move("UP", false);
            pmas.move("DOWN", true);
            pmas.move("LEFT", true);
        } else {
            //facing left
            pmas.move("RIGHT", false);
            pmas.move("UP", false);
            pmas.move("DOWN", false);
            pmas.move("LEFT", true);
        }
        cas.moveCheck(spatial);
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}