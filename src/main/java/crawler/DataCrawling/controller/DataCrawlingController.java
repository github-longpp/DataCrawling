package crawler.DataCrawling.controller;


import crawler.DataCrawling.service.DataCrawlingService;
import crawler.DataCrawling.thread.ProcessProductDataThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataCrawlingController {

    public DataCrawlingController() {
        System.setProperty("https.proxyHost", "52.179.18.244");
        System.setProperty("https.proxyPort", "8080");
    }

    @Autowired
    private DataCrawlingService dataCrawlingService;
    @GetMapping(value = "/getData")
    public String getData() {
        dataCrawlingService.crawAndsaveBrandData();
        ProcessProductDataThread run1 = new ProcessProductDataThread(0);
        Thread t1 = new Thread(run1);
        t1.start();

        ProcessProductDataThread run2 = new ProcessProductDataThread(10);
        Thread t2 = new Thread(run2);
        t2.start();

        ProcessProductDataThread run3 = new ProcessProductDataThread(20);
        Thread t3 = new Thread(run3);
        t3.start();
        return "Success";
    }
}
