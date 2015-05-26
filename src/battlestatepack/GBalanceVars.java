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
    float pspeed = 350;
    float phealth = 100;
    //Kirith's vars
    //Kirith's whole attack structure needs to be retooled
    float kpushpwr;
    float kstunpwr;
    float kspinpwr;
    float kdizzytimer = 3;
    float kmaxspintime = 1.2f;
    float kmaxchargepwr;
    //Dan's vars
    float dmaxarrowpwr = 35;
    float dminlinesize = 140;
    float dlineincrement = 60;
    float darrowpwrincrement = 10;
    float dlineanglerange = FastMath.PI / 6;
    //Standard Mob vars
    public float mstunthreshold = .5f;
    public float mpushbackmod = 1;
    public float mspeed = 200;
    public float matkpwr = 9;
    public float mhealth = 80;
    public float mstunmod = 1;
    public float mspinmod = .5f;
    //pickskill and the times are all in MobAS
}
