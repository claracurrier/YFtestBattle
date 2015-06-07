/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlestatepack;

import com.jme3.math.FastMath;

/**
 *
 * @author PC
 */
public class GBalanceVars {
    /*
     * A singleton class containing all of the fields with importance to gameplay
     * balance for the alpha stages. Ultimately this will get abstracted into
     * each mob's individual scripts, but this can also remain for Dan and Kirith
     * and make it easier to manually adjust difficulty.
     */

    public static final GBalanceVars gbal = new GBalanceVars();

    private GBalanceVars() {
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
    public final float mpushbackmod = 15;
    public final float mspeed = 200;
    public final float matkpwr = 10;
    public final float mhealth = 80;
    public final float mstunmod = .8f;
    public final float mspinstunmod = .5f;
    public final float mspinpushmod = 3.5f;
    public final float mintensitymovemod = 8f;
    public final float mminmovement = 30;
    public final float mminintensity = 50;
    //pickskill and the times are all in MobAS
}
