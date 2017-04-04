//Solution
/*
 /CS 1B
//Assignment #2 - Instructor Solution - Phase 1:  Deck Class ============

public class Foothill
{
   public static void main(String[] args)
   {
      Deck deck1 = new Deck(2);

      // deal first deck (two packs), unshuffled.
      while (deck1.getNumCards() > 0)
         System.out.print(deck1.dealCard() + " /  ");
      System.out.println("\n");

      // restock the deck and remove some aces
      deck1.init(2);
      deck1.shuffle();

      while (deck1.getNumCards() > 0)
         System.out.print(deck1.dealCard() + " / ");
      System.out.println("\n");

      // deal one pack in the second deck, first unshuffled ...
      Deck deck2 = new Deck();
      while (deck2.getNumCards() > 0)
         System.out.print(deck2.dealCard() + " /  ");
      System.out.println("\n");

      // now shuffled (don't forget to reload and shuffle the deck)
      deck2.init();
      deck2.shuffle();
      while (deck2.getNumCards() > 0)
         System.out.print(deck2.dealCard().toString() + " /  ");
      System.out.println("\n");
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
   public static final int MAX_CARDS_PER_HAND = 100;  // should cover any game

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

//class Deck  ----------------------------------------------------------------
class Deck
{
   // six full decks (without jokers) is enough for about any game
   private static final int MAX_CARDS_PER_DECK = 6 * 52;
   private static Card[] masterPack;  // one 52-Card master for initializing decks

   private Card[] cards;
   private int topCard;
   private int numPacks;

   private static boolean firstTime = true;  // avoid calling allcMstrPck > once

   public Deck(int numPacks)
   {
      allocateMasterPack();  // do not call from init()
      cards = new Card[MAX_CARDS_PER_DECK];
      init(numPacks);
   }

   static private void allocateMasterPack()
   {
      int j, k;
      Card.Suit st;
      char val;

      // we're in static method; only needed once per program: good for whole class
      if ( !firstTime )
         return;
      firstTime = false;

      // allocate
      masterPack = new Card[52];
      for (k = 0; k < 52; k++)
         masterPack[k] = new Card();

      // next set data
      for (k = 0; k < 4; k++)
      {
         // set the suit for this loop pass
         st = Card.Suit.values()[k];

         // now set all the values for this suit
         masterPack[13*k].set('A', st);
         for (val='2', j = 1; val<='9'; val++, j++)
            masterPack[13*k + j].set(val, st);
         masterPack[13*k+9].set('T', st);
         masterPack[13*k+10].set('J', st);
         masterPack[13*k+11].set('Q', st);
         masterPack[13*k+12].set('K', st);
      }
   }

   public Deck()
   {
      this(1);
   }


   // set deck from 1 to 6 packs, perfecly ordered
   public void init(int numPacks)
   {
      int k, pack;

      if (numPacks < 1 || numPacks > 6)
         numPacks = 1;

      // hand over the masterPack cards to our deck
      for (pack = 0; pack < numPacks; pack++)
         for (k = 0; k < 52; k++)
            cards[pack*52 + k] = masterPack[k];

      // if something modified a card, we would be in trouble.  fortunately,
      // we don't expect a card to ever be modified after instantiated
      // in the context of a deck. e.g. state would be for deck set-up only

      this.numPacks = numPacks;
      topCard = numPacks * 52;
   }

   public void init()
   {
      init(1);
   }

   public int getNumCards()
   {
      return topCard;
   }

   public void shuffle()
   {
      Card tempCard;
      int k, randInt;

      // topCard is size of deck
      for (k = 0; k < topCard; k++)
      {
         randInt = (int)(Math.random() * topCard);

         // swap cards k and randInt (sometimes k == randInt:  okay)
         tempCard = cards[k];
         cards[k] = cards[randInt];
         cards[randInt] = tempCard;
      }
   }

   public Card takeACard()
   {
      return new Card();
   }

   public Card dealCard()
   {
      // always deal the topCard.  
      Card errorReturn = new Card('E', Card.Suit.spades); //  in rare cases

      if (topCard == 0)
         return errorReturn;
      else
         return cards[--topCard];
   }

   public Card inspectCard(int k)
   {
      // return copy of card at position k.
      // if client tries to access out-of-bounds card, return error

      Card errorReturn = new Card('E', Card.Suit.spades); //  in rare cases

      if (k < 0 || k >= topCard)
         return errorReturn;
      else
         return cards[k];
   }

   public int numOccurrences(Card card)
   {
      int retVal, k;

      retVal = 0;

      // assumption:  card is a default item:  not deleted and state=active)
      for (k = 0; k < topCard; k++)
      {
         if (inspectCard(k).equals(card))
            retVal++;
      }
      return retVal;
   }
}
 */
