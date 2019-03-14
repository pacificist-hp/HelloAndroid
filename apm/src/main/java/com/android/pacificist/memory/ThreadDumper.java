package com.android.pacificist.memory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pacificist on 2018/8/21.
 */
public class ThreadDumper {

    public static Map<String, String> getThreadsInfo() {
        Map<String, String> info = new HashMap<>();
        Map<Thread, StackTraceElement[]> traces = Thread.getAllStackTraces();
        if (traces == null) {
            info.put("threads", "error");
        } else if (traces.size() < 400) {
            info.put("num", String.valueOf(traces.size()));
        } else {
            StringBuilder sb = new StringBuilder("");
            for (Map.Entry<Thread, StackTraceElement[]> entry : traces.entrySet()) {
                sb.append(entry.getKey().toString()).append(";");
            }
            info.put("names", sb.toString());
        }
        return info;
    }
}
