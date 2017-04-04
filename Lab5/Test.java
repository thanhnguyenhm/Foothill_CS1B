/*Source code for Assignment 6 - Queues Using Inheritance ============
 Created by Thanh Nguyen 02/16/16*/

public class Test
{
   public static void main (String[] args)
   {
      CardQueue cardQueue1 = new CardQueue();
      Card card1 = new Card('A', Card.Suit.spades);
      Card card2 = new Card('2', Card.Suit.hearts);
      Card card3 = new Card('J', Card.Suit.diamonds);
      Card card4 = new Card('T', Card.Suit.clubs);
      Card theCard;

      cardQueue1.addCard(card1);
      cardQueue1.addCard(card2);
      cardQueue1.addCard(card3);
      cardQueue1.addCard(card4);
      
      //toString queue
      System.out.println("Queue1: " + cardQueue1.toString());
      System.out.println();
      
      try
      {
         while((theCard  = cardQueue1.removeCard()) != null)               
            System.out.println( "removing..." + theCard.toString());
      } 
      catch (QueueEmptyException e)
      {
         System.out.println("Queue empty");
      }
   }
}

//******************base class Node****************************
class Node
{
// data (protected allows only certain other classes to access "next" directly)
protected Node next;

// constructor
public Node()
{
  next = null;
}

// console display
public String toString()
{
  String myStr = "(generic node) "; 
  return myStr;
}
}//end class Node

//******************base class Queue****************************
class Queue
{
 // pointer to first node in stack
 private Node top;
 
 // constructor
 public Queue()
 {
    top = null;
 }
 
 public void add(Node newNode)
 {   
    if (newNode == null) 
       return;   // emergency return
    newNode.next = top;
    top = newNode;
 }  
 
 public Node remove() throws QueueEmptyException
 {
    Node temp;
    
    temp = top;
    if (top != null)
    {
       top = top.next; 
       temp.next = null; // don't give client access to stack!
    }
    return temp;       
 }

 // console display
 public String toString()
 {
    Node p;
    String myStr = "";
    
    // Display all the nodes in the stack
    for( p = top; p != null; p = p.next )
       myStr += p.toString();
    
    return myStr;
 }
}//end base class Queue

class QueueEmptyException extends Exception
{
}

//derived class CardNode------------------------------------------------------
class CardNode extends Node
{
   // additional data for subclass
   private Card theCard;
   
   // constructor
   public CardNode(Card card)
   {
      //super();  // constructor call to base class
      theCard = card;
   }
   
   // accessor
   public Card getCard()
   {
      return theCard;
   }

   // overriding toString()
   public String toString()
   {
      String myStr = theCard.toString() + "/";
      return myStr;
   }
}//end CardNode

//derived class CardQueue----------------------------------------------------
class CardQueue extends Queue
{
   public void addCard(Card theCard)
   {
      // create a new CardNode
      CardNode cn = new CardNode(theCard);
   
      // add the Node onto the queue (base class call)
      super.add(cn);
   }

   public Card removeCard() throws QueueEmptyException
   {
      // remove a node
      CardNode cn = (CardNode)remove();
      if (cn == null)
         throw new QueueEmptyException();
      else
         return cn.getCard();
   }
}//end class CardQueue

