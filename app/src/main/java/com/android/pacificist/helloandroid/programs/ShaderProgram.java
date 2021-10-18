package com.android.pacificist.helloandroid.programs;

import android.content.Context;

import com.android.pacificist.helloandroid.util.ShaderHelper;
import com.android.pacificist.helloandroid.util.TextResourceReader;

import static android.opengl.GLES20.*;

public class ShaderProgram {
    //uniform constants
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
    protected static final String U_COLOR = "u_Color";

    //Attribute constants
    protected static final String A_POSITION = "a_Position";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

    //Shader program
    protected final int program;

    protected ShaderProgram(Context context, int vertexResId, int fragmentResId) {
        program = ShaderHelper.buildProgram(
                TextResourceReader.readRawResource(context, vertexResId),
                TextResourceReader.readRawResource(context, fragmentResId)
        );
    }

    public void useProgram() {
        glUseProgram(program);
    }
}
