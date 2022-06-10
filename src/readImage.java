/*
 *  Project 1
*/

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.lang.Object.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.*;

import org.w3c.dom.css.RGBColor;

import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;

/* This class reads the image from the image file to get each image heigh, width and RGB values. 
 * class contains Methods that generate colorCodes and Intensity values for each Image, hen map it create IntensityMatrix and colorCodeMatrix
 * The Two text files are created "intexsity.txt" and "colorcode.txt" containing
 * intensityMatrix[100][25] and colorCodeMatrix[100][64] values.
 */
public class readImage {
	int imageCount = 1;
	double intensityBins[] = new double[25];
	double intensityMatrix[][] = new double[100][25];
	double colorCodeBins[] = new double[64];
	double colorCodeMatrix[][] = new double[100][64];

	/**
	 * Each image is retrieved from the file. The height and width are found for the
	 * image and the getIntensity and getColorCode methods are called and writes the
	 * intensity bin counts in the intensity.txt and colorCodes.txt
	 * 
	 * @return writes the intensity bin counts in the intensity.txt and
	 *         colorCodes.txt
	 */
	public readImage() throws IOException {
		BufferedImage image;
		while (imageCount < 101) {
			int i = imageCount;
			int imageHeight;
			int imageWidth;
			Path rootDir = Paths.get(".").normalize().toAbsolutePath();
			File file = new File(rootDir.toString() + "\\src\\images\\" + i + ".jpg");
			image = ImageIO.read(file);
			imageHeight = image.getHeight();
			imageWidth = image.getWidth();
			getIntensity(image, imageHeight, imageWidth);
			getColorCode(image, imageHeight, imageWidth);
			imageCount++;
		}

		writeIntensity();
		writeColorCode();

	}

