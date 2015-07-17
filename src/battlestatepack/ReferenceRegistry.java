/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlestatepack;

import java.util.HashMap;

/**
 *
 * @author Clara Currier
 */
public class ReferenceRegistry {

    public static final ReferenceRegistry registry = new ReferenceRegistry();
    private HashMap<Class<?>, Object> references = new HashMap();

    private ReferenceRegistry() { //singleton
    }

    public void register(Class<?> clazz, Object reference) {
        references.put(clazz, reference);
    }

    public void remove(Class<?> clazz) {
        references.remove(clazz);
    }

    public boolean hasRegistry(Class<?> clazz) {
        return references.containsKey(clazz);
    }

    public Object get(Class<?> clazz) {
        if (references.containsKey(clazz)) {
            return references.get(clazz);
        } else {
            System.out.println("class " + clazz.getSimpleName() + " not registered");
            return null;
        }
    }
}
