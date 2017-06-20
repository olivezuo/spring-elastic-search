package com.jin.business.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jin.business.service.SkuService;

@RestController
public class SkuController {
	
	private static final Logger logger = LoggerFactory.getLogger(SkuController.class);
	
	@Autowired
	SkuService skuService;
	
	@Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

	@RequestMapping(method=RequestMethod.GET, value="/sku/search",produces = "application/json")
	public String search() {
		return skuService.search().toString();
	}
}
