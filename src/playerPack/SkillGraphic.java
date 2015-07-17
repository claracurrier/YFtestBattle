/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package playerPack;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author Clara Currier
 */
public class SkillGraphic {

    private Node rootNode;
    private AssetManager assetManager;
    private Player dan, kirith;

    public SkillGraphic(Node root, AssetManager aman, Player dan, Player kirith) {
        //set all the instance variables necessary to load graphics...
        //eg. rootNode, guiNode, inputManager, assetManager, etc.
        rootNode = root;
        assetManager = aman;
        this.dan = dan;
        this.kirith = kirith;
    }

    public void fireArrow(final Vector2f target) {
        System.out.println("Bam!");
        dan.getNode().addControl(new AbstractControl() { //timer control
            float counter = 0;
            float numarrows = 0;

            @Override
            protected void controlUpdate(float tpf) {
                if (numarrows <= 2) {
                    if (counter > .1f) {
                        ArrowControl.fireArrow(new Vector3f(target.x, target.y, 0), assetManager,
                                dan.getNode().getLocalTranslation());
                        counter = 0;
                        numarrows++;
                    } else {
                        counter += tpf;
                    }
                } else {
                    dan.getNode().removeControl(this);
                }
            }

            @Override
            protected void controlRender(RenderManager rm, ViewPort vp) {
            }
        });
    }

    public void makeGUIHelper() {
        System.out.println("Helpful stuff");
    }
}
