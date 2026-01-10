#version 440

uniform sampler2D DiffuseSampler;
uniform float Intensity;
uniform float Time;

in vec2 texCoord;
out vec4 fragColor;

float noise(vec2 p) {
    return fract(sin(dot(p, vec2(12.9898, 78.233))) * 43758.5453);
}

void main() {
    vec4 base = texture(DiffuseSampler, texCoord);

    if (Intensity <= 0.001) {
        fragColor = base;
        return;
    }

    vec2 center = texCoord - 0.5;
    float dist = length(center);
    float edge = smoothstep(0.15, 0.45, dist);

    float n = noise(texCoord * 500.0 + Time * 5.0);
    float strength = edge * Intensity;

    vec3 noisy = mix(base.rgb, vec3(n), strength);;
    fragColor = vec4(noisy, base.a);
}
