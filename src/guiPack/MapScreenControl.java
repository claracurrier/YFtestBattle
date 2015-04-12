/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guiPack;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 * @author PC
 */
public class MapScreenControl extends AbstractAppState implements ScreenController {
 
    private Application app;
    private Nifty nifty;
    
    public MapScreenControl(Nifty n){
        this.nifty=n;
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
    this.app = app;
    }
 
    @Override
    public void update(float tpf) {
     }
    
    public Nifty getNifty(){
        return nifty;
    }
 
    @Override
    public void cleanup() {
        super.cleanup();
        }
    
 
    public void bind(Nifty nifty, Screen screen) {
        }
 
    public void onStartScreen() {
       }
 
    public void onEndScreen() {
      }
}
