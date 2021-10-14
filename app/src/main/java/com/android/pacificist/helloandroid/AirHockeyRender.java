package com.android.pacificist.helloandroid;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.android.pacificist.helloandroid.util.ShaderHelper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.*;

class AirHockeyRender implements GLSurfaceView.Renderer {

    private static final String U_MATRIX = "u_Matrix";
    private static final String A_POSITION = "a_Position";
    private static final String A_COLOR = "a_Color";

    private static final int POSITION_COMPONENT_COUNT = 2; // assign 4 if add w
    private static final int COLOR_COMPONENT_COUNT = 3;

    private static final int BYTES_FLOAT = 4;
    private static final int STRIDE =
            (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_FLOAT;

    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];

    private final FloatBuffer vertexData;

    private final Context context;

    private int program;

    private int uMatrixLocation;
    private int aPositionLocation;
    private int aColorLocation;

    public AirHockeyRender(Context context) {
        float[] tableVerticesWithTriangles = {
                // Order of coordinates: X, Y, (Z, W,) R, G, B

                // Triangle Fan
                0f, 0f, /*0f, 1.5f,*/ 1f, 1f, 1f,
                -0.5f, -0.8f, /*0f, 1f,*/ 0.7f, 0.7f, 0.7f,
                0.5f, -0.8f, /*0f, 1f,*/ 0.7f, 0.7f, 0.7f,
                0.5f, 0.8f, /*0f, 2f,*/ 0.7f, 0.7f, 0.7f,
                -0.5f, 0.8f, /*0f, 2f,*/ 0.7f, 0.7f, 0.7f,
                -0.5f, -0.8f, /*0f, 1f,*/ 0.7f, 0.7f, 0.7f,

                // Line 1
                -0.5f, 0f, /*0f, 1.5f,*/ 1f, 0f, 0f,
                0.5f, 0f, /*0f, 1.5f,*/ 0f, 0f, 1f,

                // Mallet 1
                0f, -0.4f, /*0f, 1.25f,*/ 0f, 0f, 1f,

                // Mallet 2
                0f, 0.4f, /*0f, 1.75f,*/ 1f, 0f, 0f
        };

        vertexData = ByteBuffer
                .allocateDirect(tableVerticesWithTriangles.length * BYTES_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(tableVerticesWithTriangles);

        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        String vertexShaderSource = readRawResource(context, R.raw.simple_vertex_shader);
        String fragmentShaderSource = readRawResource(context, R.raw.simple_fragment_shader);

        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);

        program = ShaderHelper.linkProgram(vertexShader, fragmentShader);
        if (ShaderHelper.validateProgram(program)) {
        }

        glUseProgram(program);

        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aColorLocation = glGetAttribLocation(program, A_COLOR);

        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT,
                false, STRIDE, vertexData);

        glEnableVertexAttribArray(aPositionLocation);

        vertexData.position(POSITION_COMPONENT_COUNT);
        glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GL_FLOAT,
                false, STRIDE, vertexData);

        glEnableVertexAttribArray(aColorLocation);
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

        glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);

        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);

        glDrawArrays(GL_LINES, 6, 2);

        glDrawArrays(GL_POINTS, 8, 1);

        glDrawArrays(GL_POINTS, 9, 1);
    }

    private static String readRawResource(Context context, int resId) {
        StringBuilder body = new StringBuilder();
        try {
            InputStream inputStream = context.getResources().openRawResource(resId);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String nextLine;
            while ((nextLine = bufferedReader.readLine()) != null) {
                body.append(nextLine).append('\n');
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return body.toString();
    }
}
