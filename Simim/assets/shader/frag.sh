precision mediump float;

uniform vec4 uColor;
uniform float uAlpha;

void main()                         
{                       
   gl_FragColor = uColor;//����ƬԪ��ɫֵ
   //if (uAlpha >= 0.0) {
       gl_FragColor.a = uAlpha * uColor.a;
   //}
}