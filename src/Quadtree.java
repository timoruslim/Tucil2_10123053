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
   public int mode;

   // Compression Constants
   public int ERR_THRESHOLD;
   public int MIN_SIZE;

   // Constructor
   public Quadtree(BufferedImage image, int ERR_THRESHOLD, int MIN_SIZE, int mode) {
      this.root = new QuadtreeNode(image, 0, mode);
      this.mode = mode;
      this.ERR_THRESHOLD = ERR_THRESHOLD;
      this.MIN_SIZE = MIN_SIZE;

      buildTree(root);
   }

   // Build quadtree to compress image (divide)
   public void buildTree(QuadtreeNode node) {
      
      // Make into leaf node if sufficient
      int size = node.image.getWidth() * node.image.getHeight();
      if (node.error <= this.ERR_THRESHOLD || size <= this.MIN_SIZE || size/4 < this.MIN_SIZE) {
         if (node.depth > this.treeDepth) this.treeDepth = node.depth; 
         node.leaf = true; 
         return; 
      }

      // Recursion 
      node.divide();
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
         return node.cImage; 
      } 

      int w = node.image.getWidth(), h = node.image.getHeight(); 
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
   public void renderImage(String path, String fileType) throws IOException {
      File output = new File(path);
      BufferedImage compressedImage = createImage(treeDepth);
      ImageIO.write(compressedImage, fileType, output);
   }

   // Outputs gif of full compression process
   public void renderGif(String path) throws FileNotFoundException, IOException {
      
      ImageOutputStream output = new FileImageOutputStream(new File(path));
      GifSequenceWriter writer = new GifSequenceWriter(output, BufferedImage.TYPE_INT_RGB, 500, true);

      for (int i = 0; i < treeDepth; i++) {
         BufferedImage image = createImage(i);
         writer.writeToSequence(image);
         if (i == treeDepth - 1) {
            for (int j = 0; j < 4; j++) {
               writer.writeToSequence(image);
            }
         }
      }

      writer.close();
      output.close();

   }

}