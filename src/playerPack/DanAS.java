/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package playerPack;

import battlestatepack.BattleMain;
import battlestatepack.GVars;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.scene.Node;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;

/**
 *
 * @author Clara Currier
 */
public class DanAS extends Player {

    private InputManager inputManager;
    private AssetManager assetManager;
    private final Node dan;
    private SimpleApplication appl;
    private final AppSettings settings;
    private Vector3f playerPos;

    public DanAS(Node dan, AppSettings settings) {
        this.dan = dan;
        this.settings = settings;
        playerPos = dan.getLocalTranslation();
    }

    @Override
    public void initialize(AppStateManager asm, Application app) {
        this.appl = (SimpleApplication) app;
        this.inputManager = appl.getInputManager();
        this.assetManager = appl.getAssetManager();
        setEnabled(true);
    }

    private void fireArrow(Vector3f target) {
        Spatial arrow = makeArrow();
        BattleMain.ATKNODE.attachChild(arrow);
        arrow.addControl(new ArrowControl(target.subtract(playerPos).normalizeLocal()));
    }

    private Node makeArrow() {
        Node node = new Node("arrow");

        Geometry geom = new Geometry("Quad", new Quad(28f, 9f));
        Texture tex = assetManager.loadTexture("Textures/Arrow.png");
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", tex);
        geom.setMaterial(mat);
        float width = tex.getImage().getWidth();
        float height = tex.getImage().getHeight();

        node.setLocalTranslation(playerPos);

        Material picMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        picMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.AlphaAdditive);
        node.setMaterial(picMat);

        node.setUserData("halfwidth", width / 2);
        node.setUserData("halfheight", height / 2);
        node.setUserData("collided", false);
        node.setUserData("atkpower", GVars.gvars.arrowpower);
        node.setUserData("type", "arrow");

        node.attachChild(geom);
        return node;
    }

    private Vector2f getAimDirection() {
        //aims bullets at the direction of the mouse click
        Vector2f mouse = inputManager.getCursorPosition();

        Vector2f newvec = new Vector2f((playerPos.x) - (settings.getWidth() / 2),
                (playerPos.y) - (settings.getHeight() / 2));

        mouse = mouse.add(newvec);

        Vector2f dif = new Vector2f(mouse.x - playerPos.x, mouse.y - playerPos.y);
        return dif.normalizeLocal();
    }

    @Override
    public void update(float tpf) {
        playerPos = dan.getLocalTranslation();
    }

    @Override
    public void autoAttack(Vector3f target) {
        if (playerPos.distance(target) > GVars.gvars.dminatkdist) {
            //move closer
            System.out.println("too far");
        }
        fireArrow(target);
    }

    @Override
    public void takeDamage() {
    }

    @Override
    public Node getNode() {
        return dan;
    }
}