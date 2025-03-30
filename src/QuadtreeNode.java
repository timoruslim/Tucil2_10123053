import java.util.ArrayList;
import java.util.Arrays;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class QuadtreeNode {

   // Image fields
   public BufferedImage image; // original image
   public int mode;
   public Color color; // average color
   public double error;
   public BufferedImage cImage; // monotone image 

   // Node fields
   public int depth; 
   public ArrayList<QuadtreeNode> children; // {tl, tr, bl, br}
   public boolean leaf = false;

   // Constructor 
   public QuadtreeNode(BufferedImage image, int depth, int mode) {
      this.image = image;
      this.mode = mode;
      this.color = average();
      ErrorCalculator calc = new ErrorCalculator(image, color); 
      this.error = calc.calculateError(mode);
      this.cImage = compress();
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

   // Creates monotone image of average color 
   public BufferedImage compress() {
      int width = image.getWidth(), height = image.getHeight();
      BufferedImage cImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
      Graphics2D g = cImage.createGraphics();

      g.setColor(color);
      g.fillRect(0, 0, width, height);
      g.dispose();

      return cImage;
   }

   // Splits image into four sub-images
   public void divide() {

      int w = image.getWidth();
      int h = image.getHeight();
      int mw = w / 2;
      int mh = h / 2;

      QuadtreeNode tl = new QuadtreeNode(image.getSubimage(0, 0, mw, mh), depth++, mode); 
      QuadtreeNode tr = new QuadtreeNode(image.getSubimage(mw, 0, w-mw, mh), depth++, mode); 
      QuadtreeNode bl = new QuadtreeNode(image.getSubimage(0, mh, mw, h-mh), depth++, mode); 
      QuadtreeNode br = new QuadtreeNode(image.getSubimage(mw, mh, w-mw, h-mh), depth++, mode); 
      this.children = new ArrayList<>(Arrays.asList(tl, tr, bl, br)); 

   }

}
