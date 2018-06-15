import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

/**
 *
 * @author Kevin Lorenzo
 * BMPRunLengthEncoding Class: Used to compress a 8 Bits Grayscale Image using the Algorithm RLE.
 */
public class BMPRunLengthEncoding implements Runnable {

	private BMPImage bmpImage;

	public BMPRunLengthEncoding(BMPImage bmpImage) {
		this.bmpImage = bmpImage;
	}

	@Override
	public void run() {
		this.createGrayScaleImageRLE();
	}

	public void createGrayScaleImageRLE() {
		try(FileOutputStream grayScaleImageRLE = new FileOutputStream(bmpImage.getFileNameWithoutExtension() + "RLE.bmp")) {

			for(int index = 0; index < bmpImage.getHeadersSize(); index++) {
				if(index == 30) {
					// Image Compression [ 4 Bytes ( 1 = 8Bit RLE ) ]
					grayScaleImageRLE.write( 1>>0  );
					grayScaleImageRLE.write( 1>>8 );
					grayScaleImageRLE.write( 1>>16 );
					grayScaleImageRLE.write( 1>>24 );
					index += 3;
				} else {
					grayScaleImageRLE.write((int)bmpImage.getImageBytes()[index]&0xff);
				}
			}
			
			for(Byte runOrPixel : this.applyCompressionRLE()) {
				grayScaleImageRLE.write(runOrPixel);
			}

		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public LinkedList<Byte> applyCompressionRLE() {

		LinkedList<Byte> compressedData = new LinkedList<Byte>();

		for(int y = 0; y < bmpImage.getHeight(); y++) {
			int pixel = bmpImage.getImagePixelsMatrix()[y][0].getRGB(), run = 1;
			for(int x = 0; x < bmpImage.getWidth(); x++) {

				if( (bmpImage.getImagePixelsMatrix()[y][x].getRGB() == pixel) && (run < 255) && (x < bmpImage.getWidth() - 1) ) {
					run++;
				} else {
					compressedData.add((byte)run);
					compressedData.add((byte)pixel);
					run = 1;
					pixel = bmpImage.getImagePixelsMatrix()[y][x].getRGB();
				}

				// Two bytes with value of 0 for indicate the end of the line.
				if( (x + 1) == bmpImage.getWidth() ) {
					compressedData.add((byte)0);
					compressedData.add((byte)0);
				}

			}
		}

		compressedData.add((byte)0);
		compressedData.add((byte)1);
		compressedData.add((byte)0);
		compressedData.add((byte)0);

		return compressedData;

	}

}