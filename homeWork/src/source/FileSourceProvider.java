package source;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Implementation for loading content from local file system.
 * This implementation supports absolute paths to local file system without specifying file:// protocol.
 * Examples c:/1.txt or d:/pathToFile/file.txt
 */
public class FileSourceProvider implements SourceProvider {

    @Override
    public boolean isAllowed(String pathToSource) {
        return Files.exists(Paths.get(pathToSource));

    }

    @Override
    public String load(String pathToSource) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

            try(BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(pathToSource), StandardCharsets.UTF_8)){
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
