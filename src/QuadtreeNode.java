import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.Color;

public class QuadtreeNode {

   // Image fields
   public BufferedImage image;
   public ColorErrorPair colorError = average(image);
   public double error;

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
   public ColorErrorPair average(BufferedImage image) {
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

      int R = (int) (sumR / N);
      int G = (int) (sumG / N);
      int B = (int) (sumB / N);
      Color color = new Color(R, G, B);

      return new ColorErrorPair(color, variance(image, w, h, N, color));
   }

   // Get color Variance of image
   public double variance(BufferedImage image, int w, int h, int N, Color color) {
      double varR = 0, varG = 0, varB = 0;

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
      return 0; 
   }

   // Get color Max Pixel Difference (MPD) of image
   public double mpd(BufferedImage image) {
      return 0; 
   }

   // Get color Entropy of image
   public double entropy(BufferedImage image) {
      return 0; 
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
