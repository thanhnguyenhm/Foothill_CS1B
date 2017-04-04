/*Source code for Assignment 8 - Managing a Sorted java.util List and java.util Stack
Part 2:  stack application ===============================================
Written by Thanh Nguyen - 02/29/2016 */
import java.util.*;

public class Foothill
{
   static Card generateRandomCard()
   {
      Card.Suit suit;
      char val;

      int suitSelector, valSelector;

      // get random suit and value
      suitSelector = (int) (Math.random() * 4);
      valSelector = (int) (Math.random() * 14);

      // pick suit
      suit = Card.Suit.values()[suitSelector];
      
      // pick value
      valSelector++;   // put in range 1-14
      switch(valSelector)
      {
         case 1:
            val = 'A';
            break;
         case 10:
            val = 'T';
            break;
         case 11:
            val = 'J';
            break;
         case 12:
            val = 'Q';
            break;
         case 13:
            val = 'K';
            break;
         case 14:
            val = 'X';
            break;
         default:
            val = (char)('0' + valSelector);   // simple way to turn n into 'n'   
      }
      return new Card(val, suit);
   }
   
   static void insert(LinkedList<Card> myList, Card x)
   {
      ListIterator<Card> iter;
      Card listX;

      for (iter = myList.listIterator(); iter.hasNext(); )
      {
        listX = iter.next();
        if (x.compareTo(listX) <= 0)
        {
           iter.previous(); // back up one
           break;
        }
      }
      iter.add(x);
   }
   
   static void showList(LinkedList<Card> myList)
   {
      ListIterator<Card> iter;
      
      System.out.println( "\n_____Here's the List_______" );
      for (iter = myList.listIterator(); iter.hasNext(); )
         System.out.print("[" + iter.next() + "] \n");
      System.out.println( "_____That's all!_______\n" );
   }
   
   static boolean remove(LinkedList<Card> myList, Card x)
   {
      ListIterator<Card> iter;

      for (iter = myList.listIterator(); iter.hasNext(); )
         if (iter.next() == x)
         {
            iter.remove();
            return true;   // we found, we removed, we return
         }
      return false;
   }
   
   static boolean removeAll(LinkedList<Card> myList, Card x)
   {
      ListIterator<Card> iter;
      
      for (iter = myList.listIterator(); iter.hasNext(); )
         if (!remove(myList, x))
            return false;     
      return true;
   }   
   //main--------------------
   public static void main(String[] args)
   {
      LinkedList<Card> list1 = new LinkedList<Card>();
      LinkedList<Card> list2 = new LinkedList<Card>();
      int k;
      Card x, y;
      
      // ranking order for game "Tchoo Da Dee" ("the big two"):
      char[] valueOrder = {'3', '4', '5', '6', '7', '8', '9', 'T', 'J',
         'Q', 'K', 'A', '2'};
      Card.Suit[] suitOrder = {Card.Suit.diamonds, Card.Suit.clubs, 
            Card.Suit.hearts, Card.Suit.spades};

      for (k = 0; k < 20; k++)
      {
         x = generateRandomCard(); 
         y = new Card(x);
         insert(list1, x);      
         CardWOrderStk.pushOrdering();
         Card.setRankingOrder(valueOrder, suitOrder, 13);         
         insert(list2, y);         
         CardWOrderStk.popOrdering();
      }          
      // should be sorted
      showList(list1);
      showList(list2);        
   }//end main
}//end Foothill

//derived class CardWOrderStk
class CardWOrderStk extends Card
{
   //static member
   static Stack<OrderObject> objStk = new Stack<OrderObject>();
   
   //inner class OrderObject
   private static class OrderObject
   {
      char[] valueRanks = { '2', '3', '4', '5', '6', '7', '8', '9',
            'T', 'J', 'Q', 'K', 'A', 'X'};
      Suit[] suitRanks = {Suit.clubs, Suit.diamonds, Suit.hearts,
            Suit.spades};     
   }//end OrderObject
   
   public static void pushOrdering()
   {
      OrderObject obj = new OrderObject();
      objStk.push(obj);
   }
   
   public static boolean popOrdering()
   {  
      OrderObject obj = objStk.pop();
      if(obj == null)
         return false;
      valueRanks = obj.valueRanks;
      suitRanks = obj.suitRanks;
      return true;
   }
   
   
}//end CardWOrderStk

//class Card  ----------------------------------------------------------------
class Card
{   
 // type and constants
 public enum State {deleted, active} // not bool because later we may expand
 public enum Suit { clubs, diamonds, hearts, spades }

 // for sort.  
 static char[] valueRanks = { '2', '3', '4', '5', '6', '7', '8', '9',
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
}

/**********************SAMPLE RUN*****************************************
_____Here's the List_______
[2 of diamonds] 
[3 of clubs] 
[3 of hearts] 
[3 of spades] 
[5 of spades] 
[6 of spades] 
[6 of spades] 
[7 of diamonds] 
[7 of hearts] 
[7 of spades] 
[8 of hearts] 
[8 of spades] 
[9 of diamonds] 
[9 of spades] 
[9 of spades] 
[J of clubs] 
[Q of diamonds] 
[K of diamonds] 
[K of spades] 
[A of diamonds] 
_____That's all!_______


_____Here's the List_______
[3 of clubs] 
[3 of hearts] 
[3 of spades] 
[5 of spades] 
[6 of spades] 
[6 of spades] 
[7 of diamonds] 
[7 of hearts] 
[7 of spades] 
[8 of hearts] 
[8 of spades] 
[9 of diamonds] 
[9 of spades] 
[9 of spades] 
[J of clubs] 
[Q of diamonds] 
[K of diamonds] 
[K of spades] 
[A of diamonds] 
[2 of diamonds] 
_____That's all!_______

**************************END SAMPLE RUN********************************/