package com.android.pacificist.helloandroid.appwindow;

import android.text.TextUtils;

import com.android.pacificist.appwindow.ClientArea;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class AreaManager {

    /**
     * 窗口排布（多个窗口组合在一起）样式
     */
    private final ClientArea[][] mAreaStyles = new ClientArea[][] {
            {},
            {ClientArea.SINGLE},
            {ClientArea.TOP, ClientArea.DOUBLE_BOTTOM},
            {ClientArea.TOP, ClientArea.MIDDLE, ClientArea.TRIPLE_BOTTOM},
    };

    private Map<ClientArea, String> mAreas = new ConcurrentHashMap<>();

    private int mCurrentStyle = 0;

    AreaManager() {

    }

    void setCurrentStyle(List<String> clients) {
        if (clients == null) {
            mCurrentStyle = 0;
        } else if (clients.size() > mAreaStyles.length) {
            mCurrentStyle = 0;
        } else {
            mCurrentStyle = clients.size();
        }
    }

    ClientArea[] getCurrentAreas() {
        return mAreaStyles[mCurrentStyle];
    }

    int getCurrentStyle() {
        return mCurrentStyle;
    }

    boolean isEmptyStyle() {
        return mAreaStyles[mCurrentStyle].length == 0;
    }

    String getClientInArea(ClientArea area) {
        return mAreas.get(area);
    }

    Collection<String> getAllClients() {
        return mAreas.values();
    }

    void addClient(ClientArea clientArea, String pkg) {
        mAreas.put(clientArea, pkg);
    }

    void removeClient(String pkg) {
        for (Map.Entry<ClientArea, String> item : mAreas.entrySet()) {
            if (TextUtils.equals(item.getValue(), pkg)) {
                mAreas.remove(item.getKey());
                break;
            }
        }
    }

    ClientArea getPointArea(float x, float y) {
        ClientArea[] currentAreas = mAreaStyles[mCurrentStyle];
        for (ClientArea area : currentAreas) {
            if (area.inside(x, y)) {
                return area;
            }
        }

        return ClientArea.NONE;
    }
}
