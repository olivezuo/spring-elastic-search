package com.jin.search.elastic.entity;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
public class CustomSearchRequest {

	private String index;
	
	private String type;
	
	private Map<String, List<Object>> multiTerms;
	
	private Map<String, Object> terms;
	
	private Map<String, Pair<Object, Object>> ranges;
	
	private Map<String, String> termsAggreations;

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, List<Object>> getMultiTerms() {
		return multiTerms;
	}

	public void setMultiTerms(Map<String, List<Object>> multiTerms) {
		this.multiTerms = multiTerms;
	}

	public Map<String, Object> getTerms() {
		return terms;
	}

	public void setTerms(Map<String, Object> terms) {
		this.terms = terms;
	}

	public Map<String, Pair<Object, Object>> getRanges() {
		return ranges;
	}

	public void setRanges(Map<String, Pair<Object, Object>> ranges) {
		this.ranges = ranges;
	}

	public Map<String, String> getTermsAggreations() {
		return termsAggreations;
	}

	public void setTermsAggreations(Map<String, String> termsAggreations) {
		this.termsAggreations = termsAggreations;
	}
	
	
	
}
