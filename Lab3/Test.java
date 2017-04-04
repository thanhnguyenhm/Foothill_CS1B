//CS 1B - LOCEFF
//Assignment #3 - Option A1
 
 
import java.util.Scanner;
 
public class Test
{
   public static void main(String[] args)
   {
      int rule, k;
      String strUserIn;
     
      Scanner inputStream = new Scanner(System.in);
      Automaton2 aut;
 
      // get rule from user
      do
      {
         System.out.print("Enter Rule (0 - 255): ");
         // get the answer in the form of a string:
         strUserIn = inputStream.nextLine();
         // and convert it to a number so we can compute:
         rule = Integer.parseInt(strUserIn);
 
      } while (rule < 0 || rule > 255);
 
      // create automaton with this rule and single central dot
      aut = new Automaton2(rule);
 
      // now show it
      System.out.println("   start");
      for (k = 0; k < 100; k++)
      {
         System.out.println( aut.toStringCurrentGen() );
         aut.propagateNewGeneration();
      }
      System.out.println("   end");
      inputStream.close();
   }
}
 
class Automaton2
{
   // class constants
   public final static int MAX_DISPLAY_WIDTH = 121;
   
   // private members
   private boolean rules[] = new boolean[8];  // allocate rules[8] in constructor!
   private String thisGen;   // same here
   String extremeBit; // bit, "*" or " ", implied everywhere "outside"
   int displayWidth;  // an odd number so it can be perfectly centered
   
   //Constructor
   public Automaton2(int newRule)
   {
      setRule(newRule);
      setDisplayWidth(79);
      resetFirstGen();
      extremeBit = " ";
   }
   
   //Mutators
   public void resetFirstGen()
   {
      thisGen = "*";
   }
   
   public boolean setRule(int newRule)
   {
      int BinRule = newRule;
      for(int i = 7;i >= 0;i--)
      {
         if((int) Math.pow(2, i) <= BinRule)
         {
            rules[i] = true;
            BinRule = BinRule-(int) Math.pow(2, i);
         }
      }
      return true;
   }
   
   public boolean setDisplayWidth(int width)
   {
      if(width % 2 == 0 && width < MAX_DISPLAY_WIDTH && width > 0)
      {
      displayWidth = width;
      return true;
      }
     
      displayWidth = 79; //Defaulting
      return false;
   }
   
   //toString
   public String toStringCurrentGen()
   {
      String retVal = thisGen.trim();
      if(retVal.length() > displayWidth)
         retVal = retVal.substring(0,displayWidth);
      return retVal;
   }
   
   public void propagateNewGeneration()
   {
      String nextGen;
      int test;
      int counter2;
      char innerTest;
      
     
      thisGen = extremeBit + extremeBit + thisGen + extremeBit + extremeBit;
      nextGen = "";
      for(int i=1;i < thisGen.length()-1;i++)
      {
         counter2 = 2;
         test = 0;
 
         for(int j = -1; j < 2;j++) //Return 0-7
         {
            innerTest = thisGen.charAt(i+j);
            if(innerTest == '*')
               test = test + (int) Math.pow(2, counter2);
            counter2--;
         }
         
         if(rules[test] == true)
            nextGen = nextGen + "*";
         if(rules[test] == false)
            nextGen = nextGen + " ";
      }
      
      /*String nextGen = "";
      thisGen = extremeBit + extremeBit + thisGen + extremeBit + extremeBit;
      
      for(int i = 0; i < thisGen.length() / 2 + 1; i++)
      {
         String num = "";
         for(int j = i; j < i + 3; j ++)
         {          
            if(thisGen.charAt(j) == '*')
               num += 1;
            else
               num += 0;
         }
         int k = Integer.parseInt(num, 2);         
         if(rules[k])
            nextGen += "*";
         else
            nextGen += " ";         
      }*/
     
      //Setting extremeBit for next generation.
      if(extremeBit == "*" && rules[7] == true)
         extremeBit = "*";
     
      if(extremeBit == "*" && rules[7] == false)
         extremeBit = " ";
     
      if(extremeBit == " " && rules[0] == true)
         extremeBit = "*";
     
      if(extremeBit == " " && rules[0] == false)
         extremeBit = " ";
         
      thisGen = nextGen;
      //System.out.print("| '" + thisGen +"' |");
   }
}

 
