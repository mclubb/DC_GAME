package com.stlwd.dc_game;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class GameSurfaceView extends GLSurfaceView {

	GameRenderer mRenderer;
	float mPreviousMouseX, mPreviousMouseY;
	
	public GameSurfaceView(Context context) {
		super(context);
		
		mPreviousMouseX = 0;
		mPreviousMouseY = 0;
		
		mRenderer = new GameRenderer(context);
		
		setEGLContextClientVersion(2);
		
		setRenderer(mRenderer);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		float x = e.getX();
		float y = e.getY();
		
		switch(e.getAction()) {
			case MotionEvent.ACTION_MOVE:
				
				break;
		}
		
		mPreviousMouseX = x;
		mPreviousMouseY = y;
	}

}