/*Source code for Lab assignment 2 - Adding a Deck
 Written by Thanh Nguyen 01/17/16*/

import java.util.Random;
import java.util.Scanner;  

public class Foothill
{
   public static void main(String[] args)
   {
      //Phase 1: The Deck Class/////////////////////////////
      //----------------------------------------------------
      //----------------------------------------------------
      //----------------------------------------------------
      
      //declare a deck with 2 packs
      Deck deck = new Deck(2);     
      
      //dealing cards
      while(deck.getTopCard() != 0)
      {         
         System.out.print(deck.dealCard().toString() + " / ");
      }
      
      //reset the deck
      deck.init(2);
      
      //shuffling
      deck.shuffle();
           
      //re-deal the deck
      System.out.println("\n");
      while(deck.getTopCard() != 0)
      {         
         System.out.print(deck.dealCard().toString() + " / ");
      }
      
      //declare a sing pack deck
      Deck deck1 = new Deck();
      
      //dealing cards
      System.out.println("\n");
      while(deck1.getTopCard() != 0)
      {         
         System.out.print(deck1.dealCard().toString() + " / ");
      }
      
      //reset the deck
      deck1.init(1);
      
      //shuffling
      deck1.shuffle();
           
      //re-deal the deck
      System.out.println("\n");
      while(deck1.getTopCard() != 0)
      {         
         System.out.print(deck1.dealCard().toString() + " / ");
      }
      
      //Phase 2: The Deck and Hand Classes//////////////////
      //----------------------------------------------------
      //----------------------------------------------------
      //----------------------------------------------------
      
      //ask for number of players
      int hands;
      Scanner input = new Scanner(System.in);
      
      System.out.print("\n\nHow many hands? ");
      
      //filter input
      do
      {
         System.out.print("(1 - 10, please): ");
         hands = input.nextInt();
      }
      while (hands < 1 || hands > 10);   
      
      //create a single pack Deck
      Deck deck2 = new Deck();
      
      //create a Hand array
      Hand[] theHand = new Hand[hands];
      for(int i = 0; i < hands; i++)
         theHand[i] = new Hand();
      
      //dealing cards
      System.out.println();
      while(deck2.getTopCard() != 0)
      {  
         for(int i = 0; i < hands; i++)
         {  
            if(deck2.getTopCard() == 0)
               break;
            theHand[i].takeCard(deck2.dealCard());              
         }    
      }
         
      //print hands
      System.out.println("Here are our hands, from unshuffled deck:");
      for(int i = 0; i < hands; i++)
         System.out.println(theHand[i].toString() + "\n");      
      
      //reset the deck
      deck2.init(1);
      
      //shuffling
      deck2.shuffle();
      
      for(int i = 0; i < hands; i++)
         theHand[i].resetHand();
      
      //dealing cards after shuffling      
      while(deck2.getTopCard() != 0)
      {  
         for(int i = 0; i < hands; i++)
         {  
            if(deck2.getTopCard() == 0)
               break;           
            theHand[i].takeCard(deck2.dealCard());              
         }    
      }
      
      //print hands
      System.out.println("Here are our hands, from SHUFFLED deck: ");
      for(int i = 0; i < hands; i++)
         System.out.println(theHand[i].toString() + "\n"); 
                     
      input.close();
   }
}

//the Deck class
class Deck
{
   //public static class constants
   public static final int MAX_CARDS = 6 * 52;
   
   //private static member data
   private static Card[] masterPack = new Card[52];
   
   //private instance member data
   private Card[] card;
   private int topCard;
   private int numPacks;
   
   //public methods
   //constructor
   Deck(int numPacks)
   {
      allocateMasterPack();
      card = new Card[MAX_CARDS];      
      this.numPacks = numPacks;
      topCard = numPacks * 52;
      
      //assign values to the array
      for(int k = 0; k < numPacks; k++)
         for(int i = 0; i < 52; i++)
            card[k * 52 + i] = masterPack[i];   
   }
   
   //default constructor
   Deck()
   {
      this(1);
   }
   
   void init(int numPacks)
   {
      card = new Card[MAX_CARDS];
      topCard = numPacks * 52;
      for(int k = 0; k < numPacks; k++)
         for(int i = 0; i < 52; i++)
            card[k * 52 + i] = masterPack[i];  
   }
   
