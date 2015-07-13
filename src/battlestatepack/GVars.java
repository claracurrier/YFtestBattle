/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlestatepack;


/**
 *
 * @author Clara Currier
 */
public class GVars {

    public static final GVars gvars = new GVars();

    private GVars() {
    }
    //General vars
    public final float phealth = 100;
    public final float camspeed = 300f;
    //Kirith's vars
    public final float kminatkdist = 20f;
    public float kautocooldown = 1.2f;
    //Dan's vars
    public final float dminatkdist = 500f;
    public final float arrowspeed = 1100f;
    public final float arrowpower = 10f; //may need to be modified
    public float dautocooldown = 1.4f;
    //Standard Mob vars
    public final float mspeed = 200;
    public final float matkpwr = 10;
    public final float mhealth = 80;
    public final float mintensitymovemod = 8f;
    public final float mminmovement = 30;
    public final float mminintensity = 50;
    //pickskill and the times are all in MobAS
    //map constants that get updated upon map creation
    public int mapwidth = 0;
    public int mapheight = 0;
}
