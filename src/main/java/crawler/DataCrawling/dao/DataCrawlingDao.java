package crawler.DataCrawling.dao;



import crawler.DataCrawling.entity.BrandList;
import crawler.DataCrawling.entity.PageLinks;
import crawler.DataCrawling.entity.ProductList;
import crawler.DataCrawling.utils.ConnectionDB;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.*;

@Repository
public class DataCrawlingDao {
    private static final Log LOGGER = LogFactory.getLog(DataCrawlingDao.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public List<BrandList> getBrandList(int offset) {
        List<BrandList> productLists = new ArrayList<>();
        try {
            Query query = entityManager.createNativeQuery("SELECT * FROM `brand_list` WHERE `status` = 0");
            query.setFirstResult(offset);
            query.setMaxResults(10);
            query.unwrap(org.hibernate.query.Query.class).setResultTransformer(Transformers.aliasToBean(BrandList.class));
            productLists = query.getResultList();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return productLists;
    }

//    public List<PageLinks> getAllPageLinks() {
//        List<PageLinks> pageLinksList = new ArrayList<>();
//        try {
//            PageLinks pageLinks = new PageLinks();
//            Connection conn = ConnectionDB.getMySQLConnection();
//            Statement stmt = conn.createStatement();
//            ResultSet rs = stmt.executeQuery("SELECT * FROM page_link");
//            while(rs.next()) {
//                pageLinks.setBrandName(rs.getString(2));
//                pageLinks.setUrl(rs.getString(3));
//                pageLinksList.add(pageLinks);
//            }
//            stmt.close();
//            conn.close();
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//        return pageLinksList;
//    }
    @Transactional
    public void saveData(List<BrandList> productLists){
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO brand_list(brandName, url) ");
            sql.append("VALUES ");
            List<String> values = new ArrayList<>();
            for (int i = 0; i < productLists.size(); i++) {
                StringBuilder value = new StringBuilder();
                value.append("( ");
                value.append(":i_brandName, :i_url");
                value.append(" )");
                values.add(value.toString().replace("i_", String.valueOf(i)));
            }
            sql.append(String.join(",", values));
            Query query = entityManager.createNativeQuery(sql.toString());
            for (int i = 0; i < productLists.size(); i++) {
                BrandList brandList = productLists.get(i);
                query.setParameter(i + "brandName", brandList.getBrandName());
                query.setParameter(i + "url", brandList.getUrl());
            }
            LOGGER.info("--> Insert records :" + query.executeUpdate());

        } catch(Exception e) {
            LOGGER.error("saveData process exception: ", e);
        }
    }
    @Transactional
    public void saveDataProduct(List<ProductList> productLists) {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO product_list(productName, url, brandName) ");
            sql.append("VALUES ");
            List<String> values = new ArrayList<>();
            for (int i = 0; i < productLists.size(); i++) {
                StringBuilder value = new StringBuilder();
                value.append("( ");
                value.append(":i_productName, :i_url, :i_brandName");
                value.append(" )");
                values.add(value.toString().replace("i_", String.valueOf(i)));
            }
            sql.append(String.join(",", values));
            Query query = entityManager.createNativeQuery(sql.toString());
            for (int i = 0; i < productLists.size(); i++) {
                ProductList productList = productLists.get(i);
                query.setParameter(i + "productName", productList.getProductName());
                query.setParameter(i + "url", productList.getUrl());
                query.setParameter(i + "brandName", productList.getBrandName());
            }
            LOGGER.info("--> Insert records :" + query.executeUpdate());
        } catch(Exception e) {
            LOGGER.error("saveDataProduct process exception: ", e);

        }
    }

//    @Transactional
//    public void saveAllPageLink(HashMap<String, List<String>> pageLinkMap) {
//        try {
//            StringBuilder sql = new StringBuilder();
//            sql.append("INSERT INTO product_list(productName, url, brandName) ");
//            sql.append("VALUES ");
//            List<String> values = new ArrayList<>();
//            for (int i = 0; i < pageLinkMap.size(); i++) {
//                StringBuilder value = new StringBuilder();
//                value.append("( ");
//                value.append(":i_productName, :i_url, :i_brandName");
//                value.append(" )");
//                values.add(value.toString().replace("i_", String.valueOf(i)));
//            }
//            sql.append(String.join(",", values));
//            Query query = entityManager.createNativeQuery(sql.toString());
//            for (int i = 0; i < productLists.size(); i++) {
//                ProductList productList = productLists.get(i);
//                query.setParameter(i + "productName", productList.getProductName());
//                query.setParameter(i + "url", productList.getUrl());
//                query.setParameter(i + "brandName", productList.getBrandName());
//            }
//            LOGGER.info("--> Insert records :" + query.executeUpdate());
////            LOGGER.info("Start saveAllPageLink process");
////
////            // Lấy ra kết nối tới cơ sở dữ liệu.
////            Connection connection = ConnectionDB.getMySQLConnection();
////            PreparedStatement prepStmt = connection.prepareStatement(
////                    "INSERT INTO page_link(brandName, url) VALUES (?,?)");
////
////            for(Map.Entry<String, List<String>> entry : pageLinkMap.entrySet()) {
////                for (String url : entry.getValue()) {
////                    prepStmt.setString(1,entry.getKey());
////                    prepStmt.setString(2,url);
////                    prepStmt.execute();
////                }
////            }
////            connection.commit();
////            prepStmt.close();
////            connection.close();
//        } catch(Exception e) {
//            LOGGER.error("saveDataProduct process exception: ", e);
//
//        }
//    }
    @Transactional
    public void udpateBrandList(BrandList brandList) {
        try {
            entityManager.merge(brandList);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
