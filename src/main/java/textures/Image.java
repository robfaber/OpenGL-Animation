package textures;

import java.nio.ByteBuffer;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Image {
	
	private int width;
	private int height;
	private ByteBuffer data;
	private int format;

}