   //private method
   private static void allocateMasterPack()
   {
      if(masterPack[0] != null) //if masterPack has already been allocated
         return;             //return without doing anything
      
      //assign 52 standard cards to masterPack
      int k, j;
      Card.Suit st;
      char val;
      
      // instantiate the array elements
      for (k = 0; k < 52; k++)
         masterPack[k] = new Card();
    
      for (k = 0; k < 4; k++)
      {
         // set the suit for this loop pass
         switch(k)
         {
         case 0:
            st = Card.Suit.Clubs; break;
         case 1:
            st = Card.Suit.Diamonds; break;
         case 2:
            st = Card.Suit.Hearts; break;
         case 3:
            st = Card.Suit.Spades; break;
         default:
            // should not happen but ...
            st = Card.Suit.Spades; break;
        }

         // now set all the values for this suit
         masterPack[13*k].set('A', st);
         for (val='2', j = 1; val<='9'; val++, j++)
            masterPack[13*k + j].set(val, st);
         masterPack[13*k+9].set('T', st);
         masterPack[13*k+10].set('J', st);
         masterPack[13*k+11].set('Q', st);
         masterPack[13*k+12].set('K', st);                                   
      }
   }
   
   Card dealCard()
   {
      Card errorReturn = new Card('E', Card.Suit.Spades); // in rare cases

      if (topCard == 0)
         return errorReturn;
      else
         return card[--topCard];
   }
   
   Card inspectCard(int k)
   {
      Card errorReturn = new Card('E', Card.Suit.Spades); // in rare cases

      if (k < 0 || k >= topCard)
         return errorReturn;
      else
         return card[k];
   }
   
   void shuffle()
   {           
      for (int i = 0; i < topCard; i++)
      {
         int rand = new Random().nextInt(topCard-i)+i;
         Card temp = card[i];
         card[i] = card[rand];
         card[rand] = temp;        
      }     
   }
         
   int getTopCard() { return topCard; }
}

//The Hand Class
class Hand
{
   //static class constant
   public static final int MAX_CARDS_PER_HAND= 50;
   
   //private member data
   private Card[] myCards = new Card[MAX_CARDS_PER_HAND];
   private int numCards;
   
   //default constructor
   Hand()
   {
      myCards = new Card[MAX_CARDS_PER_HAND];
      resetHand();
   }
   
   void resetHand()
   {
      numCards = 0;
   }
   
   boolean takeCard(Card card)
   {
      if(numCards >= MAX_CARDS_PER_HAND)
         return false;

      if (myCards[numCards] == null)
         myCards[numCards] = new Card();
   
      myCards[numCards++].set( card.getValue(), card.getSuit() );
      return true;         
   }
   
   Card playCard()
   {
      Card errorReturn = new Card('E', Card.Suit.Spades);

      if (numCards == 0)
         return errorReturn;
      else
         return myCards[--numCards];
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
      Card errorReturn = new Card('E', Card.Suit.Spades); // in rare cases

      if (k <= 0 || k > numCards)
         return errorReturn;
      else
         return myCards[k - 1];
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
    Clubs,
    Diamonds,
    Hearts,
    Spades
 }
 
 //constructors
 Card(char value, Suit suit)
 {
    set(value, suit);
 }

 Card(char value)
 {
    this(value, Suit.Spades);
 }

 Card()
 {
    this('A', Suit.Spades);
 }
 
 // copy constructor
 Card(Card card)
 {
    this(card.value, card.suit);
 }
 
 //mutator
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
 
 //accessors
 char getValue() { return value; }
 Suit getSuit() { return suit; }
 boolean getErrorFlag() { return errorFlag; }
 
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
       this.value == card.value &&
       this.errorFlag != card.errorFlag)
       return true;
    else 
       return false;
 }
 
 //private method
 private boolean isValid(char value, Suit suit)
 {
    value = Character.toUpperCase(value);
    
    if (value == 'A' || value == 'K' || value == 'Q' || value == 'J'
          || value == 'T' || (value >= '2' && value <= '9'))
       return true;
    else
       return false;
 }   
}

