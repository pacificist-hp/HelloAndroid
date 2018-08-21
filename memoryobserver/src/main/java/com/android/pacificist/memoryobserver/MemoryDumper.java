package com.android.pacificist.memoryobserver;

import android.annotation.SuppressLint;
import android.os.Debug;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pacificist on 2018/7/22.
 */

public class MemoryDumper {

    static Map<String, String> getMemoryInfo() {
        Map<String, String> info = new HashMap<>();
        Runtime runtime = Runtime.getRuntime();
        info.put("max", formatMemoryText(runtime.maxMemory()));
        info.put("total", formatMemoryText(runtime.totalMemory()));
        info.put("free", formatMemoryText(runtime.freeMemory()));
        info.put("allocated", formatMemoryText(runtime.totalMemory() - runtime.freeMemory()));
        info.put("available", formatMemoryText(runtime.maxMemory() - runtime.totalMemory() + runtime.freeMemory()));
        info.put("nativeHeap", formatMemoryText(Debug.getNativeHeapSize()));
        info.put("nativeHeapAllocated", formatMemoryText(Debug.getNativeHeapAllocatedSize()));
        info.put("nativeHeapFree", formatMemoryText(Debug.getNativeHeapFreeSize()));
        return info;
    }

    static boolean isMemoryLow() {
        Runtime runtime = Runtime.getRuntime();
        long avail = runtime.maxMemory() - runtime.totalMemory() + runtime.freeMemory();
        return avail * 1.0f / runtime.maxMemory() < 0.05f;
    }

    @SuppressLint("DefaultLocale")
    private static String formatMemoryText(long value) {
        if (value > 1024 * 1024) {
            float valueInMb = value * 1.0f / 1024 / 1024;
            return String.format("%.1f MB", valueInMb);
        } else if (value > 1024) {
            float valueInKb = value * 1.0f / 1024;
            return String.format("%.1f KB", valueInKb);
        }
        return String.valueOf(value) + " B";
    }
}
