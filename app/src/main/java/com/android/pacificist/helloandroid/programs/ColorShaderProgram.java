package com.android.pacificist.helloandroid.programs;

import android.content.Context;

import com.android.pacificist.helloandroid.R;

import static android.opengl.GLES20.*;
public class ColorShaderProgram extends ShaderProgram {
    //Uniform Location
    private final int uMatrixLocation;

    //Attribute Location
    private final int aPositionLocation;
    private final int aColorLocation;

    public ColorShaderProgram(Context context) {
        super(context, R.raw.simple_vertex_shader, R.raw.simple_fragment_shader);

        // Retrieve uniform locations for the shader program.
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);

        // Retrieve attribute locations for the shader program.
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aColorLocation = glGetAttribLocation(program, A_COLOR);
    }

    public void setUniform(float[] matrix) {
        glUniformMatrix4fv(uMatrixLocation,1,false,matrix,0);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getColorAttributeLocation() {
        return aColorLocation;
    }
}
