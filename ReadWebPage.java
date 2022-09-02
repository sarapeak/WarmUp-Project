import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.*;

public class ReadWebPage
{
   public static void main(String[] args) throws IOException, InterruptedException
   {
      HttpClient client = HttpClient.newHttpClient();   //Download the webpage
      int num = 0;
      int check = 0;
      for(int start = 0; start < 2; start++)
      {
         HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://www.surlatable.com/recipes/?srule=best-matches&start="+Integer.toString(num)+"&sz=24")) //starting URL…
            .GET() // GET is default
            .build();
         
         num = num + 24;
         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      
         //output to a file so its easy to mess with (you won’t be for your finished program)
         if (check == 0)
         {
            FileOutputStream fs = new FileOutputStream("output.txt");
            PrintWriter pw = new PrintWriter(fs);
            pw.println(response.body()); //response.body() is the html source code in a string format. It outputs to a file so you can see it easier right now, but you will ultimately want to just manipulate the strings a lot
            pw.close();
         }
         else
         {
            FileOutputStream fs = new FileOutputStream("output1.txt");
            PrintWriter pw = new PrintWriter(fs);
            pw.println(response.body()); //response.body() is the html source code in a string format. It outputs to a file so you can see it easier right now, but you will ultimately want to just manipulate the strings a lot
            pw.close();
         }
         check++;
      }
   }
}