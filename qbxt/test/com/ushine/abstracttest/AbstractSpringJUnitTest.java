package com.ushine.abstracttest;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.junit.runner.RunWith;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@Component()
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@Transactional
public abstract class AbstractSpringJUnitTest<T> {
	protected Logger logger=Logger.getLogger(AbstractSpringJUnitTest.class);
	protected HttpSolrServer server;
	//不能@Autowired ISolrService
	//protected @Autowired ISolrService<T> solrService;
}
