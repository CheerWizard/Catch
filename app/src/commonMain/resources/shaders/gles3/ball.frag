#version 300 es
precision mediump float;
precision highp int;

struct Circle
{
    highp vec2 center;
    highp float radius;
    highp float smoothness;
    highp vec4 color;
};

layout(std140) uniform CircleUBO
{
    Circle circles[100];
} _18;

flat in mediump int i;
in highp vec2 uv;
layout(location = 0) out highp vec4 f_color;

void main()
{
    Circle circle;
    circle.center = _18.circles[i].center;
    circle.radius = _18.circles[i].radius;
    circle.smoothness = _18.circles[i].smoothness;
    circle.color = _18.circles[i].color;
    highp float dist = distance(uv, circle.center);
    highp float alpha = smoothstep(circle.radius, circle.radius - circle.smoothness, dist);
    f_color = vec4(circle.color.xyz, circle.color.w * alpha);
}

