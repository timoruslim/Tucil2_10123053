import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.Color;

public class QuadtreeNode {

   // Image fields
   public BufferedImage image;
   public Color color = average(image);
   public double error = variance(image, color);

   // Tree fields
   public int depth; 
   public List<QuadtreeNode> children; // {tl, tp, bl, br}
   public boolean leaf = false;

   // Constructor 
   public QuadtreeNode(BufferedImage image, int depth) {
      this.image = image;
      this.depth = depth;
   }

   // Get average RGB color of image
   public Color average(BufferedImage image) {
      double sumR = 0, sumG = 0, sumB = 0;
      int w = image.getWidth();
      int h = image.getHeight();
      int N = w * h;

      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w; x++) {
            int rgb = image.getRGB(x, y); 
            Color color = new Color(rgb); 
            
            sumR += color.getRed();
            sumG += color.getGreen();
            sumB += color.getBlue();
         }
      }

      int R = (int) (sumR / N), G = (int) (sumG / N), B = (int) (sumB / N);
      return new Color(R, G, B);
   }

   // Get color Variance of image
   public double variance(BufferedImage image, Color color) {
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
   public double mad(BufferedImage image, Color color) {
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

   // Splits image into four sub-images
   public void divide(BufferedImage image) {
      int w = image.getWidth();
      int h = image.getHeight();
      int mw = w / 2;
      int mh = h / 2;
      QuadtreeNode tl = new QuadtreeNode(image.getSubimage(0, mh, mw, mh), this.depth++);
      QuadtreeNode tp = new QuadtreeNode(image.getSubimage(mw, mh, mw, mh), this.depth++);
      QuadtreeNode bl = new QuadtreeNode(image.getSubimage(0, 0, mw, mh), this.depth++);
      QuadtreeNode br = new QuadtreeNode(image.getSubimage(mw, 0, mw, mh), this.depth++);
      this.children = new ArrayList<>(Arrays.asList(tl, tp, bl, br));
   }

}
