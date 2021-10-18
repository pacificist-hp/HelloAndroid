package com.android.pacificist.helloandroid;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.android.pacificist.helloandroid.objects.Mallet;
import com.android.pacificist.helloandroid.objects.Puck;
import com.android.pacificist.helloandroid.objects.Table;
import com.android.pacificist.helloandroid.programs.ColorShaderProgram;
import com.android.pacificist.helloandroid.programs.TextureShaderProgram;
import com.android.pacificist.helloandroid.util.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.*;

class AirHockeyRender implements GLSurfaceView.Renderer {
    private final Context context;

    //视图矩阵
    private final float[] viewMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];

    public  final float[] projectionMatrix = new float[16];
    public  final float[] modelMatrix = new float[16];

    private Table table;
    private Mallet mallet;
    private Puck puck;

    private TextureShaderProgram textureProgram;
    private ColorShaderProgram colorProgram;

    private int texture;

    public AirHockeyRender(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        table = new Table();
        mallet = new Mallet(0.08f, 0.15f, 32);
        puck = new Puck(0.06f, 0.02f, 32);

        textureProgram = new TextureShaderProgram(context);
        colorProgram = new ColorShaderProgram(context);

        texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface);
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        glViewport(0, 0, width, height);

        //用45度视野创建一个透视投影，z值为-1到-10。默认z在0位置,需要把坐标移动-1到-10以内才可见
        Matrix.perspectiveM(projectionMatrix, 0, 45, width / (float) height, 1f, 10f);
        //创建视图矩阵
        Matrix.setLookAtM(viewMatrix, 0, 0f, 1.2f, 2.2f, 0f, 0f, 0f, 0f, 1f, 0f);
    }

    @Override
    public void onDrawFrame(GL10 glUnused) {
        glClear(GL_COLOR_BUFFER_BIT);

        Matrix.multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

        // Draw the table
        positionTableInScene();
        textureProgram.useProgram();
        textureProgram.setUniform(modelViewProjectionMatrix, texture);
        table.bindData(textureProgram);
        table.draw();

        // Draw the mallets.
        positionObjectInScene(0f, mallet.height / 2f, -0.4f);
        colorProgram.useProgram();
        colorProgram.setUniform(modelViewProjectionMatrix, 1f, 0f, 0f);
        mallet.bindData(colorProgram);
        mallet.draw();

        positionObjectInScene(0f, mallet.height / 2f, .4f);
        colorProgram.setUniform(modelViewProjectionMatrix, 0f, 0f, 1f);
        // Note that we don't have to define the project data twice -- we just
        // draw the same mallet again but in a different position and with a
        // different color.
        mallet.draw();

        // Draw the puck
        positionObjectInScene(0f, puck.height / 2f, 0f);
        colorProgram.setUniform(modelViewProjectionMatrix, 1f, 1f, 0f);
        puck.bindData(colorProgram);
        puck.draw();
    }

    private void positionTableInScene() {
        // The table is defined in terms of X & Y coordinates, so we rotate it
        // 90 degrees to lie flat on the XZ plane.
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.rotateM(modelMatrix, 0, -90f, 1f, 0f, 0f);
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);
    }

    private void positionObjectInScene(float x, float y, float z) {
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, x, y, z);
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);
    }
}
