import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.*;
import java.util.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class warmupfile {

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
            System.out.println(line + " + " +linkIndex);
            System.out.println(link);
            
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
         pw2.close();
            
         }
         
         if(yesbreak==1)
            break;
      }
        
        
        
        
        pw.close();
        
        
    }
}
