#version 150

uniform sampler2D DiffuseSampler;

uniform vec4 ColorModulate;

in vec2 texCoord;

out vec4 fragColor;

void main(){
    vec3 color = (texture(DiffuseSampler, texCoord) * ColorModulate).rgb;

    color = vec3((color.r + color.g + color.b)/3);

    fragColor = vec4(color, 1.0);
}