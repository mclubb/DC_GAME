package com.stlwd.dc_game;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;

public class GameRenderer implements Renderer{

	Context mContext;
	
	float[] mProjectionMatrix = new float[16];
	float[] mViewMatrix = new float[16];
	float[] mMVPMatrix = new float[16];
	
	Game game;
	
	public GameRenderer(Context context) {
		mContext = context;
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		
		Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 5, 0, 0, 0, 0, 1, 0);
		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
		
		// Update
		game.Update();
		
		// Draw
		game.Draw(mMVPMatrix);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
		float ratio = (float)width/height;
		
		Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 1, 20);
		
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		GLES20.glClearColor(100.0f/255.0f, 137.0f/255.0f, 249.0f/255.0f, 1.0f);
		game = new Game(mContext);
	}

}
