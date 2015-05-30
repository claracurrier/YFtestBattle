/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlestatepack;

import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.debug.WireBox;
import spriteProject.Sprite;
import spriteProject.SpriteLibrary;

/**
 *
 * @author PC
 */
public class EntityMaker {

    private final AssetManager assetManager;

    public EntityMaker(AssetManager aman, AppStateManager asm) {
        this.assetManager = aman;
        SpriteLibrary.l_baseNode = BattleMain.BATTLENODE;
    }

    protected Node createSpatial(String name) {
        Node node = new Node(name);

        float width = 50f;
        float height = 80f;

//        add a material to the picture
        Material picMat = new Material(assetManager, "Common/MatDefs/Gui/Gui.j3md");
        picMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.AlphaAdditive);
        node.setMaterial(picMat);

        node.setUserData("halfwidth", width / 2);
        node.setUserData("halfheight", height / 2);
        node.setUserData("type", "player");

        node.setUserData("collided", "none");
        node.setUserData("atkpower", 0f);
        node.setUserData("atkdirection", 0);
        node.setUserData("knockback", false);

        //let the spatial move
        node.setUserData("canR", true);
        node.setUserData("canL", true);
        node.setUserData("canU", true);
        node.setUserData("canD", true);

        //set up the sprites
        makeSprites(name, node);

        //attach a hitbox 
        Geometry g = attachWireBox(width / 2, height / 2, ColorRGBA.Red);
        node.attachChild(g);
        g.center();

        return node;
    }

    //debugging
    public Geometry attachWireBox(float width, float height, ColorRGBA color) {
        Geometry g = new Geometry("WireBox", new WireBox(width, height, 0));
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", color);
        g.setMaterial(mat);
        g.setUserData("hasHitbox", true);
        return g;
    }

    private void makeSprites(String name, Node attach) {
        //give it a sprite library

        SpriteLibrary library = new SpriteLibrary(name, false, attach);

        BattleMain.sEngine.addLibrary(library);

        //this could be generalized to search for any name
        if (name.equals("Dan")) {
            //adds all of dan's running sprites
            //number convention starts from UP=0 and goes clockwise
            for (int i = 0; i < 8; i++) {
                library.addSprite(new Sprite("Textures/danrun" + i + ".png", "danrun" + i, assetManager, true, true, 6, 1, 0.09f, "Loop", "Start"));
            }


            //idle
            for (int i = 0; i < 8; i++) {
                library.addSprite(new Sprite("Textures/danidle" + i + ".png", "danidle" + i, assetManager, false, true, 1, 1, 0.08f, "Loop", "Start"));
            }

            library.setCurSprite(12);
        } else if (name.equals("Kirith")) {
            //adds all of kirith's sprites
            for (int i = 0; i < 8; i++) {
                library.addSprite(new Sprite("Textures/kirun" + i + ".png", "kirun" + i, assetManager, true, true, 6, 1, 0.09f, "Loop", "Start"));

            }
            //idle
            for (int i = 0; i < 8; i++) {
                if (i % 2 == 0) {
                    library.addSprite(new Sprite("Textures/kiidle" + i + ".png", "kiidle" + i, assetManager, false, true, 1, 1, 0.08f, "Loop", "Start"));
                } else {
                    library.addSprite(new Sprite("Textures/danidle" + i + ".png", "danidle" + i, assetManager, false, true, 1, 1, 0.08f, "Loop", "Start"));
                }
            }
            library.setCurSprite(12);
        } else {
            library.addSprite(new Sprite("Textures/monster.png", "monster", assetManager, true, true, 2, 1, 0.1f, "Loop", "Start"));
            library.setCurSprite(0);
            //does a search on the monster's name
            //adds the proper sprite
        }
    }
}