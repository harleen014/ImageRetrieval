/* Project 1
*/

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.imageio.ImageIO;
import javax.swing.*;

/* This class contains the methods to run user interface displaying 20 images in the bottom pannel to chose a query image, 
 * 4 buttons to go to previous and net page to see more images and colorCode and intensity button to get resuts.
 * The IntensityMatrix and colorCodematrix are read from intensity.txt and colorCode.txt file respectively
 * The image size and RGB values are retrived for calculating Manhattan Distance between query image and all other images.
 * The images are then sorted according to distances and these sorted 100 images are then displaed in the bottom pannel as result.
 */
public class CBIR extends JFrame {

	private JLabel photographLabel = new JLabel(); // container to hold a large
	private JButton[] button; // creates an array of JButtons
	private int[] buttonOrder = new int[101]; // creates an array to keep up with the image order
	private double[] imageSize = new double[100]; // keeps up with the image sizes
	private GridLayout gridLayout1;
	private GridLayout gridLayout2;
	private GridLayout gridLayout3;
	private GridLayout gridLayout4;
	private JPanel panelBottom1;
	private JPanel panelBottom2;
	private JPanel panelTop;
	private JPanel buttonPanel;
	private Double[][] intensityMatrix = new Double[100][25];
	private Double[][] colorCodeMatrix = new Double[100][64];
	LinkedHashMap<Integer, Double> sortedMap; // it stores the sorted retrived images mapped with manhattan distance.
	int picNo = 0;
	int imageCount = 1; // keeps up with the number of images displayed since the first page.
	int pageNo = 1;
	int sortedImagecount = 0;
	boolean isIntensityOrColor = false;
	List<Integer> Key; // list Key stores the key(sorted image nubers) from sortedMap

