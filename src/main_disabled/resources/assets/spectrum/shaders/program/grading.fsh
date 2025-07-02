#version 440

uniform sampler2D DiffuseSampler;
uniform sampler2D BloomSampler;

in vec2 texCoord;

uniform float Saturation;
uniform float Rubedo;
uniform float ColorTemperature;
uniform float DesaturateThreshold;

out vec4 fragColor;

#define sat(x) clamp(x,0.,1.)

float Luma(vec3 color) { return dot(color, vec3(0.2126, 0.7152, 0.0722)); }

// from https://github.com/trevorvanhoof/ColorGrading/blob/master/grading.glsl
vec3 colorFromKelvin(float temperature) // photographic temperature values are between 15 to 150
{
    float r, g, b;
    if(temperature <= 66.0)
    {
        r = 1.0;
        g = sat((99.4708025861 * log(temperature) - 161.1195681661) / 255.0);
        if(temperature < 19.0)
        b = 0.0;
        else
        b = sat((138.5177312231 * log(temperature - 10.0) - 305.0447927307) / 255.0);
    }
    else
    {
        r = sat((329.698727446 / 255.0) * pow(temperature - 60.0, -0.1332047592));
        g = sat((288.1221695283  / 255.0) * pow(temperature - 60.0, -0.0755148492));
        b = 1.0;
    }
    return vec3(r, g, b);
}

vec3 LinearToSRGB(vec3 rgb)
{
    rgb=max(rgb,vec3(0,0,0));
    return max(1.055*pow(rgb,vec3(0.416666667))-0.055,0.0);
}

float max3(vec3 v) {
    return max (max (v.x, v.y), v.z);
}

void main(){
	vec4 diffuseColor = texture(DiffuseSampler, texCoord);
    vec3 colorCorrect = vec3(diffuseColor.rgb);

    float luma = Luma(colorCorrect);
    float saturationGradient = clamp(pow(luma + DesaturateThreshold, 20) + log(luma + 1) + 0.05, 0.0, 1.0);

    colorCorrect = mix(vec3(luma), colorCorrect, Saturation);
    colorCorrect = colorCorrect * (vec3(1.0) / colorFromKelvin(ColorTemperature));
    colorCorrect = mix(vec3(luma), colorCorrect, saturationGradient);

    vec3 colorBoost = vec3(colorCorrect);
    colorBoost.r = clamp(pow(colorBoost.r + 1, 1.125) - 0.99, 0.0, 1.0);
    colorBoost.g = clamp(pow(colorBoost.g + 1, 1.075) - 1, 0.0, 1.0);

    colorBoost -= luma / 2.6;

    colorCorrect = mix(colorBoost, colorCorrect, saturationGradient);
    colorCorrect += luma / 15;

    if (luma > 0.4 && luma < 0.95)
            colorCorrect += 0.0125;

    if (luma > 0.275 && luma < 0.95)
        colorCorrect = min(colorCorrect + 0.005, colorCorrect * 1.005);

    colorCorrect.r += Rubedo;

    vec3 bloomColor = texture(BloomSampler, texCoord).rgb;
    float intensity = length(colorCorrect) / 0.65;

    colorCorrect += bloomColor * pow(0.3, intensity);

	fragColor = vec4(colorCorrect, 1.0);
}