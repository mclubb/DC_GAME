package com.stlwd.dc_game;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

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
		final float x = e.getX();
		final float y = e.getY();
		
		switch(e.getAction()) {
			case MotionEvent.ACTION_MOVE:
				
				break;
			case MotionEvent.ACTION_DOWN:
				queueEvent(new Runnable() {
					
					@Override
					public void run() {
						GameRenderer.getInstance().handleTouchDown(x, y);
					}
				});
				break;
		}
		
		mPreviousMouseX = x;
		mPreviousMouseY = y;
		
	
		return true;
	}

}
