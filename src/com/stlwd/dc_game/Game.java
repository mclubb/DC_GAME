package com.stlwd.dc_game;

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
		LoadContent();
	}
	
	public void Init()
	{
		gameObjects = new ArrayList<Sprite>();
	}
	
	public void LoadContent()
	{
		Board background = new Board(mContext, R.raw.table);
		background.setZ(-1.15f);
		background.setWidth(25.5f);
		background.setHeight(18.0f);
		
		gameObjects.add(background);
		
		Sprite weakness = new Sprite(mContext, R.raw.weakness);
		weakness.setX(-5.25f);
		weakness.setY(5.5f);
		weakness.setZ(-1);
		weakness.setWidth(3.0f);
		weakness.setHeight(4.0f);
		gameObjects.add(weakness);
		
		Sprite deck = new Sprite(mContext, R.raw.black_widows_gauntlets);
		deck.setX(-1.75f);
		deck.setY(5.5f);
		deck.setZ(-1);
		deck.setWidth(3.0f);
		deck.setHeight(4.0f);
		gameObjects.add(deck);
		
		Sprite kick = new Sprite(mContext, R.raw.kick);
		kick.setX(1.75f);
		kick.setY(5.5f);
		kick.setZ(-1);
		kick.setWidth(3.0f);
		kick.setHeight(4.0f);
		gameObjects.add(kick);
		
		Sprite villian = new Sprite(mContext, R.raw.loki);
		villian.setX(5.25f);
		villian.setY(5.5f);
		villian.setZ(-1);
		villian.setWidth(3.0f);
		villian.setHeight(4.0f);
		gameObjects.add(villian);
		
		Sprite card1 = new Sprite(mContext, R.raw.hyperion);
		card1.setX(-6.5f);
		card1.setY(1f);
		card1.setZ(-1);
		card1.setWidth(3.0f);
		card1.setHeight(4.0f);
		gameObjects.add(card1);
		
		Sprite card2 = new Sprite(mContext, R.raw.asgard);
		card2.setX(-3.25f);
		card2.setY(1f);
		card2.setZ(-1);
		card2.setWidth(3.0f);
		card2.setHeight(4.0f);
		gameObjects.add(card2);
		
		Sprite card3 = new Sprite(mContext, R.raw.black_widows_gauntlets);
		card3.setX(0f);
		card3.setY(1f);
		card3.setZ(-1);
		card3.setWidth(3.0f);
		card3.setHeight(4.0f);
		gameObjects.add(card3);
		
		Sprite card4 = new Sprite(mContext, R.raw.abomination);
		card4.setX(3.25f);
		card4.setY(1f);
		card4.setZ(-1);
		card4.setWidth(3.0f);
		card4.setHeight(4.0f);
		gameObjects.add(card4);
		
		Sprite card5 = new Sprite(mContext, R.raw.black_cat);
		card5.setX(6.5f);
		card5.setY(1f);
		card5.setZ(-1);
		card5.setWidth(3.0f);
		card5.setHeight(4.0f);
		gameObjects.add(card5);
		
		// Your Hand
		Sprite hand1 = new Sprite(mContext, R.raw.punch);
		hand1.setX(-6.50f);
		hand1.setY(-3.5f);
		hand1.setZ(-1);
		hand1.setWidth(3.0f);
		hand1.setHeight(4.0f);
		gameObjects.add(hand1);
		
		Sprite hand2 = new Sprite(mContext, R.raw.punch);
		hand2.setX(-3.25f);
		hand2.setY(-3.5f);
		hand2.setZ(-1);
		hand2.setWidth(3.0f);
		hand2.setHeight(4.0f);
		gameObjects.add(hand2);
		
		Sprite hand3 = new Sprite(mContext, R.raw.punch);
		hand3.setX(0.0f);
		hand3.setY(-3.5f);
		hand3.setZ(-1);
		hand3.setWidth(3.0f);
		hand3.setHeight(4.0f);
		gameObjects.add(hand3);
		
		Sprite hand4 = new Sprite(mContext, R.raw.vulnerability);
		hand4.setX(3.25f);
		hand4.setY(-3.5f);
		hand4.setZ(-1);
		hand4.setWidth(3.0f);
		hand4.setHeight(4.0f);
		gameObjects.add(hand4);
		
		Sprite hand5 = new Sprite(mContext, R.raw.vulnerability);
		hand5.setX(6.50f);
		hand5.setY(-3.5f);
		hand5.setZ(-1);
		hand5.setWidth(3.0f);
		hand5.setHeight(4.0f);
		gameObjects.add(hand5);

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
