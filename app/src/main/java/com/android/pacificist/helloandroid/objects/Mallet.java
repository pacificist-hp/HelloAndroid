package com.android.pacificist.helloandroid.objects;

import com.android.pacificist.helloandroid.data.VertexArray;

import com.android.pacificist.helloandroid.programs.ColorShaderProgram;

import java.util.List;

import static com.android.pacificist.helloandroid.objects.ObjectBuilder.*;
import static com.android.pacificist.helloandroid.util.Geometry.*;

public class Mallet {
    public static final int POSITION_COMPONENT_COUNT = 3;

    public final float radius, height;

    private final VertexArray vertexData;

    private final List<DrawCommand> drawCommandList;

    public Mallet(float radius, float height, int numPointsAroundMallet) {
        GeneratedData generatedData =  ObjectBuilder.createMallet(new Point(
                0f, 0f, 0f) ,radius,height, numPointsAroundMallet);

        this.radius = radius;
        this.height = height;

        vertexData = new VertexArray(generatedData.vertexData);
        drawCommandList = generatedData.drawCommandList;
    }

    public void bindData(ColorShaderProgram colorShaderProgram) {
        vertexData.setVertexAttribPointer(0,
                colorShaderProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT, 0);
    }

    public void draw() {
        for (DrawCommand d : drawCommandList) {
            d.draw();
        }
    }
}
