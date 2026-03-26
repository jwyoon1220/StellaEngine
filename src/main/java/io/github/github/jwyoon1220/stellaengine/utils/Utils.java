package io.github.github.jwyoon1220.stellaengine.utils;

import org.lwjgl.system.MemoryUtil;

import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Utils {

    public static FloatBuffer storeDataInFloatBuffer(float[] data) {
        var buffer = MemoryUtil.memAllocFloat(data.length);
        buffer.put(data).flip();
        return buffer;
    }
    public static IntBuffer storeDataInIntBuffer(int[] data) {
        var buffer = MemoryUtil.memAllocInt(data.length);
        buffer.put(data).flip();
        return buffer;
    }
    public static String loadResource(String fileName) throws Exception {
        String res;
        try (InputStream in = Utils.class.getResourceAsStream(fileName)) {
            Scanner sc = new Scanner(in, StandardCharsets.UTF_8);
            res = sc.useDelimiter("\\A").next();
        }
        return res;
    }

}
