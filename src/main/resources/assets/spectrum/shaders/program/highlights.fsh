#version 440

uniform sampler2D DiffuseSampler;

in vec2 texCoord;

uniform float BloomThreshold;

out vec4 fragColor;

float Luma(vec3 color) { return dot(color, vec3(0.2126, 0.7152, 0.0722)); }

void main() {
    vec3 diffuseColor = texture(DiffuseSampler, texCoord).rgb;
    float luma = Luma(diffuseColor);

    luma = max(pow((luma) / BloomThreshold, 2.0), 0.0);
    luma = min(luma, 1.0);

    if (luma >= BloomThreshold) {
        fragColor = vec4(diffuseColor * luma, 1.0);
    }
    else {
        fragColor = vec4(vec3(0.0), 1.0);
    }
}