package org.lwjgl.opengl;

import lombok.Getter;

@Getter
public class DisplayMode {
	private int width, height;

	public DisplayMode(int width, int height) {
		this.width = width;
		this.height = height;
	}

}
