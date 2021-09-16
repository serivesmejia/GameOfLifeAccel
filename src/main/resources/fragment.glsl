#version 330

in  vec2 outTexCoord;
out vec4 fragColor;

uniform sampler2D texture_sampler;

void main() {
    vec4 color = texture(texture_sampler, outTexCoord);
    fragColor = vec4(color.r, color.r, color.r, 1.0f); // copy single channel from texture to rgba
}