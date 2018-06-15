import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author Kevin Lorenzo
 * BMPGrayscale Class: Creates 8 Bits Grayscale Image.
 */
public class BMPGrayscale implements Runnable {

	private BMPImage bmpImage;

	public BMPGrayscale(BMPImage bmpImage) {
		this.bmpImage = bmpImage;
	}

	@Override
	public void run() {
		this.createGrayScaleImage();
	}

	public void createGrayScaleImage() {
		try(FileOutputStream grayScaleImage = new FileOutputStream(bmpImage.getFileNameWithoutExtension() + "Grayscale.bmp")) {

			// 4 x 256 = 1024 ( Color Table ) Convert the Image From 24 Bit To 8 Bit Grayscale
			for(int index = 0, colorNumber = 0; index < (14 + bmpImage.getSizeInfoHeader() + (4 * 256)); index++) {
				if(index == 28) {
					grayScaleImage.write(8>>0);
					grayScaleImage.write(8>>8);
					index += 1;
				} else if(index >= (14 + bmpImage.getSizeInfoHeader())) {
					// Color Number Starts From 0 to 255 ( BMP 8 Bits Format Only Allows 256 Colors )
					grayScaleImage.write(colorNumber);
					grayScaleImage.write(colorNumber);
					grayScaleImage.write(colorNumber);
					grayScaleImage.write(0);
					index += 3;
					colorNumber++;
				} else {
					grayScaleImage.write(bmpImage.getImageBytes()[index]);
				}
			}

			// Operation To Get Grayscale Colors
			for(int y = 0; y < bmpImage.getHeight(); y++) {
				for(int x = 0; x < bmpImage.getWidth(); x++) {
					int grayScalePixel = (int)((bmpImage.getImagePixelsMatrix()[y][x].getRed() * 0.299) + 
						(bmpImage.getImagePixelsMatrix()[y][x].getGreen() * 0.587) + (bmpImage.getImagePixelsMatrix()[y][x].getBlue() * 0.114));
					grayScaleImage.write( Math.min( Math.max( ((int)grayScalePixel), 0 ) , 255) );
				}
			}

		} catch(IOException ioe) {
			ioe.printStackTrace();
		}

	}

}