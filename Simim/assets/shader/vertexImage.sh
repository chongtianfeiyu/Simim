attribute vec3 aPosition;  //����λ��
attribute vec2 aTexCoor;    //������������

varying vec2 vTexPosition;

uniform vec3 uPosScale;

void main() {                            		
   gl_Position = vec4(aPosition, 1.0); //�����ܱ任�������˴λ��ƴ˶���λ��
   gl_Position.x = gl_Position.x * uPosScale.x;
   gl_Position.y = gl_Position.y * uPosScale.y;
   gl_Position.z = gl_Position.z * uPosScale.z;
   vTexPosition = aTexCoor;
}