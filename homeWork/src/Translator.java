import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import source.URLSourceProvider;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Provides utilities for translating texts to russian language.<br/>
 * Uses Yandex Translate API, more information at <a href="http://api.yandex.ru/translate/">http://api.yandex.ru/translate/</a><br/>
 * Depends on {@link URLSourceProvider} for accessing Yandex Translator API service
 */
public class Translator {
    private URLSourceProvider urlSourceProvider;
    /**
     * Yandex Translate API key could be obtained at <a href="http://api.yandex.ru/key/form.xml?service=trnsl">http://api.yandex.ru/key/form.xml?service=trnsl</a>
     * to do that you have to be authorized.
     */
    private static final String YANDEX_API_KEY = "trnsl.1.1.20151116T131147Z.7d762e4507683438.c8d54ef873fbd0659acf8fcb647a32c968a65082";
    private static final String TRANSLATION_DIRECTION = "ru";

    public Translator(URLSourceProvider urlSourceProvider) {
        this.urlSourceProvider = urlSourceProvider;
    }

    /**
     * Translates text to russian language
     * @param original text to translate
     * @return translated text
     * @throws IOException
     */
    public String translate(String original) throws IOException {
        URL url = new URL(prepareURL(original));
        URLConnection connection = url.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
        StringBuilder stringBuilder = new StringBuilder();
        String string ;
        while ((string=bufferedReader.readLine())!=null){
            stringBuilder.append(string);
        }
        return parseContent(stringBuilder.toString());
    }

    /**
     * Prepares URL to invoke Yandex Translate API service for specified text
     * @param text to translate
     * @return url for translation specified text
     */
    private String prepareURL(String text) {
        return "https://translate.yandex.net/api/v1.5/tr/translate?key=" + YANDEX_API_KEY + "&text=" + encodeText(text) + "&lang=" + TRANSLATION_DIRECTION;
    }

    /**
     * Parses content returned by Yandex Translate API service. Removes all tags and system texts. Keeps only translated text.
     * @param content that was received from Yandex Translate API by invoking prepared URL
     * @return translated text
     */
    private String parseContent(String content) {
        String result="";
        try {
            File tempXmlFile = File.createTempFile("temp","xml");
            try(PrintWriter outWriter = new PrintWriter(tempXmlFile.getAbsoluteFile())){
                outWriter.print(content);
            }catch (IOException e){
                e.printStackTrace();
            }
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder documentBuilder = factory.newDocumentBuilder();
                try {
                    Document document = documentBuilder.parse(tempXmlFile);
                    Element text = (Element) document.getDocumentElement().getElementsByTagName("text").item(0);
                    result=text.getTextContent();
                } catch (SAXException e) {
                    e.printStackTrace();
                }
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Encodes text that need to be translated to put it as URL parameter
     * @param text to be translated
     * @return encoded text
     */
    private String encodeText(String text) {
        try {
            return URLEncoder.encode(text,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            try {
                throw new UnsupportedEncodingException();
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
                return null;
            }
        }

    }
}
