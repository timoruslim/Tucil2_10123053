import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import java.text.DecimalFormat;
import java.util.Scanner;

public class App {

   static String getInfo(File file, String type) {
      String info = file.getName();
      int pos = info.lastIndexOf('.');

      if (pos <= 0 || pos == info.length() - 1) { 
         return type.equalsIgnoreCase("name") ? info : ""; 
      }

      return switch (type.toLowerCase()) {
         case "name" -> info.substring(0, pos); 
         case "extension" -> info.substring(pos + 1); 
         default -> throw new IllegalArgumentException("Invalid type: " + type);
      };
   }
   
   public static void main(String[] args) throws IOException {

      // Title
      System.out.println(
         "\n========================================================================================================================================\n" +
         " ________                    .___ __                          _________                                                                 \n" +
         " \\_____  \\  __ _______     __| _//  |________   ____   ____   \\_   ___ \\  ____   _____ _____________   ____   ______ _________________ \n" +
         "  /  / \\  \\|  |  \\__  \\   / __ |\\   __\\_  __ \\_/ __ \\_/ __ \\  /    \\  \\/ /  _ \\ /     \\\\____ \\_  __ \\_/ __ \\ /  ___//  ___/  _ \\_  __ \\\n" +
         " /   \\_/.  \\  |  // __ \\_/ /_/ | |  |  |  | \\/\\  ___/\\  ___/  \\     \\___(  <_> )  Y Y  \\  |_> >  | \\/\\  ___/ \\___ \\ \\___ (  <_> )  | \\/\n" +
         " \\_____\\ \\_/____/(____  /\\____ | |__|  |__|    \\___  >\\___  >  \\______  /\\____/|__|_|  /   __/|__|    \\___  >____  >____  >____/|__|   \n" +
         "        \\__>          \\/      \\/                   \\/     \\/          \\/             \\/|__|               \\/     \\/     \\/             \n\n" +
         "                                                Made by Timothy Niels Ruslim (10123053)                                                 \n\n" + 
         "========================================================================================================================================" 
         
      );

      boolean isRunning = true;
      Scanner scanner = new Scanner(System.in);

      File imageFile = null;
      File gifFile = null;

      int ERR_MODE = 0;
      double ERR_THRESHOLD = -1;
      int MIN_SIZE = 0;
      double COMP_TARGET = -1;

      String imagePath = null;
      String gifPath = null;

      // Main program
      while (isRunning) {

         // Input for image file path 
         while (imageFile == null) {

            System.out.print("\nApa gambar yang ingin dikompresi? [ketik alamat] \u2192 ");
            String path = scanner.nextLine( );
            File option1 = new File(path); // absolute path 
            File option2 = new File("../test/" + path); // for ease of testing 

            if (option1.exists()) {
               imageFile = option1;
            } else if (option2.exists()) {
               imageFile = option2;
            } else {
               System.out.println("File tidak ditemukan. Coba lagi. ");
            }

         } 
         
         // Input for error calculation method 
         while (ERR_MODE == 0) {

            System.out.println("""
               \nMetode perhitungan galat:
               1. Variance
               2. Mean Absolute Deviation (MAD)
               3. Max Pixel Difference
               4. Entropy
               5. Structural Similarity Index Measure (SSIM)
            """);
            System.out.print("Pilih metode perhitungan galat! [Ketik 1,2,...,5] \u2192 ");

            if (scanner.hasNextInt()) { // check integer
               int mode = scanner.nextInt();
               if (mode >= 1 && mode <= 5) { // check valid mode
                  ERR_MODE = mode;
               } else {
                  System.out.println("Angka tidak valid. Coba lagi.");
               }
            } else {
               scanner.next();
               System.out.println("Angka tidak valid. Coba lagi.");
            }

            scanner.nextLine();

         }

         // Input for threshold 
         while (ERR_THRESHOLD == -1) {

            System.out.print("\nMasukkan ambang batas! [Ketik angka ≥0] \u2192 ");

            if (scanner.hasNextDouble()) { // check double
               double threshold = scanner.nextDouble();
               if (threshold >= 0) { // check valid threshold
                  ERR_THRESHOLD = threshold;
               } else {
                  System.out.println("Angka tidak valid. Coba lagi.");
               }
            } else {
               scanner.next();
               System.out.println("Angka tidak valid. Coba lagi.");
            }

            scanner.nextLine();

         }

         // Input for minimum size 
         while (MIN_SIZE == 0) {

            System.out.print("\nMasukkan ukuran blok minimum! [Ketik 1,2,...] \u2192 ");

            if (scanner.hasNextInt()) { // check int
               int size = scanner.nextInt();
               if (size >= 1) { // check valid size
                  MIN_SIZE = size;
               } else {
                  System.out.println("Angka tidak valid. Coba lagi.");
               }
            } else {
               scanner.next();
               System.out.println("Angka tidak valid. Coba lagi.");
            }

            scanner.nextLine();

         }

         // Input for target compression percentage 
         while (COMP_TARGET == -1) {

            System.out.print("\nMasukkan target persentase kompresi! [Ketik 0-1, dengan 0 untuk menonaktifkan] \u2192 ");

            if (scanner.hasNextDouble()) { // check int
               double target = scanner.nextDouble();
               if (target >= 0 && target <= 1) { // check valid size
                  COMP_TARGET = target;
               } else {
                  System.out.println("Angka tidak valid. Coba lagi."); 
               }
            } else {
               scanner.next();
               System.out.println("Angka tidak valid. Coba lagi.");
            }

            scanner.nextLine();

         }

         // Input for compressed image file path 
         while (imagePath == null) {   

            System.out.print("\nMau simpan hasil kompresi di mana? [Ketik alamat] \u2192 ");
            String path = scanner.nextLine( );
            File folder = new File(path); // absolute path

            if (folder.exists() && folder.isDirectory()) {
               imagePath = path;
            } else if (path.equals("test")) {
               imagePath = "../test";
            } else {
               System.out.println("Alamat tidak ditemukan. Coba lagi. ");
            }

         } 

         // Input for gif file path 
         while (gifPath == null) {

            System.out.print("\nMau simpan GIF di mana? [Ketik alamat, atau 0 jika tidak ingin] \u2192 ");
            String path = scanner.nextLine();
            File option = new File(path); // absolute path

            if (path.equals("0")) {
               break;
            } else if (option.exists()) {
               gifPath = path;
            } else if (path.equals("test")) {
               gifPath = "../test";
            } else {
               System.out.println("Alamat tidak ditemukan. Coba lagi. ");
            }

         } 

         // Compression Process
         System.out.println("\n========================================================================================================================================");
         long start = System.nanoTime(); 

         BufferedImage image = ImageIO.read(imageFile);
         
         // Find threshold value for target compression rate
         if (COMP_TARGET != 0) {
            System.out.print("\nMencari ambang batas...");
            ERR_THRESHOLD = ThresholdCalculator.getThreshold(imageFile, COMP_TARGET, ERR_THRESHOLD, MIN_SIZE, ERR_MODE);
         }

         // Create quadtree for compression of image
         System.out.println("\nGambar sedang dikompresi...");
         Quadtree tree = new Quadtree(image, ERR_THRESHOLD, MIN_SIZE, ERR_MODE);
         File compressedFile = tree.renderImage(imagePath, getInfo(imageFile, "name"), getInfo(imageFile, "extension"));
         
         // Create compression process gif 
         if (gifPath != null) {
            System.out.println("GIF sedang dibuat...");
            gifFile = tree.renderGif(gifPath, getInfo(imageFile, "name"));
         }

         long end = System.nanoTime();
         System.out.println("\n========================================================================================================================================");

         // Output information
         long time = (end - start) / 1000000;
         double originalSize = imageFile.length() / (1024.0 * 1024.0);
         double compressedSize = compressedFile.length()  / (1024.0 * 1024.0);
         double compressionPercent = (1.0 - (double) compressedFile.length() / (double) imageFile.length()) * 100.0;
         DecimalFormat df = new DecimalFormat("0.000");

         System.out.println("\nWaktu kompresi           : " + time + " ms ");
         System.out.println("Ukuran sebelum           : " + df.format(originalSize) + " MB ");  
         System.out.println("Ukuran setelah           : " + df.format(compressedSize) + " MB ");
         System.out.println("Persentasi kompresi      : " + df.format(compressionPercent) + "% ");
         System.out.println("Kedalaman pohon          : " + tree.treeDepth + " ");
         System.out.println("Banyak simpul pada pohon : " + tree.nodeCount + " ");

         System.out.println("\nGambar hasil kompresi dapat ditemukan di \"" + compressedFile.getAbsolutePath() + "\". ");
         if (gifFile != null) System.out.println("GIF proses kompresi dapat ditemukan di \"" + gifFile.getAbsolutePath() + "\". ");

         // Reset settings
         imageFile = gifFile = null;
         ERR_MODE = MIN_SIZE = 0;
         ERR_THRESHOLD = COMP_TARGET = -1;
         imagePath = gifPath = null;
         image.flush();
         System.gc();

         System.out.println("\n========================================================================================================================================");

      }

      scanner.close();

   }

}