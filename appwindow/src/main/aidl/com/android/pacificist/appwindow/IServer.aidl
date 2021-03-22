// IServer.aidl
package com.android.pacificist.appwindow;

interface IServer {

    void moveAppWindow(String source, float targetX, float targetY);
    void maxAppWindow(String pkg);

}
