package com.jin.search.elastic.search;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DocumentService {

	private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);
			
	@Autowired
	TransportClient transportClient;
		
	public<D> void indexDoc(D doc, String index, String type, String id) throws JsonProcessingException{
		ObjectMapper ObjectMapper = new ObjectMapper();
		byte[] jsonDoc = ObjectMapper.writeValueAsBytes(doc);
		
		IndexResponse response = transportClient.prepareIndex(index, type, id)
		        .setSource(jsonDoc, XContentType.JSON)
		        .get();
		logger.info(response.toString());
	}
		
	public void deleteDoc(String index, String type, String id){
		DeleteResponse response = transportClient.prepareDelete(index, type, id)
		        .get();
		logger.info(response.toString());
	}
	
	public void updateDoc(String index, String type, String id, Map<String, Object> fields) throws InterruptedException, ExecutionException, IOException{
		
		XContentBuilder builder = buildDocSource(fields);
		
		UpdateRequest updateRequest = new UpdateRequest(index, type, id)
				.doc(builder);
		
		UpdateResponse response = transportClient.update(updateRequest)
				.get();
		logger.info(response.toString());
	}

	private XContentBuilder buildDocSource(Map<String, Object> fields) throws IOException {
		XContentBuilder builder = XContentFactory.jsonBuilder();
		builder.startObject();
		
		for(Map.Entry<String, Object> field: fields.entrySet()){
			String name = field.getKey();
			Object value = field.getValue();
			builder.field(name,value);			
		}
		
		builder.endObject();
		return builder;
	}
}
