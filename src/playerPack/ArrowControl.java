/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package playerPack;

import battlestatepack.BattleMain;
import battlestatepack.GVars;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;

/**
 *
 * @author Clara Currier
 */
public class ArrowControl extends AbstractControl {

    private final float speed = GVars.gvars.arrowspeed;
    private final Vector3f direction;
    private boolean rotated = false;
    private final float aim;

    public ArrowControl(Vector3f direction) {
        this.direction = direction;
        aim = new Vector2f(direction.x, direction.y).getAngle();
    }

    @Override
    protected void controlUpdate(float tpf) {
        if ((Boolean) spatial.getUserData("collided")) {
            //arrow collided
            System.out.println("arrow collided!");
            spatial.removeFromParent();
            return;
        }

        if (!rotated) {
            spatial.rotate(0, 0, aim);
            rotated = true;
        }
        spatial.move(direction.mult(speed * tpf));
    }

    public static void fireArrow(Vector3f target, AssetManager assetManager, Vector3f playerPos) {
        ArrowControl arrowCont = new ArrowControl(target.subtract(playerPos).normalizeLocal());
        Node arrow = arrowCont.makeArrow(assetManager, playerPos);
        BattleMain.ATKNODE.attachChild(arrow);
        arrow.addControl(arrowCont);
    }

    public Node makeArrow(AssetManager assetManager, Vector3f playerPos) {
        Node node = new Node("arrow");

        Geometry geom = new Geometry("Quad", new Quad(28f, 9f));
        Texture tex = assetManager.loadTexture("Textures/Arrow.png");
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", tex);
        geom.setMaterial(mat);
        float width = tex.getImage().getWidth();
        float height = tex.getImage().getHeight();

        node.setLocalTranslation(playerPos);
        node.setUserData("halfwidth", width / 2);
        node.setUserData("halfheight", height / 2);
        node.setUserData("collided", false);
        node.setUserData("atkpower", GVars.gvars.arrowpower);
        node.setUserData("type", "arrow");

        node.attachChild(geom);
        return node;
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}