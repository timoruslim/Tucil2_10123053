import java.util.HashMap;
import java.util.function.Supplier;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class ErrorCalculator {

   // Fields
   public final HashMap<Integer, Supplier<Double>> errorModes;
   public BufferedImage image;
   public Color color;

   // Constructor 
   public ErrorCalculator(BufferedImage image, Color color) {
      errorModes = new HashMap<>();
      errorModes.put(1, this::variance);
      errorModes.put(2, this::mad);
      errorModes.put(3, this::mpd);
      errorModes.put(4, this::entropy);

      this.image = image;
      this.color = color;
   }

   // Calculate the error based on the mode
   public double calculateError(int mode) {
      return errorModes.getOrDefault(mode, () -> -1.0).get();
   }

   // Get color Variance of image
   public double variance() {
      double varR = 0, varG = 0, varB = 0;
      int w = image.getWidth();
      int h = image.getHeight();
      int N = w * h;

      int meanR = color.getRed(), meanG = color.getGreen(), meanB = color.getBlue();
      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w; x++) {
            int rgb = image.getRGB(x, y); 
            Color pixelColor = new Color(rgb); 

            varR += Math.pow(pixelColor.getRed() - meanR, 2.0);
            varG += Math.pow(pixelColor.getGreen() - meanG, 2.0);
            varB += Math.pow(pixelColor.getBlue() - meanB, 2.0);
         }
      }

      varR /= N;
      varG /= N;
      varB /= N;

      return (varR + varG + varB) / 3.0; 
   }

   // Get color Mean Absolute Deviation (MAD) of image
   public double mad() {
      double madR = 0, madG = 0, madB = 0;
      int w = image.getWidth();
      int h = image.getHeight();
      int N = w * h;

      int meanR = color.getRed(), meanG = color.getGreen(), meanB = color.getBlue();
      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w; x++) {
            int rgb = image.getRGB(x, y); 
            Color pixelColor = new Color(rgb); 

            madR += Math.abs(pixelColor.getRed() - meanR);
            madG += Math.abs(pixelColor.getGreen() - meanG);
            madB += Math.abs(pixelColor.getBlue() - meanB);
         }
      }

      madR /= N;
      madG /= N;
      madB /= N;

      return (madR + madG + madB) / 3.0; 
   }

   // Get color Max Pixel Difference (MPD) of image
   public double mpd() {
      int maxR = 0, maxG = 0, maxB = 0;
      int minR = 256, minG = 256, minB = 256;
      int w = image.getWidth();
      int h = image.getHeight();

      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w; x++) {
            int rgb = image.getRGB(x, y); 
            Color color = new Color(rgb); 

            maxR = (color.getRed() > maxR) ? color.getRed() : maxR;
            maxG = (color.getGreen() > maxG) ? color.getGreen() : maxG;
            maxB = (color.getBlue() > maxB) ? color.getBlue() : maxB;

            minR = (color.getRed() < minR) ? color.getRed() : minR;
            minG = (color.getGreen() < minG) ? color.getGreen() : minG;
            minB = (color.getBlue() < minB) ? color.getBlue() : minB;
         }
      }

      int dR = maxR - minR, dG = maxG - minG, dB = maxB - minB;
      return (dR + dG + dB) / 3.0; 
   }

   // Get color Entropy of image
   public double entropy() {
      double[] histR = new double[256], histG = new double[256], histB = new double[256];
      int width = image.getWidth();
      int height = image.getHeight();
      int N = width * height;

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
         histR[i] /= N;
         histG[i] /= N;
         histB[i] /= N;

         hR += (histR[i] > 0) ? histR[i] * (Math.log(histR[i]) / Math.log(2.0)) : 0;
         hG += (histG[i] > 0) ?  histG[i] * (Math.log(histG[i]) / Math.log(2.0)) : 0;
         hB += (histB[i] > 0) ?  histB[i] * (Math.log(histB[i]) / Math.log(2.0)) : 0;
      }

      return -(hR + hG + hB) / 3.0; 
   }
   
   // Get color SSIM of image
   public double ssim() {
      double varR = 0, varG = 0, varB = 0;
      int w = image.getWidth();
      int h = image.getHeight();
      double N = w * h;

      int meanR = color.getRed(), meanG = color.getGreen(), meanB = color.getBlue();
      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w; x++) {
            int rgb = image.getRGB(x, y); 
            Color pixelColor = new Color(rgb); 

            varR += Math.pow(pixelColor.getRed() - meanR, 2.0);
            varG += Math.pow(pixelColor.getGreen() - meanG, 2.0);
            varB += Math.pow(pixelColor.getBlue() - meanB, 2.0);
         }
      }

      varR /= N - 1.0;
      varG /= N - 1.0;
      varB /= N - 1.0;

      double C2 = Math.pow(0.03 * 255, 2);
      double siR = 1 / (varR / C2 + 1), siG = 1 / (varG / C2 + 1), siB = 1 / (varB / C2 + 1);

      return 0.299 * siR + 0.587 * siG + 0.144 * siB; 
   }

}
