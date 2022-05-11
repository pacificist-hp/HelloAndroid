package com.android.pacificist.helloandroid;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES31.*;

class ComputeProgram {
    private static final String TAG = "ComputeProgram";

    private static final float[] INPUT_DATA = {
            2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17
    };

    private final Context context;

    int programId;
    int inputId;
    int outputId;

    public ComputeProgram(Context context) {
        this.context = context;
        if (init()) {
            Log.d(TAG, "++++++++++++++++\nComputeProgram init done");
        }
    }

    private boolean init() {
        // create shader
        int computeShaderId = glCreateShader(GL_COMPUTE_SHADER);
        if (computeShaderId == 0) {
            Log.e(TAG, "Create GL_COMPUTE_SHADER failed");
            return false;
        }

        glShaderSource(computeShaderId, readRawResource(context, R.raw.compute_shader));
        glCompileShader(computeShaderId);

        // create program
        programId = glCreateProgram();
        if (programId == 0) {
            Log.w(TAG, "Create program failed");
            return false;
        }

        glAttachShader(programId, computeShaderId);
        glLinkProgram(programId);

        // create input texture
        final int[] inputTextureId = new int[1];
        glGenTextures(1, inputTextureId, 0);
        inputId = inputTextureId[0];
        if (inputId == 0) {
            Log.w(TAG, "Create input texture failed");
            return false;
        }

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, inputId);
        glTexStorage2D(GL_TEXTURE_2D, 1, GL_R32F, 1, 16);


        // create output texture
        final int[] outputTextureId = new int[1];
        glGenTextures(1, outputTextureId, 0);
        outputId = outputTextureId[0];
        if (outputId == 0) {
            Log.w(TAG, "Create output texture failed");
            return false;
        }

        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, outputId);
        glTexStorage2D(GL_TEXTURE_2D, 1, GL_R32F, 1, 16);

        glBindImageTexture(0, inputId, 0, false, 0, GL_READ_ONLY, GL_R32F);
        glBindImageTexture(1, outputId, 0, false, 0, GL_WRITE_ONLY, GL_R32F);

        return true;
    }

    public void doCompute() {
        long inStart = System.currentTimeMillis();

        FloatBuffer inputData = ByteBuffer
                .allocateDirect(INPUT_DATA.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(INPUT_DATA);

        for (int i = 0; i < 16; i++) {
            Log.d(TAG, "input[" + i + "]=" + inputData.get(i));
        }

        inputData.position(0);
        glBindTexture(GL_TEXTURE_2D, inputId);
        glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, 1, 16, GL_RED, GL_FLOAT, inputData);

        glUseProgram(programId);
        glDispatchCompute(1, 1, 1);

        glMemoryBarrier(GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);

        long outStart = System.currentTimeMillis();
        Log.d(TAG, "input time: " + (outStart - inStart) + "ms");
        glBindTexture(GL_TEXTURE_2D, outputId);
//        glGetTexImage(GL_TEXTURE_2D, 0, GL_RED, GL_FLOAT, outputData);

        final int[] frameBufferIds = new int[1];
        glGenFramebuffers(1, frameBufferIds, 0);
        int fbo = frameBufferIds[0];
        glBindFramebuffer(GL_FRAMEBUFFER, fbo);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, outputId, 0);

        FloatBuffer outputData = ByteBuffer
                .allocateDirect(INPUT_DATA.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        outputData.position(0);
        glReadPixels(0, 0, 1, 16, GL_RED, GL_FLOAT, outputData);

        long end = System.currentTimeMillis();
        for (int i = 0; i < 16; i++) {
            Log.d(TAG, "output[" + i + "]=" + outputData.get(i));
        }
        Log.d(TAG, "output time: " + (end - outStart) + "ms");
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
