precision mediump float;

uniform sampler2D     sTexture;//纹理内容数据
uniform vec4 		uColor;
uniform int 		isDirectColor;
uniform float       uAlpha;

varying vec2 		vTexPosition;

void main(){
	if (isDirectColor == 1) {
		gl_FragColor = uColor;
	} else {
		gl_FragColor = texture2D(sTexture, vTexPosition);
	}
    
    //if (uAlpha >= 0.0) {
        gl_FragColor.a = uAlpha * gl_FragColor.a;
    //}
}