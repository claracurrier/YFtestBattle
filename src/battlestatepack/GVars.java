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
    public final float kbaseatkpower = 10f;
    public float kautocooldown = 1.3f;
    public final float khealth = 350;
    //Dan's vars
    public final float dminatkdist = 300f;
    public final float arrowspeed = 1100f;
    public final float dbaseatkpower = 15f;
    public float dautocooldown = 1.4f;
    public final float dhealth = 300;
    //Standard Mob vars
    public final float mspeed = 150;
    public final float matkpwr = 14;
    public final float mhealth = 250;
    public final float mintensitymovemod = 10f;
    public final float mminmovement = 50;
    public final float mminintensity = 60;
    public final float mminatkdistance = 70;
    //map constants that get updated upon map creation
    public int mapwidth = 0;
    public int mapheight = 0;
}
