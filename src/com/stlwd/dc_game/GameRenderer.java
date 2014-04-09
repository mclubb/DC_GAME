package com.stlwd.dc_game;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.stlwd.dc_game.MyUtils.Ray;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.util.Log;

public class GameRenderer implements Renderer{

	static GameRenderer _instance;
	Context mContext;
	int height;
	int width;
	
	float[] mProjectionMatrix = new float[16];
	float[] mViewMatrix = new float[16];
	float[] mProjectionViewMatrix = new float[16];
	float[] mInvertedPVMatrix = new float[16];
	
	Game game;
	
	public GameRenderer(Context context) {
		mContext = context;
		
		if(_instance == null)
		{
			_instance = this;
		}
	}
	
	public static GameRenderer getInstance()
	{
		return _instance;
	}
	
	public void handleTouchDown(float x, float y)
	{
		// Get the normalized x and y
		float normal_x = (x / width) * 2 - 1;
		float normal_y = ((y / height) * 2 - 1) * -1;
		
		Log.d("TOUCHED", "X: " + normal_x + " Y: " + normal_y);
		
		// We need to get a Ray object
		Ray ray = Ray.convertNormalized2DPointToRay(normal_x, normal_y, mInvertedPVMatrix);
		
		game.handleTouchDown(ray);
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		
		Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 15, 0, 0, 0, 0, 1, 0);
		Matrix.multiplyMM(mProjectionViewMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
		Matrix.invertM(mInvertedPVMatrix, 0, mProjectionViewMatrix, 0);
		
		// Update
		game.Update();
		
		// Draw
		game.Draw(mProjectionViewMatrix);
		
		
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
		this.width = width;
		this.height = height;
		float ratio = (float)width/height;
		
		Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 1, 20);
		
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		GLES20.glClearColor(100.0f/255.0f, 137.0f/255.0f, 249.0f/255.0f, 1.0f);
		GLES20.glEnable( GLES20.GL_DEPTH_TEST );
		GLES20.glDepthFunc( GLES20.GL_LEQUAL );
		GLES20.glDepthMask( true );
		game = new Game(mContext);
	}

}
