/*Source code for Lab Assignment 4 - Talent
 Created by Thanh Nguyen 02/01/16*/

public class Foothill
{
   public static void main(String[] args)
   {
      //create several numbers of clients
      Writer writer1 = new Writer();
      writer1.setName("Yasaku Kudo");
      writer1.setIncome(100000);
      writer1.setPercentCut(8);
      writer1.setRank("executive producer");
      
      Writer writer2 = new Writer(true, true, true, true, "producer");
      writer2.setName("Haruki Murakami");
      writer2.setIncome(8000000);
      writer2.setPercentCut(18);
      
      Writer writer3 = new Writer();
      writer3.setName("Watanabe Toru");
      writer3.setIncome(60000);
      writer3.setPercentCut(7);
           
      Actor actor1 = new Actor();
      actor1.setName("Rinko Kikuchi");
      actor1.setIncome(9500000);
      actor1.setPercentCut(25);
      
      Actor actor2 = new Actor('F', 21);
      actor2.setName("Ran Mouri");
      actor2.setIncome(18000);
      actor2.setPercentCut(6);
      
      Actor actor3 = new Actor('M', 22);            
      actor3.setName("Kudo Shinichi");
      actor3.setIncome(20000);
      actor3.setPercentCut(12);      
      
      //display writer1
      System.out.println(writer1.toString());
      
      //display writer2
      System.out.println(writer2.toString());
      
      //display actor1
      System.out.println(actor1.toString());
      
      //display actor2
      System.out.println(actor2.toString());
      
      //create Agent object
      Agent agent1 = new Agent("Agent 1");
      
      //add several clients to agent1
      agent1.addClient(actor1);
      agent1.addClient(actor2);
      agent1.addClient(actor3);
      agent1.addClient(writer1);       
      agent1.addClient(writer2);
      agent1.addClient(writer3);
      System.out.println(agent1.toStringClientsShort());
      System.out.println();
      System.out.println(agent1.toStringClientsLong());
      
      //display income
      System.out.println("\nTotal income of " + agent1.getName() + " "
            + "this year: " + agent1.getIncome());
      
      //remove clients    
      agent1.removeClient(writer3);
      System.out.println("\nAgent after removing");
      System.out.println(agent1.toStringClientsShort());
   }
}

/***************************************************
****************CLIENT BASE CLASS****************/
class Client
{
   //private member data
   private String name;
   private long incomeThisYear;
   private double percentCut;
   
   //constant members
   private final static int MIN_NAME = 3;
   private final static int MAX_NAME = 60;
   private final static long MIN_INCOME = 0;
   private final static long MAX_INCOME = 1000000000;
   private final static double MIN_PERCENT = 0;
   private final static double MAX_PERCENT = 99.99;
   
   //constructors
   public Client()
   {
      this("default", MIN_INCOME, MIN_PERCENT);
   }
   
   public Client(String name, long incomeThisYear, double percentCut)
   {
      if(!setName(name))
         this.name = "default";
      if(!setIncome(incomeThisYear))
         this.incomeThisYear = MIN_INCOME;;
      if(!setPercentCut(percentCut))
         this.percentCut = 10;
   }
   
   //setter for name
   public boolean setName(String name)
   {
      if(!isValidName(name))
         return false;
      this.name = name;
      return true;
   }
   
   //name validator
   private static boolean isValidName(String name)
   {
      if (name.length() >= MIN_NAME && name.length() <= MAX_NAME)
         return true;
      return false;
   }
   
   //setter for income
   public boolean setIncome(long income)
   {
      if(!isValidIncome(income))         
         return false;
      this.incomeThisYear = income;
      return true;
   }
   
   //income validator
   private static boolean isValidIncome(long income)
   {
      if(income >= MIN_INCOME && income <= MAX_INCOME)
         return true;
      return false;
   }
   
   //setter for percentCut
   public boolean setPercentCut(double percentCut)
   {
      if(!isValidPercentCut(percentCut))
         return false;
      this.percentCut = percentCut;
      return true;
   }
   
