package com.info.rest;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.info.request.SearchRequest;
import com.info.response.SearchResponse;
import com.info.service.IEligibilityService;

@RestController
public class ReportRestController {
	
	@Autowired
	private IEligibilityService service;
	
	@GetMapping("/plans")
	public ResponseEntity<List<String>> getPlanNames(){
		List<String> planNames = service.getUniquePlanNames();
		return new ResponseEntity<>(planNames,HttpStatus.OK);
	}
	
	@GetMapping("/statuses")
	public ResponseEntity<List<String>> getPlanStatuses(){
		List<String> planStatuses = service.getUniquePlanStatuses();
		return new ResponseEntity<>(planStatuses,HttpStatus.OK);
	}
	
	@PostMapping("/search")
	public ResponseEntity<List<SearchResponse>> search(@RequestBody SearchRequest request) throws Exception{
		List<SearchResponse> response = service.search(request);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	@GetMapping("/excel")
	public void excelReport(HttpServletResponse response) throws Exception {
		response.setContentType("application/octet-stream");
		String headerKey="Content-DIsposition";
		String headerValue="attachment;filename=data.xls";
		response.setHeader(headerKey, headerValue);
		service.generateExcel(response);
	}
	
	@GetMapping("/pdf")
	public void pdfReport(HttpServletResponse response) throws Exception {
		response.setContentType("application/pdf");
		String headerKey="Content-DIsposition";
		String headerValue="attachment;filename=data.pdf";
		response.setHeader(headerKey, headerValue);
		service.generatePdf(response);
	}
}
