package com.vitco.util.pref;

import com.vitco.util.FileTools;
import com.vitco.util.error.ErrorHandlerInterface;

import java.io.*;
import java.util.HashMap;

/**
 * Stores & Loads preferences (Integer, String, Object)
 * Storing is executed after the PreDestroy of all the other objects.
 */
public class Preferences implements PreferencesInterface {

    private HashMap<String, Object> map = new HashMap<String, Object>();

    @Override
    public boolean contains(String key) {
        return map.containsKey(key);
    }

    // var & setter
    private ErrorHandlerInterface errorHandler;
    @Override
    public void setErrorHandler(ErrorHandlerInterface errorHandler) {
        this.errorHandler = errorHandler;
    }

    @Override
    public void storeObject(String key, Object value) {
        map.put(key, value);
    }

    @Override
    public Object loadObject(String key) {
        return map.containsKey(key) ? map.get(key) : null;
    }

    @Override
    public void storeBoolean(String key, boolean value) {
        map.put(key, value);
    }

    @Override
    public void storeInteger(String key, int value) {
        map.put(key, value);
    }

    @Override
    public void storeString(String key, String value) {
        map.put(key, value);
    }

    @Override
    public boolean loadBoolean(String key) {
        return map.containsKey(key) ? (Boolean)map.get(key) : false;
    }

    @Override
    public int loadInteger(String key) {
        return map.containsKey(key) ? (Integer)map.get(key) : 0;
    }

    @Override
    public String loadString(String key) {
        return map.containsKey(key) ? (String)map.get(key) : "";
    }

    // var % setter
    private String storageFileName;
    @Override
    public void setStorageFile(String filename) {
        storageFileName = filename;
    }

    // "manually" executed after all PreDestroys are called
    @Override
    public void save() {
        // store the map in file
        File dataFile = new File(storageFileName);
        if (dataFile.getParentFile().exists() || dataFile.getParentFile().mkdirs()) {
            try {
                FileOutputStream fileOut = new FileOutputStream(dataFile);
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(map);
            } catch (FileNotFoundException e) {
                errorHandler.handle(e); // should not happen
            } catch (IOException e) {
                errorHandler.handle(e); // should not happen
            }
        }
    }

    // executed when initiated (spring "init-method")
    @Override
    public void load() {
        File dataFile = new File(storageFileName);
        if (dataFile.exists()) {
            try {
                FileInputStream fileIn = new FileInputStream(dataFile);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                map = FileTools.castHash((HashMap) in.readObject(), String.class, Object.class);
                in.close();
                fileIn.close();
            } catch (IOException e) {
                errorHandler.handle(e); // should not happen
            } catch (ClassNotFoundException e) {
                errorHandler.handle(e); // should not happen
            }
        }
    }

}