import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;

public class ThresholdCalculator {
   
   // Hashmap for threshold range depending on mdoe
   public static final HashMap<Integer, Double> errorMax = new HashMap<>();
   static {
      errorMax.put(1, 65025.0);
      errorMax.put(2, 255.0);
      errorMax.put(3, 255.0);
      errorMax.put(4, 8.0);
      errorMax.put(5, 1.0);
   }

   // Find threshold given target compression percentage using BINARY SEARCH
   public static double getThreshold(File imageFile, double COMP_TARGET, double ERR_THRESHOLD, int MIN_SIZE, int ERR_MODE) throws IOException {

      // Find image file
      String fileExtension = App.getInfo(imageFile, "extension");
      double originalSize = imageFile.length();

      // Create temporary directory to place images
      File tempDir = new File(System.getProperty("java.io.tmpdir") + "/quadtree-temp");
      if (!tempDir.exists()) tempDir.mkdir();

      // Some parameters
      double high = errorMax.get(ERR_MODE);
      double low = 0.0;
      double threshold = ERR_THRESHOLD;
      int iteration = 0;
      int maxIterations = 10;

      // Binary search implementations
      while (iteration < maxIterations && high - low > 0.01) {
         
         if (iteration > 0) threshold = (high + low) / 2.0;

         // Create tree using given threshold 
         BufferedImage image = ImageIO.read(imageFile);
         Quadtree tree = new Quadtree(image, threshold, MIN_SIZE, ERR_MODE);
         // System.out.println("Created tree");
         
         File compressedFile = tree.renderImage(tempDir.getAbsolutePath(), "temp_" + iteration, fileExtension);
         // System.out.println("Created temporary image");

         // Check compression
         double compressedSize = compressedFile.length();
         double compressionPercent = 1.0 - (compressedSize / originalSize);

         // System.out.println("Iteration " + (iteration + 1) + ": using threshold " + threshold + " to get " + (compressionPercent * 100) + "% compression");
         if (Math.abs(compressionPercent - COMP_TARGET) < 0.005) { 

            deleteAll(tempDir);
            return threshold; // found correct threshold

         } else if (compressionPercent > COMP_TARGET) {

            high = threshold; // too much compression, decrease threshold

         } else {

            low = threshold; // too little compression, increase threshold

         }

         // Free up space
         compressedFile.delete();
         image.flush();
         tree = null;
         System.gc();
         
         iteration++;

      }
      
      deleteAll(tempDir); 
      return (high + low) / 2.0; 
   }

   // Delete all files in a directory (including itself) 
   public static void deleteAll(File file) {
      if (file.exists()) {
         if (file.isDirectory()) {
            for (File subfile : file.listFiles()) {
               deleteAll(subfile);
            }
         }
         file.delete();
         System.gc();
      }
   }

}
