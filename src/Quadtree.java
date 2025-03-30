import java.util.ArrayList;
import java.awt.image.BufferedImage;

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
      
      // Reached leaf node
      if (node.error <= this.ERR_THRESHOLD || node.size <= this.MIN_SIZE) {
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

   // Get all leaf nodes (smallest blocks after compression) at a given depth
   public ArrayList<QuadtreeNode> getLeaves(int depth) {
      ArrayList<QuadtreeNode> leaves = new ArrayList<>();

      // stuff

      return leaves;

   }

}