	public static void main(String args[]) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				CBIR app = new CBIR();
				app.setVisible(true);
			}
		});
	}

	public CBIR() {
		// The following lines set up the interface including the layout of the buttons
		// and JPanels.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// boolean isIntensityOrCorolr = false;
		setTitle("Icon Demo: Please Select an Image");
		panelBottom1 = new JPanel();
		panelBottom2 = new JPanel();
		panelTop = new JPanel();
		buttonPanel = new JPanel();
		gridLayout1 = new GridLayout(4, 5, 5, 5);
		gridLayout2 = new GridLayout(2, 1, 5, 5);
		gridLayout3 = new GridLayout(1, 2, 5, 5);
		gridLayout4 = new GridLayout(2, 3, 5, 5);
		setLayout(gridLayout2);
		panelBottom1.setLayout(gridLayout1);
		panelBottom2.setLayout(gridLayout1);
		panelTop.setLayout(gridLayout3);
		add(panelTop);
		add(panelBottom1);
		photographLabel.setVerticalTextPosition(JLabel.BOTTOM);
		photographLabel.setHorizontalTextPosition(JLabel.CENTER);
		photographLabel.setHorizontalAlignment(JLabel.CENTER);
		photographLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		buttonPanel.setLayout(gridLayout4);
		panelTop.add(photographLabel);

		panelTop.add(buttonPanel);
		JButton previousPage = new JButton("Previous Page");
		JButton nextPage = new JButton("Next Page");
		JButton intensity = new JButton("Intensity");
		JButton colorCode = new JButton("Color Code");
		buttonPanel.add(previousPage);
		buttonPanel.add(nextPage);
		buttonPanel.add(intensity);
		buttonPanel.add(colorCode);
		nextPage.addActionListener(new nextPageHandler());
		previousPage.addActionListener(new previousPageHandler());
		intensity.addActionListener(new intensityHandler());
		colorCode.addActionListener(new colorCodeHandler());
		setSize(1100, 750);
		// this centers the frame on the screen
		setLocationRelativeTo(null);

		button = new JButton[101];
		/*
		 * This for loop goes through the images in the database and stores them as
		 * icons and adds the images to JButtons and then to the JButton array
		 */
		for (int i = 1; i < 101; i++) {
			ImageIcon icon;
			icon = new ImageIcon(getClass().getResource("images/" + i + ".jpg"));

			if (icon != null) {
				button[i] = new JButton(icon);
				// panelBottom1.add(button[i]);
				button[i].addActionListener(new IconButtonHandler(i, icon));
				buttonOrder[i] = i;
			}
		}
		try {
			setImageSize();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		readIntensityFile();
		readColorCodeFile();
		displayFirstPage();
	}

	// ** This method stores the each image size in array imageSize[100] **//
	private void setImageSize() throws IOException {
		// TODO Auto-generated method stub
		BufferedImage image;
		int i = 0;
		while (imageCount < 101) {

			int imageHeight;
			int imageWidth;
			Path rootDir = Paths.get(".").normalize().toAbsolutePath();
			File file = new File(rootDir.toString() + "\\src\\images\\" + imageCount + ".jpg");
			image = ImageIO.read(file);
			imageHeight = image.getHeight();
			imageWidth = image.getWidth();
			imageSize[i] = imageHeight * imageWidth;
			imageCount++;
			i++;
		}

	}

	/*
	 * This method opens the intensity text file containing the intensity matrix
	 * with the histogram bin values for each image. The contents of the matrix are
	 * processed and stored in a two dimensional array called intensityMatrix.
	 */
	public void readIntensityFile() {
		String line = "";
		int lineNumber = 0;
		// a text file is located in source code folder in the project
		Path rootDir = Paths.get(".").normalize().toAbsolutePath();
		File file = new File(rootDir.toString() + "\\intensity.txt");
		try {
			Reader input = new FileReader(file);
			@SuppressWarnings("resource")
			BufferedReader bufferReader = new BufferedReader(input);

			for (lineNumber = 0; lineNumber < 100; lineNumber++)// for each row representing Image
			{
				line = bufferReader.readLine().trim();

				String[] currpoint = line.split("\\s+");

				for (int j = 0; j < 25; j++)// for each column
				{
					intensityMatrix[lineNumber][j] = Double.valueOf(currpoint[j]);
				}

			}

			bufferReader.close();

		} catch (IOException EE) {
			System.out.println("The file intensity.txt does not exist");
		}

	}

	/*
	 * This method opens the color code text file containing the color code matrix
	 * with the histogram bin values for each image. The contents of the matrix are
	 * processed and stored in a two dimensional array called colorCodeMatrix.
	 */
	private void readColorCodeFile() {
		int lineNumber = 0;
		Path rootDir = Paths.get(".").normalize().toAbsolutePath();
		File file = new File(rootDir.toString() + "\\colorCodes.txt");

		try {

			Reader input = new FileReader(file);

			@SuppressWarnings("resource")
			BufferedReader bufferReader = new BufferedReader(input);
			for (lineNumber = 0; lineNumber < 100; lineNumber++) {
				String line = bufferReader.readLine().trim();
				String[] colorCodeOfPixel = line.split("\\s+");
				for (int j = 0; j < 64; j++) {
					colorCodeMatrix[lineNumber][j] = Double.valueOf(colorCodeOfPixel[j]);
				}
			}

			bufferReader.close();

		} catch (IOException EE) {
			System.out.println("The file ColorCodesc.txt does not exist");
		}

	}

	/*
	 * This method displays the first twenty images in the panelBottom. The for loop
	 * starts at number one and gets the image number stored in the buttonOrder
	 * array and assigns the value to imageButNo. The button associated with the
	 * image is then added to panelBottom1. The for loop continues this process
	 * until twenty images are displayed in the panelBottom1
	 */
	private void displayFirstPage() {
		int imageButNo = 0;
		panelBottom1.removeAll();
		for (int i = 1; i < 21; i++) {
			imageButNo = buttonOrder[i];
			panelBottom1.add(button[imageButNo]);
			imageCount++;
		}
		panelBottom1.revalidate();
		panelBottom1.repaint();

	}

	/*
	 * This class implements an ActionListener for each iconButton. When an icon
	 * button is clicked, the image on the the button is added to the
	 * photographLabel and the picNo is set to the image number selected and being
	 * displayed.
	 */
	private class IconButtonHandler implements ActionListener {
		int pNo = 0;
		ImageIcon iconUsed;

		IconButtonHandler(int i, ImageIcon j) {
			pNo = i;
			iconUsed = j; // sets the icon to the one used in the button
		}

		public void actionPerformed(ActionEvent e) {
			photographLabel.setIcon(iconUsed);
			picNo = pNo;
		}

	}

	/*
	 * This class implements an ActionListener for the nextPageButton. The last
	 * image number to be displayed is set to the current image count plus 20. If
	 * the endImage number equals 101, then the next page button does not display
	 * any new images because there are only 100 images to be displayed. The first
	 * picture on the next page is the image located in the buttonOrder array at the
	 * imageCount
	 */
	private class nextPageHandler implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			// isintenstyOrColor is false if Intesity or colorCode method is not clicked
			// the next page button display unsorted ordered images
			if (isIntensityOrColor == false) {
				int imageButNo = 0;
				int endImage = imageCount + 20;
				if (endImage <= 101) {
					panelBottom1.removeAll();
					for (int i = imageCount; i < endImage; i++) {
						imageButNo = buttonOrder[i];
						panelBottom1.add(button[imageButNo]);
						imageCount++;

					}

					panelBottom1.revalidate();
					panelBottom1.repaint();
				}

			}
			// if isintensityOrColor is true then the next page button display the sorted
			// (according to rank) retrived images for the query image/
			else {
				int imageButNo = 0;
				int endImage = sortedImagecount + 20;
				if (endImage <= 100) {
					panelBottom1.removeAll();
					for (int i = sortedImagecount; i < endImage; i++) {
						imageButNo = buttonOrder[Key.get(i) + 1];
						panelBottom1.add(button[imageButNo]);
						sortedImagecount++;

					}

					panelBottom1.revalidate();
					panelBottom1.repaint();
				}

			}

		}

	}

	/*
	 * This class implements an ActionListener for the previousPageButton. The last
	 * image number to be displayed is set to the current image count minus 40. If
	 * the endImage number is less than 1, then the previous page button does not
	 * display any new images because the starting image is 1. The first picture on
	 * the next page is the image located in the buttonOrder array at the imageCount
	 */
	private class previousPageHandler implements ActionListener {

		public void actionPerformed(ActionEvent e) { // isintenstyOrColor is false if Intesity or colorCode method is
														// not clicked
														// the previous page button display unsorted ordered images
			if (isIntensityOrColor == false) {
				int imageButNo = 0;
				int startImage = imageCount - 40;
				int endImage = imageCount - 20;
				if (startImage >= 1) {
					panelBottom1.removeAll();
					/*
					 * The for loop goes through the buttonOrder array starting with the startImage
					 * value and retrieves the image at that place and then adds the button to the
					 * panelBottom1.
					 */
					for (int i = startImage; i < endImage; i++) {
						imageButNo = buttonOrder[i];
						panelBottom1.add(button[imageButNo]);
						imageCount--;
					}

					panelBottom1.revalidate();
					panelBottom1.repaint();
				}
			}
			// if isintensityOrColor is true then the previous page button display the
			// sorted (according to rank) retrived images for the query image/
			else {
				int imageButNo = 0;
				int startImage = sortedImagecount - 40;
				int endImage = sortedImagecount - 20;
				if (startImage >= 0) {
					panelBottom1.removeAll();
					/*
					 * The for loop goes through the buttonOrder array starting with the startImage
					 * value and retrieves the image at that place and then adds the button to the
					 * panelBottom1.
					 */
					for (int i = startImage; i < endImage; i++) {
						imageButNo = buttonOrder[Key.get(i) + 1];
						panelBottom1.add(button[imageButNo]);
						sortedImagecount--;
					}

					panelBottom1.revalidate();
					panelBottom1.repaint();
				}
			}

		}

	}

	/*
	 * This class implements an ActionListener when the user selects the
	 * intensityHandler button. The image number that the user would like to find
	 * similar images for is stored in the variable pic. pic takes the image number
	 * associated with the image selected and subtracts one to account for the fact
	 * that the intensityMatrix starts with zero and not one. The size of the image
	 * is retrieved from the imageSize array. The selected image's intensity bin
	 * values are compared to all the other image's intensity bin values and a score
	 * is determined for how well the images compare. The images are then arranged
	 * from most similar to the least.
	 */
	private class intensityHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) { // isIntensityOrCorolr set to true for handeling next and previous
														// page button according to sorted images
			isIntensityOrColor = true;
			Double manhattanDistance = 0.0;
			LinkedHashMap<Integer, Double> map = new LinkedHashMap<Integer, Double>();
			int compareImage = 0;
			int pic = (picNo - 1);
			int picIntensity = 0;

			for (compareImage = 0; compareImage < 100; compareImage++) { // intializing manhattanDistance for each image
																			// to zero
				manhattanDistance = 0.0;
				// updating distance of query image to itself as zero in map
				if (pic == compareImage) {
					map.put(compareImage, manhattanDistance);

				}
				for (picIntensity = 0; picIntensity < 25; picIntensity++) { // calculateing manhattan distance taking
																			// absolute value as distance cannot be
																			// negative.
					manhattanDistance += Math.abs((intensityMatrix[pic][picIntensity] / imageSize[pic])
							- (intensityMatrix[compareImage][picIntensity] / imageSize[compareImage]));

				}
				// adding each image number with its distance with query image in HashMap
				map.put(compareImage, manhattanDistance);
			}
			// sorting the HashMap according to values
			sortedMap = sortByValue(map);
			// list Key stores the key(sorted image nubers) from sortedMap
			Key = new ArrayList<Integer>(sortedMap.keySet());
			// display all 100 sorted images
			displayFirstPageSimimlarImages(sortedMap);
		}
	}

	/*
	 * This class implements an ActionListener when the user selects the colorCode
	 * button. The image number that the user would like to find similar images for
	 * is stored in the variable pic. pic takes the image number associated with the
	 * image selected and subtracts one to account for the fact that the
	 * intensityMatrix starts with zero and not one. The size of the image is
	 * retrieved from the imageSize array. The selected image's intensity bin values
	 * are compared to all the other image's intensity bin values and a score is
	 * determined for how well the images compare. The images are then arranged from
	 * most similar to the least.
	 */
	private class colorCodeHandler implements ActionListener {

		public void actionPerformed(ActionEvent e) { // isIntensityOrCorolr set to true for handeling next and previous
														// page button according to sorted images
			isIntensityOrColor = true;
			LinkedHashMap<Integer, Double> map = new LinkedHashMap<Integer, Double>(); // to store image number as key
																						// and manhattan distance as
																						// value
			int compareImage = 0;
			int pic = (picNo - 1);
			int picColorCode = 0;
			Double manhattanDistance = 0.0;
			for (compareImage = 0; compareImage < 100; compareImage++) { // intializing manhattanDistance for each image
																			// to zero
				manhattanDistance = 0.0;
				// updating distance of query image to itself as zero in map
				if (pic == compareImage) {
					map.put(compareImage, 0.0);
				} else {
					for (picColorCode = 0; picColorCode < 64; picColorCode++) { // calculateing manhattan distance
																				// taking absolute value as distance
																				// cannot be negative.
						manhattanDistance += Math.abs((colorCodeMatrix[pic][picColorCode] / imageSize[pic])
								- (colorCodeMatrix[compareImage][picColorCode] / imageSize[compareImage]));
					}
				}
				// adding each image number with its distance with query image in HashMap
				map.put(compareImage, manhattanDistance);
			}
			// sorting the HashMap according to values
			sortedMap = sortByValue(map);
			// list Key stores the key(sorted image nubers) from sortedMap
			Key = new ArrayList<Integer>(sortedMap.keySet());

			// display all 100 sorted images
			displayFirstPageSimimlarImages(sortedMap);

		}
	}

	/*
	 * this method sorts the Hashmap map according to values, where image is key and
	 * distance from query image is value. method eturns the sorted HashMap.
	 */
	private LinkedHashMap<Integer, Double> sortByValue(LinkedHashMap<Integer, Double> hm) {
		// Create a list from elements of HashMap
		List<Map.Entry<Integer, Double>> list = new ArrayList<Map.Entry<Integer, Double>>(hm.entrySet());

		// Sort the list
		Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
			public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		// put data from sorted list to hashmap
		LinkedHashMap<Integer, Double> temp = new LinkedHashMap<Integer, Double>();
		for (Map.Entry<Integer, Double> aa : list) {
			temp.put(aa.getKey(), aa.getValue());
		}
		return temp;
	}

	/*
	 * This method displays all the 100 resulting images in ascending order
	 * according distances with query image in the pannelBottom after colorCode or
	 * intesity button is pressesd.The for loop starts with first index of sorted
	 * HashMap and gets the image by key of HashMap representing image number stored
	 * in the buttonOrder array and assigns the value to imageButNo. The button
	 * associated with the image is then added to panelBottom1. The for loop
	 * continues this process until 100 images are displayed in the panelBottom1
	 */
	private void displayFirstPageSimimlarImages(LinkedHashMap<Integer, Double> sortedMap) {
		int imageButNo = 0;
		panelBottom1.removeAll();
		// iterate through sorted hashMap to add image to button
		for (int i = 0; i < 20; i++) {
			imageButNo = buttonOrder[Key.get(i) + 1];
			panelBottom1.add(button[imageButNo]);
			sortedImagecount++;
		}
		panelBottom1.revalidate();
		panelBottom1.repaint();

	}

}
