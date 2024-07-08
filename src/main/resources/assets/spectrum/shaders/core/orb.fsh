#version 150

uniform float GameTime;
uniform vec2 ScreenSize;
uniform mat3 IViewRotMat;
uniform mat4 ProjMat;
uniform vec3 CameraPos;

in mat3 viewRotMat;
in vec4 vertexColor;

out vec4 fragColor;

const int STEPS = 200;
const float EPSILON = 0.001;

float hitDistance(vec3 point) {
    return length(point - vec3(0.5, 0.5, 0.5)) - 0.5;
}

vec3 estimateNormal(vec3 p) {
    return normalize(vec3(
        hitDistance(vec3(p.x + EPSILON, p.y, p.z)) - hitDistance(vec3(p.x - EPSILON, p.y, p.z)),
        hitDistance(vec3(p.x, p.y + EPSILON, p.z)) - hitDistance(vec3(p.x, p.y - EPSILON, p.z)),
        hitDistance(vec3(p.x, p.y, p.z  + EPSILON)) - hitDistance(vec3(p.x, p.y, p.z - EPSILON))
    ));
}

void main() {
    vec2 uv = gl_FragCoord.xy / ScreenSize;// uvs
    vec2 pixelPos = uv * 2 - 1;// normalized pixel position
    vec4 clipSpace = vec4(pixelPos, -1, 1);// Clip space
    vec4 cameraSpace = inverse(ProjMat) * clipSpace;// Camera/View space
    vec3 dir = normalize(IViewRotMat * cameraSpace.xyz);// World space

    vec3 from = CameraPos;
    vec3 light = vec3(5., 5., 1.);

    float s = 0.1;
    // ray marching
    for (int r=0; r<STEPS; r++) {
        vec3 p=from+s*dir;//*.5;

        float distance = hitDistance(p);

        if (distance < EPSILON) {
            vec3 normal = estimateNormal(p);
            vec3 lightRay = normalize(light - p);

            float tint = dot(normal, lightRay) * 0.3 + 0.7;
            fragColor = vec4(vertexColor.rgb * tint, vertexColor.a);

            return;
        }

        s += distance;

        if (s > 100.0) {
            fragColor = vec4(0.05, 0., 0.1, 0);
            return;
        }
    }

    fragColor = vec4(0.05, 0., 0.1, 0);
}