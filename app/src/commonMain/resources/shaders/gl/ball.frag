#version 330
#ifdef GL_ARB_shading_language_420pack
#extension GL_ARB_shading_language_420pack : require
#endif

struct Circle
{
    vec2 center;
    float radius;
    float smoothness;
    vec4 color;
};

layout(binding = 0, std140) uniform CircleUBO
{
    Circle circles[100];
} _18;

flat in int i;
in vec2 uv;
layout(location = 0) out vec4 f_color;

void main()
{
    Circle circle;
    circle.center = _18.circles[i].center;
    circle.radius = _18.circles[i].radius;
    circle.smoothness = _18.circles[i].smoothness;
    circle.color = _18.circles[i].color;
    float dist = distance(uv, circle.center);
    float alpha = smoothstep(circle.radius, circle.radius - circle.smoothness, dist);
    f_color = vec4(circle.color.xyz, circle.color.w * alpha);
}

