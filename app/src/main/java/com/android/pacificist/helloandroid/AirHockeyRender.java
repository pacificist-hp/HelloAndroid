package com.android.pacificist.helloandroid;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.android.pacificist.helloandroid.objects.Mallet;
import com.android.pacificist.helloandroid.objects.Table;
import com.android.pacificist.helloandroid.programs.ColorShaderProgram;
import com.android.pacificist.helloandroid.programs.TextureShaderProgram;
import com.android.pacificist.helloandroid.util.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.*;

class AirHockeyRender implements GLSurfaceView.Renderer {
    private final Context context;

    public  final float[] projectionMatrix = new float[16];
    public  final float[] modelMatrix = new float[16];

    private Table mTable;
    private Mallet mMallet;

    private TextureShaderProgram textureShaderProgram;
    private ColorShaderProgram colorShaderProgram;

    private int texture;

    public AirHockeyRender(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        mTable = new Table();
        mMallet = new Mallet();

        textureShaderProgram = new TextureShaderProgram(context);
        colorShaderProgram = new ColorShaderProgram(context);

        texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface);
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        glViewport(0, 0, width, height);

        Matrix.perspectiveM(projectionMatrix, 0, 45, width / (float) height, 1f, 10f);

        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, 0f, 0f, -2.5f);
        Matrix.rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f);

        final float[] temp = new float[16];
        Matrix.multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);
    }

    @Override
    public void onDrawFrame(GL10 glUnused) {
        glClear(GL_COLOR_BUFFER_BIT);

        // draw the table
        textureShaderProgram.useProgram();
        textureShaderProgram.setUniform(projectionMatrix, texture);
        mTable.bindData(textureShaderProgram);
        mTable.draw();

        // draw the mallets
        colorShaderProgram.useProgram();
        colorShaderProgram.setUniform(projectionMatrix);
        mMallet.bindData(colorShaderProgram);
        mMallet.draw();
    }
}
