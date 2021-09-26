package com.android.pacificist.helloandroid;

import android.util.Log;

import static android.opengl.GLES20.*;

class ShaderHelper {
    private static final String TAG = "ShaderHelper";

    public static int compileVertexShader(String shaderCode) {
        return compileShader(GL_VERTEX_SHADER, shaderCode);
    }

    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GL_FRAGMENT_SHADER, shaderCode);
    }

    private static int compileShader(int type, String shaderCode) {
        final int shaderObjectId = glCreateShader(type);
        if (shaderObjectId == 0) {
            Log.w(TAG, "Could not create a new shader failed: type=" + type);
            return 0;
        }

        glShaderSource(shaderObjectId, shaderCode);

        glCompileShader(shaderObjectId);
        Log.d(TAG, "CompileShader:\n" + shaderCode
                + "\nInfo: " + glGetShaderInfoLog(shaderObjectId));

        final int[] compileStatus = new int[1];
        glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0);
        if (compileStatus[0] == 0) {
            // If failed, delete it
            glDeleteShader(shaderObjectId);
            Log.w(TAG, "Compilation of shader failed");
            return 0;
        }

        return shaderObjectId;
    }

    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        final int programObjectId = glCreateProgram();
        if (programObjectId == 0) {
            Log.w(TAG, "Could not create a new program");
            return 0;
        }

        glAttachShader(programObjectId, vertexShaderId);
        glAttachShader(programObjectId, fragmentShaderId);

        glLinkProgram(programObjectId);
        Log.d(TAG, "LinkProgram Info: " + glGetProgramInfoLog(programObjectId));

        final int[] linkStatus = new int[1];
        glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] == 0) {
            // If failed, delete it
            glDeleteProgram(programObjectId);
            Log.w(TAG, "Linking of program failed");
            return 0;
        }

        return programObjectId;
    }

    public static boolean validateProgram(int programObjectId) {
        glValidateProgram(programObjectId);

        final int[] validateStatus = new int[1];
        glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0);

        Log.d(TAG, "ValidateProgram Status: " + validateStatus[0]
                + ", Info: " + glGetProgramInfoLog(programObjectId));

        return validateStatus[0] != 0;
    }
}
