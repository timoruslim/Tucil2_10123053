import java.util.HashMap;
import java.util.function.BiFunction;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class ErrorCalculator {

   // Hashmap for error calculation mode
   public static final HashMap<Integer, BiFunction<BufferedImage, QuadtreeNode, Double>> errorModes = new HashMap<>();
   static {
      errorModes.put(1, ErrorCalculator::variance);
      errorModes.put(2, ErrorCalculator::mad);
      errorModes.put(3, ErrorCalculator::mpd);
      errorModes.put(4, ErrorCalculator::entropy);
      errorModes.put(5, ErrorCalculator::ssim);
   }

   // Calculate the error based on the mode
   public static double calculateError(int mode, BufferedImage image, QuadtreeNode node) {
      return errorModes.get(mode).apply(image, node);
   }

   // Get color Variance of image
   public static double variance(BufferedImage image, QuadtreeNode node) {
      double varR = 0, varG = 0, varB = 0;
      int x = node.patch[0];
      int y = node.patch[1];
      int w = node.patch[2];
      int h = node.patch[3];
      int N = w * h;

      int[] pixels = new int[N];
      image.getRGB(x, y, w, h, pixels, 0, w);

      int meanR = node.color.getRed(), meanG = node.color.getGreen(), meanB = node.color.getBlue();
      for (int i = 0; i < N; i++) {
         Color pixelColor = new Color(pixels[i]);

         varR += Math.pow(pixelColor.getRed() - meanR, 2.0);
         varG += Math.pow(pixelColor.getGreen() - meanG, 2.0);
         varB += Math.pow(pixelColor.getBlue() - meanB, 2.0);
      }

      varR /= N;
      varG /= N;
      varB /= N;

      return (varR + varG + varB) / 3.0; 
   }

   // Get color Mean Absolute Deviation (MAD) of image
   public static double mad(BufferedImage image, QuadtreeNode node) {
      double madR = 0, madG = 0, madB = 0;
      int x = node.patch[0];
      int y = node.patch[1];
      int w = node.patch[2];
      int h = node.patch[3];
      int N = w * h;

      int[] pixels = new int[N];
      image.getRGB(x, y, w, h, pixels, 0, w);

      int meanR = node.color.getRed(), meanG = node.color.getGreen(), meanB = node.color.getBlue();
      for (int i = 0; i < N; i++) {
         Color pixelColor = new Color(pixels[i]); 

         madR += Math.abs(pixelColor.getRed() - meanR);
         madG += Math.abs(pixelColor.getGreen() - meanG);
         madB += Math.abs(pixelColor.getBlue() - meanB);
      }

      madR /= N;
      madG /= N;
      madB /= N;

      return (madR + madG + madB) / 3.0; 
   }

   // Get color Max Pixel Difference (MPD) of image
   public static double mpd(BufferedImage image, QuadtreeNode node) {
      int maxR = 0, maxG = 0, maxB = 0;
      int minR = 256, minG = 256, minB = 256;
      int x = node.patch[0];
      int y = node.patch[1];
      int w = node.patch[2];
      int h = node.patch[3];
      int N = w * h;

      int[] pixels = new int[N];
      image.getRGB(x, y, w, h, pixels, 0, w);

      for (int i = 0; i < N; i++) {
         Color pixelColor = new Color(pixels[i]); 

         maxR = (pixelColor.getRed() > maxR) ? pixelColor.getRed() : maxR;
         maxG = (pixelColor.getGreen() > maxG) ? pixelColor.getGreen() : maxG;
         maxB = (pixelColor.getBlue() > maxB) ? pixelColor.getBlue() : maxB;

         minR = (pixelColor.getRed() < minR) ? pixelColor.getRed() : minR;
         minG = (pixelColor.getGreen() < minG) ? pixelColor.getGreen() : minG;
         minB = (pixelColor.getBlue() < minB) ? pixelColor.getBlue() : minB;
      }

      int dR = maxR - minR, dG = maxG - minG, dB = maxB - minB;
      return (dR + dG + dB) / 3.0; 
   }

   // Get color Entropy of image
   public static double entropy(BufferedImage image, QuadtreeNode node) {
      double[] histR = new double[256], histG = new double[256], histB = new double[256];
      int x = node.patch[0];
      int y = node.patch[1];
      int w = node.patch[2];
      int h = node.patch[3];
      int N = w * h;

      int[] pixels = new int[N];
      image.getRGB(x, y, w, h, pixels, 0, w);

      for (int i = 0; i < N; i++) {
         Color pixelColor = new Color(pixels[i]);

         histR[pixelColor.getRed()]++;
         histG[pixelColor.getGreen()]++;
         histB[pixelColor.getBlue()]++;
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
   public static double ssim(BufferedImage image, QuadtreeNode node) {
      double varR = 0, varG = 0, varB = 0;
      int x = node.patch[0];
      int y = node.patch[1];
      int w = node.patch[2];
      int h = node.patch[3];
      int N = w * h;

      int[] pixels = new int[N];
      image.getRGB(x, y, w, h, pixels, 0, w);

      int meanR = node.color.getRed(), meanG = node.color.getGreen(), meanB = node.color.getBlue();
      for (int i = 0; i < N; i++) {
         Color pixelColor = new Color(pixels[i]); 

         varR += Math.pow(pixelColor.getRed() - meanR, 2.0);
         varG += Math.pow(pixelColor.getGreen() - meanG, 2.0);
         varB += Math.pow(pixelColor.getBlue() - meanB, 2.0);
      }

      varR /= (double) N - 1.0;
      varG /= (double) N - 1.0;
      varB /= (double) N - 1.0;

      double C2 = Math.pow(0.03 * 255.0, 2);
      double siR = 1 / (varR / C2 + 1), siG = 1 / (varG / C2 + 1), siB = 1 / (varB / C2 + 1);

      return 0.299 * siR + 0.587 * siG + 0.144 * siB; 
   }

}
