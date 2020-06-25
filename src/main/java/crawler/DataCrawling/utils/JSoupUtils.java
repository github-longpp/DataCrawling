package crawler.DataCrawling.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JSoupUtils {
    private static String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36";
    public static Document connectJSoup(String url) {
        Document document = null;
        try {
            document = Jsoup.connect(url)
                    .userAgent(userAgent)
                    .ignoreContentType(true)
                    .header("Content-Language", "en-US")
                    .timeout(0)
                    .get();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return document;
    }
}