	/**
	 * This method we get RGB values for each pixel of perticular image and
	 * substiute the values in Intensity formula to get intensity value for each
	 * pixel. Each index in the intensityBins array are divided into range of 10,
	 * from 0 to 255. The intensity value is added to intensityBin[] according to
	 * what range it belongs. The intensityBins[25] values are then added to
	 * intensitymatrix[100][25] for each image, where image represent rows and
	 * intensityBins columns.
	 * 
	 * @param image
	 * @param height
	 * @param width
	 * @return
	 */
	public void getIntensity(BufferedImage image, int height, int width) {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				Color c = new Color(image.getRGB(j, i));
				int red = c.getRed();
				int green = c.getGreen();
				int blue = c.getBlue();
				double intensity = Math.round(0.299 * red + 0.587 * green + 0.114 * blue);
				if (intensity >= 0 && intensity < 10) {
					intensityBins[0]++;
				} else if (intensity >= 10 && intensity < 20) {
					intensityBins[1]++;
				} else if (intensity >= 20 && intensity < 30) {
					intensityBins[2]++;
				} else if (intensity >= 30 && intensity < 40) {
					intensityBins[3]++;
				} else if (intensity >= 40 && intensity < 50) {
					intensityBins[4]++;
				} else if (intensity >= 50 && intensity < 60) {
					intensityBins[5]++;
				} else if (intensity >= 60 && intensity < 70) {
					intensityBins[6]++;
				} else if (intensity >= 70 && intensity < 80) {
					intensityBins[7]++;
				} else if (intensity >= 80 && intensity < 90) {
					intensityBins[8]++;
				} else if (intensity >= 90 && intensity < 100) {
					intensityBins[9]++;
				} else if (intensity >= 100 && intensity < 110) {
					intensityBins[10]++;
				} else if (intensity >= 110 && intensity < 120) {
					intensityBins[11]++;
				} else if (intensity >= 120 && intensity < 130) {
					intensityBins[12]++;
				} else if (intensity >= 130 && intensity < 140) {
					intensityBins[13]++;
				} else if (intensity >= 140 && intensity < 150) {
					intensityBins[14]++;
				} else if (intensity >= 150 && intensity < 160) {
					intensityBins[15]++;
				} else if (intensity >= 160 && intensity < 170) {
					intensityBins[16]++;
				} else if (intensity >= 170 && intensity < 180) {
					intensityBins[17]++;
				} else if (intensity >= 180 && intensity < 190) {
					intensityBins[18]++;
				} else if (intensity >= 190 && intensity < 200) {
					intensityBins[19]++;
				} else if (intensity >= 200 && intensity < 210) {
					intensityBins[20]++;
				} else if (intensity >= 210 && intensity < 220) {
					intensityBins[21]++;
				} else if (intensity >= 220 && intensity < 230) {
					intensityBins[22]++;
				} else if (intensity >= 230 && intensity < 240) {
					intensityBins[23]++;
				} else if (intensity >= 240 && intensity <= 255) {
					intensityBins[24]++;
				}
			}
		}
		// In the intensity matrix populating the perticular image row with
		// intensityBins values
		for (int i = 0; i < intensityBins.length; i++) {
			intensityMatrix[imageCount - 1][i] = intensityBins[i];
		}
	}

	/**
	 * This method generate the color codes for each pixel of the image. The RGB
	 * values are obtained and converted from integer to binary, each color channel
	 * value is represented in 8 bits and whole colorcode for each pixel is 24
	 * bits.Number of bits by concatinating two Significant digits of binary
	 * representaion of each color to convert color code for each pixel to 6-bits.
	 * The colorcodes fequency is stored in array colorCodeBins[64].The
	 * colorCodematrix[100][64] is populated with ColorCodeBins[] values for each
	 * image.
	 * 
	 * @param image
	 * @param height
	 * @param width
	 * @return
	 */
	public void getColorCode(BufferedImage image, int height, int width) {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {

				Color c = new Color(image.getRGB(j, i));
				int red = c.getRed();
				int green = c.getGreen();
				int blue = c.getBlue();
				// converting integer values of RGB values into binary strings with 8 character
				// presition i.e to get 8 bits for each value
				String redChannelInBinary = String.format("%8s", Integer.toBinaryString(red)).replaceAll(" ", "0");
				String greenChannelInBinary = String.format("%8s", Integer.toBinaryString(green)).replaceAll(" ", "0");
				String blueChannelInBinary = String.format("%8s", Integer.toBinaryString(blue)).replaceAll(" ", "0");
				// stringBuilder class is used for mutable strings so that to append two most
				// significatnt bits to sixBitColorCode.
				StringBuilder sixBitColorCode = new StringBuilder();
				// to conver 24 bits color code value to 6 bit color code value we append two
				// most significant digit of binary representation of RGB values to
				// sixBitColorCode
				sixBitColorCode.append(redChannelInBinary.charAt(0));
				sixBitColorCode.append(redChannelInBinary.charAt(1));
				sixBitColorCode.append(greenChannelInBinary.charAt(0));
				sixBitColorCode.append(greenChannelInBinary.charAt(1));
				sixBitColorCode.append(blueChannelInBinary.charAt(0));
				sixBitColorCode.append(blueChannelInBinary.charAt(1));
				String SixBitColorCodeString = sixBitColorCode.toString();
				// converting binary string to integer
				int SixBitColorCodeValue = Integer.parseInt(SixBitColorCodeString, 2);
				// incrementing the count at the index which is represented ny colorCodeValue
				// for each pixel to create colorCodeBin array.
				colorCodeBins[SixBitColorCodeValue]++;
			}

		}
		// In the colorCodematrix populating the perticular image row with colorCodeBins
		// values
		for (int i = 0; i < colorCodeBins.length; i++) {
			colorCodeMatrix[imageCount - 1][i] = colorCodeBins[i];
		}

	}

	/**
	 * This method writes the contents of the colorCode matrix to a file named //
	 * colorCodes.txt.
	 **/
	public void writeColorCode() {

		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 100; i++)// for each row
		{
			for (int j = 0; j < 64; j++)// for each column
			{
				builder.append(colorCodeMatrix[i][j]);// append to the output string
				if (j < colorCodeMatrix.length - 1)// if this is not the last row element
					builder.append(" ");// then add comma (if you don't like commas you can use spaces)
			}
			builder.append("\n");// append new line at the end of the row
		}

		try {
			Path rootDir = Paths.get(".").normalize().toAbsolutePath();
			File statText = new File(rootDir.toString() + "\\colorCodes.txt");
			Writer w = new BufferedWriter(new FileWriter(statText));
			w.write(builder.toString());// save the string representation of the board
			w.close();
		} catch (IOException e) {
			System.err.println("Problem writing to the file colorCodes.txt");
		}

	}

	/**
	 * This method writes the contents of the intensity matrix to a file called //
	 * intensity.txt
	 **/
	public void writeIntensity() {

		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 100; i++)// for each row
		{
			for (int j = 0; j < 25; j++)// for each column
			{
				builder.append(intensityMatrix[i][j]); // append to the output string
				if (j < intensityMatrix.length - 1) // if this is not the last row element
					builder.append(" "); // then add comma (if you don't like commas you can use spaces)
			}
			builder.append("\n"); // append new line at the end of the row
		}

		try {
			Path rootDir = Paths.get(".").normalize().toAbsolutePath();
			File statText = new File(rootDir.toString() + "\\intensity.txt");
			Writer w = new BufferedWriter(new FileWriter(statText));
			w.write(builder.toString());// save the string representation of the board
			w.close();
		} catch (IOException e) {
			System.err.println("Problem writing to the file intensity.txt");
		}

	}

	/**
	 * Main method of the readImage class. Calls readImage method
	 */
	public static void main(String[] args) throws IOException {
		new readImage();
	}

}
