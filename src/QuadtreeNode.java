import java.util.ArrayList;
import java.util.Arrays;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class QuadtreeNode {

   // Image fields
   public int[] patch = new int[4]; // (x, y, w, h)
   public int mode;
   public Color color; // average color
   public double error;

   // Node fields
   public int depth; 
   public ArrayList<QuadtreeNode> children; // {tl, tr, bl, br}
   public boolean leaf = false;

   // Constructor 
   public QuadtreeNode(BufferedImage image, int[] patch, int depth, int mode) {
      this.depth = depth;
      this.patch = patch;
      this.mode = mode;
      this.color = average(image);
      this.error = ErrorCalculator.calculateError(mode, image, this);
   }

   // Get average RGB color of image
   public Color average(BufferedImage image) {
      double sumR = 0, sumG = 0, sumB = 0;
      int x = patch[0];
      int y = patch[1];
      int w = patch[2];
      int h = patch[3];
      int N = w * h;

      int[] pixels = new int[N];
      image.getRGB(x, y, w, h, pixels, 0, w);

      for (int i = 0; i < N; i++) {
         Color color = new Color(pixels[i]); 
         sumR += color.getRed();
         sumG += color.getGreen();
         sumB += color.getBlue();
      }

      int R = (int) (sumR / N), G = (int) (sumG / N), B = (int) (sumB / N);
      return new Color(R, G, B);
   }

   // Creates monotone image of average color 
   public BufferedImage compress() {
      int w = patch[2];
      int h = patch[3];

      BufferedImage cImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
      Graphics2D g = cImage.createGraphics();

      g.setColor(color);
      g.fillRect(0, 0, w, h);
      g.dispose();

      return cImage;
   }

   // Splits image into four sub-images
   public void divide(BufferedImage image) {

      int x = patch[0];
      int y = patch[1];
      int w = patch[2];
      int h = patch[3];
      int mw = w / 2;
      int mh = h / 2;

      QuadtreeNode tl = new QuadtreeNode(image, new int[]{x, y, mw, mh}, depth + 1, mode); 
      QuadtreeNode tr = new QuadtreeNode(image, new int[]{x+mw, y, w-mw, mh}, depth + 1, mode); 
      QuadtreeNode bl = new QuadtreeNode(image, new int[]{x, y+mh, mw, h-mh}, depth + 1, mode); 
      QuadtreeNode br = new QuadtreeNode(image, new int[]{x+mw, y+mh, w-mw, h-mh}, depth + 1, mode); 
      this.children = new ArrayList<>(Arrays.asList(tl, tr, bl, br)); 

   }

}
