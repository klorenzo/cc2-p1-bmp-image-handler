import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author Kevin Lorenzo
 * BMPRunKernelFilter Class: Used to apply an image kernel. Works for 8 Bits Grayscale and 24 Bits Color.
 */
public class BMPRunKernelFilter implements Runnable {

	private BMPImage bmpImage;
	private BMPImageKernel bmpImageKernel;

	public BMPRunKernelFilter(BMPImage bmpImage, BMPImageKernel bmpImageKernel) {
		this.bmpImage = bmpImage;
		this.bmpImageKernel = bmpImageKernel;
	}

	@Override
	public void run() {
		this.applyImageKernel();
	}

	public void applyImageKernel() {
		try(FileOutputStream imageWithKernel = new FileOutputStream(bmpImage.getFileNameWithoutExtension() + "Kernel.bmp")) {

			BMPColor[][] imagePixelsMatrix = bmpImage.getImagePixelsMatrix();
			float[][] kernelValues = bmpImageKernel.getValues();

			// Writes The Image Header

			for(int index = 0; index < bmpImage.getHeadersSize(); index++) {
				imageWithKernel.write((int)bmpImage.getImageBytes()[index]&0xff);
			}

			if(bmpImage.getBitCount() == 24) {

				for(int y = 0; y < bmpImage.getHeight(); y++) {
					for(int x = 0; x < bmpImage.getWidth(); x++) {

						// Get Adjacent Pixels

						BMPColor pixel1 = imagePixelsMatrix[ (y + 1 >= bmpImage.getHeight() ? y: y + 1) ][ (x - 1 <= -1 ? x: x - 1) ];
						BMPColor pixel2 = imagePixelsMatrix[ (y + 1 >= bmpImage.getHeight() ? y: y + 1) ][x];
						BMPColor pixel3 = imagePixelsMatrix[ (y + 1 >= bmpImage.getHeight() ? y: y + 1) ][ (x + 1 >= bmpImage.getWidth() ? x: x + 1) ];

						BMPColor pixel4 = imagePixelsMatrix[y][ (x - 1 <= -1 ? x: x - 1) ];
						BMPColor pixel5 = imagePixelsMatrix[y][x];
						BMPColor pixel6 = imagePixelsMatrix[y][ (x + 1 >= bmpImage.getWidth() ? x: x + 1) ];

						BMPColor pixel7 = imagePixelsMatrix[ (y - 1 <= -1 ? y: y - 1) ][ (x - 1 <= -1 ? x: x - 1) ];
						BMPColor pixel8 = imagePixelsMatrix[ (y - 1 <= -1 ? y: y - 1) ][x];
						BMPColor pixel9 = imagePixelsMatrix[ (y - 1 <= -1 ? y: y - 1) ][ (x + 1 >= bmpImage.getWidth() ? x: x + 1) ];

						// Apply The Image Kernel

						float newPixelRed = (pixel1.getRed() * kernelValues[0][0]) + (pixel2.getRed() * kernelValues[0][1]) + (pixel3.getRed() * kernelValues[0][2]) +
							(pixel4.getRed() * kernelValues[1][0]) + (pixel5.getRed() * kernelValues[1][1]) + (pixel6.getRed() * kernelValues[1][2]) +
							(pixel7.getRed() * kernelValues[2][0]) + (pixel8.getRed() * kernelValues[2][1]) + (pixel9.getRed() * kernelValues[2][2]);

						float newPixelGreen = (pixel1.getGreen() * kernelValues[0][0]) + (pixel2.getGreen() * kernelValues[0][1]) + (pixel3.getGreen() * kernelValues[0][2]) +
							(pixel4.getGreen() * kernelValues[1][0]) + (pixel5.getGreen() * kernelValues[1][1]) + (pixel6.getGreen() * kernelValues[1][2]) +
							(pixel7.getGreen() * kernelValues[2][0]) + (pixel8.getGreen() * kernelValues[2][1]) + (pixel9.getGreen() * kernelValues[2][2]);

						float newPixelBlue = (pixel1.getBlue() * kernelValues[0][0]) + (pixel2.getBlue() * kernelValues[0][1]) + (pixel3.getBlue() * kernelValues[0][2]) +
							(pixel4.getBlue() * kernelValues[1][0]) + (pixel5.getBlue() * kernelValues[1][1]) + (pixel6.getBlue() * kernelValues[1][2]) +
							(pixel7.getBlue() * kernelValues[2][0]) + (pixel8.getBlue() * kernelValues[2][1]) + (pixel9.getBlue() * kernelValues[2][2]);

						imageWithKernel.write( Math.min( Math.max( ((int)newPixelBlue), 0 ) , 255) );
						imageWithKernel.write( Math.min( Math.max( ((int)newPixelGreen), 0 ) , 255) );
						imageWithKernel.write( Math.min( Math.max( ((int)newPixelRed), 0 ) , 255) );

					}
				}

			} else if(bmpImage.getBitCount() == 8) {

				for(int y = 0; y < bmpImage.getHeight(); y++) {
					for(int x = 0; x < bmpImage.getWidth(); x++) {

						// Get Adjacent Pixels

						BMPColor pixel1 = imagePixelsMatrix[ (y + 1 >= bmpImage.getHeight() ? y: y + 1) ][ (x - 1 <= -1 ? x: x - 1) ];
						BMPColor pixel2 = imagePixelsMatrix[ (y + 1 >= bmpImage.getHeight() ? y: y + 1) ][x];
						BMPColor pixel3 = imagePixelsMatrix[ (y + 1 >= bmpImage.getHeight() ? y: y + 1) ][ (x + 1 >= bmpImage.getWidth() ? x: x + 1) ];

						BMPColor pixel4 = imagePixelsMatrix[y][ (x - 1 <= -1 ? x: x - 1) ];
						BMPColor pixel5 = imagePixelsMatrix[y][x];
						BMPColor pixel6 = imagePixelsMatrix[y][ (x + 1 >= bmpImage.getWidth() ? x: x + 1) ];

						BMPColor pixel7 = imagePixelsMatrix[ (y - 1 <= -1 ? y: y - 1) ][ (x - 1 <= -1 ? x: x - 1) ];
						BMPColor pixel8 = imagePixelsMatrix[ (y - 1 <= -1 ? y: y - 1) ][x];
						BMPColor pixel9 = imagePixelsMatrix[ (y - 1 <= -1 ? y: y - 1) ][ (x + 1 >= bmpImage.getWidth() ? x: x + 1) ];

						// Apply The Image Kernel

						float newPixel = (pixel1.getRGB() * kernelValues[0][0]) + (pixel2.getRGB() * kernelValues[0][1]) + (pixel3.getRGB() * kernelValues[0][2]) +
							(pixel4.getRGB() * kernelValues[1][0]) + (pixel5.getRGB() * kernelValues[1][1]) + (pixel6.getRGB() * kernelValues[1][2]) +
							(pixel7.getRGB() * kernelValues[2][0]) + (pixel8.getRGB() * kernelValues[2][1]) + (pixel9.getRGB() * kernelValues[2][2]);

						imageWithKernel.write( Math.min( Math.max( ((int)newPixel), 0 ) , 255) );

					}
				}

			}

		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

}