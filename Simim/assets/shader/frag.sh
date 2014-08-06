precision mediump float;

uniform vec4 uColor;
uniform float uAlpha;

void main()                         
{                       
   gl_FragColor = uColor;//给此片元颜色值
   //if (uAlpha >= 0.0) {
       gl_FragColor.a = uAlpha * uColor.a;
   //}
}