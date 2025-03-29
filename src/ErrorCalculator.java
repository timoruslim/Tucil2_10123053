import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ErrorCalculator {
   public final Map<Integer, Function<BufferedImage, Double>> errorModes;
   public Color color;

   public ErrorCalculator(Color color) {
      errorModes = new HashMap<>();
      errorModes.put(1, this::variance);
      errorModes.put(2, this::mad);
      errorModes.put(3, this::mpd);
      errorModes.put(4, this::entropy);
      this.color = color;
   }

   public double calculateError(BufferedImage image, int mode) {
      return errorModes.getOrDefault(mode, img -> Double.NaN).apply(image);
   }

   // Get color Variance of image
   public double variance(BufferedImage image) {
      double varR = 0, varG = 0, varB = 0;
      int w = image.getWidth();
      int h = image.getHeight();
      int N = w * h;

      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w; x++) {
            int rgb = image.getRGB(x, y); 
            Color pixelColor = new Color(rgb); 

            varR += Math.pow(pixelColor.getRed() - color.getRed(), 2);
            varG += Math.pow(pixelColor.getGreen() - color.getGreen(), 2);
            varB += Math.pow(pixelColor.getBlue() - color.getBlue(), 2);
         }
      }

      varR /= N;
      varG /= N;
      varB /= N;

      return (varR + varG + varB) / 3; 
   }

   // Get color Mean Absolute Deviation (MAD) of image
   public double mad(BufferedImage image) {
      double madR = 0, madG = 0, madB = 0;
      int w = image.getWidth();
      int h = image.getHeight();
      int N = w * h;

      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w; x++) {
            int rgb = image.getRGB(x, y); 
            Color pixelColor = new Color(rgb); 

            madR += Math.abs(pixelColor.getRed() - color.getRed());
            madG += Math.abs(pixelColor.getGreen() - color.getGreen());
            madB += Math.abs(pixelColor.getBlue() - color.getBlue());
         }
      }

      madR /= N;
      madG /= N;
      madB /= N;

      return (madR + madG + madB) / 3; 
   }

   // Get color Max Pixel Difference (MPD) of image
   public double mpd(BufferedImage image) {
      int maxR = 0, maxG = 0, maxB = 0;
      int minR = 256, minG = 256, minB = 256;
      int w = image.getWidth();
      int h = image.getHeight();

      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w; x++) {
            int rgb = image.getRGB(x, y); 
            Color color = new Color(rgb); 

            maxR = (color.getRed() > maxR) ? color.getRed() : maxR;
            maxG = (color.getGreen() > maxG) ? color.getRed() : maxG;
            maxB = (color.getBlue() > maxB) ? color.getRed() : maxB;

            minR = (color.getRed() < minR) ? color.getRed() : minR;
            minG = (color.getGreen() < minG) ? color.getRed() : minG;
            minB = (color.getBlue() < minB) ? color.getRed() : minB;
         }
      }

      int dR = maxR - minR, dG = maxG - minG, dB = maxB - minB;
      return (dR + dG + dB) / 3; 
   }

   // Get color Entropy of image
   public double entropy(BufferedImage image) {
      int[] histR = new int[256], histG = new int[256], histB = new int[256];
      int width = image.getWidth();
      int height = image.getHeight();
      int totalPixels = width * height;

      for (int y = 0; y < height; y++) {
         for (int x = 0; x < width; x++) {
               int rgb = image.getRGB(x, y);
               Color color = new Color(rgb);

               histR[color.getRed()]++;
               histG[color.getGreen()]++;
               histB[color.getBlue()]++;
         }
      }

      double hR = 0, hG = 0, hB = 0;
      for (int i = 0; i < 256; i++) {
         double pR = (double) histR[i] / totalPixels;
         double pG = (double) histG[i] / totalPixels;
         double pB = (double) histB[i] / totalPixels;

         hR += pR * (Math.log(pR) / Math.log(2));
         hG += pG * (Math.log(pG) / Math.log(2));
         hB += pB * (Math.log(pB) / Math.log(2));
      }

      return -(hR + hG + hB) / 3; 
   }
   
}