   //percentCut validator
   private static boolean isValidPercentCut(double percentCut)
   {
      if(percentCut >= MIN_PERCENT && percentCut <= MAX_PERCENT)
         return true;
      return false;
   }
   
   //getters
   public String getName() { return name; }
   public long getInCome() { return incomeThisYear; }
   public double getPercentCut() {return percentCut; }
   
   //display data
   public String toString()
   {
      String myStr = "";     
      myStr = "Name: " + name + " - Income: " + incomeThisYear +
            " - Percent Cut: " + percentCut;     
      return myStr;
   }
}


/***************************************************
****************DERIVED WRITER CLASS***************/
class Writer extends Client
{
   //additional writer members
   private boolean technical, government, international, degree;
   private String rank;
   
   //constructors
   Writer()
   {
      this(true, true, true, true, "staff writer");
   }
   
   Writer(boolean technical, boolean government, boolean international,
         boolean degree, String rank)
   {
      super();
      setTechnical(technical);
      setGovernment(government);
      setInternational(international);
      setDegree(degree);
      if(!setRank(rank))
         this.rank = "staff writer";
   }

   //setters
   public boolean setTechnical(boolean technical)
   {
      this.technical = technical;
      return true;
   }
   
   public boolean setGovernment(boolean government)
   {
      this.government = government;
      return true;
   }
   
   public boolean setInternational(boolean international)
   {
      this.international = international;
      return true;
   }
   
   public boolean setDegree(boolean degree)
   {
      this.degree = degree;
      return true;
   }
   
   public boolean setRank(String rank)
   {
      if(!isValidRank(rank))
         return false;
      this.rank = rank;
      return true;
   }
   
   //rank validator
   private boolean isValidRank(String rank)
   {
      if(rank == "staff writer" || rank == "story editor" ||
         rank == "co-producer" || rank == "producer" ||
         rank == "co-executive producer" || rank == "executive producer")
         return true;
      return false;
   }
   
   //getters
   public boolean technical() {return technical;}
   public boolean government() {return government;}
   public boolean international() {return international;}
   public boolean degree() {return degree;}
   public String rank() {return rank;}
   
   //display 
   public String toString()
   {
      String myStr = super.toString() + "\n";     
      myStr += "Technical: " + technical + " - Government: " + government +
            " - International: " + international + " - degree: " + 
            degree + " Rank: " + rank + "\n";     
      return myStr;
   }         
}

/***************************************************
****************DERIVED ACTOR CLASS****************/
class Actor extends Client
{
   //additional member
   private char gender;
   private int age;
   
   //static constant member
   public static final int MIN_AGE = 0;
   public static final int MAX_AGE = 150;
   
   //constructors
   Actor()
   {
      this('F', 27);
   }
   
   Actor(char gender, int age)
   {
      super();
      if(!setGender(gender))
         this.gender = 'F';
      if(!setAge(age))
         this.age = 27;
   }
   
   //setters
   public boolean setGender(char gender)
   {
      if(!isValidGender(gender))
         return false;
      this.gender = gender;
      return true;
   }
   
   //gender validator
   private boolean isValidGender(char gender)
   {
      if(gender == 'F' || gender == 'M')
         return true;
      return false;
   }
   
   public boolean setAge(int age)
   {
      if(!isValidAge(age))
         return false;
      this.age = age;
      return true;
   }
   
   //age validator
   private boolean isValidAge(int age)
   {
      if(age < MIN_AGE || age > MAX_AGE)
         return false;
      return true;
   }
   
   //display
   public String toString()
   {
      String myStr = super.toString() + "\n";   
      myStr += "Gender: " + gender + " - Age: " + age + "\n";
      return myStr;
   }  
   
   //getters
   public char getGender() {return gender;}
   public int getAge() {return age;}
}


/***************************************************
****************AGENT CLASS************************/
class Agent
{
   //member data
   private String name;
   private Client[] myClients = new Client[MAX_CLIENTS];
   private int numClients;
   
