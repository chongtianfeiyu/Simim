precision mediump float;

uniform int     uColorSelect;
uniform vec4 	uAmbient;
uniform vec4 	uDiffuse;
uniform vec4 	uSpecular;
uniform vec4 	uColor;
uniform float   uAlpha;

uniform sampler2D 	sTexture;//纹理内容数据

varying vec4 	ambient;
varying vec4 	diffuse;
varying vec4 	specular;
varying vec2 	vTexPosition;

void main(){
	if (uColorSelect == 1) {
		gl_FragColor = uColor * (ambient + diffuse + specular);
	} else if (uColorSelect == 0) {
		gl_FragColor = uAmbient * ambient + uDiffuse * diffuse + uSpecular * specular;
	} else {
		gl_FragColor = texture2D(sTexture, vTexPosition) * (ambient + diffuse + specular);
	}
	
    //if (uAlpha >= 0.0) {
        gl_FragColor.a = uAlpha * gl_FragColor.a;
    //}
}