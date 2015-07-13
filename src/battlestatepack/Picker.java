/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlestatepack;

import MapPack.Tile;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import playerPack.AutoAttackCont;
import playerPack.Player;

/**
 *
 * @author Clara Currier
 */
public class Picker implements ActionListener {
    
    private final Camera cam;
    private final InputManager input;
    private Vector2f mouse;
    private Player activePlayer;
    private Node activeNode;
    private Node rootNode;
    private BattleMain bmain;
    
    public Picker(Camera c, InputManager in, Node rootNode, BattleMain bm) {
        cam = c;
        input = in;
        this.rootNode = rootNode;
        bmain = bm;
    }
    
    public void setActiveChar(Player player) {
        activePlayer = player;
        activeNode = player.getNode();
    }
    
    protected void mouseKey(boolean enabled) {
        if (enabled) {
            if (!input.hasMapping("LeftClick")) {
                input.addMapping("LeftClick", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
                input.addMapping("RightClick", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
                input.addListener(this, "LeftClick");
                input.addListener(this, "RightClick");
            }
        } else {
            if (input.hasMapping("LeftClick")) {
                input.deleteMapping("LeftClick");
                input.deleteMapping("RightClick");
                input.removeListener(this);
            }
        }
    }
    
    @Override
    public void onAction(String name, boolean keyPressed, float tpf) {
        if (!keyPressed) {
            mouse = input.getCursorPosition();
            Vector3f mousePosition3d = cam.getWorldCoordinates(mouse, 0).clone();
            Vector3f direction = cam.getWorldCoordinates(mouse, 1f).subtractLocal(mousePosition3d).normalizeLocal();

            // 1. Reset results list.
            CollisionResults results = new CollisionResults();
            // 2. Aim the ray from cam loc to cam direction.
            Ray ray = new Ray(mousePosition3d, direction);
            // 3. Collect intersections between Ray and rootNode (where map is attached)
            rootNode.collideWith(ray, results);
            Geometry picked = results.getClosestCollision().getGeometry();
            
            if (results.size() > 0) {
                if (name.equals("LeftClick")) {
                    if (picked.getName().contains("dan")) {
                        handleSwitch("dan");
                    } else if (picked.getName().contains("ki")) {
                        handleSwitch("ki");
                    } else {
                        handleMovement(picked);
                    }
                } else if (name.equals("RightClick")) {
                    if (picked.getName().contains("monster")) {
                        handleAttack(picked.getParent().getParent().getLocalTranslation());
                        //ignore the sprites and get to the node
                    }
                }
                //TODO: add special triggers for skills that require mouse picking

            } else {
                // No hits? 
                System.out.println("nothing");
            }
        }
    }
    
    private void handleMovement(Geometry pickedGeom) {
        if (activeNode.getControl(MoveCont.class) != null) {
            //cancel movement (don't bother cancelling pathfinding)
            activeNode.removeControl(MoveCont.class);
        }
        if (activeNode.getControl(AutoAttackCont.class) != null) {
            //cancel attacking
            activeNode.removeControl(AutoAttackCont.class);
        }
        // The closest collision point is what was truly hit:
        if (pickedGeom instanceof Tile) {
            Tile start = new Tile(new Vector2f(
                    activeNode.getLocalTranslation().x,
                    activeNode.getLocalTranslation().y),
                    (Tile) pickedGeom);
            Pathfinder pathfinder = new Pathfinder(start, (Tile) pickedGeom);
            activeNode.addControl(pathfinder);
            start = null;
        } else {
            System.out.println("not a tile");
        }
    }
    
    private void handleSwitch(String character) {
        if ((!bmain.getCurChar() && character.equals("dan"))
                || (bmain.getCurChar() && character.equals("ki"))) {
            bmain.switchChar();
        }
    }
    
    private void handleAttack(Vector3f target) {
        if (activeNode.getControl(AutoAttackCont.class) != null) {
            activeNode.removeControl(AutoAttackCont.class);
        }
        activeNode.addControl(new AutoAttackCont(
                activeNode.getName().equals("Dan")
                ? GVars.gvars.dautocooldown : GVars.gvars.kautocooldown,
                activePlayer, target));
    }
}