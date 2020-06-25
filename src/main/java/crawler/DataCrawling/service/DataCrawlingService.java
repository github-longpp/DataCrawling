package crawler.DataCrawling.service;


import crawler.DataCrawling.dao.DataCrawlingDao;
import crawler.DataCrawling.entity.BrandList;
import crawler.DataCrawling.entity.ProductList;
import crawler.DataCrawling.logic.DataCrawlingLogic;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataCrawlingService {
    private static final Log LOGGER = LogFactory.getLog(DataCrawlingService.class);
    private DataCrawlingDao dataCrawlingDao = new DataCrawlingDao();
    private DataCrawlingLogic dataCrawlingLogic = new DataCrawlingLogic();

    public void crawAndsaveBrandData() {
        try {
            LOGGER.info("Start crawAndsaveBrandData process");
            List<BrandList> brandLists = dataCrawlingLogic.crawBrandData();
            dataCrawlingDao.saveData(brandLists);
        } catch (Exception e) {
            LOGGER.error("crawAndsaveBrandData process exception: ", e);
        }
    }

    public void crawAndSaveProductData(int offset) {
        try {
            LOGGER.info("Start crawAndSaveProductData process");
            List<ProductList> productLists = dataCrawlingLogic.crawProductData(offset);
            dataCrawlingDao.saveDataProduct(productLists);
        } catch (Exception e) {
            LOGGER.error("crawAndSaveProductData process exception: ", e);
        }
    }
}
