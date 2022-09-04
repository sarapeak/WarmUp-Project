import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.*;
import java.util.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class warmupfileupdated {

    public static void main(String[] args) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.surlatable.com/recipes/?srule=best-matches&start=0&sz=24")) //starting URL…
                .GET() // GET is default
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

         
//output to a file so its easy to mess with (you won’t be for your finished program)
         FileOutputStream fs = new FileOutputStream("outputmenu.txt");
         PrintWriter pw = new PrintWriter(fs);
         pw.println(response.body()); //response.body() is the html source code in a string format. It outputs to a file so you can see it easier right now, but you will ultimately want to just manipulate the strings a lot
        
        //scanner to open the big web file
        Scanner s = new Scanner(new FileInputStream("outputmenu.txt"));
        
        //thumb = tall tell
        String word = "thumb";
        
        //loop through all of the lines
         while(s.hasNextLine()) {
         
         String line = s.nextLine();
         int linkIndex= -1;
         int linkIndexEnd = -1;
         int yesbreak =0;
         
         //if we find thumb, that means there is an important link
         if(line.indexOf(word)!=-1) {
       
            //find the first and last index of the link
            linkIndex = line.indexOf(word) + 18;
            linkIndexEnd = line.indexOf("title") - 2;
            
            String link = line.substring(linkIndex,linkIndexEnd);
            
            //debugging
            yesbreak = 1;
            
            //open up that file and save it to a new one
                    HttpClient client2 = HttpClient.newHttpClient();
                    HttpRequest request2 = HttpRequest.newBuilder()
                    
                .uri(URI.create(link)) //starting URL…
                .GET() // GET is default
                .build();

        HttpResponse<String> response2 = client2.send(request2,
                HttpResponse.BodyHandlers.ofString());

         
//output to a file so its easy to mess with (you won’t be for your finished program)
         FileOutputStream fs2 = new FileOutputStream("outputmenuspecific.txt");
         PrintWriter pw2 = new PrintWriter(fs2);
         pw2.println(response2.body());
         
         Scanner s2 = new Scanner(new FileInputStream("outputmenuspecific.txt"));
         String ingredients = "";

                 
         while(s2.hasNextLine()) {
         //System.out.print("test");
         String line2 = s2.nextLine();
              int yesbreak2 =0;
         String ingredientstart = "<ul>";
         String ingredientstop = "</ul>";
                  //if we find thumb, that means there is an important link
         if(line2.indexOf(ingredientstart)!=-1) {
            
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
            //debugging
            yesbreak2 =1;
            
            }
            
            if (yesbreak2 == 1)
            break;            
                    
         }
         
         //turn unicode fractions into doubles 
         
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
         
        
         //ingredients.replace("&#188",".25")
      System.out.println(ingredients);
      
         Thread.sleep(10000);
         pw2.close();
        
        }
        
        }
        pw.close();
        
       } 
    }
