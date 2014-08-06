precision mediump float;

uniform int 	isDirectColor;
uniform vec4 	uAmbient;
uniform vec4 	uDiffuse;
uniform vec4 	uSpecular;
uniform vec4 	uColor;
uniform float   uAlpha;

varying vec4 	ambient;
varying vec4 	diffuse;
varying vec4 	specular;

void main(){
	if (isDirectColor == 1)
	{
		gl_FragColor = uColor * (ambient + diffuse + specular);
	} else {
		gl_FragColor = uAmbient * ambient + uDiffuse * diffuse + uSpecular * specular;
	}
	
	//if (uAlpha >= 0.0) {
        gl_FragColor.a = uAlpha * gl_FragColor.a;
    //}
}