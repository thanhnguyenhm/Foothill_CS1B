/*Source code for Lab assignment 1
 Written by Thanh Nguyen 01/10/16

public class Foothill
{
   public static void main(String[] args)
   {
      //create 4 cards
      Card card1 = new Card();                    //card 1: valid
      
      Card card2 = new Card('X', Card.Suit.clubs);     //card 2: invalid 
      card2.set('T', Card.Suit.hearts);                //change to valid
      
      Card card3 = new Card('8');                 //card 3: valid
      card3.set('M', Card.Suit.diamonds);              //change to invalid
      
      Card card4 = new Card('2', Card.Suit.clubs);  //card 4: valid
      
      //create a Hand object
      Hand hand1 = new Hand();
      
      //taking cards
      int k = 0;
      while(k < Hand.MAX_CARDS)
      {
         if(k < Hand.MAX_CARDS)
         {
            hand1.takeCard(card1);
            k++;
         }      
         if(k < Hand.MAX_CARDS)
         {
            hand1.takeCard(card2);
            k++;
         }     
         if(k < Hand.MAX_CARDS)
         {
            hand1.takeCard(card3);
            k++;
         }     
         if(k < Hand.MAX_CARDS)
         {
            hand1.takeCard(card4);
            k++;
         }          
      }
      
      //display hand
      System.out.println("Hand full\nAfter deal");      
      System.out.println(hand1.toString());
                  
      //inspect cards
      System.out.println("\nTesting inspectCard()");
      System.out.println(hand1.inspectCard(1).toString()); //first card
      System.out.println(hand1.inspectCard(50).toString()); //last card
      System.out.println(hand1.inspectCard(51).toString()); //invalid k
      
      //playing cards
      while(hand1.getNumCards() != 0)
      {         
         System.out.println("Playing " + hand1.playCard());
      }     
      
      //display empty hand when all cards was played
      System.out.println("\nAfter playing all cards\n"
            + hand1.toString());
          
   }
}

//The Card class
class Card
{    
   //private member data
   private char value;
   private Suit suit;
   private boolean errorFlag;
   
   //public enum type
   enum Suit
   {
      clubs,
      diamonds,
      hearts,
      spades
   }
   
   //constructors
   Card(char value, Suit suit)
   {
      set(value, suit);
   }

   Card(char value)
   {
      this(value, Suit.spades);
   }

   Card()
   {
      this('A', Suit.spades);
   }
   
   //mutator
   public boolean set(char value, Suit suit)
   {
      char myChar;

      //assign suit
      this.suit = suit;
      
      myChar = Character.toUpperCase(value);
      
      //test char
      if (isValid(value, suit))
      {
         this.value = myChar;
         errorFlag = false;
      }        
      else
      {
         errorFlag = true;
      }
      
      return errorFlag;
   }
   
   //accessors
   char getValue() { return value; }
   Suit getSuit() { return suit; }
   boolean getErrorFlag() {return errorFlag; }
   
   //stringizer
   public String toString()
   {
      String str;

      if(errorFlag == true)
         str = "[invalid]";
      else
         str = String.valueOf(value) + " of " + suit;
      
      return str;
   }
   
   boolean equals(Card card)
   {
      if(this.suit == card.suit &&
         this.value == card.value)
         return true;
      else 
         return false;
   }
   
   //private method
   private boolean isValid(char value, Suit suit)
   {
      if (value == 'A' || value == 'K' || value == 'Q' || value == 'J'
            || value == 'T' || (value >= '2' && value <= '9'))
         return true;
      else
         return false;
   }   
}

//The Hand Class
class Hand
{
   //static class constant
   public static final int MAX_CARDS = 50;
   
   //private member data
   private Card[] myCards = new Card[MAX_CARDS];
   private int numCards;
   
   //default constructor
   Hand()
   {
      numCards = 0;
   }
   
   void resetHand()
   {
      numCards = 0;
   }
   
   boolean takeCard(Card card)
   {
      if(numCards > MAX_CARDS)
         return false;
      else
      {
         myCards[numCards] = new Card(card.getValue(), card.getSuit());
         numCards++;
         return true;
      }              
   }
   
   Card playCard()
   {
      Card playCard = new Card(myCards[numCards - 1].getValue(),
            myCards[numCards - 1].getSuit());
      
      //remove card
      numCards--;
      
      return playCard;
   }
   
   public String toString()
   {
      String myStr = "Hand = ( "; 
      
      int k = 0;
            
      if(k < numCards)
      {
         while(k < numCards - 1)
         {
            myStr += myCards[k].toString() + ", ";
            k++;
         }
         myStr += myCards[k].toString() + " ";
      }
      
      myStr += ")";
      
      return myStr;         
   }
   
   public int getNumCards() { return numCards; }
   
   Card inspectCard(int k)
   {
      Card theCard = new Card();
      
      if(k > MAX_CARDS || k <= 0) //invalid k
         theCard.set('L', Card.Suit.hearts); //return bad card
      else
         theCard.set(myCards[k - 1].getValue(), myCards[k - 1].getSuit());
      
      return theCard;
   }
}*/

