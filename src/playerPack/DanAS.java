/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package playerPack;

import battlestatepack.GVars;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;

/**
 *
 * @author Clara Currier
 */
public class DanAS extends Player {

    private AssetManager assetManager;
    private final Node dan;
    private SimpleApplication appl;
    private Vector3f playerPos;

    public DanAS(Node dan) {
        this.dan = dan;
        playerPos = dan.getLocalTranslation();
    }

    @Override
    public void initialize(AppStateManager asm, Application app) {
        this.appl = (SimpleApplication) app;
        this.assetManager = appl.getAssetManager();
        setEnabled(true);
    }

    @Override
    public void update(float tpf) {
        playerPos = dan.getLocalTranslation();
    }

    @Override
    public void autoAttack(Vector3f target) {
        if (playerPos.distance(target) <= GVars.gvars.dminatkdist) {
            ArrowControl.fireArrow(target, assetManager, playerPos);
        }
    }

    @Override
    public Node getNode() {
        return dan;
    }
}