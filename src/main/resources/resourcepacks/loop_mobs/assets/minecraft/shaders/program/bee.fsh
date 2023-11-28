#version 150

uniform sampler2D DiffuseSampler;

uniform vec4 ColorModulate;

uniform float Time;

in vec2 texCoord;

out vec4 fragColor;

vec3 rgb(int r, int g, int b) {
    return vec3(r,g,b)/255.;
}

float module(vec2 vector) {
    return sqrt(pow(vector.x, 2) + pow(vector.y, 2));
}

void main(){
    vec3 color = (texture(DiffuseSampler, texCoord) * ColorModulate).rgb;

    vec3 yellow = rgb(252, 186, 3);
    vec3 black = vec3(.375);

    color = vec3((color.r+color.g+color.b)/3);

    float speed = .20;

    if (mod(module(texCoord-0.5) - Time*speed, .20) < .125) {
        color = color * yellow;
    } else {
        color = color * black;
    }

    fragColor = vec4(color, 1.0);
}