//Solution
//CS 1B
//Assignment #1 - Instructor Solution - Phase 1:  Card Class ============

/*public class Foothill
{
 public static void main(String[] args)
 {
    Card card1 = new Card(),
          card2 = new Card('x'), card3 = new Card('j', Card.Suit.clubs);  

    // show initial values
    System.out.println( card1 + "\n" + card2 + "\n" +
          card3 + "\n");

    // make some changes
    card2.set('Q', Card.Suit.spades);     // turns card2 good
    card1.set('V', Card.Suit.diamonds);    // turns card1 bad

    System.out.println( card1 + "\n" + card2 + "\n" +
          card3 + "\n");
 }
}*/

/*//Card class --------------------------------------------------------------------
class Card
{   
 // type and constants
 public enum Suit { clubs, diamonds, hearts, spades }

 // private data
 private char value;
 private Suit suit;
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

    // convert to uppercase to simplify
    upVal = Character.toUpperCase(value);

    if ( !isValid(upVal, suit))
    {
       errorFlag = true;
       return false;
    }
    
    // else implied
    errorFlag = false;
    this.value = upVal;
    this.suit = suit;
    return true;
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

 public boolean getErrorFlag()
 {
    return errorFlag;
 }

 public boolean equals(Card card)
 {
    if (this.value != card.value)
       return false;
    if (this.suit != card.suit)
       return false;
    if (this.errorFlag != card.errorFlag)
       return false;
    return true;
 }

 // stringizer
 public String toString()
 {
    String retVal;

    if (errorFlag)
       return "** illegal **";

    // else implied

    retVal =  String.valueOf(value);
    retVal += " of ";
    retVal += String.valueOf(suit);

    return retVal;
 }

 // helper
 private boolean isValid(char value, Suit suit)
 {
    char upVal;

    // convert to uppercase to simplify (need #include <cctype>)
    upVal = Character.toUpperCase(value);

    // check for validity
    if (
          upVal == 'A' || upVal == 'K'
          || upVal == 'Q' || upVal == 'J'
          || upVal == 'T'
          || (upVal >= '2' && upVal <= '9')
          )
       return true;
    else
       return false;
 }
}*/

/* ---------------------- run Card class test ------------------
A of spades
** illegal **
J of clubs

** illegal **
Q of spades
J of clubs
--------------------------------------------------------------- */



//CS 1B
//Assignment #1 - Instructor Solution - Phase 2:  Hand Class ============

public class Foothill
{
 public static void main(String[] args)
 {
    Card card1 = new Card('3', Card.Suit.clubs),
          card2 = new Card('T', Card.Suit.clubs),
          card3 = new Card('9', Card.Suit.hearts),
          playedCard;      

    Hand hand = new Hand();

    while (true )
    {
       if ( ! hand.takeCard(card1) )
       {
          System.out.println( "Hand full" );
          break;
       }
       if ( ! hand.takeCard(card2) )
       {
          System.out.println( "Hand full" );
          break;
       }
       if ( ! hand.takeCard(card3) )
       {
          System.out.println( "Hand full" );
          break;
       }
    }

    System.out.println("After deal\n" +  hand.toString() +"\n");

    System.out.println("Testing inspectCard()");
    System.out.println(hand.inspectCard(5).toString());
    System.out.println(hand.inspectCard(200).toString());

    while ( hand.getNumCards() > 0 )
       System.out.println("Playing " + hand.playCard().toString());

    System.out.println("\nAfter playing all cards\n"+ hand.toString() );
 }
}

//Card class --------------------------------------------------------------------
class Card
{   
 // type and constants
 public enum Suit { clubs, diamonds, hearts, spades }

 // private data
 private char value;
 private Suit suit;
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

    // convert to uppercase to simplify
    upVal = Character.toUpperCase(value);

    if ( !isValid(upVal, suit))
    {
       errorFlag = true;
       return false;
    }

    // else implied
    errorFlag = false;
    this.value = upVal;
    this.suit = suit;
    return true;
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

 public boolean getErrorFlag()
 {
    return errorFlag;
 }

 public boolean equals(Card card)
 {
    if (this.value != card.value)
       return false;
    if (this.suit != card.suit)
       return false;
    if (this.errorFlag != card.errorFlag)
       return false;
    return true;
 }

 // stringizer
 public String toString()
 {
    String retVal;

    if (errorFlag)
       return "** illegal **";

    // else implied

    retVal =  String.valueOf(value);
    retVal += " of ";
    retVal += String.valueOf(suit);

    return retVal;
 }