/*//class Card  ----------------------------------------------------------------
class Card
{   
 // type and constants
 public enum State {deleted, active} // not bool because later we may expand
 public enum Suit { clubs, diamonds, hearts, spades }

 // for sort.  
 public static char[] valueRanks = { '2', '3', '4', '5', '6', '7', '8', '9',
    'T', 'J', 'Q', 'K', 'A', 'X'};
 static Suit[] suitRanks = {Suit.clubs, Suit.diamonds, Suit.hearts,
    Suit.spades};
 static int numValsInOrderingArray = 14;  // 'X' = Joker

 // private data
 private char value;
 private Suit suit;
 State state;
 boolean errorFlag;

 // 4 overloaded constructors
 public Card(char value, Suit suit)
 {
    set(value, suit);
 }

 public Card(char value)
 {
    this(value, Suit.spades);
 }
 public Card()
 {
    this('A', Suit.spades);
 }
 // copy constructor
 public Card(Card card)
 {
    this(card.value, card.suit);
 }

 // mutators
 public boolean set(char value, Suit suit)
 {
    char upVal;            // for upcasing char

    // can't really have an error here
    this.suit = suit;  

    // convert to uppercase to simplify
    upVal = Character.toUpperCase(value);

    // check for validity
    if (
          upVal == 'A' || upVal == 'K'
          || upVal == 'Q' || upVal == 'J'
          || upVal == 'T' || upVal == 'X'
          || (upVal >= '2' && upVal <= '9')
          )
    {
       errorFlag = false;
       state = State.active;
       this.value = upVal;
    }
    else
    {
       errorFlag = true;
       return false;
    }

    return !errorFlag;
 }

 public void setState( State state)
 {
    this.state = state;
 }

 // accessors
 public char getVal()
 {
    return value;
 }

 public Suit getSuit()
 {
    return suit;
 }

 public State getState()
 {
    return state;
 }

 public boolean getErrorFlag()
 {
    return errorFlag;
 }

 // stringizer
 public String toString()
 {
    String retVal;

    if (errorFlag)
       return "** illegal **";
    if (state == State.deleted)
       return "( deleted )";

    // else implied

    if (value != 'X')
    {
       // not a joker
       retVal =  String.valueOf(value);
       retVal += " of ";
       retVal += String.valueOf(suit);
    }
    else
    {
       // joker
       retVal = "joker";

       if (suit == Suit.clubs)
          retVal += " 1";
       else if (suit == Suit.diamonds)
          retVal += " 2";
       else if (suit == Suit.hearts)
          retVal += " 3";
       else if (suit == Suit.spades)
          retVal += " 4";
    }

    return retVal;
 }

 public boolean equals(Card card)
 {
    if (this.value != card.value)
       return false;
    if (this.suit != card.suit)
       return false;
    if (this.errorFlag != card.errorFlag)
       return false;
    if (this.state != card.state)
       return false;
    return true;
 }

 // sort member methods
 public int compareTo(Card other)
 {
    if (this.value == other.value)
       return ( getSuitRank(this.suit) - getSuitRank(other.suit) );

    return (
          getValueRank(this.value)
          - getValueRank(other.value)
          );
 }

 public static void setRankingOrder(
       char[] valueOrderArr, Suit[] suitOrdeArr,
       int numValsInOrderingArray )
 {
    int k;

    // expects valueOrderArr[] to contain only cards used per pack,
    // including jokers, needed to define order for the game environment

    if (numValsInOrderingArray < 0 || numValsInOrderingArray > 14)
       return;

    Card.numValsInOrderingArray = numValsInOrderingArray;

    for (k = 0; k < numValsInOrderingArray; k++)
       Card.valueRanks[k] = valueOrderArr[k];

    for (k = 0; k < 4; k++)
       Card.suitRanks[k] = suitOrdeArr[k];
 }

 public static int getSuitRank(Suit st)
 {
    int k;

    for (k = 0; k < 4; k++)
       if (suitRanks[k] == st)
          return k;

    // should not happen
    return 0;
 }

 public  static int getValueRank(char val)
 {
    int k;

    for (k = 0; k < numValsInOrderingArray; k++)
       if (valueRanks[k] == val)
          return k;

    // should not happen
    return 0;
 }

 public static void arraySort(Card[] array, int arraySize)
 {
    for (int k = 0; k < arraySize; k++)
       if (!floatLargestToTop(array, arraySize - 1 - k))
          return;
 }

 private static boolean floatLargestToTop(Card[] array, int top)
 {
    boolean changed = false;
    Card temp;

    for (int k = 0; k < top; k++)
       if (array[k].compareTo(array[k+1]) > 0)
       {
          temp = array[k];
          array[k] = array[k+1];
          array[k+1] = temp;
          changed = true;
       };
       return changed;
 }
}*/



/*import javax.swing.Icon;
import javax.swing.ImageIcon;

public class Test
{
   static final int NUM_CARD_IMAGES = 57; // 52 + 4 jokers + 1 back-of-card image
   static String[] icon = new String[NUM_CARD_IMAGES];
   public static void main(String[] args)
   {
      String filename = "images/";
      
      for(int i = 0; i < NUM_CARD_IMAGES; i++)
      {
         for (int k = 0; k < 4; k++)
         {
            String st = "";
            int j;
            char val;
            
            // set the suit for this loop pass
            switch(k)
            {
            case 0:
               st = "C"; break;
            case 1:
               st = "D"; break;
            case 2:
               st = "H"; break;
            case 3:
               st = "S"; break;
           }

         // now set all the values for this suit
         filename += "A" + st + ".gif";
         icon[13*k] = new String(filename);
         
         for (val = '2', j = 1; val <= '9'; val++, j++)
            icon[13*k+j] = filename + val + st + ".gif";        
         icon[13*k+9] = filename + "T" + st + ".gif";
         icon[13*k+10] = filename + "J" + st + ".gif";
         icon[13*k+11] = filename + "Q" + st + ".gif";  
         icon[13*k+12] = filename + "K" + st + ".gif";
         }
      }
      for(int i = 0; i < NUM_CARD_IMAGES; i++)
         System.out.println(icon[i] + "\n");
   }//end main
}//end class



String filename = "images/";

for(int i = 0; i < NUM_CARD_IMAGES; i++)
{
   for (int k = 0; k < 4; k++)
   {
      String st = "";
      int j;
      char val;
      
      // set the suit for this loop pass
      switch(k)
      {
      case 0:
         st = "C"; break;
      case 1:
         st = "D"; break;
      case 2:
         st = "H"; break;
      case 3:
         st = "S"; break;
     }

   // now set all the values for this suit
   filename += "A" + st + ".gif";
   icon[i] = new ImageIcon(filename);
   
   for (val = '2', j = 1; val <= '9'; val++, j++)
      icon[i] = new ImageIcon(filename + val + st + ".gif");        
   icon[i] = new ImageIcon(filename + "T" + st + ".gif");
   icon[i] = new ImageIcon(filename + "J" + st + ".gif");
   icon[i] = new ImageIcon(filename + "Q" + st + ".gif");  
   icon[i] = new ImageIcon(filename + "K" + st + ".gif");
   }
}*/