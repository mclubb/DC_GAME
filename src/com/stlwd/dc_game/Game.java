 package com.stlwd.dc_game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.stlwd.dc_game.MyUtils.Ray;

public class Game {
	Context mContext;
	
	GameState gameState;
	List<Sprite> gameObjects;
	
	
	public Game(Context context)
	{
		mContext = context;
		gameState = GameState.Title;
		Init();
		try {
			LoadContent();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void Init()
	{
		gameObjects = new ArrayList<Sprite>();
	}
	
	public void LoadContent() throws IOException
	{
		InputStream in = mContext.getResources().openRawResource(R.raw.init);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
		
		String line;
		
		while( (line = bufferedReader.readLine()) != null)
		{
			Log.d("LINE", line);
			if( !line.startsWith("#"))
			{
				String[] elements = line.split(",");
				
				String _type = elements[0];
				float _x = Float.parseFloat(elements[1]);
				float _y = Float.parseFloat(elements[2]);
				float _z = Float.parseFloat(elements[3]);
				float _h = Float.parseFloat(elements[4]);
				float _w = Float.parseFloat(elements[5]);
				float _d = Float.parseFloat(elements[6]);
				float _rot = Float.parseFloat(elements[8]);
				int ResourceID =  mContext.getResources().getIdentifier(elements[7], "raw", mContext.getPackageName());
				
				if( _type.equals("board"))
				{
					Board board = new Board(mContext,ResourceID ,_x, _y, _z, _h, _w, _d, _rot);
					gameObjects.add(board);
				}
				
				if( _type.equals("sprite"))
				{
					Sprite sprite = new Sprite(mContext, ResourceID, _x, _y, _z, _h, _w, _d, _rot);
					gameObjects.add(sprite);
				}
			}
		}


	}
	
	public void handleTouchDown(Ray ray)
	{
		Log.d("Touched", "Checking gameobjects");
		for(Sprite s : gameObjects)
		{
			if( s.Type() == "Sprite")
			{
				if( ray.intersects(s))
				{
					Log.d("Touched", "Touched something");
					s.toggle();
					return;
				}
			}
		}
	}
	
	public void Update()
	{
		Iterator<Sprite> it = gameObjects.iterator();
		while(it.hasNext())
		{
			Sprite object = it.next();
			object.Update();
		}
	}
	
	public void Draw(float[] MVPMatrix)
	{
		Iterator<Sprite> it = gameObjects.iterator();
		while(it.hasNext())
		{
			Sprite object = it.next();
			object.Draw(MVPMatrix);
		}
	}
	
	public enum GameState {
		Title,
		Menu,
		CharacterSelect,
		Game,
	}
}
