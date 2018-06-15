import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.Scanner;

/**
 *
 * @author Kevin Lorenzo
 * BMPImageHandler Class: Main Class of the project. Validates the flags, the image file and the kernel file.
 */
public class BMPImageHandler {

	private static final String FLAG_CORE = "-core";
	private static final String FLAG_ROTATE = "-rotate";
	private static final String FLAG_RESIZE = "-resize";
	private static final String FLAG_GRAYSCALE = "-grayscale";
	private static final String FLAG_RLE = "-rle";
	private static final String FLAG_KERNEL = "-kernel";
	private static final String FLAG_ALL = "-all";
	private static final String FLAG_HELP = "-help";

	public static void main(String[] args) throws Exception {

		int parametersLength = args.length;

		String msgInvalidParameters = "\n>>> ERROR : Please, execute \"java BMPImageHandler -help\" to get more information.";
		String msgInvalidImage = "\n>>> ERROR : Invalid Image File. The image file doesn't exists or is not a BMP image.";
		String msgInvalidKernel = "\n>>> ERROR : Invalid Kernel File. The kernel file doesn't exists or its content is not valid.";

		String flag = "", imageFileName = "", kernelFileName = "";

		// Parameters Validation: Checks Flag, Image File and Kernel File.

		boolean isAllValid = false;

		if(parametersLength == 1) {
			flag = args[0];
			if(flag.equals(FLAG_HELP)) {
				isAllValid = true;
			}
		} else if(parametersLength == 2) {
			flag = args[0];
			imageFileName = args[1];
			if(flag.equals(FLAG_CORE) || flag.equals(FLAG_ROTATE) || flag.equals(FLAG_RESIZE) || flag.equals(FLAG_GRAYSCALE) || flag.equals(FLAG_RLE)) {
				if(!BMPImageHandler.isValidBMPImageFile(imageFileName)) {
					System.out.print(msgInvalidImage);
				} else {
					isAllValid = true;
				}
			}
		} else if(parametersLength == 3) {
			flag = args[0];
			kernelFileName = args[1];
			imageFileName = args[2];
			if(flag.equals(FLAG_KERNEL) || flag.equals(FLAG_ALL)) {
				if(!BMPImageHandler.isValidBMPImageFile(imageFileName)) {
					System.out.print(msgInvalidImage);
				} else if(!BMPImageHandler.isValidKernelFile(kernelFileName)) {
					System.out.print(msgInvalidKernel);
				} else {
					isAllValid = true;
				}
			}
		}

		if(!isAllValid) {
			System.out.println(msgInvalidParameters);
			System.exit(1);
		}

		// Successful Validation: Executes the required operation.

		BMPImage bmpImage = null;
		ArrayList<String> imageFiles = null;
		if(!flag.equals(FLAG_HELP)) {
			bmpImage = new BMPImage(imageFileName);
			imageFiles = new ArrayList<>();
			imageFiles.add(imageFileName); // The Original Image
		}

		switch(flag) {
			case FLAG_CORE:
				Thread thread1 = new Thread(new BMPCore(bmpImage));
				thread1.start();
				thread1.join();
				imageFiles.add(bmpImage.getFileNameWithoutExtension() + "Red.bmp");
				imageFiles.add(bmpImage.getFileNameWithoutExtension() + "Green.bmp");
				imageFiles.add(bmpImage.getFileNameWithoutExtension() + "Blue.bmp");
				imageFiles.add(bmpImage.getFileNameWithoutExtension() + "Sepia.bmp");
				new BMPViewer(imageFiles).setVisible(true);
				break;
			case FLAG_ROTATE:
				Thread thread2 = new Thread(new BMPRotations(bmpImage));
				thread2.start();
				thread2.join();
				imageFiles.add(bmpImage.getFileNameWithoutExtension() + "HRotation.bmp");
				imageFiles.add(bmpImage.getFileNameWithoutExtension() + "VRotation.bmp");
				new BMPViewer(imageFiles).setVisible(true);
				break;
			case FLAG_RESIZE:
				Thread thread3 = new Thread(new BMPResize(bmpImage));
				thread3.start();
				thread3.join();
				imageFiles.add(bmpImage.getFileNameWithoutExtension() + "Thin.bmp");
				imageFiles.add(bmpImage.getFileNameWithoutExtension() + "Flat.bmp");
				new BMPViewer(imageFiles).setVisible(true);
				break;
			case FLAG_GRAYSCALE:
				Thread thread4 = new Thread(new BMPGrayscale(bmpImage));
				thread4.start();
				thread4.join();
				new BMPViewer(imageFiles).setVisible(true);
				break;
			case FLAG_RLE:
				Thread thread5 = new Thread(new BMPRunLengthEncoding(bmpImage));
				thread5.start();
				thread5.join();
				break;
			case FLAG_KERNEL:
				Thread thread6 = new Thread(new BMPRunKernelFilter(bmpImage, new BMPImageKernel(kernelFileName)));
				thread6.start();
				thread6.join();
				imageFiles.add(bmpImage.getFileNameWithoutExtension() + "Kernel.bmp");
				new BMPViewer(imageFiles).setVisible(true);
				break;
			case FLAG_ALL:

				BMPImageKernel bmpImageKernel = new BMPImageKernel(kernelFileName);

				Thread a = new Thread(new BMPCore(bmpImage));
				Thread b = new Thread(new BMPRotations(bmpImage));
				Thread c = new Thread(new BMPResize(bmpImage));
				Thread d = new Thread(new BMPRunKernelFilter(bmpImage, bmpImageKernel));

				// Only For Grayscale Images
				
				Thread threadGrayscale = new Thread(new BMPGrayscale(bmpImage));
				threadGrayscale.start();
				threadGrayscale.join();

				BMPImage grayscaleImage = new BMPImage(bmpImage.getFileNameWithoutExtension() + "Grayscale.bmp");
				
				Thread e = new Thread(new BMPRunLengthEncoding(grayscaleImage));
				Thread f = new Thread(new BMPRunKernelFilter(grayscaleImage, bmpImageKernel));

				Thread[] threads = new Thread[]{a, b, c, d, e, f};

				for(int i = 0; i < threads.length; i++) {
					threads[i].start();
					threads[i].join();
				}

				imageFiles.add(bmpImage.getFileNameWithoutExtension() + "Red.bmp");
				imageFiles.add(bmpImage.getFileNameWithoutExtension() + "Green.bmp");
				imageFiles.add(bmpImage.getFileNameWithoutExtension() + "Blue.bmp");
				imageFiles.add(bmpImage.getFileNameWithoutExtension() + "Sepia.bmp");
				imageFiles.add(bmpImage.getFileNameWithoutExtension() + "HRotation.bmp");
				imageFiles.add(bmpImage.getFileNameWithoutExtension() + "VRotation.bmp");
				imageFiles.add(bmpImage.getFileNameWithoutExtension() + "Thin.bmp");
				imageFiles.add(bmpImage.getFileNameWithoutExtension() + "Flat.bmp");
				imageFiles.add(bmpImage.getFileNameWithoutExtension() + "Kernel.bmp");

				new BMPViewer(imageFiles).setVisible(true);

				break;
			case FLAG_HELP:
				StringBuilder help = new StringBuilder();
				help.append("\n-----------------------------------------------------------------------------------------\n");
				help.append("BMP Image Handler: To execute a task, use someone of the next commands:");
				help.append("\n-----------------------------------------------------------------------------------------\n\n");
				help.append("java BMPImageHandler -core imageFileName.bmp\n");
				help.append(">>> Creates Red Scale, Green Scale, Blue Scale and Sepia Images.\n\n");
				help.append("java BMPImageHandler -rotate imageFileName.bmp\n");
				help.append(">>> Used to rotate the images horizontally and vertically.\n\n");
				help.append("java BMPImageHandler -resize imageFileName.bmp\n");
				help.append(">>> Used to create Thin Images and Flat Images.\n\n");
				help.append("java BMPImageHandler -grayscale imageFileName.bmp\n");
				help.append(">>> Creates 8 Bits Grayscale Image.\n\n");
				help.append("java BMPImageHandler -rle imageFileName.bmp\n");
				help.append(">>> Used to compress a 8 Bits Grayscale Image using the Algorithm RLE.\n\n");
				help.append("java BMPImageHandler -kernel kernelFileName.txt imageFileName.bmp\n");
				help.append(">>> Used to apply an image kernel. Works for 8 Bits Grayscale and 24 Bits Color.\n\n");
				help.append("java BMPImageHandler -all kernelFileName.txt imageFileName.bmp\n");
				help.append(">>> Executes all the previous tasks.\n");
				help.append("\n-----------------------------------------------------------------------------------------\n\n");
				System.out.println(help.toString());
				break;
		}

	}

	public static boolean isValidBMPImageFile(String imageFileName) {
		File imageFile = new File(imageFileName);
		if(imageFile.exists()) {
			try(FileInputStream imageStream = new FileInputStream(imageFile)) {
				// BMP Image Signature must be "BM" (B = 66 [First Byte] and M = 77 [Second Byte]).
				if(imageStream.read() == 66 && imageStream.read() == 77) {
					return true;
				}
			} catch(IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return false;
	}

	public static boolean isValidKernelFile(String kernelFileName) {
		File kernelFile = new File(kernelFileName);
		try(Scanner kernelScanner = new Scanner(kernelFile)) {
			int numberOfLines = 0;
			while(kernelScanner.hasNextLine()) {
				numberOfLines++;
				if(!Pattern.matches("([-]?[0-9]*\\.?[0-9]+)\\s{1}([-]?[0-9]*\\.?[0-9]+)\\s{1}([-]?[0-9]*\\.?[0-9]+)", kernelScanner.nextLine())) {
					break;
				}
			}
			if(numberOfLines == 3) {
				return true;
			}
		} catch(FileNotFoundException fnfe) {
			return false;
		}
		return false;
	}

}