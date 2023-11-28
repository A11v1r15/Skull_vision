#version 150

uniform sampler2D DiffuseSampler;

uniform vec4 ColorModulate;

in vec2 texCoord;

out vec4 fragColor;

float module(vec2 vector) {
    return sqrt(pow(vector.x, 2) + pow(vector.y, 2));
}

void main(){
    const float PI = 3.14159265359;

    vec2 coord = texCoord;

    coord += vec2(sin(texCoord.x*PI*10), sin(texCoord.y*PI*10))/(150);

    coord = ((coord-.5)*(1-module((coord - .5)/sqrt(2)/1.5)))+0.5;

    vec3 color = (texture(DiffuseSampler, coord) * ColorModulate).rgb;

    vec3 green = vec3(0.3, 0.7, 0.3);

    color = mix(color, green, .25);

    fragColor = vec4(color, 1.0);
}