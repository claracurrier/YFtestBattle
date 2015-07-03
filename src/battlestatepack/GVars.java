/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlestatepack;

import com.jme3.math.FastMath;

/**
 *
 * @author Clara Currier
 */
public class GVars {

    public static final GVars gvars = new GVars();

    private GVars() {
    }
    //General vars
    public final float pspeed = 350;
    public final float pspeedAI = 200; //unused
    public final float phealth = 100;
    public final float pAIactivate = 1;
    public final float pdistancefromchar = 220;
    public final float pdistancefrommob = 180;
    public final float pdistancefromrest = 120;
    public final float pdistancefromsafety = 230;
    public final float camspeed = 450f;
    //Kirith's vars
    public final float kspinpower = 7;
    public final float kdizzytimer = 3;
    public final float kmaxspintime = 1.1f;
    public final float kmaxchargepower = 10;
    public final float kpowerincrement = 3f;
    //Dan's vars
    public final float dmaxarrowpwr = 40;
    public final float dminlinesize = 140;
    public final float dlineincrement = 60;
    public final float darrowpwrincrement = 14;
    public final float dlineanglerange = FastMath.PI / 6;
    public final float ddistancefromki = 150;
    //Standard Mob vars
    public final float mstunthreshold = .6f;
    public final float mpushbackmod = 39;
    public final float mpushstunmod = .2f;
    public final float mspeed = 200;
    public final float matkpwr = 10;
    public final float mhealth = 80;
    public final float mstunmod = 1f;
    public final float mspinstunmod = .4f;
    public final float mspinpushmod = 3.5f;
    public final float mintensitymovemod = 8f;
    public final float mminmovement = 30;
    public final float mminintensity = 50;
    //pickskill and the times are all in MobAS
}
