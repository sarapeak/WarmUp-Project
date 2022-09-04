import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.*;
import java.util.*;

public class ReadWebPage
{
   public static void main(String[] args) throws IOException, InterruptedException
   {
      HttpClient client = HttpClient.newHttpClient();   //Download the webpage
      
      //int num = 0;     //Used to change the page
      //int check = 0;   //Temporary to check if the page changed
      
      //Loops through to read and capture the first two pages
      //for(int start = 0; start < 1; start++)
      //{
         HttpRequest request = HttpRequest.newBuilder()
            //Temporary URL
            .uri(URI.create("https://www.surlatable.com/liege-waffles/REC-266788.html?cgid=recipes&start=0")) //starting URL…
            .GET() // GET is default
            .build();
         
         //https://www.surlatable.com/recipes/?srule=best-matches&start="+Integer.toString(num)+"&sz=24
         //Increments num by 24 as observed in the website url
         //num = num + 24;
         
         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
         //If check is zero, outputs to the first output
         //if (check == 0)
         //{
            //output to a file so its easy to mess with (you won’t be for your finished program)
            FileOutputStream fs = new FileOutputStream("output.txt");
            PrintWriter pw = new PrintWriter(fs);
            pw.println(response.body()); //response.body() is the html source code in a string format. It outputs to a file so you can see it easier right now, but you will ultimately want to just manipulate the strings a lot
            pw.close();
         //}
         //Otherwise outputs to the second output
         /*else
         {
            FileOutputStream fs = new FileOutputStream("output1.txt");
            PrintWriter pw = new PrintWriter(fs);
            pw.println(response.body()); //response.body() is the html source code in a string format. It outputs to a file so you can see it easier right now, but you will ultimately want to just manipulate the strings a lot
            pw.close();
         }
         check++;   //Increments check by 1
      }*/
      
      //To scan the file
      Scanner scan = new Scanner(new FileInputStream("output.txt"));
      
      //Variables
      String keyServes = "Serves";
      String keyProcedure = "Procedure";
      String servings = "";
      String procedure = "";
      String temp = "";
      String temp2 = "";
      
      //Loops through the file
      while(scan.hasNextLine())
      {
         temp = scan.nextLine();   //Scans for each line
         //Checks if the line contains the keyword procedure
         if(temp.indexOf(keyProcedure) != -1)
         {
            //Loops through twice
            for(int checker = 0; checker < 2; checker++)
            {
               temp = scan.nextLine();
            }
            //Loops until given a </div>
            while(scan.hasNextLine() && procedure.indexOf("</div>") == -1)
            {
               procedure += scan.nextLine();   //Adds to the procedure string
               procedure += "\n";
            }
         }
         //Checks to find the keyword Serves
         else if(temp.indexOf(keyServes) != -1)
         {
            //Loops through twice
            for(int checker = 0; checker < 2; checker++)
            {
               temp = scan.nextLine();
            }
            //Adds the line to servings string
            servings = scan.nextLine();
         }
      }
      
      //Replaces unwanted items with nothing
      procedure = procedure.replace("<i>", "");
      procedure = procedure.replace("</i>", "");
      procedure = procedure.replace("<b>", "");
      procedure = procedure.replace("</b>", "");
      procedure = procedure.replace("<br>", "");
      procedure = procedure.replace("</div>", "");
      
      //Prints out the servings and procedure
      System.out.println(servings);
      System.out.println(procedure);
   }
}