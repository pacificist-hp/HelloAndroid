#version 310 es
precision mediump image2D;
layout (local_size_x = 1, local_size_y = 16) in;
layout (r32f, binding = 0) uniform image2D in_array;
layout (r32f, binding = 1) uniform image2D out_array;

void main()
{
    ivec2 pos = ivec2(gl_GlobalInvocationID.xy);
    vec4 value = imageLoad(in_array, pos);
    value.x += 5.0f;
    imageStore(out_array, pos, value);
}
