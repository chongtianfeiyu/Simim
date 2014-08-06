precision mediump float;

uniform sampler2D Sample0;

uniform int Orientation;
uniform int BlurAmount;
uniform float BlurScale;
uniform float BlurStrength;
uniform vec4 uColor;
uniform int isDirectColor;
uniform float uAlpha;
uniform vec2 uTexSize;

varying vec2 vTexPosition;

float Gaussian (float x, float deviation) {
	return (1.0 / sqrt(2.0 * 3.141592 * deviation)) * exp(-((x * x) / (2.0 * deviation)));	
}


void main () {
	float halfBlur = float(BlurAmount) * 0.5;
	vec4 colour = vec4(0.0);
	vec4 texColour = vec4(0.0);
	
	// Gaussian deviation
	float deviation = halfBlur * 0.35;
	deviation *= deviation;
	float strength = 1.0 - BlurStrength;
	
	if (isDirectColor != 1) {
		if (Orientation == 0 ) {
			// Horizontal blur
			for (int i = 0; i < BlurAmount; ++i) {
				float offset = float(i) - halfBlur;
				texColour = texture2D(Sample0, vTexPosition + vec2(offset * BlurScale * uTexSize.x, 0.0)) * Gaussian(offset * strength, deviation);
				colour += texColour;
			}
		} else {
			// Vertical blur
			for (int i = 0; i < BlurAmount; ++i) {
				float offset = float(i) - halfBlur;
				texColour = texture2D(Sample0, vTexPosition + vec2(0.0, offset * BlurScale * uTexSize.y)) * Gaussian(offset * strength, deviation);
				colour += texColour;
			}
	 	}
	} else {
		colour = uColor;
	}
	
	// Apply colour
	gl_FragColor = clamp(colour, 0.0, 1.0);
	gl_FragColor.w = gl_FragColor.w * uAlpha;
}
