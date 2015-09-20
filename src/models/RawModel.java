package models;

public class RawModel {
	private int vaoID;
	private int vertexCount;
	
	public RawModel(int vaoID, int vertexCount)
	{
		this.vaoID = vaoID; //Assigns the raw model to have a VAO that stores it's information
		this.vertexCount = vertexCount;
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}
}
