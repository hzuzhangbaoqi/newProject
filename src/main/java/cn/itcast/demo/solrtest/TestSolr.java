package cn.itcast.demo.solrtest;

import com.pyg.pojo.TbItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-solr.xml")
public class TestSolr {

    @Autowired
    private SolrTemplate solrTemplate;

    @Test
    public void save() throws Exception {
        TbItem item = new TbItem();
        item.setId(1L);
        item.setBrand("三星");
        item.setCategory("手机");
        item.setGoodsId(1L);
        item.setSeller("华为2号专卖店");
        item.setTitle("华为Mate9");
        item.setPrice(new BigDecimal(2000));
        solrTemplate.saveBean(item);
        solrTemplate.commit();
    }

    @Test
    public void getValue() throws Exception {
        TbItem item = solrTemplate.getById(1, TbItem.class);
        System.out.println(item.getTitle());
    }

    @Test
    public void delete() throws Exception {
        solrTemplate.deleteById("1");
        solrTemplate.commit();
    }

    @Test
    public void saveBeans() throws Exception {
        List<TbItem> itemList = new ArrayList<TbItem>();

        for (int i=1;i<=100;i++){
            TbItem item = new TbItem();
            item.setId((long)i);
            item.setBrand("三星");
            item.setCategory("手机");
            item.setGoodsId(1L);
            item.setSeller("华为"+i+"号专卖店");
            item.setTitle("华为Mate"+i);
            item.setPrice(new BigDecimal(2000+i));
            itemList.add(item);

        }
        solrTemplate.saveBeans(itemList);
        solrTemplate.commit();


    }

    @Test
    public void query() throws Exception {
        Query query=new SimpleQuery("*:*");
        // where title like "%2%"
        Criteria criteria = new Criteria("item_title").contains("2");
        //where title like "%2%" and title like "%5%"
        criteria=criteria.and("item_title").contains("5");
        query.addCriteria(criteria);
        //起始位置
        query.setOffset(0);
        query.setRows(13);
        ScoredPage<TbItem> scoredPage = solrTemplate.queryForPage(query, TbItem.class);
        List<TbItem> itemList = scoredPage.getContent();
        System.out.println(scoredPage.getTotalElements());
        for (TbItem item:itemList){
            System.out.println(item.getTitle());
        }
    }

    @Test
    public void deleteAll() throws Exception {
        Query query=new SimpleQuery("*:*");
        solrTemplate.delete(query);
        solrTemplate.commit();
    }
}
