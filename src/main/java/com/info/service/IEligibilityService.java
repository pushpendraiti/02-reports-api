package com.info.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.info.request.SearchRequest;
import com.info.response.SearchResponse;

public interface IEligibilityService {
	
	public List<String> getUniquePlanNames();

	public List<String> getUniquePlanStatuses();

	public List<SearchResponse> search(SearchRequest request) throws Exception;

	public void generateExcel(HttpServletResponse response) throws Exception;

	public void generatePdf(HttpServletResponse response) throws Exception;
}
