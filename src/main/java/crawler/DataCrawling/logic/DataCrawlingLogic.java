package crawler.DataCrawling.logic;


import crawler.DataCrawling.dao.DataCrawlingDao;
import crawler.DataCrawling.entity.BrandList;
import crawler.DataCrawling.entity.ProductList;
import crawler.DataCrawling.utils.JSoupUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataCrawlingLogic {
    private static final Log LOGGER = LogFactory.getLog(DataCrawlingLogic.class);

    @Autowired
    private DataCrawlingDao dataCrawlingDao;

    public List<BrandList> crawBrandData() {
        LOGGER.info("Start crawBrandData process");
        List<BrandList> brandLists = new ArrayList<>();
        try {
            Document doc = JSoupUtils.connectJSoup("https://www.gsmarena.com/makers.php3");
            Elements links = doc.select("div.st-text > table > tbody > tr > td > a[href]");


            for (Element link : links) {
                BrandList brandList = new BrandList();
                String brandName = "";
                String brandNameFull = link.text();
                String[] brandNameArray = brandNameFull.split(" ");
                if (brandNameArray.length > 3) {
                    brandName = brandNameArray[0] + brandNameArray[1];
                } else {
                    brandName = brandNameArray[0];
                }
                brandList.setBrandName(brandName);
                brandList.setUrl("https://gsmarena.com/" + link.attr("href"));
                brandLists.add(brandList);
            }
        } catch (Exception e) {
            LOGGER.error("crawBrandData process exception: ", e);
        }
        return brandLists;
    }

    public List<ProductList> crawProductData(int offset) {
        LOGGER.info("Start crawProductData process");
        HashMap<String, List<String>> pageLinkMap = getAllPageLinks(offset);
        List<ProductList> productLists = new ArrayList<>();
        try {
            for(Map.Entry<String, List<String>> entry : pageLinkMap.entrySet()) {
                for (String url : entry.getValue()) {
                    Document doc = JSoupUtils.connectJSoup(url);

                    Elements links = doc.select("div.makers > ul > li > a[href]");

                    ProductList productList = new ProductList();
                    for (Element link : links) {
                        productList.setProductName(link.getElementsByTag("span").text());
                        productList.setBrandName(entry.getKey());
                        productList.setUrl("https://gsmarena.com/" + link.attr("href"));
                        productLists.add(productList);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("crawProductData process exception: ", e);
        }
        return productLists;
    }

    public HashMap<String, List<String>> getAllPageLinks(int offset) {
        LOGGER.info("Start getAllPageLinks process");
        List<BrandList> brandLists = dataCrawlingDao.getBrandList(offset);
        HashMap<String, List<String>> pageLinkMap = new HashMap<>();
        List<String> pageLinksList = new ArrayList<>();
        try {
            for (BrandList brandList : brandLists) {
                pageLinksList.add(brandList.getUrl());
                Document doc = JSoupUtils.connectJSoup(brandList.getUrl());
                Elements links = doc.select("div.nav-pages > a[href]");
                for (Element link : links) {
                    pageLinksList.add("https://www.gsmarena.com/" + link.attr("href"));
                }
                pageLinkMap.put(brandList.getBrandName(), pageLinksList);
                brandList.setStatus(1);
                dataCrawlingDao.udpateBrandList(brandList);
            }

        } catch (Exception e) {
            LOGGER.error("getAllPageLinks process exception: ", e);
        }
//        dataCrawlingDao.saveAllPageLink(pageLinkMap);
        return pageLinkMap;
    }
}
