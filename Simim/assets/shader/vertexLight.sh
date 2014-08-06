uniform mat4 uMVPMatrix;
uniform mat4 uMMatrix;
uniform mat4 uNorMatrix;

uniform vec4 uLightLocation;
uniform int  uIsLightOpen;
uniform vec3 uCamera;
uniform float uShininess;

uniform vec4 uLightAmbient;
uniform vec4 uLightDiffuse;
uniform vec4 uLightSpecular;

attribute vec3 aPosition;
attribute vec3 aNormal;

varying vec4 ambient;
varying vec4 diffuse;
varying vec4 specular;


void pointLight() {
	ambient = uLightAmbient;
	vec3 newNormal = (uNorMatrix * vec4(aNormal, 0)).xyz;
	newNormal = normalize(newNormal);

	vec3 eye = uCamera - (uMMatrix * vec4(aPosition, 1)).xyz;
	eye = normalize(eye);
	
	vec3 vp;
	if (uLightLocation[3] > 0.5) {
        vp = uLightLocation.xyz - (uMMatrix * vec4(aPosition, 1)).xyz;
	} else {
		vp = uLightLocation.xyz;
	}
	vp = normalize(vp);
		
	float nDotViewPosition = max(0.0, dot(newNormal, vp));
	//nDotViewPosition = min(1.0, nDotViewPosition);
	diffuse = uLightDiffuse * nDotViewPosition;

	vec3 halfVector = normalize(vp + eye);
	float nDotViewHalfVector = max(0.0, dot(newNormal, halfVector));
	//nDotViewHalfVector = min(1.0, nDotViewHalfVector);
	float powerFactor = pow(nDotViewHalfVector, uShininess);

	specular = uLightSpecular * powerFactor;
}

void main()
{
	gl_Position = uMVPMatrix * vec4(aPosition, 1);
	if (uIsLightOpen == 1) {
		pointLight();
	} else {
		ambient = vec4(0.2, 0.2, 0.2, 0.2);
		diffuse = vec4(0.2, 0.2, 0.2, 0.2);
		specular = vec4(0.6, 0.6, 0.6, 0.6);
	}
}