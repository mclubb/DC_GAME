package com.stlwd.dc_game;

import android.opengl.Matrix;
import android.util.FloatMath;
import android.util.Log;

public class MyUtils {

	public static class Ray {
		public final Point point;
		public final Vector vector;
		
		public Ray(Point point, Vector vector) {
			this.point = point;
			this.vector = vector;
		}

		public static Ray convertNormalized2DPointToRay(float x, float y, float[] invertedMatrix)
		{
			// Normalized Device Coordinates
			final float[] nearPointNDC = { x, y, -1, 1 };
			final float[] farPointNDC = { x, y, 1, 1 };
			
			//World Coordinates
			final float[] nearPointWorld = new float[4];
			final float[] farPointWorld = new float[4];
			
			// To get the world coordinates
			// We must multiply the NDC and invertedMatrix to get the 
			// points in world space
			Matrix.multiplyMV(nearPointWorld, 0, invertedMatrix, 0, nearPointNDC, 0);
			Matrix.multiplyMV(farPointWorld, 0, invertedMatrix, 0, farPointNDC, 0);
			
			// This presents a problem with the "w" variable in that it is now inversed
			// This is known as the perspective divide
			//To undo the divide we divide x, y, z with w
			nearPointWorld[0] /= nearPointWorld[3];
			nearPointWorld[1] /= nearPointWorld[3];
			nearPointWorld[2] /= nearPointWorld[3];
			
			farPointWorld[0] /= farPointWorld[3];
			farPointWorld[1] /= farPointWorld[3];
			farPointWorld[2] /= farPointWorld[3];
			
			Point nearPointRay = new Point(nearPointWorld[0], nearPointWorld[1], nearPointWorld[2]);
			Point farPointRay = new Point(farPointWorld[0], farPointWorld[1], farPointWorld[2]);
			
			return new Ray(nearPointRay, Vector.vectorBetween(nearPointRay, farPointRay));
			
		}
		
		public boolean intersects(Sprite sprite)
		{
			float distance = distanceBetween(sprite.getSphere().center, this);
			Log.d("DISTANCE", distance + "");
			float size = sprite.getSphere().radius;
			return  distance < size;
		}

		private float distanceBetween(Point center, Ray ray) {
			Vector p1 = Vector.vectorBetween(ray.point, center);
			Vector p2 = Vector.vectorBetween(ray.point.translate(ray.vector), center);
			
			float areaOfTriangleTimesTwo = p1.crossProduct(p2).length();
			Log.d("AREA", areaOfTriangleTimesTwo + " ");
			float lengthOfBase = ray.vector.length();
			Log.d("LENGTH", lengthOfBase + " ");
			float distanceFromPointToRay = areaOfTriangleTimesTwo / lengthOfBase;
			
			
			return distanceFromPointToRay;
		}
		

		
	}
	
	public static class Sphere
	{
		public float radius;
		public Point center;
		
		public Sphere(Point center, float radius)
		{
			this.center = center;
			this.radius = radius;
		}
	}
	
	public static class Vector
	{
		public float x = 0f;
		public float y = 0f;
		public float z = 0f;
		public Vector(float x, float y, float z)
		{
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		public static Vector vectorBetween(Point near, Point far)
		{
			return new Vector(far.x - near.x, far.y - near.y, far.z - near.z);
		}
		
		public float length()
		{
			return FloatMath.sqrt(x * x + y * y + z * z);
		}
		
		public Vector crossProduct(Vector other)
		{
			return new Vector((y * other.z) - (z * other.y),
					(z * other.x) - (x * other.z),
					(x * other.y)- (y * other.x));
		}
	}
	
	public static class Point
	{
		float x, y, z;
		public Point(float x, float y, float z)
		{
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		public Point translate(Vector vector)
		{
			return new Point(x + vector.x, y + vector.y, z + vector.z);
		}
	}
	
}
