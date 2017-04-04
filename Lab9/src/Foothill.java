package Lab9;
/*Source code for Assignment 9 - Implementing Soft Deletion in a General Tree
Written by Thanh Nguyen - 03/06/2016 =================================*/

public class Foothill
{ 
   // -------  main --------------
   public static void main(String[] args) throws Exception
   {
      FHsdTree<String> sceneTree = new FHsdTree<String>();
      FHsdTreeNode<String> tn;
      
      // create a scene in a room
      tn = sceneTree.addChild(null, "room");

      // add three objects to the scene tree
      sceneTree.addChild(tn, "Lily the canine");
      sceneTree.addChild(tn, "Miguel the human");
      sceneTree.addChild(tn, "table");
      // add some parts to Miguel
      tn = sceneTree.find("Miguel the human");

      // Miguel's left arm
      tn = sceneTree.addChild(tn, "torso");
      tn = sceneTree.addChild(tn, "left arm");
      tn =  sceneTree.addChild(tn, "left hand");
      sceneTree.addChild(tn, "thumb");
      sceneTree.addChild(tn, "index finger");
      sceneTree.addChild(tn, "middle finger");
      sceneTree.addChild(tn, "ring finger");
      sceneTree.addChild(tn, "pinky");

      // Miguel's right arm
      tn = sceneTree.find("Miguel the human");
      tn = sceneTree.find(tn, "torso", 0);
      tn = sceneTree.addChild(tn, "right arm");
      tn =  sceneTree.addChild(tn, "right hand");
      sceneTree.addChild(tn, "thumb");
      sceneTree.addChild(tn, "index finger");
      sceneTree.addChild(tn, "middle finger");
      sceneTree.addChild(tn, "ring finger");
      sceneTree.addChild(tn, "pinky");

      // add some parts to Lily
      tn = sceneTree.find("Lily the canine");
      tn = sceneTree.addChild(tn, "torso");
      sceneTree.addChild(tn, "right front paw");
      sceneTree.addChild(tn, "left front paw");
      sceneTree.addChild(tn, "right rear paw");
      sceneTree.addChild(tn, "left rear paw");
      sceneTree.addChild(tn, "spare mutant paw");
      sceneTree.addChild(tn, "wagging tail");

      // add some parts to table
      tn = sceneTree.find("table");
      sceneTree.addChild(tn, "north east leg");
      sceneTree.addChild(tn, "north west leg");
      sceneTree.addChild(tn, "south east leg");
      sceneTree.addChild(tn, "south west leg");

      sceneTree.display();
      System.out.println("\nVirtual Size: " + sceneTree.size());
      System.out.println("\nPhysical Size: " + sceneTree.sizePhysical() + "\n");

      // clone
      FHsdTree<String> ClonedTree = (FHsdTree<String>) sceneTree.clone();

      // remove some nodes
      sceneTree.remove("spare mutant paw");
      sceneTree.remove("Miguel the human");
      sceneTree.remove("an imagined higgs boson");
      
      sceneTree.display();
      System.out.println("\nVirtual Size: " + sceneTree.size());
      
      sceneTree.displayPhysical();
      System.out.println("\nPhysical Size: " + sceneTree.sizePhysical() + "\n");

      sceneTree.collectGarbage();

      sceneTree.displayPhysical();
 
      System.out.println("\nVirtual Size: " + sceneTree.size());
      System.out.println("\nPhysical Size: " + sceneTree.sizePhysical() + "\n");

      // see if the clone worked
      System.out.println("Clone display");
      ClonedTree.display();
      System.out.println("\nClone's Virtual Size: " + ClonedTree.size() + "\n");
   }
}

//class FHsdTree
class FHsdTree<E> implements Cloneable
{
   private int mSize;
   FHsdTreeNode<E> mRoot;
   
   public int sizePhysical() { return mSize; }
   public void displayPhysical() { displayPhysical(mRoot, 0); }
   public void displayPhysical(FHsdTreeNode<E> treeNode, int level) 
   {
      String indent;

      // stop runaway indentation/recursion
      if  (level > (int)blankString.length() - 1)
      {
         System.out.println( blankString + " ... " );
         return;
      }
      
      if (treeNode == null)
         return;

      indent = blankString.substring(0, level);

      // pre-order processing done here ("visit")
      System.out.println( indent + treeNode.data ) ;
      
      // recursive step done here
      displayPhysical( treeNode.firstChild, level + 1 );
      if (level > 0 )
         displayPhysical( treeNode.sib, level );
   }
   public void collectGarbage() { collectGarbage(mRoot); }
   public void collectGarbage(FHsdTreeNode<E> root)   
   {
      if(mSize == 0 || root == null)
         return;
      
      if(root.deleted)  
      {
         removeNode(root);
         return;
      }
         
              
      for(FHsdTreeNode<E> temp = root.firstChild; temp != null; temp = temp.sib)
         collectGarbage(temp);     
   }
   
