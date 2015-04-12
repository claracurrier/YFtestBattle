/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlestatepack;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import java.lang.reflect.Constructor;
import lemurVersionedPack.VersionedReference;

/**
 *
 * @author PC
 */
public class MobControl extends AbstractControl {
    
    private String mobType;
    private Spatial myspat;
    private VersionedReference vCollide;
    private Node dan, ki;
    private Node atkNode;
    
    public MobControl(String mt, Spatial mob, CollideAS cc, Node dan, Node ki) throws Exception {
        mobType = mt;
        myspat = mob;
        mob.addControl(findCont(mobType));
        vCollide = cc.getvCollide();
        
        this.dan = dan;
        this.ki = ki;

        /* method to check and conduct all behavior patterns given
         * by going back to the spatial and looking up variables, then 
         * calling execute methods on that skill held in the skill class.
         */
    }
    
    public void setAtkNode(Node atk){
        atkNode = atk;
    }
    
    private Control findCont(String name) throws Exception {
        /* Reflection method in order to convert the mob's name
         * into an actual constructor. Remember to throw the
         * correct exceptions (rn it's bad form), and then reference
         * the reflection methods as well instead of making controls
         * TempWC is temp!!
         */
        
        Class<?> clazz = Class.forName("battlestatepack." + name);
        Constructor<?> constr = clazz.getConstructor(Integer.class, Integer.class);
        Control cont = (Control) constr.newInstance(
                myspat.getUserData("wwidth"), myspat.getUserData("wheight"));
        return cont;
    }
    
    private Vector2f getTargDir(Node targ) {
        Vector2f targvel = new Vector2f(
                targ.getLocalTranslation().x - spatial.getLocalTranslation().x,
                targ.getLocalTranslation().y - spatial.getLocalTranslation().y);
        return targvel.normalizeLocal();
    }
    
    private void pursuitBehavior(float tpf) {
        //temporary storage for behavior, will need to be refactored

        //1) get target position
        //2) get movement direction (normalize the difference in locations
        //3) move towards target

    }
    
    @Override
    protected void controlUpdate(float tpf) {
        if(!atkNode.getLocalTranslation().equals(spatial.getLocalTranslation())){
            atkNode.setLocalTranslation(spatial.getLocalTranslation());
            //make sure box follows mob 
            //not working
        }
        
        Vector3f loc = spatial.getLocalTranslation();
        if (loc.x > 1500
                || loc.y > 1500
                || loc.x < 0
                || loc.y < 0) {
            
        }
        
        //place to poll for collisions
        {
            if (!spatial.getUserData("collided").equals("none")) {
                //collision
                if (spatial.getUserData("collided").equals("arrow")) {
                    movedir((Integer) spatial.getUserData("atkdirection"));
                    
                } else if (spatial.getUserData("collided").equals("pushback")) {
                    movedir((Integer) spatial.getUserData("atkdirection"));
                    
                } else if (spatial.getUserData("collided").equals("stun")) {
                    movedir((Integer) spatial.getUserData("atkdirection"));
                    
                } else if (spatial.getUserData("collided").equals("spin")) {
                    movedir((Integer) spatial.getUserData("atkdirection"));
                }
                spatial.setUserData("collided", "none");
            }
        }
    }
    
    private void movedir(int dir) {
        switch (dir) { //moves the node opposite of the direction it was it
            case 0:
                spatial.move(0, -81f, 0);
                break;
            case 1:
                spatial.move(-51f, -81f, 0);
                break;
            case 2:
                spatial.move(-51f, 0, 0);
                break;
            case 3:
                spatial.move(-51f, 81f, 0);
                break;
            case 4:
                spatial.move(0, 81f, 0);
                break;
            case 5:
                spatial.move(51f, 81f, 0);
                break;
            case 6:
                spatial.move(51f, 0, 0);
                break;
            case 7:
                spatial.move(51f, -81f, 0);
                break;
        }
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}