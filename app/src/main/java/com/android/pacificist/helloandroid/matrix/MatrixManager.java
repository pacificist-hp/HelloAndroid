package com.android.pacificist.helloandroid.matrix;

import android.app.Application;

import com.tencent.matrix.Matrix;
import com.tencent.matrix.iocanary.IOCanaryPlugin;
import com.tencent.matrix.iocanary.config.IOConfig;

public class MatrixManager {

    public static void init(Application application) {
        Matrix.Builder builder = new Matrix.Builder(application);
        builder.patchListener(new MatrixPluginListener(application));

        IOCanaryPlugin ioCanaryPlugin = new IOCanaryPlugin(
                new IOConfig.Builder().dynamicConfig(new MatrixConfigImpl()).build());
        builder.plugin(ioCanaryPlugin);
        Matrix.init(builder.build());
        ioCanaryPlugin.start();
    }
}
