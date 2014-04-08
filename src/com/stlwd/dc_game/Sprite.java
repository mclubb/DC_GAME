package com.stlwd.dc_game;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

import com.stlwd.dc_game.MyUtils.Point;
import com.stlwd.dc_game.MyUtils.Sphere;

public class Sprite {

	int mProgramId;
	int textureId;
	Sphere bounding;
	boolean selected;
	String type;
	
	FloatBuffer mVertexBuffer;
	ShortBuffer mIndexBuffer;
	FloatBuffer mUVBuffer;
	String vertexShaderCode = "attribute vec4 aPosition;" +
			"uniform mat4 aMatrix;" +
			"attribute vec2 aTexCoord;" +
			"varying vec2 vTexCoord;" +
			"void main() {" +
			"gl_Position = aMatrix * aPosition;" +
			"vTexCoord = aTexCoord;" +
			"}";
	
	String fragmentShaderCode = "precision mediump float;" +
			"varying vec2 vTexCoord;" +
			"uniform sampler2D aTextureSample;" +
			"void main() {" +
			"gl_FragColor = texture2D( aTextureSample, vTexCoord);" +
			"}";
	
	Context mContext;
	
	public boolean isActive = false;
	
	float x, y, z, width, height, depth;
	
	public Sprite(Context context, int ResourceID) {
		x = 0; y = 0; z= 0; 
		width = 1; height = 1; depth = 0;
		mContext = context;
		selected = false;
		type = "Sprite";
		
		SetupImage(ResourceID);
		InitGL();
		InitModel();
	}
	
	public String Type()
	{
		return type;
	}
	
	public float getX() {
		return x;
	}



	public void setX(float x) {
		this.x = x;
		setSphere();
	}



	public float getY() {
		return y;
	}


	public void setY(float y) {
		this.y = y;
		setSphere();
	}
	
	public float getZ() {
		return z;
	}
	
	public void setSphere()
	{
		bounding = new Sphere(new Point(x, y, z), width / 2);
	}
	
	public void setSphere(float x, float y, float z, float radius)
	{
		bounding = new Sphere(new Point(x, y, z), radius);
	}

	public Sphere getSphere()
	{
		return bounding;
	}


	public void setZ(float z) {
		this.z = z;
		setSphere();
	}



	public float getWidth() {
		return width;
		
	}



	public void setWidth(float width) {
		this.width = width;
		setSphere();
	}



	public float getHeight() {
		return height;
	}



	public void setHeight(float height) {
		this.height = height;
		setSphere();
	}

	public void toggle()
	{
		if( selected )
		{
			selected = false;
		}
		else 
		{
			selected = true;
		}
	}


	public void Update() {
		
	}
	
	public void Draw(float[] MVPMatrix) {
		GLES20.glUseProgram(mProgramId);
		
		// Position
		int mPositionHandle = GLES20.glGetAttribLocation(mProgramId, "aPosition");
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glVertexAttribPointer(mPositionHandle, 2, GLES20.GL_FLOAT, false, 0, mVertexBuffer);
		
		int mTextureCoordLocation = GLES20.glGetAttribLocation(mProgramId, "aTexCoord");
		GLES20.glEnableVertexAttribArray(mTextureCoordLocation);
		GLES20.glVertexAttribPointer(mTextureCoordLocation, 2, GLES20.GL_FLOAT, false, 0, mUVBuffer);
		
		int mTextureHandle = GLES20.glGetUniformLocation(mProgramId, "aTextureSample");
		//Activate the texture
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		// Bin dthe texture to the unit
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		
		GLES20.glUniform1i(mTextureHandle, 0);
		
		// Matrix Stuff
		float[] ModelMatrix = new float[16];
		Matrix.setIdentityM(ModelMatrix, 0);
		
		if( selected )
		{
			setSphere(-(width * 2.5f) / 2, 1, z + 1, (width * 2.5f) / 2);
			Matrix.translateM(ModelMatrix, 0, -(width * 2.5f) / 2, 1, z + 1);
			Matrix.scaleM(ModelMatrix, 0, width * 2.5f, height * 2.5f, 1);
		}
		else
		{
			setSphere();
			Matrix.translateM(ModelMatrix, 0, x, y, z);
			Matrix.scaleM(ModelMatrix, 0, width, height, 1);
		}
		
		
		float[] temp = ModelMatrix.clone();
		Matrix.multiplyMM(ModelMatrix, 0, MVPMatrix, 0, temp, 0);
		int mMatrixHandle = GLES20.glGetUniformLocation(mProgramId, "aMatrix");
		GLES20.glUniformMatrix4fv(mMatrixHandle, 1, false, ModelMatrix, 0);
		
		// Draw the stuff here
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, mIndexBuffer.limit(), GLES20.GL_UNSIGNED_SHORT, mIndexBuffer);
		
		GLES20.glDisableVertexAttribArray(mPositionHandle);
	}
	
	private void SetupImage(int ResourceID)
	{
		float[] uvs = {
			0, 0, 0, 1, 1, 1, 1, 0	
		};
		
		mUVBuffer = ByteBuffer.allocateDirect(uvs.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		mUVBuffer.put(uvs).position(0);
		
		
		int[] texturenames = new int[1];
		GLES20.glGenTextures(1, texturenames, 0);
		
		// Read the resource
		Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), ResourceID);
		
		// Bind the texture
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[0]);
		
		// Set filtering
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		
		// Load the bitmap into the texture
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
		
		bitmap.recycle();
		textureId = texturenames[0];
	}
	
	private void InitGL() {
		
		
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
		float w = (float)1/2;
		float h = (float)1/2;
		float[] vertices = {-w, h, -w, -h, w, -h, w, h};
		short[] indices = {0,1,2,0,2,3};
		mVertexBuffer = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		mVertexBuffer.put(vertices).position(0);
		
		mIndexBuffer = ByteBuffer.allocateDirect(vertices.length * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
		mIndexBuffer.put(indices).position(0);
	}
	
}
