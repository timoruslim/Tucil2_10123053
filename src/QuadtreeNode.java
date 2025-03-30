import java.util.ArrayList;
import java.util.Arrays;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class QuadtreeNode {

   // Image fields
   public BufferedImage image;
   public int size;
   public int mode;
   public Color color; // average color
   public double error;

   // Node fields
   public int depth; 
   public ArrayList<QuadtreeNode> children; // {tl, tp, bl, br}
   public boolean leaf = false;

   // Constructor 
   public QuadtreeNode(BufferedImage image, int depth, int mode) {
      this.image = image;
      this.size = image.getWidth() * image.getHeight();
      this.mode = mode;
      this.color = average();
      ErrorCalculator calc = new ErrorCalculator(image, color); 
      this.error = calc.calculateError(mode);
      this.depth = depth;
   }

   // Get average RGB color of image
   public Color average() {
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

   // Splits image into four sub-images
   public void divide() {
      int w = image.getWidth();
      int h = image.getHeight();
      int mw = w / 2;
      int mh = h / 2;
      QuadtreeNode tl = new QuadtreeNode(image.getSubimage(0, mh, mw, mh), this.depth++, mode);
      QuadtreeNode tp = new QuadtreeNode(image.getSubimage(mw, mh, mw, mh), this.depth++, mode);
      QuadtreeNode bl = new QuadtreeNode(image.getSubimage(0, 0, mw, mh), this.depth++, mode);
      QuadtreeNode br = new QuadtreeNode(image.getSubimage(mw, 0, mw, mh), this.depth++, mode);
      this.children = new ArrayList<>(Arrays.asList(tl, tp, bl, br));
   }

}
