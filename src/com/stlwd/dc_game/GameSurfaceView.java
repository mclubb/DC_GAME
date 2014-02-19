package com.stlwd.dc_game;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class GameSurfaceView extends GLSurfaceView {

	GameRenderer mRenderer;
	
	public GameSurfaceView(Context context) {
		super(context);
		
		mRenderer = new GameRenderer(context);
		
		setEGLContextClientVersion(2);
		
		setRenderer(mRenderer);
	}

}
