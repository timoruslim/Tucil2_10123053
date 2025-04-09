import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

public class Quadtree {
   
   // Tree Fields 
   public QuadtreeNode root;
   public int treeDepth = 0; 
   public int nodeCount = 0;

   // Compression Fields
   public BufferedImage image;
   public int ERR_MODE;
   public double ERR_THRESHOLD;
   public int MIN_SIZE;

   // Constructor
   public Quadtree(BufferedImage image, double ERR_THRESHOLD, int MIN_SIZE, int ERR_MODE) {
      this.root = new QuadtreeNode(image, new int[]{0, 0, image.getWidth(), image.getHeight()}, 0, ERR_MODE);
      this.image = image;
      this.ERR_MODE = ERR_MODE;
      this.ERR_THRESHOLD = ERR_THRESHOLD;
      this.MIN_SIZE = MIN_SIZE;

      buildTree(root);
   }

   // Build quadtree to compress image (divide)
   public void buildTree(QuadtreeNode node) {
      nodeCount++;
      
      // Make into leaf node if sufficient
      int size = node.patch[2] * node.patch[3];
      boolean withinThreshold = ERR_MODE == 5 ? node.error >= ERR_THRESHOLD : node.error <= ERR_THRESHOLD;
      if (withinThreshold || size <= MIN_SIZE || size/4.0 < MIN_SIZE) {
         if (node.depth > treeDepth) treeDepth = node.depth; 
         // System.out.println("Leaf: " + node.error);
         node.leaf = true; 
         return; 
      }

      // Recursion 
      node.divide(image);
      // System.out.println("Branch: " + node.error);
      for (QuadtreeNode child : node.children) {
         buildTree(child);
      }

   }

   // Call recursion to create image
   public BufferedImage createImage(int depth) {
      return createImageRecursion(root, depth);
   }

   // Create compressed image after building quadtree at given depth
   private BufferedImage createImageRecursion(QuadtreeNode node, int depth) {

      if (node.leaf || node.depth == depth || node.children == null) { 
         return node.compress(); 
      } 

      int w = node.patch[2], h = node.patch[3]; 
      BufferedImage rImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB); 
      Graphics2D g = rImage.createGraphics(); 

      BufferedImage tl = createImageRecursion(node.children.get(0), depth); 
      BufferedImage tr = createImageRecursion(node.children.get(1), depth); 
      BufferedImage bl = createImageRecursion(node.children.get(2), depth); 
      BufferedImage br = createImageRecursion(node.children.get(3), depth); 

      g.drawImage(tl, 0, 0, null);  
      g.drawImage(tr, w/2, 0, null); 
      g.drawImage(bl, 0, h/2, null); 
      g.drawImage(br, w/2, h/2, null); 
      g.dispose();

      return rImage;

   }

   // Instantly outputs file of fully compressed image
   public File renderImage(String path, String fileName, String fileType) throws IOException {

      File output = new File(path + "\\" + fileName + "_compressed." + fileType);
      BufferedImage compressedImage = createImage(treeDepth);
      ImageIO.write(compressedImage, fileType, output);

      return output;
   }

   // Outputs gif of full compression process
   public File renderGif(String path, String fileName) throws FileNotFoundException, IOException {
      
      File file = new File(path + "\\" + fileName + "_compressed.gif");
      ImageOutputStream output = new FileImageOutputStream(file);
      GifSequenceWriter writer = new GifSequenceWriter(
         output, 
         BufferedImage.TYPE_INT_RGB, 
         500, 
         true
      );

      for (int i = 0; i <= treeDepth; i++) {
         BufferedImage image = createImage(i);
         writer.writeToSequence(image);
         if (i == treeDepth) for (int j = 0; j < 4; j++) writer.writeToSequence(image);
      }

      writer.close();
      output.close();

      return file;

   }

}