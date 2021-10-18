package com.android.pacificist.helloandroid.objects;

import com.android.pacificist.helloandroid.data.VertexArray;
import com.android.pacificist.helloandroid.programs.ColorShaderProgram;

import java.util.List;

import static com.android.pacificist.helloandroid.objects.ObjectBuilder.*;
import static com.android.pacificist.helloandroid.util.Geometry.*;

public class Puck {
    public static final int POSITION_COMPONENT_COUNT = 3;

    public float radius, height;

    private VertexArray vertexData;

    private List<ObjectBuilder.DrawCommand> drawCommandList;

    public Puck(float radius, float height, int numPointsAroundPuck) {
        GeneratedData generatedData = ObjectBuilder.createPuck(new Cylinder(
                new Point(0f, 0f, 0f), radius, height), numPointsAroundPuck);

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
