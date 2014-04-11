package com.stlwd.dc_game;

import android.content.Context;

public class Card extends Sprite{
	boolean active = false;
	
	public Card(Context context, int ResourceID, float x, float y, float z, float h, float w, float d, float rot)
	{
		super(context, ResourceID, x, y, z, h, w, d, rot);
	}
}
