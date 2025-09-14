#version 330
#ifdef GL_ARB_shading_language_420pack
#extension GL_ARB_shading_language_420pack : require
#endif
#ifdef GL_ARB_shader_draw_parameters
#extension GL_ARB_shader_draw_parameters : enable
#endif

out int i;
#ifdef GL_ARB_shader_draw_parameters
#define SPIRV_Cross_BaseInstance gl_BaseInstanceARB
#else
uniform int SPIRV_Cross_BaseInstance;
#endif
out vec2 uv;
layout(location = 1) in vec2 a_uv;
layout(location = 0) in vec2 a_pos;

void main()
{
    i = (gl_InstanceID + SPIRV_Cross_BaseInstance);
    uv = a_uv;
    gl_Position = vec4(a_pos, 0.0, 1.0);
}

