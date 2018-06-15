import java.io.FileInputStream;
import java.io.IOException;

/**
 *
 * @author Kevin Lorenzo
 * BMPImage Class: Used to encapsulate a BMP Image. And also provides several methods to get image properties.
 */
public class BMPImage {

	private String fileNameWithoutExtension;

	private int headersSize, sizeInfoHeader, width, height, bitCount;

	private byte[] imageBytes;

	private BMPColor[][] imagePixelsMatrix;

	public BMPImage(String imageFileName) {
		try(FileInputStream imageStream = new FileInputStream(imageFileName)) {

			this.imageBytes = new byte[imageStream.available()];
			this.fileNameWithoutExtension = imageFileName.substring(0, imageFileName.length() - 4);

			imageStream.read(this.imageBytes);

			// Size of Info Header [ 4 Bytes ( Byte 15 - Byte 18 ) ]
			this.sizeInfoHeader = (((int)this.imageBytes[17]&0xff)<<24) | (((int)this.imageBytes[16]&0xff)<<16) | (((int)this.imageBytes[15]&0xff)<<8) | ((int)this.imageBytes[14]&0xff);
			// Image Width in Pixels [ 4 Bytes ( Byte 19 - Byte 22 ) ]
			this.width = (((int)this.imageBytes[21]&0xff)<<24) | (((int)this.imageBytes[20]&0xff)<<16) | (((int)this.imageBytes[19]&0xff)<<8) | ((int)this.imageBytes[18]&0xff);
			// Image Height in Pixels [ 4 Bytes ( Byte 23 - Byte 26 ) ]
			this.height = (((int)this.imageBytes[25]&0xff)<<24) | (((int)this.imageBytes[24]&0xff)<<16) | (((int)this.imageBytes[23]&0xff)<<8) | ((int)this.imageBytes[22]&0xff);
			// Image Bit Count [ 2 Bytes ( Byte 28 - Byte 29 ) ]
			this.bitCount = (((int)this.imageBytes[29]&0xff)<<8) | ((int)this.imageBytes[28]&0xff);

			// BMP Image Headers (14 + [ Size of Info Header, Normally 40 or 124 ] ) = 54 or 138 in 24 Bits Format
			// BMP Image Headers (14 + [ Size of Info Header, Normally 40 or 124 ] + [ Color Table 4x256 ] ) = 54 or 138 + 1024 in 8 Bits Format
			this.headersSize = this.bitCount == 24 ? (14 + this.sizeInfoHeader) : (14 + this.sizeInfoHeader + (4 * 256));

			// Image Pixels Matrix: Contains All The Pixels Of The Image
			this.imagePixelsMatrix = new BMPColor[this.height][this.width];

			if(this.bitCount == 24) {

				for(int y = 0, byteIndex = this.headersSize; y < this.height; y++) {
					for(int x = 0; x < this.width; x++, byteIndex += 3) {
						int rValue = (int)this.imageBytes[byteIndex + 2] & 0xff;
						int gValue = (int)this.imageBytes[byteIndex + 1] & 0xff;
						int bValue = (int)this.imageBytes[byteIndex + 0] & 0xff;
						this.imagePixelsMatrix[y][x] = new BMPColor(rValue, gValue, bValue);
					}
				}

			} else if(this.bitCount == 8) {

				for(int y = 0, byteIndex = this.headersSize; y < this.height; y++) {
					for(int x = 0; x < this.width; x++, byteIndex++) {
						this.imagePixelsMatrix[y][x] = new BMPColor((int)this.imageBytes[byteIndex] & 0xff);
					}
				}

			}

		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public String getFileNameWithoutExtension() {
		return this.fileNameWithoutExtension;
	}

	public int getHeadersSize() {
		return this.headersSize;
	}

	public int getSizeInfoHeader() {
		return this.sizeInfoHeader;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public int getBitCount() {
		return this.bitCount;
	}

	public byte[] getImageBytes() {
		return this.imageBytes;
	}

	public BMPColor[][] getImagePixelsMatrix() {
		return this.imagePixelsMatrix;
	}

}