#version 150

uniform sampler2D DiffuseSampler;
uniform float Intensity;
uniform float Time;
uniform float BlockSize;

in vec2 texCoord;
out vec4 fragColor;

float noise(vec2 p) {
    return fract(sin(dot(p, vec2(12.9898, 78.233))) * 43758.5453);
}

void main() {
    vec4 base = texture(DiffuseSampler, texCoord);

    // Query texture resolution
    ivec2 size = textureSize(DiffuseSampler, 0);
    vec2 screenSize = vec2(size);

    vec2 center = texCoord - 0.5;
    float dist = length(center);

    // --- Block UV for localized randomness ---
    vec2 pixel = texCoord * screenSize;
    vec2 block = floor(pixel / BlockSize);
    vec2 blockUV = block / (screenSize / BlockSize);

    // --- Independent CMY inward distortion ---
    float pullC = noise(blockUV * 7.0 + Time * 0.6 + vec2(1.2, 3.4)) * 0.25;
    float pullM = noise(blockUV * 7.0 + Time * 0.6 + vec2(5.6, 7.8)) * 0.25;
    float pullY = noise(blockUV * 7.0 + Time * 0.6 + vec2(9.0, 2.1)) * 0.25;

    // Distorted distances per channel
    float distC = dist - pullC;
    float distM = dist - pullM;
    float distY = dist - pullY;

    // Per‑channel edge fade
    float edgeC = smoothstep(0.15, 0.45, distC);
    float edgeM = smoothstep(0.15, 0.45, distM);
    float edgeY = smoothstep(0.15, 0.45, distY);

    // --- CMY noise ---
    float nC = noise(blockUV * 500.0 + Time * 5.0 + vec2(1.23, 4.56));
    float nM = noise(blockUV * 500.0 + Time * 5.0 + vec2(7.89, 2.34));
    float nY = noise(blockUV * 500.0 + Time * 5.0 + vec2(5.67, 8.90));

    // RGB → CMY
    vec3 cmy = vec3(1.0) - base.rgb;

    // Apply noise with per‑channel edge strength
    cmy.r = mix(cmy.r, nC, edgeC * Intensity);
    cmy.g = mix(cmy.g, nM, edgeM * Intensity);
    cmy.b = mix(cmy.b, nY, edgeY * Intensity);

    // CMY → RGB
    vec3 rgb = vec3(1.0) - cmy;

    fragColor = vec4(rgb, base.a);
}
