/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlestatepack;

import MapPack.JMEMap2d;
import MapPack.Tile;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import guiPack.MainMenu;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Clara Currier
 */
public class Pathfinder extends AbstractControl {

    Tile start, end;
    //The future that is used to check the execution status:
    Future future = null;
    Pathway way = null;
    ScheduledThreadPoolExecutor executor = MainMenu.getExecutor();

    public Pathfinder(Tile s, Tile e) {
        start = s;
        end = e;
    }

    @Override
    protected void controlUpdate(float tpf) {
        try {
            //If we have no waylist and not started a callable yet, do so!
            if (way == null && future == null) {
                //start the callable on the executor
                future = executor.submit(findWay);    //  Thread starts!
            } //If we have started a callable already, we check the status
            else if (future != null) {
                //Get the waylist when its done
                if (future.isDone()) {
                    way = (Pathway) future.get();
                    future = null;
                } else if (future.isCancelled()) {
                    //Set future to null. Maybe we succeed next time...
                    future = null;
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            Logger.getLogger(JMEMap2d.class.getName()).log(Level.SEVERE, null, e);
        }
        if (way != null) {
            //.... Success! Let's process the wayList and move the NPC...
            //add a new MoveCont using the pathway
            spatial.removeControl(this);
        }
    }

    public static class PriorityList<T> extends LinkedList {

        public void add(Comparable tile) {
            for (int i = 0; i < size(); i++) {
                if (tile.compareTo(get(i)) <= 0) {
                    add(i, tile);
                    return;
                }
            }
            addLast(tile);
        }
    }
    private Callable<Pathway> findWay = new Callable<Pathway>() {
        @Override
        public Pathway call() throws Exception {
            //... Now process data and find the way ...
            ArrayList<Tile> closedset = new ArrayList<>();
            PriorityList<Tile> openset = new PriorityList<>();

            start.costFromStart = 0;
            start.estimatedCostToGoal = start.getEstimatedCost(end);
            start.pathParent = null;
            openset.add(start);

            while (!openset.isEmpty()) {
                Tile current = (Tile) openset.removeFirst(); // lowest f-score val
                if (current.equals(end)) {
                    return way;
                }

                closedset.add(current);
                for (Tile neighbor : current.getNeighbors()) {
                    if (closedset.contains(neighbor)) {
                        continue;
                    }
                    float tentative_g_cost = current.costFromStart
                            + current.getCost(neighbor);

                    if (!openset.contains(neighbor)
                            || tentative_g_cost < neighbor.costFromStart) {
                        neighbor.pathParent = current;
                        neighbor.costFromStart = tentative_g_cost;
                        neighbor.estimatedCostToGoal = neighbor.costFromStart
                                + neighbor.getEstimatedCost(end);
                        if (!openset.contains(neighbor)) {
                            openset.add(neighbor);
                        }
                    }
                }
            }
            //failure
            return way;
        }
    };

    public Pathway reconstructWay(Tile begin, Tile current) {
        return new Pathway(0, 0); //contains the lengths and number of turns to make
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}