package com.stlwd.dc_game;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

public class Board extends Sprite{

	public Board(Context context, int ResourceID)
	{
		super(context, ResourceID);
		type = "Board";
	}
	
	
	
	@Override
	public void Draw(float[] MVPMatrix)
	{
		GLES20.glUseProgram(mProgramId);
		
		// Position
		int mPositionHandle = GLES20.glGetAttribLocation(mProgramId, "aPosition");
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glVertexAttribPointer(mPositionHandle, 2, GLES20.GL_FLOAT, false, 0, mVertexBuffer);
		
		int mTextureCoordLocation = GLES20.glGetAttribLocation(mProgramId, "aTexCoord");
		GLES20.glEnableVertexAttribArray(mTextureCoordLocation);
		GLES20.glVertexAttribPointer(mTextureCoordLocation, 2, GLES20.GL_FLOAT, false, 0, mUVBuffer);
		
		int mTextureHandle = GLES20.glGetUniformLocation(mProgramId, "aTextureSample");
		//Activate the texture
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		// Bin dthe texture to the unit
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		
		GLES20.glUniform1i(mTextureHandle, 0);
		
		// Matrix Stuff
		float[] ModelMatrix = new float[16];
		Matrix.setIdentityM(ModelMatrix, 0);
		Matrix.translateM(ModelMatrix, 0, x, y, z);
		Matrix.scaleM(ModelMatrix, 0, width, height, 1);
		
		float[] temp = ModelMatrix.clone();
		Matrix.multiplyMM(ModelMatrix, 0, MVPMatrix, 0, temp, 0);
		int mMatrixHandle = GLES20.glGetUniformLocation(mProgramId, "aMatrix");
		GLES20.glUniformMatrix4fv(mMatrixHandle, 1, false, ModelMatrix, 0);
		
		// Draw the stuff here
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, mIndexBuffer.limit(), GLES20.GL_UNSIGNED_SHORT, mIndexBuffer);
		
		GLES20.glDisableVertexAttribArray(mPositionHandle);
	
	}
}
