attribute vec3 aPosition;  //顶点位置
attribute vec2 aTexCoor;    //顶点纹理坐标

varying vec2 vTexPosition;

uniform vec3 uPosScale;

void main() {                            		
   gl_Position = vec4(aPosition, 1.0); //根据总变换矩阵计算此次绘制此顶点位置
   gl_Position.x = gl_Position.x * uPosScale.x;
   gl_Position.y = gl_Position.y * uPosScale.y;
   gl_Position.z = gl_Position.z * uPosScale.z;
   vTexPosition = aTexCoor;
}