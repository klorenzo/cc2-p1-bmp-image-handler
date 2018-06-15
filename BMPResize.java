import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author Kevin Lorenzo
 * BMPResize Class: Used to create Thin Images and Flat Images.
 */
public class BMPResize implements Runnable {

	private BMPImage bmpImage;

	public BMPResize(BMPImage bmpImage) {
		this.bmpImage = bmpImage;
	}

	@Override
	public void run() {
		this.createThinImage();
		this.createFlatImage();
	}

	public void createThinImage() {
		try(FileOutputStream thinImage = new FileOutputStream(bmpImage.getFileNameWithoutExtension() + "Thin.bmp")) {

			for(int index = 0; index < (14 + bmpImage.getSizeInfoHeader()); index++) {
				if(index == 18) {
					thinImage.write( (bmpImage.getWidth() / 2)>>0  );
					thinImage.write( (bmpImage.getWidth() / 2)>>8  );
					thinImage.write( (bmpImage.getWidth() / 2)>>16 );
					thinImage.write( (bmpImage.getWidth() / 2)>>24 );
					index += 3;
				} else {
					thinImage.write(bmpImage.getImageBytes()[index]);
				}
			}

			for(int y = 0; y < bmpImage.getHeight(); y++) {
				for(int x = 0; x < bmpImage.getWidth(); x += 2) {
					thinImage.write(bmpImage.getImagePixelsMatrix()[y][x].getBlue());
					thinImage.write(bmpImage.getImagePixelsMatrix()[y][x].getGreen());
					thinImage.write(bmpImage.getImagePixelsMatrix()[y][x].getRed());
				}
			}

		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void createFlatImage() {
		try(FileOutputStream flatImage = new FileOutputStream(bmpImage.getFileNameWithoutExtension() + "Flat.bmp")) {

			for(int index = 0; index < (14 + bmpImage.getSizeInfoHeader()); index++) {
				if(index == 22) {
					flatImage.write( (bmpImage.getHeight() / 2)>>0  );
					flatImage.write( (bmpImage.getHeight() / 2)>>8  );
					flatImage.write( (bmpImage.getHeight() / 2)>>16 );
					flatImage.write( (bmpImage.getHeight() / 2)>>24 );
					index += 3;
				} else {
					flatImage.write(bmpImage.getImageBytes()[index]);
				}
			}

			for(int y = 0; y < bmpImage.getHeight(); y += 2) {
				for(int x = 0; x < bmpImage.getWidth(); x++) {
					flatImage.write(bmpImage.getImagePixelsMatrix()[y][x].getBlue());
					flatImage.write(bmpImage.getImagePixelsMatrix()[y][x].getGreen());
					flatImage.write(bmpImage.getImagePixelsMatrix()[y][x].getRed());
				}
			}

		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

}