package me.monst.qwicktree.util;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * SLAPI = Saving/Loading API API for Saving and Loading Objects.
 *
 * @author Tomsik68
 */
public class SLAPI {
    public static void save(Object obj, String path) throws Exception {
        ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(path)));
        oos.writeObject(obj);
        oos.flush();
        oos.close();
    }
    
    public static Object load(String path) throws Exception {
        ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(path)));
        Object result = ois.readObject();
        ois.close();
        return result;
    }
    
    /* ### WRAPPER FUNCTIONS ### */
    
    @SuppressWarnings("unchecked")
    public static HashMap<String, Integer> loadIgnoreList(File folder, String file) {
        File path = new File(folder, file);
        
        try {
            return (HashMap<String, Integer>) load(path.getAbsolutePath());
        } catch (Exception e) {
            return new HashMap<>();
        }
    }
    
    public static void saveIgnoreList(File folder, String file, HashMap<String, Integer> ignoreList) {
        File path = new File(folder, file);
        
        try {
            save(ignoreList, path.getAbsolutePath());
        } catch (Exception ignored) {}
    }
    
    @SuppressWarnings("unchecked")
    public static List<String> loadDisabledList(File folder, String file) {
        File path = new File(folder, file);
        
        try {
            return (List<String>) load(path.getAbsolutePath());
        } catch (Exception e) {
            return new LinkedList<>();
        }
    }
    
    public static void saveDisabledList(File folder, String file, List<String> disabledList) {
        File path = new File(folder, file);
        
        try {
            save(disabledList, path.getAbsolutePath());
        } catch (Exception ignored) {}
    }
}
