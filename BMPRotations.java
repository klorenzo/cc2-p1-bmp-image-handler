import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author Kevin Lorenzo
 * BMPRotations Class: Used to rotate the images horizontally and vertically.
 */
public class BMPRotations implements Runnable {

	private BMPImage bmpImage;

	public BMPRotations(BMPImage bmpImage) {
		this.bmpImage = bmpImage;
	}

	@Override
	public void run() {
		this.rotateImage180Horizontally();
		this.rotateImage180Vertically();
	}

	public void rotateImage180Horizontally() {
		try(FileOutputStream imageRotated = new FileOutputStream(bmpImage.getFileNameWithoutExtension() + "HRotation.bmp")) {

			for(int index = 0; index < (14 + bmpImage.getSizeInfoHeader()); index++) {
				imageRotated.write(bmpImage.getImageBytes()[index]);
			}

			for(int y = bmpImage.getHeight() - 1; y >= 0; y--) {
				for(int x = 0; x < bmpImage.getWidth(); x++) {				
					imageRotated.write(bmpImage.getImagePixelsMatrix()[y][x].getBlue());
					imageRotated.write(bmpImage.getImagePixelsMatrix()[y][x].getGreen());
					imageRotated.write(bmpImage.getImagePixelsMatrix()[y][x].getRed());
				}
			}

		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void rotateImage180Vertically() {
		try(FileOutputStream imageRotated = new FileOutputStream(bmpImage.getFileNameWithoutExtension() + "VRotation.bmp")) {

			for(int index = 0; index < (14 + bmpImage.getSizeInfoHeader()); index++) {
				imageRotated.write(bmpImage.getImageBytes()[index]);
			}

			for(int y = 0; y < bmpImage.getHeight(); y++) {
				for(int x = bmpImage.getWidth() - 1; x >= 0; x--) {				
					imageRotated.write(bmpImage.getImagePixelsMatrix()[y][x].getBlue());
					imageRotated.write(bmpImage.getImagePixelsMatrix()[y][x].getGreen());
					imageRotated.write(bmpImage.getImagePixelsMatrix()[y][x].getRed());
				}
			}

		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

}