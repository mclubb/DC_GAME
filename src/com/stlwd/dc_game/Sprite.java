package com.stlwd.dc_game;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

public class Sprite {

	int mProgramId;
	
	FloatBuffer mVertexBuffer;
	ShortBuffer mIndexBuffer;
	
	float x, y, z, width, height, depth;
	
	public Sprite() {
		x = 0; y = 0; z= 0; 
		width = 1; height = 1; depth = 0;
		InitGL();
		InitModel();
	}
	
	
	
	public float getX() {
		return x;
	}



	public void setX(float x) {
		this.x = x;
	}



	public float getY() {
		return y;
	}


	public void setY(float y) {
		this.y = y;
	}
	
	public float getZ() {
		return z;
	}



	public void setZ(float z) {
		this.z = z;
	}



	public float getWidth() {
		return width;
	}



	public void setWidth(float width) {
		this.width = width;
	}



	public float getHeight() {
		return height;
	}



	public void setHeight(float height) {
		this.height = height;
	}



	public void Update() {
		
	}
	
	public void Draw(float[] MVPMatrix) {
		GLES20.glUseProgram(mProgramId);
		
		// Position
		int mPositionHandle = GLES20.glGetAttribLocation(mProgramId, "aPosition");
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glVertexAttribPointer(mPositionHandle, 2, GLES20.GL_FLOAT, false, 0, mVertexBuffer);
		
		// Matrix Stuff
		float[] ModelMatrix = new float[16];
		Matrix.setIdentityM(ModelMatrix, 0);
		Matrix.translateM(ModelMatrix, 0, x, y, z);
		Matrix.scaleM(ModelMatrix, 0, width, height, 1);
		
		float[] temp = ModelMatrix.clone();
		Matrix.multiplyMM(ModelMatrix, 0, MVPMatrix, 0, temp, 0);
		int mMatrixHandle = GLES20.glGetUniformLocation(mProgramId, "aMatrix");
		GLES20.glUniformMatrix4fv(mMatrixHandle, 1, false, ModelMatrix, 0);
		
		// Draw the stuff here
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, mIndexBuffer.limit(), GLES20.GL_UNSIGNED_SHORT, mIndexBuffer);
		
		GLES20.glDisableVertexAttribArray(mPositionHandle);
	}
	
	private void InitGL() {
		String vertexShaderCode = "attribute vec4 aPosition;" +
				"uniform mat4 aMatrix;" +
				"void main() {" +
				"gl_Position = aMatrix * aPosition;" +
				"}";
		
		String fragmentShaderCode = "precision mediump float;" +
				"void main() {" +
				"gl_FragColor = vec4(1.0f, 1.0f, 1.0f, 1.0f);" +
				"}";
		
		int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
		GLES20.glShaderSource(vertexShader, vertexShaderCode);
		GLES20.glCompileShader(vertexShader);
		
		int[] compileStatus = new int[1];
		GLES20.glGetShaderiv(vertexShader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
		
		if( compileStatus[0] == 0) {
			Log.d("SHADER", "Failed to compile vertex shader");
			GLES20.glDeleteShader(vertexShader);
		}
		
		int fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
		GLES20.glShaderSource(fragmentShader, fragmentShaderCode);
		GLES20.glCompileShader(fragmentShader);
		
		GLES20.glGetShaderiv(fragmentShader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
		if( compileStatus[0] == 0) {
			Log.d("SHADER", "Failed to compile fragment shader");
			GLES20.glDeleteShader(fragmentShader);
		}
		
		mProgramId = GLES20.glCreateProgram();
		GLES20.glAttachShader(mProgramId, vertexShader);
		GLES20.glAttachShader(mProgramId, fragmentShader);
		GLES20.glLinkProgram(mProgramId);
		
		int[] linkStatus = new int[1];
		GLES20.glGetProgramiv(mProgramId, GLES20.GL_LINK_STATUS, linkStatus, 0);
		
		if( linkStatus[0] == 0 ) {
			Log.d("PROGRAM", "Failed to link program");
			GLES20.glDeleteProgram(mProgramId);
		}
	}
	
	private void InitModel() {
		float w = 1/2;
		float h = 1/2;
		float[] vertices = {-w, h, -w, -h, w, -h, w, h};
		short[] indices = {0,1,2,0,2,3};
		mVertexBuffer = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		mVertexBuffer.put(vertices).position(0);
		
		mIndexBuffer = ByteBuffer.allocateDirect(vertices.length * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
		mIndexBuffer.put(indices).position(0);
	}
	
}