/*****************************SAMPLE RUN*********************************
K of Spades / Q of Spades / J of Spades / T of Spades / 9 of Spades / 8 of Spade
s / 7 of Spades / 6 of Spades / 5 of Spades / 4 of Spades / 3 of Spades / 2 of S
pades / A of Spades / K of Hearts / Q of Hearts / J of Hearts / T of Hearts / 9 
of Hearts / 8 of Hearts / 7 of Hearts / 6 of Hearts / 5 of Hearts / 4 of Hearts 
/ 3 of Hearts / 2 of Hearts / A of Hearts / K of Diamonds / Q of Diamonds / J of
 Diamonds / T of Diamonds / 9 of Diamonds / 8 of Diamonds / 7 of Diamonds / 6 of
 Diamonds / 5 of Diamonds / 4 of Diamonds / 3 of Diamonds / 2 of Diamonds / A of
 Diamonds / K of Clubs / Q of Clubs / J of Clubs / T of Clubs / 9 of Clubs / 8 o
f Clubs / 7 of Clubs / 6 of Clubs / 5 of Clubs / 4 of Clubs / 3 of Clubs / 2 of 
Clubs / A of Clubs / K of Spades / Q of Spades / J of Spades / T of Spades / 9 o
f Spades / 8 of Spades / 7 of Spades / 6 of Spades / 5 of Spades / 4 of Spades /
 3 of Spades / 2 of Spades / A of Spades / K of Hearts / Q of Hearts / J of Hear
ts / T of Hearts / 9 of Hearts / 8 of Hearts / 7 of Hearts / 6 of Hearts / 5 of 
Hearts / 4 of Hearts / 3 of Hearts / 2 of Hearts / A of Hearts / K of Diamonds /
 Q of Diamonds / J of Diamonds / T of Diamonds / 9 of Diamonds / 8 of Diamonds /
 7 of Diamonds / 6 of Diamonds / 5 of Diamonds / 4 of Diamonds / 3 of Diamonds /
 2 of Diamonds / A of Diamonds / K of Clubs / Q of Clubs / J of Clubs / T of Clu
bs / 9 of Clubs / 8 of Clubs / 7 of Clubs / 6 of Clubs / 5 of Clubs / 4 of Clubs
 / 3 of Clubs / 2 of Clubs / A of Clubs / 

3 of Spades / K of Diamonds / 7 of Clubs / 4 of Hearts / A of Hearts / 8 of Spad
es / 9 of Hearts / 5 of Clubs / J of Spades / Q of Clubs / A of Clubs / Q of Hea
rts / Q of Spades / A of Clubs / 8 of Hearts / J of Hearts / K of Clubs / 4 of C
lubs / K of Spades / K of Hearts / 5 of Diamonds / J of Clubs / 9 of Diamonds / 
9 of Clubs / T of Spades / 4 of Diamonds / 6 of Diamonds / 9 of Diamonds / 9 of 
Clubs / T of Hearts / T of Spades / 3 of Clubs / 9 of Spades / 3 of Diamonds / 6
 of Hearts / 2 of Spades / 7 of Hearts / 6 of Hearts / 4 of Diamonds / 7 of Club
s / 2 of Clubs / A of Spades / Q of Spades / 6 of Spades / 3 of Hearts / 2 of He
arts / A of Diamonds / 5 of Clubs / 9 of Spades / J of Spades / 2 of Spades / 7 
of Diamonds / 9 of Hearts / A of Hearts / 3 of Hearts / 8 of Clubs / 8 of Spades
 / 8 of Hearts / 4 of Clubs / J of Clubs / T of Clubs / J of Diamonds / T of Clu
bs / J of Hearts / K of Spades / 2 of Clubs / 5 of Hearts / 4 of Spades / T of D
iamonds / Q of Hearts / J of Diamonds / Q of Diamonds / 7 of Spades / 2 of Diamo
nds / Q of Diamonds / 5 of Hearts / 4 of Hearts / K of Clubs / 6 of Spades / 5 o
f Spades / 3 of Clubs / 7 of Hearts / A of Diamonds / 3 of Spades / 2 of Hearts 
/ 8 of Clubs / 5 of Spades / K of Diamonds / Q of Clubs / 5 of Diamonds / 3 of D
iamonds / 8 of Diamonds / 2 of Diamonds / 6 of Clubs / 6 of Clubs / 7 of Spades 
/ K of Hearts / T of Hearts / T of Diamonds / 8 of Diamonds / A of Spades / 4 of
 Spades / 6 of Diamonds / 7 of Diamonds / 

K of Spades / Q of Spades / J of Spades / T of Spades / 9 of Spades / 8 of Spade
s / 7 of Spades / 6 of Spades / 5 of Spades / 4 of Spades / 3 of Spades / 2 of S
pades / A of Spades / K of Hearts / Q of Hearts / J of Hearts / T of Hearts / 9 
of Hearts / 8 of Hearts / 7 of Hearts / 6 of Hearts / 5 of Hearts / 4 of Hearts 
/ 3 of Hearts / 2 of Hearts / A of Hearts / K of Diamonds / Q of Diamonds / J of
 Diamonds / T of Diamonds / 9 of Diamonds / 8 of Diamonds / 7 of Diamonds / 6 of
 Diamonds / 5 of Diamonds / 4 of Diamonds / 3 of Diamonds / 2 of Diamonds / A of
 Diamonds / K of Clubs / Q of Clubs / J of Clubs / T of Clubs / 9 of Clubs / 8 o
f Clubs / 7 of Clubs / 6 of Clubs / 5 of Clubs / 4 of Clubs / 3 of Clubs / 2 of 
Clubs / A of Clubs / 

J of Hearts / 3 of Hearts / 4 of Clubs / A of Diamonds / K of Spades / J of Club
s / Q of Hearts / 6 of Hearts / 9 of Clubs / 2 of Hearts / 5 of Spades / 3 of Sp
ades / 4 of Hearts / K of Hearts / 7 of Spades / K of Clubs / T of Clubs / 8 of 
Hearts / 6 of Diamonds / 2 of Spades / Q of Diamonds / 9 of Diamonds / 7 of Club
s / 8 of Spades / 9 of Hearts / 8 of Clubs / 3 of Clubs / 4 of Spades / Q of Clu
bs / 3 of Diamonds / A of Hearts / 6 of Clubs / T of Hearts / 5 of Hearts / 5 of
 Clubs / 5 of Diamonds / 7 of Hearts / T of Diamonds / 4 of Diamonds / K of Diam
onds / J of Spades / 8 of Diamonds / 9 of Spades / 6 of Spades / J of Diamonds /
 T of Spades / 2 of Diamonds / Q of Spades / A of Clubs / 7 of Diamonds / A of S
pades / 2 of Clubs / 

How many hands? (1 - 10, please): 6

Here are our hands, from unshuffled deck:
Hand = ( K of Spades, 7 of Spades, A of Spades, 8 of Hearts, 2 of Hearts, 9 of D
iamonds, 3 of Diamonds, T of Clubs, 4 of Clubs )

Hand = ( Q of Spades, 6 of Spades, K of Hearts, 7 of Hearts, A of Hearts, 8 of D
iamonds, 2 of Diamonds, 9 of Clubs, 3 of Clubs )

Hand = ( J of Spades, 5 of Spades, Q of Hearts, 6 of Hearts, K of Diamonds, 7 of
 Diamonds, A of Diamonds, 8 of Clubs, 2 of Clubs )

Hand = ( T of Spades, 4 of Spades, J of Hearts, 5 of Hearts, Q of Diamonds, 6 of
 Diamonds, K of Clubs, 7 of Clubs, A of Clubs )

Hand = ( 9 of Spades, 3 of Spades, T of Hearts, 4 of Hearts, J of Diamonds, 5 of
 Diamonds, Q of Clubs, 6 of Clubs )

Hand = ( 8 of Spades, 2 of Spades, 9 of Hearts, 3 of Hearts, T of Diamonds, 4 of
 Diamonds, J of Clubs, 5 of Clubs )

Here are our hands, from SHUFFLED deck: 
Hand = ( A of Diamonds, 6 of Spades, A of Clubs, 3 of Diamonds, 6 of Diamonds, A
 of Spades, A of Hearts, 6 of Hearts, Q of Hearts )

Hand = ( J of Hearts, K of Spades, 2 of Clubs, 3 of Hearts, 2 of Hearts, 8 of He
arts, K of Hearts, 8 of Spades, 2 of Diamonds )

Hand = ( 4 of Diamonds, 9 of Hearts, 7 of Hearts, 9 of Clubs, 9 of Diamonds, J o
f Spades, Q of Diamonds, 2 of Spades, K of Diamonds )

Hand = ( Q of Clubs, 5 of Spades, 4 of Hearts, T of Hearts, 7 of Diamonds, 9 of 
Spades, 4 of Clubs, J of Diamonds, 6 of Clubs )

Hand = ( T of Diamonds, J of Clubs, 8 of Clubs, 5 of Hearts, 3 of Spades, 5 of C
lubs, 4 of Spades, 5 of Diamonds )

Hand = ( T of Spades, 7 of Clubs, 8 of Diamonds, T of Clubs, Q of Spades, 3 of C
lubs, K of Clubs, 7 of Spades )
******************************END SAMPLE RUN**************************/
