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
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.WireBox;
import com.jme3.system.AppSettings;
import spriteProject.Sprite;
import spriteProject.SpriteLibrary;

/**
 *
 * @author PC
 */
public class EntityMaker {

    private AssetManager assetManager;
    private AppSettings settings;

    public EntityMaker(AssetManager aman, AppSettings s, AppStateManager asm) {
        this.assetManager = aman;
        this.settings = s;
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

    protected Node createMobAtk(Spatial mob) {
        Node node = new Node("mobatkbox");

        node.setLocalTranslation(mob.getLocalTranslation());

        mob.setUserData("atkbox", node);

        node.setUserData("halfwidth", 25f);
        node.setUserData("halfheight", 40f);
        node.setUserData("type", "attackbox");
        node.setUserData("atkpower", 50);

        return node;

    }

    protected Spatial createMob(String name, CollideAS cc, Node dan, Node ki) throws Exception {
        //remember to define exceptions later
        Spatial mob = createSpatial(name);

        Node atknode = new Node("mobattack");

        //this is where there will be an xml reader to take in mob info
        //for now it will just copy the standard spatial method
        //whole bunch of mob.setUserData()
        //then an if statement to check for skills
        mob.setUserData("alive", true);
        mob.setUserData("mobBehavior", "TempWC");
        mob.setUserData("wwidth", settings.getWidth());
        mob.setUserData("wheight", settings.getHeight());
        mob.setUserData("type", "mob");

        MobControl mc = new MobControl("TempWC", mob, cc, dan, ki);
        mc.setAtkNode(atknode);
        mob.addControl(mc);


        mob.move(500, 500, 0);
        return mob;
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
                if (i == 5) { //DL
                    library.addSprite(new Sprite("Textures/danrun4.png", "danrun" + i, assetManager, true, true, 6, 1, 0.08f, "Loop", "Start"));
                } else if (i == 3) { //DR
                    library.addSprite(new Sprite("Textures/danrun4.png", "danrun" + i, assetManager, true, true, 6, 1, 0.08f, "Loop", "Start"));
                } else {
                    library.addSprite(new Sprite("Textures/danrun" + i + ".png", "danrun" + i, assetManager, true, true, 6, 1, 0.09f, "Loop", "Start"));
                }

            }
            //idle
            for (int i = 0; i < 8; i++) {
                    library.addSprite(new Sprite("Textures/danidle" + i + ".png", "danidle" + i, assetManager, false, true, 1, 1, 0.08f, "Loop", "Start"));
                
            }


        } else if (name.equals("Kirith")) {
            //adds all of kirith's sprites
//temporarily uses dan's sprites
            for (int i = 0; i < 8; i++) {
                library.addSprite(new Sprite("Textures/kirun" + i + ".png", "kirun" + i, assetManager, true, true, 6, 1, 0.09f, "Loop", "Start"));

            }
            //idle
            for (int i = 0; i < 8; i++) {
                if (i % 2 == 0) {
                    library.addSprite(new Sprite("Textures/kiidle" + i + ".png", "kiidle" + i, assetManager, false, true, 1, 1, 0.08f, "Loop", "Start"));
                }else{
                    library.addSprite(new Sprite("Textures/danidle" + i + ".png", "danidle" + i, assetManager, false, true, 1, 1, 0.08f, "Loop", "Start"));
                }
            }
            library.activateSprite(12);
        } else {
            library.addSprite(new Sprite("Textures/monster.png", "monster", assetManager, true, true, 2, 1, 0.1f, "Loop", "Start"));
            library.activateSprite(0);
            //does a search on the monster's name
            //adds the proper sprite
        }
    }
}