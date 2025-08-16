#version 300 es

precision highp float;

layout(location = 0) in vec2 a_pos;

out vec2 uv;

void main() {
    uv = a_pos * 0.5 + 0.5;
    gl_Position = vec4(a_pos, 0.0, 1.0);
}