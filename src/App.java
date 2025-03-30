import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class App {
   public static void main(String[] args) throws IOException {

      int mode = 2;

      File imageFile = new File("D:\\ITB\\Semester 4\\Strategi Algoritma\\Tugas Kecil 2\\test\\Adiel.png"); 
      BufferedImage image = ImageIO.read(imageFile);
      Quadtree tree = new Quadtree(image, 0, 0, mode);

      System.out.println(tree.root.error);

   }
}