 // helper
 private boolean isValid(char value, Suit suit)
 {
    char upVal;

    // convert to uppercase to simplify (need #include <cctype>)
    upVal = Character.toUpperCase(value);

    // check for validity
    if (
          upVal == 'A' || upVal == 'K'
          || upVal == 'Q' || upVal == 'J'
          || upVal == 'T'
          || (upVal >= '2' && upVal <= '9')
          )
       return true;
    else
       return false;
 }
}

//class Hand  ----------------------------------------------------------------
class Hand
{
 public static final int MAX_CARDS_PER_HAND = 4;  // should cover any game

 private Card[] myCards;
 private int numCards;

 //constructor
 public Hand()
 {
    // careful - we are only allocating the references
    myCards = new Card[MAX_CARDS_PER_HAND];
    resetHand();
 }

 // mutators
 public void resetHand() { numCards = 0; }

 public boolean takeCard(Card card)
 {
    if (numCards >= MAX_CARDS_PER_HAND)
       return false;

    // be frugal - only allocate when needed
    if (myCards[numCards] == null)
       myCards[numCards] = new Card();

    // don't just assign:  mutator assures active/undeleted      
    myCards[numCards++].set( card.getVal(), card.getSuit() );
    return true;
 }

 public Card playCard()
 {
    // always play  highest card in array.  client will prepare this position.
    // in rare case that client tries to play from a spent hand, return error

    Card errorReturn = new Card('E', Card.Suit.spades); // in rare cases

    if (numCards == 0)
       return errorReturn;
    else
       return myCards[--numCards];
 }

 // accessors
 public String toString()
 {
    int k;
    String retVal = "Hand =  ( ";

    for (k = 0; k < numCards; k++)
    {
       retVal += myCards[k].toString();
       if (k < numCards - 1)
          retVal += ", ";
    }
    retVal += " )";
    return retVal;
 }

 int getNumCards()
 {
    return numCards;
 }

 Card inspectCard(int k)
 {
    // return copy of card at position k.
    // if client tries to access out-of-bounds card, return error

    Card errorReturn = new Card('E', Card.Suit.spades); // in rare cases

    if (k < 0 || k >= numCards)
       return errorReturn;
    else
       return myCards[k];
 }
}

/***********************SAMPLE RUN*******************************
 * 
 * Hand full
After deal
Hand = ( A of spades, T of hearts, 8 of diamonds, 2 of clubs, A of spades, T of 
hearts, 8 of diamonds, 2 of clubs, A of spades, T of hearts, 8 of diamonds, 2 of
 clubs, A of spades, T of hearts, 8 of diamonds, 2 of clubs, A of spades, T of h
earts, 8 of diamonds, 2 of clubs, A of spades, T of hearts, 8 of diamonds, 2 of 
clubs, A of spades, T of hearts, 8 of diamonds, 2 of clubs, A of spades, T of he
arts, 8 of diamonds, 2 of clubs, A of spades, T of hearts, 8 of diamonds, 2 of c
lubs, A of spades, T of hearts, 8 of diamonds, 2 of clubs, A of spades, T of hea
rts, 8 of diamonds, 2 of clubs, A of spades, T of hearts, 8 of diamonds, 2 of cl
ubs, A of spades, T of hearts )

Testing inspectCard()
A of spades
T of hearts
[invalid]
Playing T of hearts
Playing A of spades
Playing 2 of clubs
Playing 8 of diamonds
Playing T of hearts
Playing A of spades
Playing 2 of clubs
Playing 8 of diamonds
Playing T of hearts
Playing A of spades
Playing 2 of clubs
Playing 8 of diamonds
Playing T of hearts
Playing A of spades
Playing 2 of clubs
Playing 8 of diamonds
Playing T of hearts
Playing A of spades
Playing 2 of clubs
Playing 8 of diamonds
Playing T of hearts
Playing A of spades
Playing 2 of clubs
Playing 8 of diamonds
Playing T of hearts
Playing A of spades
Playing 2 of clubs
Playing 8 of diamonds
Playing T of hearts
Playing A of spades
Playing 2 of clubs
Playing 8 of diamonds
Playing T of hearts
Playing A of spades
Playing 2 of clubs
Playing 8 of diamonds
Playing T of hearts
Playing A of spades
Playing 2 of clubs
Playing 8 of diamonds
Playing T of hearts
Playing A of spades
Playing 2 of clubs
Playing 8 of diamonds
Playing T of hearts
Playing A of spades
Playing 2 of clubs
Playing 8 of diamonds
Playing T of hearts
Playing A of spades

After playing all cards
Hand = ( )

******************************END SAMPLE RUN************************ */

