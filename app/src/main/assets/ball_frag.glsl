#version 300 es

precision highp float;

in vec2 uv;

out vec4 f_color;

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
    vec4 result = vec4(0.0);
    for (int i = 0; i < 100; i++) {
        float dist = distance(uv, circles[i].center);
        float alpha = smoothstep(circles[i].radius, circles[i].radius - circles[i].smoothness, dist);
        vec4 color = vec4(circles[i].color.rgb, circles[i].color.a * alpha);
        result = color + result * (1.0 - color.a);
    }
    f_color = result;
}