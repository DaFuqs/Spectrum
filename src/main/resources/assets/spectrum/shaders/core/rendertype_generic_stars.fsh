#version 150

#moj_import <matrix.glsl>

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;

uniform float GameTime;
uniform int EndPortalLayers;
uniform mat3 IViewRotMat;

in float vertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;
in vec3 camera;
in vec3 viewRotation;

out vec4 fragColor;

#define iterations 17
#define formuparam 0.53

#define volsteps 20
#define stepsize 0.1

#define zoom   0.800
#define tile   0.850
#define speed  1

#define brightness 0.0015
#define darkmatter 0.300
#define distfading 0.730
#define saturation 0.850


void main()
{
    //get coords and direction
    vec2 resolution = vec2(1440, 900);
    vec3 dir= normalize(vec3(vec2(gl_FragCoord.xy - .5 *resolution.xy)/resolution.y, 1));

    vec3 from= camera;


    //volumetric rendering
    float s=0.1,fade=1.;
    vec3 v=vec3(0.);
    for (int r=0; r<volsteps; r++) {
        vec3 p=from+s*dir;//*.5;
        //p = abs(vec3(tile)-mod(p,vec3(tile*2.))); // tiling fold
        //float pa,a=pa=0.;
        //for (int i=0; i<iterations; i++) {
        //    p=abs(p)/dot(p,p)-formuparam; // the magic formula
        //    a+=abs(length(p)-pa); // absolute sum of average change
        //    pa=length(p);
        //}
        //float dm=max(0.,darkmatter-a*a*.001); //dark matter
        //a*=a*a; // add contrast
        //if (r>6) fade*=1.-dm; // dark matter, don't render near
        ////v+=vec3(dm,dm*.5,0.);
        //v+=fade;
        //v+=vec3(s,s*s,s*s*s*s)*a*brightness*fade; // coloring based on distance
        //fade*=distfading; // distance fading

        float distance = length(p) - 0.25;
        if (distance < 0.0001) {
            fragColor = vec4(1.);
            break;
        }

        //s+=stepsize;
        s+=distance;
        if (s > 100) {
            fragColor = vec4(0., 0., 0., 1.);
            return;
        }
    }
    //v=mix(vec3(length(v)),v,saturation); //color adjust
    //fragColor = vec4(v *.01,1.);

}

