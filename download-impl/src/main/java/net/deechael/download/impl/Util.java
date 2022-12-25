package net.deechael.download.impl;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

public final class Util {

    private final static String ID_IDENTITIES = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String randomId(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(ID_IDENTITIES.charAt(random.nextInt(ID_IDENTITIES.length())));
        }
        return stringBuilder.toString();
    }

    public static boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isLong(String string) {
        try {
            Long.parseLong(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void initFile(File file, long size) {
        File parent = file.getParentFile();
        if (parent != null)
            if (!parent.exists())
                parent.mkdirs();
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            RandomAccessFile r = new RandomAccessFile(file, "rwd");
            r.setLength(size);
            r.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, List<Map.Entry<String, String>>> cloneCookies(Map<String, List<Map.Entry<String, String>>> map) {
        Map<String, List<Map.Entry<String, String>>> newMap = new HashMap<>();
        for (Map.Entry<String, List<Map.Entry<String, String>>> entry : map.entrySet()) {
            List<Map.Entry<String, String>> values = new ArrayList<>();
            for (Map.Entry<String, String> value : values) {
                values.add(new AbstractMap.SimpleEntry<>(value.getKey(), value.getValue()));
            }
            newMap.put(entry.getKey(), values);
        }
        return newMap;
    }

    public static String processCookie(Map<String, List<Map.Entry<String, String>>> cookies) {
        StringBuilder stringBuilder = new StringBuilder();
        for (List<Map.Entry<String, String>> entries : cookies.values()) {
            for (Map.Entry<String, String> entry : entries) {
                stringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("; ");
            }
        }
        return stringBuilder.toString();
    }

    public static Map<String, String> remap(Map<String, List<String>> map) {
        Map<String, String> newMap = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            if (entry.getValue().size() == 0)
                continue;
            if (entry.getKey() == null)
                continue;
            newMap.put(entry.getKey().toLowerCase(), entry.getValue().get(0));
        }
        return newMap;
    }

    private Util() {
    }

}
