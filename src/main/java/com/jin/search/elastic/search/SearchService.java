package com.jin.search.elastic.search;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.Pair;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.jin.search.elastic.entity.CustomSearchRequest;

@Service
public class SearchService {
	
	private static final Logger logger = LoggerFactory.getLogger(SearchService.class);
	
	@Autowired
	TransportClient transportClient;
	
	BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();;
	
	private SearchResponse doSearch(SearchRequestBuilder searchRequestBuilder){
		
		SearchResponse response = searchRequestBuilder.get();
		return response;
	}
	
	public SearchResponse search(CustomSearchRequest customSearchRequest) {
				
		SearchRequestBuilder searchRequestBuilder = transportClient.prepareSearch(customSearchRequest.getIndex())
				.setTypes(customSearchRequest.getType())
				.setSearchType(SearchType.DEFAULT)
				.setFetchSource(false);
		
		searchRequestBuilder.setQuery(this.buildBoolQueryForSingleTerms(customSearchRequest.getTerms())
										.buildBoolQueryForMultiTerms(customSearchRequest.getMultiTerms())
										.buildBoolQueryForRanges(customSearchRequest.getRanges())
										.getBoolQueryBuilder());
		
		searchRequestBuilder = this.buildAggregationsForTerms(searchRequestBuilder, customSearchRequest.getTermsAggreations());
		
		return this.doSearch(searchRequestBuilder);
		
	}
	
	private SearchService buildBoolQueryForSingleTerms(Map<String, Object> terms) {
		if(CollectionUtils.isEmpty(terms)) {
			return this;
		}
		
		for(Map.Entry<String, Object> termsQuery: terms.entrySet()){
			boolQueryBuilder.must(this.buildTermQuery(termsQuery));
		}
		return this;
	}

	private TermQueryBuilder buildTermQuery(Entry<String, Object> termQuery) {
		logger.info("Here is the Term Query: {} | {}", termQuery.getKey(), termQuery.getValue());
		TermQueryBuilder termQueryBuilder = new TermQueryBuilder(termQuery.getKey(), termQuery.getValue());
		return termQueryBuilder;
		
	}
	
	private SearchService buildBoolQueryForMultiTerms(Map<String, List<Object>> multiTerms) {
		if(CollectionUtils.isEmpty(multiTerms)) {
			return this;
		}
		
		for(Map.Entry<String, List<Object>> termsQuery: multiTerms.entrySet()){
			boolQueryBuilder.must(this.buildTermsQuery(termsQuery));
		}
		return this;
	}

	private TermsQueryBuilder buildTermsQuery(Entry<String, List<Object>> termsQuery) {
		logger.info("Here is the Terms Query: {} | {}", termsQuery.getKey(), termsQuery.getValue());
		TermsQueryBuilder termsQueryBuilder = new TermsQueryBuilder(termsQuery.getKey(), termsQuery.getValue().toArray());
		return termsQueryBuilder;
		
	}

	
	private SearchService buildBoolQueryForRanges(Map<String, Pair<Object, Object>> ranges) {
		if(CollectionUtils.isEmpty(ranges)) {
			return this;
		}
		
		for(Map.Entry<String, Pair<Object, Object>> range: ranges.entrySet()){
			boolQueryBuilder.must(this.buildRangeQuery(range));
		}
		return this;
	}
	
	private RangeQueryBuilder buildRangeQuery(Entry<String, Pair<Object, Object>> range) {
		logger.info("Here is the range Query: {} | {}", range.getKey(), range.getValue());
		RangeQueryBuilder rangeQueryBuilder = new RangeQueryBuilder(range.getKey());
		rangeQueryBuilder.from(range.getValue().getLeft());
		rangeQueryBuilder.to(range.getValue().getRight());
		return rangeQueryBuilder;
	}
	
	
	
	private TermsAggregationBuilder buildTermsAggregation(Entry<String, String> termsAggreation){
		TermsAggregationBuilder termsAggregationBuilder = new TermsAggregationBuilder(termsAggreation.getKey(),null);
		termsAggregationBuilder.field(termsAggreation.getValue());
		return termsAggregationBuilder;
		
	}
	
	private SearchRequestBuilder buildAggregationsForTerms(SearchRequestBuilder searchRequestBuilder, Map<String, String> termsAggreations) {
		if(!CollectionUtils.isEmpty(termsAggreations)) {
			for(Map.Entry<String, String> termsAggreation: termsAggreations.entrySet()){
				searchRequestBuilder.addAggregation(this.buildTermsAggregation(termsAggreation));
			}
		}
		
		return searchRequestBuilder;
		
	}

	public BoolQueryBuilder getBoolQueryBuilder() {
		return boolQueryBuilder;
	}
	
	public void setBoolQueryBuilder(BoolQueryBuilder boolQueryBuilder) {
		this.boolQueryBuilder = boolQueryBuilder;
	}

}