   public FHsdTree() { clear(); }
   public boolean empty() { return (mSize == 0); }
   public int size() { return size(mRoot); }

   private int size(FHsdTreeNode<E> root)
   {
      int size = 1;
      if(mSize == 0 || root == null)
         return 0;
      
      if(root.deleted)
         return 0;
      
      for(FHsdTreeNode<E> temp = root.firstChild; temp != null; temp = temp.sib)
         if(!temp.deleted)
            size += size(temp);
      return size;
   }

   public void clear() { mSize = 0; mRoot = null; }
   
   public FHsdTreeNode<E> find(E x) { return find(mRoot, x, 0); }
   public boolean remove(E x) { return remove(mRoot, x); }
   public void  display()  { display(mRoot, 0); }
   
   public < F extends Traverser< ? super E > > 
   void traverse(F func)  { traverse(func, mRoot, 0); }
   
   public FHsdTreeNode<E> addChild( FHsdTreeNode<E> treeNode,  E x )
   {
      // empty tree? - create a root node if user passes in null
      if (mSize == 0)
      {
         if (treeNode != null)
            return null; // error something's fishy.  treeNode can't right
         mRoot = new FHsdTreeNode<E>(false, x, null, null, null);
         mRoot.myRoot = mRoot;
         mSize = 1;
         return mRoot;
      }
      if (treeNode == null)
         return null; // error inserting into non_null tree with a null parent
      if (treeNode.myRoot != mRoot)
         return null;  // silent error, node does not belong to this tree

      // push this node into the head of the sibling list; adjust prev pointers
      FHsdTreeNode<E> newNode = new FHsdTreeNode<E>(treeNode.deleted, x, 
         treeNode.firstChild, null, treeNode, mRoot);  // sb, chld, prv, rt
      treeNode.firstChild = newNode;
      if (newNode.sib != null)
         newNode.sib.prev = newNode;
      ++mSize;
      return newNode;  
   }
   
   public FHsdTreeNode<E> find(FHsdTreeNode<E> root, E x, int level)
   {
      FHsdTreeNode<E> retval;

      if (mSize == 0 || root == null)
         return null;

      if (root.data.equals(x))
         if(!root.deleted)
            return root;

      // otherwise, recurse.  don't process sibs if this was the original call
      if ( level > 0 && (retval = find(root.sib, x, level)) != null )
         return retval;
      return find(root.firstChild, x, ++level);
   }
   
   public boolean remove(FHsdTreeNode<E> root, E x)
   {
      FHsdTreeNode<E> tn = null;

      if (mSize == 0 || root == null)
         return false;
     
      if ( (tn = find(root, x, 0)) != null )
      {
         //removeNode(tn);
         tn.deleted = true;
         //mSize--;
         return true;
      }
      return false;
   }
   
   private void removeNode(FHsdTreeNode<E> nodeToDelete )
   {
      if (nodeToDelete == null || mRoot == null)
         return;
      if (nodeToDelete.myRoot != mRoot)
         return;  // silent error, node does not belong to this tree

      // remove all the children of this node
      while (nodeToDelete.firstChild != null)
         removeNode(nodeToDelete.firstChild);

      if (nodeToDelete.prev == null)
         mRoot = null;  // last node in tree
      else if (nodeToDelete.prev.sib == nodeToDelete)
         nodeToDelete.prev.sib = nodeToDelete.sib; // adjust left sibling
      else
         nodeToDelete.prev.firstChild = nodeToDelete.sib;  // adjust parent

      // adjust the successor sib's prev pointer
      if (nodeToDelete.sib != null)
         nodeToDelete.sib.prev = nodeToDelete.prev;
      mSize--;
   }
   
   public Object clone() throws CloneNotSupportedException
   {
      FHsdTree<E> newObject = (FHsdTree<E>)super.clone();
      newObject.clear();  // can't point to other's data

      newObject.mRoot = cloneSubtree(mRoot);
      newObject.mSize = mSize;
      newObject.setMyRoots(newObject.mRoot);
      
      return newObject;
   }
   
   private FHsdTreeNode<E> cloneSubtree(FHsdTreeNode<E> root)
   {
      FHsdTreeNode<E> newNode;
      if (root == null)
         return null;

      // does not set myRoot which must be done by caller
      newNode = new FHsdTreeNode<E>
      (
         root.deleted,
         root.data, 
         cloneSubtree(root.sib), cloneSubtree(root.firstChild),
         null
      );
      
      // the prev pointer is set by parent recursive call ... this is the code:
      if (newNode.sib != null)
         newNode.sib.prev = newNode;
      if (newNode.firstChild != null)
         newNode.firstChild.prev = newNode;
      return newNode;
   }
   
