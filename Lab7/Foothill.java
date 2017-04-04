/*Source code for Assignment 7 - Optical Barcode Readers and Writers ============
 Created by Thanh Nguyen 02/22/16*/

public class Foothill
{
   public static void main(String[] args)
   {
      String[] sImageIn = 
         { 
               "                                      ",
               "                                      ",
               "                                      ",
               "* * * * * * * * * * * * * * * * *     ",
               "*                                *    ",
               "**** * ****** ** ****** *** ****      ",
               "* ********************************    ",
               "*    *   *  * *  *   *  *   *  *      ",
               "* **    *      *   **    *       *    ",
               "****** ** *** **  ***** * * *         ",
               "* ***  ****    * *  **        ** *    ",
               "* * *   * **   *  *** *   *  * **     ",
               "**********************************    "
         };
         
         String[] sImageIn_2 = 
         { 
               "                                          ",
               "                                          ",
               "* * * * * * * * * * * * * * * * * * *     ",
               "*                                    *    ",
               "**** *** **   ***** ****   *********      ",
               "* ************ ************ **********    ",
               "** *      *    *  * * *         * *       ",
               "***   *  *           * **    *      **    ",
               "* ** * *  *   * * * **  *   ***   ***     ",
               "* *           **    *****  *   **   **    ",
               "****  *  * *  * **  ** *   ** *  * *      ",
               "**************************************    "
         };
         
         BarcodeImage bc = new BarcodeImage(sImageIn);
         DataMatrix dm = new DataMatrix(bc);
        
         // First secret message
         dm.translateImageToText();
         dm.displayTextToConsole();
         dm.displayImageToConsole();
         
         // second secret message
         bc = new BarcodeImage(sImageIn_2);
         dm.scan(bc);
         dm.translateImageToText();
         dm.displayTextToConsole();
         dm.displayImageToConsole();
         
         // create your own message
         dm.readText("CS 1B rocks more than Zeppelin");
         dm.generateImageFromText();
         dm.displayTextToConsole();
         dm.displayImageToConsole();
   }
}
//=========================class DataMatrix=================================
class DataMatrix implements BarcodeIO
{
   //data
   public static final char BLACK_CHAR = '*';
   public static final char WHITE_CHAR = ' ';
   
   private BarcodeImage image;
   private String text;
   private int actualWidth, actualHeight;
   
   //constructors
   DataMatrix()
   {
      image = new BarcodeImage();
      actualWidth = actualHeight = 0;
      text = "undefined";
   }   
   DataMatrix(BarcodeImage image)
   {
      this();
      scan(image);
   }   
   DataMatrix(String text)
   {
      this();
      if(!readText(text))
         this.text = "undefined";           
   }
   
   //mutator for text
   public boolean readText(String text)
   {
      if(text == null)       
         return false;
      
      this.text = text;     
      return true;     
   }   
   //mutator for image
   public boolean scan(BarcodeImage image)
   {
      try
      {
         this.image = (BarcodeImage)image.clone();
      } catch (CloneNotSupportedException e)
      {
         return false;
      }
      
      //set height and width
      this.actualHeight = computeSignalHeight();
      this.actualWidth = computeSignalWidth();
           
      return true;
   }
   //accessors 
   public int getActualWidth() {return actualWidth;}
   public int getActualHeight() {return actualHeight;}
   
   //compute height and width
   private int computeSignalHeight()
   {
      int height = 0, i = 0;
      boolean found = false;
      
      while(!found)
      {
         if(image.getPixel(i, 0)) //find '*' in first column
         {           
            height = BarcodeImage.MAX_HEIGHT - i;
            found = true;
         }           
         i++;
      }     
      return height;
   }   
   private int computeSignalWidth()
   {
      int width = 0, i = 0;
      boolean found = false;
      
      while(!found)
      {
         if(!image.getPixel(BarcodeImage.MAX_HEIGHT - 1, i))
         {
            width = i;
            found = true;
         }
         i++;
      }
      return width;
   }
 
   //image->text
   public boolean translateImageToText()
   {
      text = "";
      for(int i = 1; i < actualWidth - 1; i++)
         this.text += readCharFromCol(i);
      return true;
   }  
   private char readCharFromCol(int col)
   {
      char theChar = 'E';
      int row, i, temp = 0;
      
      for(row = BarcodeImage.MAX_HEIGHT - 2, i = 0;
            i < actualHeight - 2; i++, row--)
      {
         if(image.getPixel(row, col))
         {           
            temp += Math.pow(2, i);
         }         
      }
      theChar = (char)temp;
          
      return theChar;
   }
     
   //text->image
   public boolean generateImageFromText()
   {      
      for(int col = 1; col < text.length() + 1; col++)
      {
         char theChar = text.charAt(col - 1); //convert String to each char
         int code = (int)theChar; //convert char to int
         writeCharToCol(col, code);
      }      
      return true;
   }   
   private boolean writeCharToCol(int col, int code)
   {     
      int row, i;
      boolean[] value = new boolean[8];
      
      for(int k = 0; k < 8; k++)
      {
         value[k] = (code % 2 != 0);
         code /= 2;         
      }
      
      for(row = BarcodeImage.MAX_HEIGHT - 2, i = 0;
            i < 8; row--, i++)
      {
         image.setPixel(row, col, value[i]);
      }
      
      return true;
   }
   
