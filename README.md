# ImageRetrieval
This project is to implement a simple Content-Based Image Retrieval system based on two different color histogram comparison methods, i.e., Intensity and Color-code methods.
Color histogram comparison is a simple but effective approach in CBIR systems. Here are two ways to combine the information from 3 color channels (R, G, B):

A. Intensity Method
I = 0.299R + 0.587G + 0.114B
By this way, the 24-bit of RGB (8 bits for each color channel) color intensities can be transformed into a single 8-bit value. The histogram bins selected for this case is listed below:
H1: I E [0,10); H2: I E [10,20)  ;….. H25: I E [240,255].

B. Color-Code Method
The 24-bit of RGB color intensities can be transformed into a 6-bit color code, composed from the most significant 2 bits of each of the three colors components, as illustrated in the following figure.
The 6-bit color code will provide 64 histogram bins. For example, the R, G, and B values for a pixel are 128, 0, and 255 respectively. So, the bit representations of them are 10000000, 00000000, and 11111111. Then the 6-bit color code value will be 100011. In color code, there will be 64 bins with H1: 000000, H2: 000001, H3: 000010, … H64: 111111.

Histogram Comparison
You need to implement the distance metrics for histogram comparison. Let Hi(j) denote the number of pixels in jth bin for the ith image. Then the difference between the ith image and kth image can be given by the following distance metric:
  where Mi*Ni is the number of pixels in image i, and Mk*Nk is the number of pixels in image k.

The graphic user interface should allow users to browse all the images in the image database, select the query image, and to view the retrieved images. Given a query image, the retrieved images should be displayed to user according to their similarity ranks to the query image. In particular, the similarity rank decreases from left to right, and top to bottom. We have used the sample code for the implementation of the project.

Execution Steps: 
The project consists of two important java files CBIR.java and readImage.java files. 
Extract the “ImageRetrieval.zip” folder and import the project into a desired IDE (eg: Eclipse). The /src folder contains the main files and images folder which contains images. At the root directory of the project already created instesity.txt and colorCodes.txt files are present but if needed can be created again by running readImage.java. 
readImage.java can be run as a java application, its main method generates two files instesity.txt and colorCodes.txt files at the root directory of the project for each method which contains the intensity values count for different histogram bins of all the given images. 
To get the UI interface, run CBIR.java as a java application, this will open the UI with the given images, displaying first 20 images in the panel bottom of the UI with 4 buttons at top panel: Previous Page, Next Page, Intensity and Color Code. 

1-	Select any image from the given images as your query image. 
2-	Select any color code method to retrieve the images like the query images: Intensity/Color code. 
3-	Based on the implementation of the chosen method, the images will be retrieved in the panel bottom based upon the distance calculation in ascending order and images will be displayed in the same order. We have shown all the images in the result together, so that images can be compared from most similar to least similar in one page effectively without scrolling the pages.
