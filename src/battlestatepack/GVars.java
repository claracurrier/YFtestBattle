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
    public final float camspeed = 380f;
    //Kirith's vars
    public final float kminatkdist = 80f;
    public final float kbaseatkpower = 8f;
    public float kautocooldown = 1.2f;
    public final float khealth = 900;
    //Dan's vars
    public final float dminatkdist = 300f;
    public final float arrowspeed = 1100f;
    public final float dbaseatkpower = 10f;
    public float dautocooldown = 1.4f;
    public final float dhealth = 500;
    //Standard Mob vars
    public final float mspeed = 150;
    public final float matkpwr = 10;
    public final float mhealth = 8000;
    public final float mintensitymovemod = 8f;
    public final float mminmovement = 30;
    public final float mminintensity = 50;
    public final float mminatkdistance = 50;
    //pickskill and the times are all in MobAS
    //map constants that get updated upon map creation
    public int mapwidth = 0;
    public int mapheight = 0;
}
