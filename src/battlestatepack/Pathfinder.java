/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlestatepack;

import MapPack.MapLoader;
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

    private Tile start, end;
    private Future future = null;
    private Pathway way = null;
    private final ScheduledThreadPoolExecutor executor = MainMenu.getExecutor();

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
                    spatial.removeControl(this);
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            Logger.getLogger(MapLoader.class.getName()).log(Level.SEVERE, null, e);
            future = null;
            spatial.removeControl(this);
        }
        if (way != null) {
            if (!way.getPath().isEmpty()) {
                //.... Success! Add a new MoveCont using the pathway
                spatial.addControl(new MoveCont(way, spatial.getName()));
            } else{
                System.out.println("empty");
            }
            spatial.removeControl(this);
        }
    }

    private class PriorityList<T> extends LinkedList {

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
            Pathway bestWay = new Pathway();
            ArrayList<Tile> closedset = new ArrayList<>();
            PriorityList<Tile> openset = new PriorityList<>();

            start.costFromStart = 0;
            start.estimatedCostToGoal = start.getCost(end);
            start.pathParent = null;
            openset.add(start);

            while (!openset.isEmpty() && closedset.size() < 2200) {
                Tile current = (Tile) openset.removeFirst(); // lowest f-score val
                if (current.equals(end)) {
                    return reconstructWay(end);
                }

                closedset.add(current);
                for (Tile neighbor : current.getNeighbors(false)) {
                    for (Tile plusNeighbor : neighbor.getNeighbors(true)) {
                        //checks to make sure character can fit
                        if (plusNeighbor.isClosed()) {
                            closedset.add(neighbor);
                            break;
                        }
                    }
                    if (closedset.contains(neighbor)) {
                        continue;
                    }
                    float possibleNewCost = current.costFromStart
                            + current.getCost(neighbor);

                    if (!openset.contains(neighbor)
                            || possibleNewCost < neighbor.costFromStart) {
                        neighbor.pathParent = current;
                        neighbor.costFromStart = possibleNewCost;
                        neighbor.estimatedCostToGoal = neighbor.costFromStart
                                + neighbor.getCost(end);
                        if (!openset.contains(neighbor)) {
                            openset.add(neighbor);
                        }
                    }

                    if (neighbor.getCost(end) < bestWay.getClosenessScore()) {
                        //Keeps track of approximate shortest route in case end is impossible
                        bestWay = reconstructWay(neighbor);
                        bestWay.setClosenessScore(neighbor.getCost(end));
                    }
                }
            }
            //Didn't reach goal
            return bestWay;
        }
    };

    private Pathway reconstructWay(Tile node) {
        LinkedList<Tile> path = new LinkedList<>();
        while (node.pathParent != null) {
            path.addFirst(node);
            node = node.pathParent;
        }
        path.addFirst(start);
        return new Pathway(path); //contains the tiles that lie on the path
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}