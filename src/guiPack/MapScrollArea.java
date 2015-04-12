/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guiPack;

import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.input.InputManager;
import com.jme3.material.Material;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;
import de.lessvoid.nifty.Nifty;

/**
 *
 * @author PC
 */
public class MapScrollArea {

    private AssetManager asm;
    private AppStateManager stateManager;

    public MapScrollArea(AssetManager asm, AppStateManager appm) {
        this.asm = asm;
        stateManager = appm;
    }

    public Node makeTestMap() {
        //will be made generic with more maps later
        //or even replaced w/ xml loader
        Node map = new Node();
        Material picMat = new Material(asm, "Common/MatDefs/Gui/Gui.j3md");
        map.setMaterial(picMat);

        float width = 1500f;
        float height = 1500f;
        map.setUserData("width", width);
        map.setUserData("height", height);
        map.setUserData("ignoreme", true);

        Picture pic = new Picture("testBattle");
        Texture2D tex = (Texture2D) asm.loadTexture("Interface/testBattle.png");
        pic.setTexture(asm, tex, true);

        pic.move(0, 0, -0.5f);
        map.attachChild(pic);

        return map;
    }

    public void makeScrollArea(InputManager inm,
            AudioRenderer arr, ViewPort gvp) {
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(asm, inm, arr, gvp);

        Nifty nifty = niftyDisplay.getNifty();
        gvp.addProcessor(niftyDisplay);
        nifty.setIgnoreKeyboardEvents(true);
        nifty.setIgnoreMouseEvents(true);

        MapScreenControl msc = new MapScreenControl(nifty);
        stateManager.attach(msc);
        nifty.fromXml("Interface/TestScrollArea.xml", "map", msc);
    }
}
