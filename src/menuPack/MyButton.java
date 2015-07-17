/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package menuPack;

import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import tonegod.gui.controls.buttons.Button;
import tonegod.gui.core.Screen;

/**
 *
 * @author tonegod
 * @author PC
 */
public class MyButton extends Button {

    /**
     * Creates a new instance of the Button control
     *
     * @param screen The screen control the Element is to be added to
     * @param UID A unique String identifier for the Element
     * @param position A Vector2f containing the x/y position of the Element
     */
    public MyButton(Screen screen, String UID, Vector2f position) {
        this(screen, UID, position,
                screen.getStyle("Button").getVector2f("defaultSize"),
                screen.getStyle("Button").getVector4f("resizeBorders"),
                screen.getStyle("Button").getString("defaultImg"));
    }

    /**
     * Creates a new instance of the Button control
     *
     * @param screen The screen control the Element is to be added to
     * @param UID A unique String identifier for the Element
     * @param position A Vector2f containing the x/y position of the Element
     * @param dimensions A Vector2f containing the width/height dimensions of
     * the Element
     */
    public MyButton(Screen screen, String UID, Vector2f position, Vector2f dimensions) {
        this(screen, UID, position, dimensions,
                screen.getStyle("Button").getVector4f("resizeBorders"),
                screen.getStyle("Button").getString("defaultImg"));
    }

    /**
     * Creates a new instance of the Button control
     *
     * @param screen The screen control the Element is to be added to
     * @param UID A unique String identifier for the Element
     * @param position A Vector2f containing the x/y position of the Element
     * @param dimensions A Vector2f containing the width/height dimensions of
     * the Element
     * @param resizeBorders A Vector4f containg the border information used when
     * resizing the default image (x = N, y = W, z = E, w = S)
     * @param defaultImg The default image to use for the Slider's track
     */
    public MyButton(Screen screen, String UID, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
        super(screen, UID, position, dimensions, resizeBorders, defaultImg);
    }

    @Override
    public void onKeyPress(KeyInputEvent evt) {
        //no
    }

    @Override
    public void onKeyRelease(KeyInputEvent evt) {
        //stop
    }

    @Override
    public void onButtonMouseLeftDown(MouseButtonEvent mbe, boolean bln) {
    }

    @Override
    public void onButtonMouseRightDown(MouseButtonEvent mbe, boolean bln) {
    }

    @Override
    public void onButtonMouseLeftUp(MouseButtonEvent mbe, boolean bln) {
    }

    @Override
    public void onButtonMouseRightUp(MouseButtonEvent mbe, boolean bln) {
    }

    @Override
    public void onButtonFocus(MouseMotionEvent mme) {
    }

    @Override
    public void onButtonLostFocus(MouseMotionEvent mme) {
    }
}
