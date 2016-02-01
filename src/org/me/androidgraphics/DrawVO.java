package org.me.androidgraphics;

public class DrawVO {
	
	int mVertexBufferObjectId = 0;
	int textureCoordBufferIndex = 0;
    int  indexBufferIndex = 0;    
    int  normalBufferIndex = 0;
    int numOfIndices;
    public float x = 0;
    public float y = 0;
    public float z = 0;
    public float angle = 0;

	/**
	 * @param mVertexBufferObjectId
	 * @param textureCoordBufferIndex
	 * @param indexBufferIndex
	 * @param normalBufferIndex
	 * @param numOfIndices
	 */
	public DrawVO(int mVertexBufferObjectId, int textureCoordBufferIndex,
			int indexBufferIndex, int normalBufferIndex, int numOfIndices) {
		super();
		this.mVertexBufferObjectId = mVertexBufferObjectId;
		this.textureCoordBufferIndex = textureCoordBufferIndex;
		this.indexBufferIndex = indexBufferIndex;
		this.normalBufferIndex = normalBufferIndex;
		this.numOfIndices = numOfIndices;
	}
	/**
	 * @return the mVertexBufferObjectId
	 */
	public int getmVertexBufferObjectId() {
		return mVertexBufferObjectId;
	}
	/**
	 * @return the textureCoordBufferIndex
	 */
	public int getTextureCoordBufferIndex() {
		return textureCoordBufferIndex;
	}
	/**
	 * @return the indexBufferIndex
	 */
	public int getIndexBufferIndex() {
		return indexBufferIndex;
	}
	/**
	 * @return the normalBufferIndex
	 */
	public int getNormalBufferIndex() {
		return normalBufferIndex;
	}
	/**
	 * @return the numOfIndices
	 */
	public int getNumOfIndices() {
		return numOfIndices;
	}
	/**
	 * @param mVertexBufferObjectId the mVertexBufferObjectId to set
	 */
	public void setmVertexBufferObjectId(int mVertexBufferObjectId) {
		this.mVertexBufferObjectId = mVertexBufferObjectId;
	}
	/**
	 * @param textureCoordBufferIndex the textureCoordBufferIndex to set
	 */
	public void setTextureCoordBufferIndex(int textureCoordBufferIndex) {
		this.textureCoordBufferIndex = textureCoordBufferIndex;
	}
	/**
	 * @param indexBufferIndex the indexBufferIndex to set
	 */
	public void setIndexBufferIndex(int indexBufferIndex) {
		this.indexBufferIndex = indexBufferIndex;
	}
	/**
	 * @param normalBufferIndex the normalBufferIndex to set
	 */
	public void setNormalBufferIndex(int normalBufferIndex) {
		this.normalBufferIndex = normalBufferIndex;
	}
	/**
	 * @param numOfIndices the numOfIndices to set
	 */
	public void setNumOfIndices(int numOfIndices) {
		this.numOfIndices = numOfIndices;
	}
	/**
	 * @return the x
	 */
	public float getX() {
		return x;
	}
	/**
	 * @return the y
	 */
	public float getY() {
		return y;
	}
	/**
	 * @return the z
	 */
	public float getZ() {
		return z;
	}
	/**
	 * @return the angle
	 */
	public float getAngle() {
		return angle;
	}
	/**
	 * @param x the x to set
	 */
	public void setX(float x) {
		this.x = x;
	}
	/**
	 * @param y the y to set
	 */
	public void setY(float y) {
		this.y = y;
	}
	/**
	 * @param z the z to set
	 */
	public void setZ(float z) {
		this.z = z;
	}
	/**
	 * @param angle the angle to set
	 */
	public void setAngle(float angle) {
		this.angle = angle;
	}
    
    
    
}
