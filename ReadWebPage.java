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
      
      int num = 0;     //Used to change the page
      
      FileOutputStream fos = new FileOutputStream("recipeDoc.csv", true);
      PrintWriter pw3 = new PrintWriter(fos);
      pw3.println("Author, Path, Name, Servings, Ingredients, Instructions \n");
      
      //Loops through to read and capture the first two pages
      for(int start = 0; start < 4; start++)
      {
         HttpRequest request = HttpRequest.newBuilder()
            //Temporary URL
            .uri(URI.create("https://www.surlatable.com/recipes/?srule=best-matches&start="+Integer.toString(num)+"&sz=24")) //starting URL…
            .GET() // GET is default
            .build();
         
         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
         
         //output to a file so its easy to mess with (you won’t be for your finished program)
         FileOutputStream fs = new FileOutputStream("output.txt");
         PrintWriter pw = new PrintWriter(fs);
         pw.println(response.body()); //response.body() is the html source code in a string format. It outputs to a file so you can see it easier right now, but you will ultimately want to just manipulate the strings a lot
      
         //To scan the file
         Scanner scan = new Scanner(new FileInputStream("output.txt"));
         
         //Variables
         String word = "thumb";
         String ingredientstart = "<ul>";
         String ingredientstop = "</ul>";
         String keyServes = "Serves";
         String keyProcedure = "Procedure";
         String pathKey = "-element\""; // the tell tail for the path
         String authorKey = "recipe-author"; //tell tail for the author
         String titleKey = "<title>";
         String temp = "";
         
         //Loops through the file
         while(scan.hasNextLine())
         {
            temp = scan.nextLine();   //Scans for each line
            int linkIndex= -1;
            int linkIndexEnd = -1;
            
            //if we find thumb, that means there is an important link
            if(temp.indexOf(word) != -1)
            {
               int check = 0;
               //find the first and last index of the link
               linkIndex = temp.indexOf(word) + 18;
               linkIndexEnd = temp.indexOf("title") - 2;
            
               String link = temp.substring(linkIndex,linkIndexEnd);
            
               //open up that file and save it to a new one
               HttpClient client2 = HttpClient.newHttpClient();
               HttpRequest request2 = HttpRequest.newBuilder()
                  .uri(URI.create(link)) //starting URL…
                  .GET() // GET is default
                  .build();

               HttpResponse<String> response2 = client2.send(request2,HttpResponse.BodyHandlers.ofString());
               
               //output to a file so its easy to mess with (you won’t be for your finished program)
               FileOutputStream fs2 = new FileOutputStream("outputmenuspecific.txt");
               PrintWriter pw2 = new PrintWriter(fs2);
               pw2.println(response2.body());
         
               Scanner s2 = new Scanner(new FileInputStream("outputmenuspecific.txt"));
               
               String ingredients = "";
               String servings = "";
               String procedure = ""; 
               String line = "";
               String author = "";
               String title = "";
               ArrayList<String> path = new ArrayList<String>();
               
               while(s2.hasNextLine())
               {               
                  String line2 = s2.nextLine();
                  
                  if(line2.indexOf(titleKey) != -1 && check < 1)
                  {
                     check++;
                     line2 = line2.replace("<title>", "");
                     line2 = line2.replace("</title>", "");
                     line2 = line2.replace("| Sur La Table", "");
                     title = line2;
                     title = title.replace("\n", "");
                     title = title.replace(",", "");
                  }
                  else if(line2.indexOf(authorKey) != -1)
                  {
                     author = s2.nextLine(); //save the author as a string variable
                     author = author.replace("\n", "");
                     author = author.replace(",", "");
                  }
                  else if(line2.indexOf(pathKey) != -1)
                  {
                     path.add(line2.substring(line2.lastIndexOf("\">")+2,line2.lastIndexOf("<"))); //need from "> to <
                  }
                  //Checks to find the keyword Serves
                  else if(line2.indexOf(keyServes) != -1)
                  {
                     //Loops through twice
                     for(int checker = 0; checker < 2; checker++)
                     {
                        line2 = s2.nextLine();
                     }
                     //Adds the line to servings string
                     servings = s2.nextLine();
                     
                     servings = servings.replace("&#189;",".5");
                     servings = servings.replace("&#188;",".25");
                     servings = servings.replace("&#8531;",".33");
                     servings = servings.replace("&#8532;",".66");
                     servings = servings.replace("&#190;",".75");
                          
                     servings = servings.replace("<br>","");
                     servings = servings.replace("<b>","");
                     servings = servings.replace("<i>","");
                     servings = servings.replace("</i>","");
                     servings = servings.replace("</div>","");
                     servings = servings.replace("</b>","");
                     servings = servings.replace("&#8217;","'");
                     servings = servings.replace("&#233;","é");
                     servings = servings.replace("&#232;","è");
                     servings = servings.replace("&#224;","à");
                     servings = servings.replace("&#34;","");
                     servings = servings.replace("&#8539;",".125");
                     
                     servings = servings.replace("\n", "");
                     servings = servings.replace(",", "");
                  }
                  //if we find thumb, that means there is an important link
                  else if(line2.indexOf(ingredientstart)!=-1)
                  {
                     //find the first and last index of the link
                     String thenextline = s2.nextLine();
                     while (thenextline.indexOf(ingredientstop)==-1)
                     {
                        //works with the lines in the ingredients
                        //lbm = line before modification
             
                        int lstop = thenextline.indexOf("</li>");
                        ingredients += "\n";
                        if (lstop !=-1)
                           ingredients += thenextline.substring(4,lstop);
                        thenextline = s2.nextLine();
                     }
                     
                     ingredients = ingredients.replace("&#189;",".5");
                     ingredients = ingredients.replace("&#188;",".25");
                     ingredients = ingredients.replace("&#8531;",".33");
                     ingredients = ingredients.replace("&#8532;",".66");
                     ingredients = ingredients.replace("&#190;",".75");
                          
                     ingredients = ingredients.replace("<br>","");
                     ingredients = ingredients.replace("<b>","");
                     ingredients = ingredients.replace("<i>","");
                     ingredients = ingredients.replace("</i>","");
                     ingredients = ingredients.replace("</div>","");
                     ingredients = ingredients.replace("</b>","");
                     ingredients = ingredients.replace("&#8217;","'");
                     ingredients = ingredients.replace("&#233;","é");
                     ingredients = ingredients.replace("&#232;","è");
                     ingredients = ingredients.replace("&#224;","à");
                     ingredients = ingredients.replace("&#34;","");
                     ingredients = ingredients.replace("&#8539;",".125");
                     
                     ingredients = ingredients.replace("\n", "");
                     ingredients = ingredients.replace(",", "");
                  }  
                  //Checks if the line contains the keyword procedure
                  else if(line2.indexOf(keyProcedure) != -1)
                  {
                     //Loops through twice
                     for(int checker = 0; checker < 2; checker++)
                     {
                        line2 = s2.nextLine();
                     }
                     //Loops until given a </div>
                     while(s2.hasNextLine() && procedure.indexOf("</div>") == -1)
                     {
                        procedure += s2.nextLine();   //Adds to the procedure string
                        procedure += "\n";
                     }
                     
                     procedure = procedure.replace("&#189;",".5");
                     procedure = procedure.replace("&#188;",".25");
                     procedure = procedure.replace("&#8531;",".33");
                     procedure = procedure.replace("&#8532;",".66");
                     procedure = procedure.replace("&#190;",".75");
                              
                     procedure = procedure.replace("<br>","");
                     procedure = procedure.replace("<b>","");
                     procedure = procedure.replace("<i>","");
                     procedure = procedure.replace("</i>","");
                     procedure = procedure.replace("</div>","");
                     procedure = procedure.replace("</b>","");
                     procedure = procedure.replace("&#8217;","'");
                     procedure = procedure.replace("&#233;","é");
                     procedure = procedure.replace("&#232;","è");
                     procedure = procedure.replace("&#224;","à");
                     procedure = procedure.replace("&#34;","");
                     procedure = procedure.replace("&#8539;",".125");
                     
                     procedure = procedure.replace("\n", "");
                     procedure = procedure.replace(",", "");
                  }
               }
               pw3.print(author + ",");
               for(int i = 0; i < path.size(); i++)
               {
                  pw3.print(path.get(i) + "/");
               }
               pw3.print(","+title);
               pw3.print(","+servings);
               pw3.print(","+ingredients);
               pw3.print(","+procedure);
               pw3.print("\n");
               
               Thread.sleep(15000);
               pw2.close();
            }
         }
         //Increments num by 24 to go to next page (as observed in the website url)
         num = num + 24;
         
         Thread.sleep(15000);
         pw.close();
      }
      pw3.close();
   }
}