#version 310 es

precision highp float;

layout(location = 0) flat in int i;
layout(location = 1) in vec2 uv;

layout(location = 0) out vec4 f_color;

struct Circle {
    vec2 center;
    float radius;
    float smoothness;
    vec4 color;
};

layout(std140, binding = 0) uniform CircleUBO {
    Circle circles[100];
};

void main() {
    Circle circle = circles[i];
    float dist = distance(uv, circle.center);
    float alpha = smoothstep(circle.radius, circle.radius - circle.smoothness, dist);
    f_color = vec4(circle.color.rgb, circle.color.a * alpha);
}