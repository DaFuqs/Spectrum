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

    vec3 from = CameraPos * speed + vec3(GameTime);

    //volumetric rendering
    float s=0.1, fade=1.;
    vec3 v=vec3(0.);
    for (int r=0; r<volsteps; r++) {
        vec3 p=from+s*dir*.5;
        p = abs(vec3(tile)-mod(p, vec3(tile*2.)));// tiling fold
        float pa, a=pa=0.;
        for (int i=0; i<iterations; i++) {
            p=abs(p)/dot(p, p)-formuparam;// the magic formula
            a+=abs(length(p)-pa);// absolute sum of average change
            pa=length(p);
        }
        float dm=max(0., darkmatter-a*a*.001);//dark matter
        a*=a*a;// add contrast
        if (r>6) fade*=1.-dm;// dark matter, don't render near
        //v+=vec3(dm,dm*.5,0.);
        v+=fade;
        v+=vec3(s, s*s, s*s*s*s)*a*brightness*fade;// coloring based on distance
        fade*=distfading;// distance fading

        s+=stepsize;
    }
    v=mix(vec3(length(v)), v, saturation);//color adjust
    fragColor = vec4(v *.01, 1.) * vertexColor;
}