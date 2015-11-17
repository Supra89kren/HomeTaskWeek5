package source;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Implementation for loading content from specified URL.<br/>
 * Valid paths to load are http://someurl.com, https://secureurl.com, ftp://frpurl.com etc.
 */
public class URLSourceProvider implements SourceProvider {

    @Override
    public boolean  isAllowed(String pathToSource) {
        try{
            Path path = Paths.get(new URI(pathToSource));
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String load(String pathToSource) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        try(BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(new URI(pathToSource)), StandardCharsets.UTF_8)){
            String readLine;
            while ((readLine=bufferedReader.readLine())!=null){
                stringBuilder.append(readLine);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return stringBuilder.toString();

    }
}
