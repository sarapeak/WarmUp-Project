import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.*;
import java.util.*;
public class ReadWebPageTAP
{
    public static void main(String[] args) throws IOException, InterruptedException
    {
       HttpClient client = HttpClient.newHttpClient(); //downloads the webpage
       HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://www.surlatable.com/mussels-from-brussels/REC-275789.html?cgid=recipes&start=NaN")) //starting URL...
            .GET() // GET is default
            .build();
       HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
       FileOutputStream fs = new FileOutputStream("recipe.txt");
       PrintWriter pw = new PrintWriter(fs);
       pw.println(response.body());
       pw.close();
        
       //use a scanner to go through the text file
       Scanner scan = new Scanner(new FileInputStream("recipe.txt"));
       String authorKey = "recipe-author"; //tell tail for the author 
       String line = "";
       String author = "";
        
       String pathKey = "-element\""; // the tell tail for the path
       String title;
       ArrayList<String> path = new ArrayList<String>(); //create an array list of the path
        
       while(scan.hasNextLine())
       {
           line = scan.nextLine();
           if(line.indexOf(authorKey)!=-1)
           {
               author = scan.nextLine(); //save the author as a string variable
           }

           if(line.indexOf(pathKey) != -1)
           {
             path.add(line.substring(line.lastIndexOf("\">")+2,line.lastIndexOf("<"))); //need from "> to <
           }
       }
       title = path.get(path.size()-1); //title is the last index in arraylist
       //instead of print statements write to CSV file ?
       System.out.println(title);
       for(int i=0; i<path.size(); i++)
       {
         System.out.print(path.get(i) + "/");
       }
       System.out.println("\n"+author);
         
     }
}