   // recursively sets all myRoots to mRoot
   private void setMyRoots(FHsdTreeNode<E> treeNode)
   {
      if (treeNode == null)
         return;

      treeNode.myRoot = mRoot;
      setMyRoots(treeNode.sib);
      setMyRoots(treeNode.firstChild);
   }
   
   // define this as a static member so recursive display() does not need
   // a local version
   final static String blankString = "                                    ";
   
   // let be public so client can call on subtree
   public void display(FHsdTreeNode<E> root, int level) 
   {
      String indent;

      // stop runaway indentation/recursion
      if  (level > (int)blankString.length() - 1)
      {
         System.out.println( blankString + " ... " );
         return;
      }
      
      if (root == null || root.deleted)
         return;

      indent = blankString.substring(0, level);

      // pre-order processing done here ("visit")
      System.out.println( indent + root.data ) ;
      
      // recursive step done here
      for (FHsdTreeNode<E> temp = root.firstChild; temp != null; temp = temp.sib)
         if(!temp.deleted)
            display(temp, level + 1);
   }
      
   // often helper of typical public version, but also callable by on subtree
   public <F extends Traverser<? super E>> 
   void traverse(F func, FHsdTreeNode<E> treeNode, int level)
   {
      if (treeNode == null)
         return;

      func.visit(treeNode.data);
      
      // recursive step done here
      traverse( func, treeNode.firstChild, level + 1);
      if (level > 0 )
         traverse( func,  treeNode.sib, level);
   }
}

//class FHsdTreeNode
class FHsdTreeNode<E>
{
   //new private member
   boolean deleted = false;
   
   // use protected access so the tree, in the same package, 
   // or derived classes can access members 
   protected FHsdTreeNode<E> firstChild, sib, prev;
   protected E data;
   protected FHsdTreeNode<E> myRoot;  // needed to test for certain error

   public FHsdTreeNode(boolean del, E d, FHsdTreeNode<E> sb, FHsdTreeNode<E> chld, FHsdTreeNode<E> prv )
   {
      deleted = false;
      firstChild = chld; 
      sib = sb;
      prev = prv;
      data = d;
      myRoot = null;
   }
   
   public FHsdTreeNode()
   {
      this(false, null, null, null, null);
   }
   
   public E getData() { return data; }

   // for use only by FHtree (default access)
   protected FHsdTreeNode(boolean del, E d, FHsdTreeNode<E> sb, FHsdTreeNode<E> chld, 
      FHsdTreeNode<E> prv, FHsdTreeNode<E> root )
   {
      this(del, d, sb, chld, prv);
      myRoot = root;
   }
}

//Traverser
interface Traverser<E>
{
   public void visit(E x);
}

/***********************SAMPLE RUN***********************************
 * room
 table
  south west leg
  south east leg
  north west leg
  north east leg
 Miguel the human
  torso
   right arm
    right hand
     pinky
     ring finger
     middle finger
     index finger
     thumb
   left arm
    left hand
     pinky
     ring finger
     middle finger
     index finger
     thumb
 Lily the canine
  torso
   wagging tail
   spare mutant paw
   left rear paw
   right rear paw
   left front paw
   right front paw

Virtual Size: 30

Physical Size: 30

room
 table
  south west leg
  south east leg
  north west leg
  north east leg
 Lily the canine
  torso
   wagging tail
   left rear paw
   right rear paw
   left front paw
   right front paw

Virtual Size: 13
room
 table
  south west leg
  south east leg
  north west leg
  north east leg
 Miguel the human
  torso
   right arm
    right hand
     pinky
     ring finger
     middle finger
     index finger
     thumb
   left arm
    left hand
     pinky
     ring finger
     middle finger
     index finger
     thumb
 Lily the canine
  torso
   wagging tail
   spare mutant paw
   left rear paw
   right rear paw
   left front paw
   right front paw

Physical Size: 30

room
 table
  south west leg
  south east leg
  north west leg
  north east leg
 Lily the canine
  torso
   wagging tail
   left rear paw
   right rear paw
   left front paw
   right front paw

Virtual Size: 13

Physical Size: 13

Clone display
room
 table
  south west leg
  south east leg
  north west leg
  north east leg
 Miguel the human
  torso
   right arm
    right hand
     pinky
     ring finger
     middle finger
     index finger
     thumb
   left arm
    left hand
     pinky
     ring finger
     middle finger
     index finger
     thumb
 Lily the canine
  torso
   wagging tail
   spare mutant paw
   left rear paw
   right rear paw
   left front paw
   right front paw

Clone's Virtual Size: 30
*************************END SAMPLE RUN***********************************/