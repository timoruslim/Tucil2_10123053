import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class App {
   public static void main(String[] args) throws IOException {

      int mode = 2;

      File imageFile = new File("D:\\ITB\\Semester 4\\Strategi Algoritma\\Tugas Kecil 2\\test\\Adiel.png"); 
      BufferedImage image = ImageIO.read(imageFile);
      Quadtree tree = new Quadtree(image, 5, 2, mode);

      BufferedImage compressedImage = tree.createImage(tree.treeDepth);
      File output = new File("D:\\ITB\\Semester 4\\Strategi Algoritma\\Tugas Kecil 2\\test\\AdielCompressed.png");
      ImageIO.write(compressedImage, "png", output);
   }
}