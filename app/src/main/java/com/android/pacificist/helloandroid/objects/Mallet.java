package com.android.pacificist.helloandroid.objects;

import com.android.pacificist.helloandroid.data.VertexArray;
import com.android.pacificist.helloandroid.programs.ColorShaderProgram;

import static android.opengl.GLES20.*;
import static com.android.pacificist.helloandroid.Constants.*;

public class Mallet {
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT
            + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private static final float[] VERTEX_DATA = {
            // Order of coordinates: X, Y, R, G, B
            0.0f, -0.4f, 0f, 0f, 1f,
            0.0f, 0.4f, 1.0f, 0f, 0f
    };

    private final VertexArray vertexArray;

    public Mallet() {
        vertexArray = new VertexArray(VERTEX_DATA);
    }

    public void bindData(ColorShaderProgram colorShaderProgram) {
        vertexArray.setVertexAttribPointer(0
                ,colorShaderProgram.getPositionAttributeLocation()
                ,POSITION_COMPONENT_COUNT
                ,STRIDE);

        vertexArray.setVertexAttribPointer(POSITION_COMPONENT_COUNT
                ,colorShaderProgram.getColorAttributeLocation()
                ,COLOR_COMPONENT_COUNT
                ,STRIDE);
    }

    public void draw() {
        glDrawArrays(GL_POINTS, 0 ,2);
    }
}
