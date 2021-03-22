# AppWindow

adb shell am start-foreground-service -n com.client.map/.WindowService --es area TOP
adb shell am start-foreground-service -n com.client.music/.WindowService --es area MIDDLE
adb shell am start-foreground-service -n com.client.surface/.WindowService --es area TRIPLE_BOTTOM

adb shell am force-stop com.client.map
adb shell am force-stop com.client.music
adb shell am force-stop com.client.surface

adb shell am startservice -n com.android.pacificist.helloandroid/.appwindow.AppWindowService --es type areaStyle