   //display
   public void displayTextToConsole()
   {
      System.out.println(text);
   }
   public void displayImageToConsole()
   {
      int row, col;
      
      System.out.println();
      for ( col = 0; col < actualWidth + 2; col++ )
         System.out.print("-");
      System.out.println();
      
      for (row = BarcodeImage.MAX_HEIGHT - actualHeight; 
            row < BarcodeImage.MAX_HEIGHT; row++)
      {
         System.out.print("|");
         for (col = 0; col < actualWidth; col++)
         {
            if(image.getPixel(row, col))
               System.out.print(BLACK_CHAR);
            else
               System.out.print(WHITE_CHAR);
         }
         System.out.println("|");
      }
      
      for ( col = 0; col < actualWidth + 2; col++ )
         System.out.print("-");
      System.out.println();     
   }
   
}//end class DataMatrix

//=========================interface BarcodeIO==============================
interface BarcodeIO
{
   public boolean scan(BarcodeImage bc);
   public boolean readText(String text);
   public boolean generateImageFromText();
   public boolean translateImageToText();
   public void displayTextToConsole();
   public void displayImageToConsole();
}

//=========================class BarcodeImage================================
class BarcodeImage implements Cloneable
{
   //member data
   public static final int MAX_HEIGHT = 30;
   public static final int MAX_WIDTH = 65;
   
   private boolean[][] image_data;
   
   //constructors
   BarcodeImage()
   {
      int row, col;
      image_data = new boolean[MAX_HEIGHT][MAX_WIDTH];
      for ( row = 0; row < image_data.length; row++ )
         for ( col = 0; col < image_data[row].length; col++ )
            image_data[row][col] = false;
   }
   
   BarcodeImage(String[] str_data)
   {
      this();
      int row, col, str_row;
      
      if ( !checkSize(str_data) )
         return;  // silent, but there's an error, for sure.

      for (row = MAX_HEIGHT - str_data.length, str_row = 0; row < MAX_HEIGHT; 
            row++, str_row++)
         for(col = 0; col < str_data[str_row].length(); col++)
         {
            if(str_data[str_row].charAt(col) == '*')
               image_data[row][col] = true;
            else
               image_data[row][col] = false;
         }            
   }
   //checksize
   private boolean checkSize(String[] data)
   {
      if (data == null)
         return false;
      if (data.length > MAX_HEIGHT)
         return false;
      for(int i = 0; i < data.length; i++)
         if(data[i].length() > MAX_WIDTH)
            return false;
      return true;
   }
   
   //accessor and mutator
   public boolean setPixel(int row, int col, boolean value)
   {
      if (row < 0 || row >= MAX_HEIGHT || col < 0 || col >= MAX_WIDTH)
         return false;
      image_data[row][col] = value;
      return true;
   }
   
   public boolean getPixel(int row, int col)
   {
      if (row < 0 || row >= MAX_HEIGHT || col < 0 || col >= MAX_WIDTH)
         return false;
      return image_data[row][col];
   }
   
   //clone
   public Object clone() throws CloneNotSupportedException
   {
      int row, col;
      
      // always do this first - parent will clone its data correctly
      BarcodeImage newBc = (BarcodeImage)super.clone();
      
      // now do the immediate class member objects
      newBc.image_data = new boolean[MAX_HEIGHT][MAX_WIDTH];
      for ( row = 0; row < MAX_HEIGHT; row++ )
         for ( col = 0; col < MAX_WIDTH; col++ )
            newBc.image_data[row][col] = this.image_data[row][col];
      
      return newBc;
   }
   
   //display
   public void displayToConsole()
   {
      int row, col;
      
      // top row border
      System.out.println();
      for ( col = 0; col < MAX_WIDTH + 2; col++ )
         System.out.print("-");
      System.out.println();
      
      // now each row from 0 to MAX_WIDTH, adding border chars
      for ( row = 0; row < MAX_HEIGHT; row++ )
      {
         System.out.print("|");
         for ( col = 0; col < MAX_WIDTH; col++ )
         {
            if(image_data[row][col])
               System.out.print("*");
            else  
               System.out.print(" ");
         }           
         System.out.println("|");
      }
      
      // bottom
      for (col = 0; col < MAX_WIDTH + 2; col++)
         System.out.print("-");
      System.out.println();
   }  
}//end class BarcodeImage

/*===========================SAMPLE RUN=====================================
Don't forget to remove the tabs!

------------------------------------
|* * * * * * * * * * * * * * * * * |
|*                                *|
|**** * ****** ** ****** *** ****  |
|* ********************************|
|*    *   *  * *  *   *  *   *  *  |
|* **    *      *   **    *       *|
|****** ** *** **  ***** * * *     |
|* ***  ****    * *  **        ** *|
|* * *   * **   *  *** *   *  * ** |
|**********************************|
------------------------------------
You did it!  Great work.  Celebrate.

----------------------------------------
|* * * * * * * * * * * * * * * * * * * |
|*                                    *|
|**** *** **   ***** ****   *********  |
|* ************ ************ **********|
|** *      *    *  * * *         * *   |
|***   *  *           * **    *      **|
|* ** * *  *   * * * **  *   ***   *** |
|* *           **    *****  *   **   **|
|****  *  * *  * **  ** *   ** *  * *  |
|**************************************|
----------------------------------------
CS 1B rocks more than Zeppelin

----------------------------------------
|* * * * * * * * * * * * * * * * * * * |
|*                                    *|
|***  * ***** **** **** *************  |
|*  ** ***************** **************|
|* * *  *   *   *  *    * **     * *   |
|*       * *  **    * * *    ***     **|
|*       *    ** * *  *  *  ** *   *** |
|***  * *****  **     * *      ***   **|
|*** *   **** ** *   *   *  * *   * *  |
|**************************************|
----------------------------------------
=================================END SAMPLE RUN=============================*/