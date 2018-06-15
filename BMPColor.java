/**
 *
 * @author Kevin Lorenzo
 * BMPColor Class: Used to encapsulate a pixel.
 */
public class BMPColor {

	private int red, green, blue, rgb;

	public BMPColor(int rgb) {
		this.rgb = rgb;
	}

	public BMPColor(int red, int green, int blue) {
		this.rgb = (red<<16) + (green<<8) + (blue<<0);
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public int getRed() {
		return this.red;
	}

	public int getGreen() {
		return this.green;
	}

	public int getBlue() {
		return this.blue;
	}

	public int getRGB() {
		return this.rgb;
	}

}