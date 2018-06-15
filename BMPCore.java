import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author Kevin Lorenzo
 * BMPCore Class: Creates Red Scale, Green Scale, Blue Scale and Sepia Images.
 */
public class BMPCore implements Runnable {

	private BMPImage bmpImage;

	public BMPCore(BMPImage bmpImage) {
		this.bmpImage = bmpImage;
	}

	public void createRedScaleImage() {
		String redScaleImageFileName = bmpImage.getFileNameWithoutExtension() + "Red.bmp";
		try(FileOutputStream redScaleImage = new FileOutputStream(redScaleImageFileName)) {

			for(int index = 0; index < bmpImage.getHeadersSize(); index++) {
				redScaleImage.write(bmpImage.getImageBytes()[index]);
			}

			for(int index = bmpImage.getHeadersSize(); index < bmpImage.getImageBytes().length; index += 3) {
				redScaleImage.write(0);
				redScaleImage.write(0);
				redScaleImage.write(((int)bmpImage.getImageBytes()[index + 2]&0xff));
			}

		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void createGreenScaleImage() {
		String greenScaleImageFileName = bmpImage.getFileNameWithoutExtension() + "Green.bmp";
		try(FileOutputStream greenScaleImage = new FileOutputStream(greenScaleImageFileName)) {

			for(int index = 0; index < bmpImage.getHeadersSize(); index++) {
				greenScaleImage.write(bmpImage.getImageBytes()[index]);
			}

			for(int index = bmpImage.getHeadersSize(); index < bmpImage.getImageBytes().length; index += 3) {
				greenScaleImage.write(0);
				greenScaleImage.write(((int)bmpImage.getImageBytes()[index + 1]&0xff));
				greenScaleImage.write(0);
			}

		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void createBlueScaleImage() {
		String blueScaleImageFileName = bmpImage.getFileNameWithoutExtension() + "Blue.bmp";
		try(FileOutputStream blueScaleImage = new FileOutputStream(blueScaleImageFileName)) {

			for(int index = 0; index < bmpImage.getHeadersSize(); index++) {
				blueScaleImage.write(bmpImage.getImageBytes()[index]);
			}

			for(int index = bmpImage.getHeadersSize(); index < bmpImage.getImageBytes().length; index += 3) {
				blueScaleImage.write(((int)bmpImage.getImageBytes()[index]&0xff));
				blueScaleImage.write(0);
				blueScaleImage.write(0);
			}

		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void createSepiaScaleImage() {
		String sepiaScaleImageFileName = bmpImage.getFileNameWithoutExtension() + "Sepia.bmp";
		try(FileOutputStream sepiaScaleImage = new FileOutputStream(sepiaScaleImageFileName)) {

			for(int index = 0; index < bmpImage.getHeadersSize(); index++) {
				sepiaScaleImage.write(bmpImage.getImageBytes()[index]);
			}

			for(int index = bmpImage.getHeadersSize(); index < bmpImage.getImageBytes().length; index += 3) {

				int rValue = (int)bmpImage.getImageBytes()[index + 2] & 0xff;
				int gValue = (int)bmpImage.getImageBytes()[index + 1] & 0xff;
				int bValue = (int)bmpImage.getImageBytes()[index + 0] & 0xff;

				// Operation To Get Sepia Colors ( Recommended by Microsoft )
				int sepiaBValue = (int)((rValue * 0.272) + (gValue * 0.534) + (bValue * 0.131));
				int sepiaGValue = (int)((rValue * 0.349) + (gValue * 0.686) + (bValue * 0.168));
				int sepiaRValue = (int)((rValue * 0.393) + (gValue * 0.769) + (bValue * 0.189));

				// If Sepia Colors are greater than 255, then sets the pixel with 255.
				sepiaScaleImage.write(Math.min(sepiaBValue, 255));
				sepiaScaleImage.write(Math.min(sepiaGValue, 255));
				sepiaScaleImage.write(Math.min(sepiaRValue, 255));

			}

		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

	@Override
	public void run() {

		this.createRedScaleImage();
		this.createGreenScaleImage();
		this.createBlueScaleImage();
		this.createSepiaScaleImage();

	}

}