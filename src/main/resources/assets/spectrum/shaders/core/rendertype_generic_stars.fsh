#version 150

uniform float GameTime;
uniform vec2 ScreenSize;
uniform mat3 IViewRotMat;
uniform vec3 CameraPos;

in mat4 iProjMat;
in vec4 vertexColor;

out vec4 fragColor;

const int iterations = 17;
const float formuparam = 0.53;
const int volsteps = 20;
const float stepsize = 0.1;
const float speed =   0.0625 * 0.0625;
const float tile =   0.850;
const float brightness = 0.0015;
const float darkmatter = 0.300;
const float distfading = 0.730;
const float saturation = 0.850;

void main() {
    vec2 uv = gl_FragCoord.xy / ScreenSize;// uvs
    vec2 pixelPos = uv * 2 - 1;// normalized pixel position
    vec4 clipSpace = vec4(pixelPos, -1, 1);// Clip space
    vec4 cameraSpace = iProjMat * clipSpace;// Camera/View space
    vec3 dir = normalize(IViewRotMat * cameraSpace.xyz);// World space

    vec3 from = CameraPos * speed + vec3(sin(GameTime) + 1, cos(GameTime) + 1, sin(GameTime) * cos(GameTime * GameTime) + 1);


    fragColor = vec4(vec3(0.), 1.);
}