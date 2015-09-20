package renderEngine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import models.RawModel;

/*
This class  stores vertex data into a VAO
*/

public class Loader {
	
	private List<Integer> vaos = new ArrayList<Integer>(); //Stores all of the VAOS
	private List<Integer> vbos = new ArrayList<Integer>(); //Stores all of the VBOS
	private List<Integer> textures = new ArrayList<Integer>(); //Stores all of the textures
	
	public RawModel loadToVao(float[] positions, float[] textureCoords, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, positions); //Stores the data from 'positions' into the first attribute list of the VAO
		storeDataInAttributeList(1, 2, textureCoords); //Stores the data from 'textureCoords' into the second attribute list of the VAO

		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}//Stores the vertex data from 'positions' into attribute list 0 in the vao, then returns a RawModel using the data in the vao
	
	public int loadTexture(String fileName) {
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream("res/" + fileName + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int textureID = texture.getTextureID();
		textures.add(textureID );
		return textureID;
		
	}
	
	private int createVAO() {
		int vaoID = GL30.glGenVertexArrays(); //Creates an empty VAO and stores the ID into the variable vaoID
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID); //Activates the VAO (you need to activate it in order to do anything to it)
		return vaoID;
	}
	
	public void cleanUp() {
		for (int vao : vaos) {
			GL30.glDeleteVertexArrays(vao); //Deletes all the VAOS
		}
		for (int vbo : vbos) {
			GL15.glDeleteBuffers(vbo); //Delets all the VAOS
		}
		for (int texture : textures) {
			GL11.glDeleteTextures(texture);
		}
	} //This method deletes all the VAOS and VBOS once the application is shut down
	
	private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
		int vboID = GL15.glGenBuffers(); //Creates a VBO and stores the ID into the variable vboID
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID); //Activates the VBO (using the VBO type and ID)
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW); //Stores the data from the FloatBuffer into the currently bound VBO, specifying the type and what the data is used for (in this case static, since we won't edit it)
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0); //Puts the currently bound VBO into the specified attribute list of the currently bound VAO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); //Unbinds the currently bound VBO
	}
	
	private void unbindVAO() {
		GL30.glBindVertexArray(0); //Unbind the currently bound VAO	
	}
	
	private void bindIndicesBuffer(int[] indices) {
		int vbo = GL15.glGenBuffers(); //Create an new empty VBO
		vbos.add(vbo); //Add the VBO to the VBOs list
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo); //Activate the VBO
		IntBuffer buffer = storeDataInIntBuffer(indices); //Create an IntBuffer out of the indices data
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW); //Put the indices data into the VBO	
	}
	/*
	 This VBO is different from normal VBOs. Rather than having to
	 manually place them into one of the attribute lists in the VAO,
	 a special "slot" is reserved for them in the VAO. In the line
	 where we bind a VBO specified as a GL_ELEMENT_ARRAY_BUFFER, it
	 automatically adds it to this slot. If we were to unbind it, it
	 would simply remove it from this slot.
	 */
	
	private IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length); 
		buffer.put(data);
		buffer.flip();
		return buffer;
	} //Check FloatBuffer to see comments for individual lines
	
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length); //Creates a new empty float buffer
		buffer.put(data); //Puts the data into the float buffer
		buffer.flip(); //Changes the buffer from expecting to be written to to expecting to be read from
		return buffer;
	} //Data stored in a VBO must be a FloatBuffer - this method converts float[] data into a FloatBuffer
}