   //static constants
   public static final int MAX_CLIENTS = 100;
   public static final int MIN_NAME_LEN = 3;
   public static final int MAX_NAME_LEN = 60;
   
   //constructor
   Agent(String name)
   {
      if(!setName(name))
         this.name = "default";
   }
   
   //setter
   public boolean setName(String name)
   {
      if(!isValidName(name))
         return false;
      this.name = name;
      return true;
   }
   
   //name validator
   private boolean isValidName(String name)
   {
      if(name.length() < MIN_NAME_LEN || 
            name.length() > MAX_NAME_LEN)
         return false;
      return true;
   }
   
   //getter
   public String getName() {return name;}
   
   //add client to agent list
   boolean addClient(Client client)
   {
      if(numClients >= MAX_CLIENTS)
         return false;

      if (myClients[numClients] == null)
         myClients[numClients] = new Client();
   
      myClients[numClients].setName(client.getName());
      myClients[numClients].setIncome(client.getInCome());
      myClients[numClients].setPercentCut(client.getPercentCut());
      numClients++;
      return true;         
   }
   
   //remove client from agent list
   boolean removeClient(Client client)
   {
      boolean found = false;
      
      for(int i = 0; i < numClients; i++)
      {
         if(found)         
            myClients[i - 1] = myClients[i];         
         
         if(myClients[i].getName() == client.getName())         
            found = true;                
      }           
      myClients[numClients - 1] = null;
      numClients--;
      if(found)
         return true;
      return false;     
   }
   
   String toStringClientsShort()
   {
      String myStr;      
      myStr = name + " (Short Version): ";
      int i = 0;
      
      if(i < numClients)
      {
         while(i < numClients - 1)
         {
            myStr += myClients[i].getName() + ", ";
            i++;
         }
         myStr += myClients[i].getName() + " ";
      }
      
      return myStr;
   }
   
   String toStringClientsLong()
   {
      String myStr;
      myStr = name + " (Long Version): \n";
      int i = 0;
      
      if(i < numClients)
      {
         while(i < numClients - 1)
         {
            myStr += myClients[i].toString() + ", \n";
            i++;
         }
         myStr += myClients[i].toString() + " ";
      }
      
      return myStr;
   }
   
   double getIncome()
   {
      double returnIncome = 0;
      
      for (int i = 0; i < numClients; i++)
         returnIncome += myClients[i].getInCome()
         * myClients[i].getPercentCut() / 100;
      
      return returnIncome;
   }
}

/**********************SAMPLE RUN********************************
Name: Yasaku Kudo - Income: 100000 - Percent Cut: 8.0
Technical: true - Government: true - International: true - degree: true Rank: ex
ecutive producer

Name: Haruki Murakami - Income: 8000000 - Percent Cut: 18.0
Technical: true - Government: true - International: true - degree: true Rank: pr
oducer

Name: Rinko Kikuchi - Income: 9500000 - Percent Cut: 25.0
Gender: F - Age: 27

Name: Ran Mouri - Income: 18000 - Percent Cut: 6.0
Gender: F - Age: 21

Agent 1 (Short Version): Rinko Kikuchi, Ran Mouri, Kudo Shinichi, Yasaku Kudo, H
aruki Murakami, Watanabe Toru 

Agent 1 (Long Version): 
Name: Rinko Kikuchi - Income: 9500000 - Percent Cut: 25.0, 
Name: Ran Mouri - Income: 18000 - Percent Cut: 6.0, 
Name: Kudo Shinichi - Income: 20000 - Percent Cut: 12.0, 
Name: Yasaku Kudo - Income: 100000 - Percent Cut: 8.0, 
Name: Haruki Murakami - Income: 8000000 - Percent Cut: 18.0, 
Name: Watanabe Toru - Income: 60000 - Percent Cut: 7.0 

Total income of Agent 1 this year: 3830680.0

Agent after removing
Agent 1 (Short Version): Rinko Kikuchi, Ran Mouri, Kudo Shinichi, Yasaku Kudo, H
aruki Murakami 

****************************END SAMPLE RUN*****************************/