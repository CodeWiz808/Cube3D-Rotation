package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private Vector3f position = new Vector3f(0, 0, 0);
	private float pitch; //How high or low the camera is aimed
	private float yaw; //How much left or right the camera is aimed left or right
	private float roll; //How much the camera is tiled left or right
	
	public Camera() {
		
	}

	public void move() {
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			position.z -= 0.02f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			position.z += 0.02f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			position.x += 0.02f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			position.x -= 0.02f;
		}
	}
	
	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	
}
