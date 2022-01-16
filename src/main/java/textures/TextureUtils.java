package textures;

import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
//import org.lwjgl.opengl.GLContext;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

//import de.matthiasmann.twl.utils.PNGDecoder;
//import de.matthiasmann.twl.utils.PNGDecoder.Format;
//import utils.MyFile;

public class TextureUtils {

//	protected static TextureData decodeTextureFile(MyFile file) {
//		int width = 0;
//		int height = 0;
//		ByteBuffer buffer = null;
//		try {
//			InputStream in = file.getInputStream();
//			PNGDecoder decoder = new PNGDecoder(in);
//			width = decoder.getWidth();
//			height = decoder.getHeight();
//			buffer = ByteBuffer.allocateDirect(4 * width * height);
//			decoder.decode(buffer, width * 4, Format.BGRA);
//			buffer.flip();
//			in.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.err.println("Tried to load texture " + file.getName() + " , didn't work");
//			System.exit(-1);
//		}
//		return new TextureData(buffer, width, height);
//	}
//
//	protected static int loadTextureToOpenGL(TextureData data, TextureBuilder builder) {
//		int texID = GL11.glGenTextures();
//		GL13.glActiveTexture(GL13.GL_TEXTURE0);
//		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texID);
//		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
//		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, data.getWidth(), data.getHeight(), 0, GL12.GL_BGRA,
//				GL11.GL_UNSIGNED_BYTE, data.getBuffer());
//		if (builder.isMipmap()) {
//			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
//			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
//			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
//			if (builder.isAnisotropic() && GLContext.getCapabilities().GL_EXT_texture_filter_anisotropic) {
//				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, 0);
//				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT,
//						4.0f);
//			}
//		} else if (builder.isNearest()) {
//			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
//			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
//		} else {
//			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
//			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
//		}
//		if (builder.isClampEdges()) {
//			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
//			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
//		} else {
//			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
//			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
//		}
//		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
//		return texID;
//	}
	
	public static int loadTexture(Image image) {
		int textureId = GL11.glGenTextures();
		GL20.glBindTexture(GL11.GL_TEXTURE_2D, textureId);

		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		GL20.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
		GL20.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, 0); // To play with when the midmap is transitioned

		GL20.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL20.GL_REPEAT);
		GL20.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL20.GL_REPEAT);

		if (image != null) {
			GL30.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, image.getWidth(), image.getHeight(), 0,
					image.getFormat(), GL11.GL_UNSIGNED_BYTE, image.getData());
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			STBImage.stbi_image_free(image.getData()); // Free the image memory
		}
		return textureId;
	}

	public static Image loadImage(String fileName, boolean flip) {
		URL url = TextureUtils.class.getResource(fileName);
		String file = url.getFile();

		MemoryStack stack = MemoryStack.stackPush();
		IntBuffer w = stack.mallocInt(1);
		IntBuffer h = stack.mallocInt(1);
		IntBuffer comp = stack.mallocInt(1);
		STBImage.stbi_set_flip_vertically_on_load(flip);
		ByteBuffer image = STBImage.stbi_load(file, w, h, comp, 0);
		if (image == null) {
			System.err.println("Failed to load image; " + fileName);
			return null;
		}
		int width = w.get();
		int height = h.get();
		int components = comp.get();
		int format;
		if (components == 3) {
			format = GL11.GL_RGB;
		} else {
			format = GL11.GL_RGBA;
		}
		return new Image(width, height, image, format);
	}
}
