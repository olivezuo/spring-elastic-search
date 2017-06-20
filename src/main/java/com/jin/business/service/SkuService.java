package com.jin.business.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jin.business.entity.Sku;
import com.jin.search.elastic.entity.CustomSearchRequest;
import com.jin.search.elastic.search.DocumentService;
import com.jin.search.elastic.search.SearchService;

@Service
public class SkuService {

	@Autowired
	DocumentService documentService;
	
	@Autowired
	SearchService searchService;
	
	public void addDoc() throws InterruptedException, ExecutionException, IOException{
		Sku sku = new Sku();
		
		sku.setHeight(10.0);
		sku.setWidth(5.0);
		sku.setLength(20.0);
		sku.setName("FirstSKU");
		sku.setShortDescription("This is our second sku on site");
		sku.setSku("SKU001");
		
		documentService.indexDoc(sku, "sku", "sku", sku.getSku());				
	}
	
	//@PostConstruct
	public void updateDoc() throws InterruptedException, ExecutionException, IOException{
		
		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("name", "FourthSKU");
		fields.put("length", 21);
		
		documentService.updateDoc("sku", "sku", "sku001", fields);	
	}
	
	public SearchResponse search() {
		CustomSearchRequest customSearchRequest = new CustomSearchRequest();
		
		customSearchRequest.setIndex("sku");
		customSearchRequest.setType("sku");
		
		Map<String, Object> terms = new HashMap<String, Object>();
		terms.put("sku", "sku001");
		customSearchRequest.setTerms(terms);
		
		Map<String, String> termsAggreations = new HashMap<String, String>();
		termsAggreations.put("Width", "width");
		termsAggreations.put("Length", "length");
		customSearchRequest.setTermsAggreations(termsAggreations);
		
		SearchResponse searchResponse = searchService.search(customSearchRequest);
		
		return searchResponse;
		
	}
}
