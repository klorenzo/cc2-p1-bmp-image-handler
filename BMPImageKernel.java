import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author Kevin Lorenzo
 * BMPImageKernel Class: Used to encapsulate an Image Kernel.
 */
public class BMPImageKernel {

	private float[][] values = new float[3][3];

	public BMPImageKernel(String kernelFileName) {
		try(Scanner scanner = new Scanner(new File(kernelFileName))) {

			while(scanner.hasNextFloat()) {
				for(int y = 0; y < 3; y++) {
					for(int x = 0; x < 3; x++) {
						values[y][x] = scanner.nextFloat();
					}
				}
			}

		} catch(FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}
	}

	public float[][] getValues() {
		return this.values;
	}

}