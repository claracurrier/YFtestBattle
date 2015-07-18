/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package playerPack;

import battlestatepack.EntityWrapper;
import battlestatepack.BattleMain;
import battlestatepack.GVars;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.debug.WireBox;

/**
 *
 * @author Clara Currier
 */
public class KirithWrapper extends EntityWrapper {

    private final Node kirith;
    private Vector3f playerPos;
    private SimpleApplication app;
    private boolean attacking = false;
    private float hbtimer = 0;

    public KirithWrapper(Node ki) {
        this.kirith = ki;
        health = GVars.gvars.khealth;
    }

    @Override
    public void update(float tpf) {
        playerPos = kirith.getLocalTranslation();

        if (attacking) {
            Node box = (Node) BattleMain.ATKNODE.getChild("kiautoattack");
            if (hbtimer > .1f || !box.getUserData("collided").equals("none")) {
                attacking = false;
                BattleMain.ATKNODE.detachChildNamed(box.getName());
                hbtimer = 0;
            } else {
                hbtimer += tpf;
            }
        }
    }

    @Override
    public void autoAttack(Vector3f target) {
        if (playerPos.distance(target) <= GVars.gvars.kminatkdist) {
            attacking = true;
            makeAttackBox(findAttackDir(playerPos, target));
        }
    }

    @Override
    public Node getNode() {
        return kirith;
    }

    @Override
    public void initialize(AppStateManager asm, Application app) {
        this.app = (SimpleApplication) app;
        kirith.setUserData("wrapper", this);
    }

    private void makeAttackBox(int dir) {
        Node node = new Node("kiautoattack");

        float x = kirith.getWorldTranslation().x;
        float y = kirith.getWorldTranslation().y;
        float boxw = 0;
        float boxh = 0;

        switch (dir) {
            //moves the node to appropriate location relative to ki
            case 0: //up
                node.setLocalTranslation(x, y + 81f, 0);
                boxw = 15;
                boxh = 50;
                break;
            case 1:
                node.setLocalTranslation(x + 51f, y + 81f, 0);
                boxw = 30;
                boxh = 30;
                break;
            case 2: //right
                node.setLocalTranslation(x + 51f, y, 0);
                boxw = 50;
                boxh = 15;
                break;
            case 3:
                node.setLocalTranslation(x + 51f, y - 81f, 0);
                boxw = 30;
                boxh = 30;
                break;
            case 4: //down
                node.setLocalTranslation(x + 0, y - 81f, 0);
                boxw = 15;
                boxh = 50;
                break;
            case 5:
                node.setLocalTranslation(x - 51f, y - 81f, 0);
                boxw = 30;
                boxh = 30;
                break;
            case 6: //left
                node.setLocalTranslation(x - 51f, y, 0);
                boxw = 50;
                boxh = 15;
                break;
            case 7:
                node.setLocalTranslation(x - 51f, y + 81f, 0);
                boxw = 30;
                boxh = 30;
                break;
        }

        node.setUserData("type", "kiatkbox");
        node.setUserData("collided", "none");
        node.setUserData("atkpower", 10f);
        node.setUserData("halfwidth", boxw / 2);
        node.setUserData("halfheight", boxh / 2);
        node.attachChild(tempWireBox(boxw, boxh));
        BattleMain.ATKNODE.attachChild(node);
    }

    private Geometry tempWireBox(float width, float height) {
        //temp wirebox to see the hitboxes
        Geometry g = new Geometry("attackBox", new WireBox(width, height, 0));
        Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", ColorRGBA.Blue);
        g.setMaterial(mat);
        return g;
    }

    private int findAttackDir(Vector3f targ, Vector3f cur) {
        Vector2f newvec = new Vector2f(cur.x, cur.y).subtractLocal(targ.x, targ.y);
        float aim = newvec.normalizeLocal().getAngle();

        if (aim <= 5 * FastMath.PI / 6 && aim > 2 * FastMath.PI / 3) {
            //up left
            return 7;
        } else if (aim <= 2 * FastMath.PI / 3 && aim > FastMath.PI / 3) {
            //facing up
            return 0;
        } else if (aim <= FastMath.PI / 3 && aim > FastMath.PI / 6) {
            //up right
            return 1;
        } else if (aim <= FastMath.PI / 6 && aim > -FastMath.PI / 6) {
            //facing right
            return 2;
        } else if (aim <= -FastMath.PI / 6 && aim > -FastMath.PI / 3) {
            //down right
            return 3;
        } else if (aim <= -FastMath.PI / 3 && aim > -2 * FastMath.PI / 3) {
            //facing down
            return 4;
        } else if (aim <= -2 * FastMath.PI / 3 && aim > -5 * FastMath.PI / 6) {
            //down left
            return 5;
        } else {
            //facing left
            return 6;
        }
    }
}