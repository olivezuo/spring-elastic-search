package com.jin.search.elastic.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ElasticsearchProperties.class)
public class ElasticSearchConfig {
	private static final Logger logger = LoggerFactory.getLogger(ElasticSearchConfig.class);

	@Autowired
	ElasticsearchProperties elasticsearchProperties;

	@Bean
    public TransportClient elasticTransportClient() throws UnknownHostException {
		String usernamepassword = elasticsearchProperties.getProperties().get("username") + ":" + elasticsearchProperties.getProperties().get("password");
		Settings settings = Settings.builder().put("cluster.name", elasticsearchProperties.getClusterName())
				.put("client.transport.sniff", false)
				.put("xpack.security.user", usernamepassword)
				.build();
        String server = elasticsearchProperties.getClusterNodes().split(":")[0];
        Integer port = Integer.parseInt(elasticsearchProperties.getClusterNodes().split(":")[1]);
        TransportClient client = new PreBuiltXPackTransportClient(settings).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(server), port));
        
        return  client;

    }
}
