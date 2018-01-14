package com.unsocialmedia.intouch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.unsocialmedia.intouch.Controllers.Customer;
import com.unsocialmedia.intouch.cassandra.CassandraConfig;
import org.apache.cassandra.exceptions.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cassandra.core.cql.CqlIdentifier;
import org.springframework.data.cassandra.core.CassandraAdminOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CassandraConfig.class)
public class IntouchApplicationTests {

	private static final Logger LOGGER = LoggerFactory.getLogger(IntouchApplicationTests.class);

	public static final String KEYSPACE_CREATION_QUERY = "CREATE KEYSPACE IF NOT EXISTS testKeySpace WITH replication = { 'class': 'SimpleStrategy', 'replication_factor': '3' };";

	public static final String KEYSPACE_ACTIVATE_QUERY = "USE testKeySpace;";

	public static final String DATA_TABLE_NAME = "customer";

	@Autowired
	private CassandraAdminOperations adminTemplate;

	@Test
	public void contextLoads() {
	}

	@BeforeClass
	public static void startCassandraEmbedded() throws InterruptedException, TTransportException, ConfigurationException, IOException {
		EmbeddedCassandraServerHelper.startEmbeddedCassandra();
		Cluster cluster = Cluster.builder()
				.addContactPoints("127.0.0.1").withPort(9142).build();
		Session session = cluster.connect();
	}
	@Before
	public void createTable() {
		adminTemplate.createTable(
				true, CqlIdentifier.cqlId(DATA_TABLE_NAME),
				Customer.class, new HashMap<String, Object>());
	}

	@AfterClass
	public static void stopCassandraEmbedded() {
		EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
	}

}
