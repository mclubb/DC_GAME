package com.stlwd.dc_game;

import android.content.Context;

public enum CardType
{
	VILLAIN,
	SUPER_VILLAIN,
	HERO,
	EQUIPMENT, 
	SUPER_POWER,
	LOCATION,
	STARTER,
	WEAKNESS
}

public class Card extends Sprite{
	boolean active = false;
	
	String title;
	String description; // Don't know if I will go into this much detail
	CardType type;
	int victoryPoints;
	int cost;
	
	public Card(Context context, int ResourceID, float x, float y, float z, float h, float w, float d, float rot)
	{
		super(context, ResourceID, x, y, z, h, w, d, rot);
	}
}
