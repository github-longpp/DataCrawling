package crawler.DataCrawling.thread;


import crawler.DataCrawling.service.DataCrawlingService;

public class ProcessProductDataThread implements Runnable {
    private static DataCrawlingService dataCrawlingService = new DataCrawlingService();
    private int offset;

    public ProcessProductDataThread(int offset) {
        this.offset = offset;
    }

    @Override
    public void run() {
        dataCrawlingService.crawAndSaveProductData(offset);
    }
}
