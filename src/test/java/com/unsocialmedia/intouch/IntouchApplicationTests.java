package com.unsocialmedia.intouch;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.hamcrest.Matchers;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.unsocialmedia.intouch.Controllers.Customer;
import com.unsocialmedia.intouch.cassandra.CassandraConfig;
import org.apache.cassandra.exceptions.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.cassandra.core.cql.CqlIdentifier;
import org.springframework.data.cassandra.core.CassandraAdminOperations;
import org.springframework.data.cassandra.core.CassandraAdminTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import sun.rmi.runtime.Log;


import javax.xml.ws.Response;
import java.io.IOException;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

//@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CassandraConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"spring.cloud.config.enabled:false", "eureka.client.enabled:false"})
@EnableAutoConfiguration
public class IntouchApplicationTests {

    @Value("http://localhost:${local.server.port}")
    public String host;

    private static final Logger LOGGER = LoggerFactory.getLogger(IntouchApplicationTests.class);

    public static final String KEYSPACE_CREATION_QUERY = "CREATE KEYSPACE IF NOT EXISTS keySpaceTest WITH replication = { 'class': 'SimpleStrategy', 'replication_factor': '3' };";

    public static final String KEYSPACE_ACTIVATE_QUERY = "USE keySpaceTest;";

    public static final String DATA_TABLE_NAME = "customer";

    @Autowired
    private CassandraAdminOperations adminTemplate;

    @ClassRule
    public static OutputCapture outputCapture = new OutputCapture();

    @BeforeClass
    public static void startCassandraEmbedded() throws InterruptedException, TTransportException, ConfigurationException, IOException {
//        EmbeddedCassandraServerHelper.startEmbeddedCassandra(25000);
//        final Cluster cluster = Cluster.builder()
//                .addContactPoints("127.0.0.1").withPort(9042).build();
//        LOGGER.info("Cluster created");
//        final Session session = cluster.connect();
        EmbeddedCassandraServerHelper.startEmbeddedCassandra(20000L);
        Cluster cluster = new Cluster.Builder().addContactPoints("127.0.0.1").withPort(9142).build();
        Session session = cluster.connect();
        session.execute(KEYSPACE_CREATION_QUERY);
        session.execute(KEYSPACE_ACTIVATE_QUERY);
        LOGGER.info("KeySpace created and activated.");
        Thread.sleep(5000);
    }
//
//    	@Test
//	public void contextLoads() {
//	}

    @Before
    public void createTable() {
        adminTemplate.createTable(
                true, CqlIdentifier.cqlId(DATA_TABLE_NAME),
                Customer.class, new HashMap<String, Object>());
    }

    @Test
    public void testDefaultSettings() throws IOException {

        // Given
        HttpUriRequest request = new HttpGet( host + "/cassandra");

        // When
        HttpResponse response = HttpClientBuilder.create().build().execute( request );
        LOGGER.info(response.toString());
        String output = response.toString();
        assertThat(output, containsString("firstName='Bob', lastName='Smith'"));
    }

    @AfterClass
    public static void stopCassandraEmbedded() {
        EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
    }

}
