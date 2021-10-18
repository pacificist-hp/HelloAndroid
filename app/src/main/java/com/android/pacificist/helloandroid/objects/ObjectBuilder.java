package com.android.pacificist.helloandroid.objects;

import java.util.ArrayList;
import java.util.List;

import static android.opengl.GLES20.*;
import static com.android.pacificist.helloandroid.util.Geometry.*;

public class ObjectBuilder {

    interface DrawCommand {
        void draw();
    }

    static class GeneratedData {
        final float[] vertexData;
        final List<DrawCommand> drawCommandList;

        public GeneratedData(float[] vertexData, List<DrawCommand> drawCommandList) {
            this.vertexData = vertexData;
            this.drawCommandList = drawCommandList;
        }
    }

    private final List<DrawCommand> drawList = new ArrayList<>();

    public static final int FLOATS_PRE_VERTEX = 3;
    private final float[] vertexData;
    private int offset = 0;

    private ObjectBuilder(int sizeInVertex) {
        vertexData = new float[sizeInVertex * FLOATS_PRE_VERTEX];
    }

    public GeneratedData build() {
        return new GeneratedData(vertexData, drawList);
    }

    /**
     * 计算圆柱体顶部顶点数量
     * 一个圆柱体的顶部是一个用三角形扇构造的圆;它有一个顶点在圆心，围着圆的每个点都有一个顶点，并且围着圆的第一个顶点要重复两次才能使圆闭合
     *
     * @param numPoints
     * @return
     */
    private static int sizeOfCircleInVertices(int numPoints) {
        return 1 + (numPoints + 1);
    }

    /**
     * 计算圆柱体侧面顶点的数量
     * 一个圆柱体的侧面是一个卷起来的长方形，由一个三角形带构造，围着顶部圆的每个点都需要两个顶点，并且前两个顶点要重复两次才能使这个管闭合
     *
     * @param numPoints
     * @return
     */
    private static int sizeOfOpenCylinderInVertices(int numPoints) {
        return (numPoints + 1) * 2;
    }

    /**
     * 生成一个冰球
     *
     * @param puck
     * @param numPoints
     * @return
     */
    static GeneratedData createPuck(Cylinder puck, int numPoints) {
        int size = sizeOfCircleInVertices(numPoints)
                + sizeOfOpenCylinderInVertices(numPoints);

        ObjectBuilder builder = new ObjectBuilder(size);

        Circle puckTop = new Circle(
                puck.center.translateY(puck.height / 2), puck.radius);

        builder.appendCircle(puckTop, numPoints);
        builder.appendOpenCylinder(puck, numPoints);

        return builder.build();
    }

    /**
     * 生成一个木槌
     * 手柄的高度占整体高度的75%，基部高度占整体高度的25%。手柄宽度占整体宽度的1/3。
     *
     * @param center
     * @param radius
     * @param height
     * @param numPoints
     * @return
     */
    static GeneratedData createMallet(Point center, float radius, float height, int numPoints) {
        int size = sizeOfCircleInVertices(numPoints) * 2
                + sizeOfOpenCylinderInVertices(numPoints) * 2;

        ObjectBuilder builder = new ObjectBuilder(size);

        //First, generate the mallet base.
        float baseHeight = height * 0.25f;

        Circle baseCircle = new Circle(
                center.translateY(-baseHeight), radius);
        Cylinder baseCylinder = new Cylinder(
                baseCircle.center.translateY(-baseHeight / 2f), radius, baseHeight);

        builder.appendCircle(baseCircle, numPoints);
        builder.appendOpenCylinder(baseCylinder, numPoints);

        //Second, generate the mallet handle.
        float handleHeight = height * 0.75f;
        float handleRadius = radius / 3f;

        Circle handleCircle = new Circle(center.translateY(height / 2f), handleRadius);
        Cylinder handleCylinder = new Cylinder(handleCircle.center.translateY(-handleHeight / 2f), handleRadius, handleHeight);

        builder.appendCircle(handleCircle, numPoints);
        builder.appendOpenCylinder(handleCylinder, numPoints);

        return builder.build();
    }

    private void appendCircle(Circle circle, int numPoints) {
        final int startVertex = offset / FLOATS_PRE_VERTEX;
        final int numVertex = sizeOfCircleInVertices(numPoints);

        //center point of fan
        vertexData[offset++] = circle.center.x;
        vertexData[offset++] = circle.center.y;
        vertexData[offset++] = circle.center.z;

        // Fan around center point. <= is used because we want to
        // generate the point at the starting angle twice to complete the fan.
        for (int i = 0; i < numPoints; i++) {
            float radiusInAngles =  (i / (float)numPoints) * ((float) Math.PI * 2f);

            vertexData[offset++] = circle.center.x
                    + circle.radius * (float) Math.cos(radiusInAngles);
            vertexData[offset++] = circle.center.y;
            vertexData[offset++] = circle.center.z
                    + circle.radius * (float) Math.sin(radiusInAngles);
        }

        drawList.add(() -> glDrawArrays(GL_TRIANGLE_FAN, startVertex, numVertex));
    }

    private void appendOpenCylinder(Cylinder cylinder, int numPoints) {
        final int startVertex = offset / FLOATS_PRE_VERTEX;
        final int numVertices = sizeOfOpenCylinderInVertices(numPoints);
        final float yStart = cylinder.center.y - (cylinder.height / 2f);
        final float yEnd = cylinder.center.y + (cylinder.height / 2f);

        for (int i = 0; i <= numPoints; i++) {
            float angleInRadians = (i / (float) numPoints) * ((float) Math.PI * 2f);

            float xPosition = cylinder.center.x
                    + cylinder.radius * (float) Math.cos(angleInRadians);
            float zPosition = cylinder.center.z
                    + cylinder.radius * (float) Math.sin(angleInRadians);

            vertexData[offset++] = xPosition;
            vertexData[offset++] = yStart;
            vertexData[offset++] = zPosition;

            vertexData[offset++] = xPosition;
            vertexData[offset++] = yEnd;
            vertexData[offset++] = zPosition;
        }

        drawList.add(() -> glDrawArrays(GL_TRIANGLE_STRIP, startVertex, numVertices));
    }
}
