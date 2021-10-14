package com.android.pacificist.helloandroid.programs;

import android.content.Context;

import com.android.pacificist.helloandroid.R;

import static android.opengl.GLES20.*;

public class TextureShaderProgram extends ShaderProgram {
    //Uniform Location
    private int uMatrixLocation;
    private int uTextureUnitLocation;

    private int a_TextureCoordinatesLocation;
    private int a_PositionLocation;

    public TextureShaderProgram(Context context) {
        super(context, R.raw.texture_vertex_shader, R.raw.texture_fragment_shader);

        // Retrieve uniform locations for the shader program.
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);

        // Retrieve attribute locations for the shader program.
        a_PositionLocation = glGetAttribLocation(program, A_POSITION);
        a_TextureCoordinatesLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES);
    }

    //传入矩阵和纹理给Uniform
    public void setUniform(float[] matrix, int textureId) {
        //传入矩阵
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix,0);

        //把活动的纹理单元设置为纹理单元0
        glActiveTexture(GL_TEXTURE0);

        //绑定这个纹理单元
        glBindTexture(GL_TEXTURE_2D, textureId);

        //把被选定的纹理单元传递给片段着色器中的u_TextureUnit
        glUniform1i(uTextureUnitLocation, 0);
    }

    public int getTextureCoordinatesAttributeLocation() {
        return a_TextureCoordinatesLocation;
    }

    public int getPositionAttributeLocation() {
        return a_PositionLocation;
    }
}
