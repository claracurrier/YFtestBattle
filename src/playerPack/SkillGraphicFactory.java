/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package playerPack;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;

/**
 *
 * @author Clara Currier
 */
public class SkillGraphicFactory {

    private static Node rootNode;
    private static AssetManager assetManager;
    private static Player dan, kirith;

    public void setup(Node root, AssetManager aman, Player dan, Player kirith) {
        //adds anything necessary to use the skillGraphic class
        rootNode = root;
        assetManager = aman;
        SkillGraphicFactory.dan = dan;
        SkillGraphicFactory.kirith = kirith;
    }

    public static SkillGraphic newInstance() {
        return new SkillGraphic(rootNode, assetManager, dan, kirith);
    }
}
