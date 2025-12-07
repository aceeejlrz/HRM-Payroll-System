package com.hrmpayroll.util;

import java.io.*;
import java.util.Optional;

/**
 * Generic serialization utility for persistent storage
 */
public class SerializationUtil {

    /**
     * Saves a serializable object to file
     */
    public static void save(Serializable data, String filePath) {
        // Ensure directory exists
        File file = new File(filePath);
        file.getParentFile().mkdirs();

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(data);
        } catch (IOException e) {
            System.err.println("Serialization error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Loads an object from file with proper type handling
     */
    @SuppressWarnings("unchecked")
    public static <T> Optional<T> load(String filePath, Class<T> type) {
        File file = new File(filePath);
        if (!file.exists()) {
            return Optional.empty();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            Object obj = ois.readObject();
            if (type.isInstance(obj)) {
                return Optional.of((T) obj);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Deserialization error: " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Legacy load method for backward compatibility
     * @deprecated Use load(String filePath, Class<T> type) instead
     */
    @Deprecated
    public static Optional<Object> load(String filePath) {
        return load(filePath, Object.class);
    }
}