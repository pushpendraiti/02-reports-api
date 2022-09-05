package com.info.service.impl;

import java.awt.Color;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import com.info.entity.EligibilityDetails;
import com.info.repo.IEligibilityDetailsRepo;
import com.info.request.SearchRequest;
import com.info.response.SearchResponse;
import com.info.service.IEligibilityService;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class EligibilityServiceImpl implements IEligibilityService {

	@Autowired
	private IEligibilityDetailsRepo eligibilityDetailsRepo;

	@Override
	public List<String> getUniquePlanNames() {

		List<EligibilityDetails> findAll = eligibilityDetailsRepo.findAll();
		List<String> planList = findAll.stream().map(EligibilityDetails::getName).distinct()
				.collect(Collectors.toList());

		return planList;

		/*
		 * List<String> findPlanNames = eligibilityDetailsRepo.findPlanNames();
		 * 
		 */
		// return findPlanNames;

	}

	@Override
	public List<String> getUniquePlanStatuses() {

		List<EligibilityDetails> findAll = eligibilityDetailsRepo.findAll();
		List<String> planStatusList = findAll.stream().map(EligibilityDetails::getPlanStatus).distinct()
				.collect(Collectors.toList());
		return planStatusList;

		// return eligibilityDetailsRepo.findPlanStatuses();
	}

	@Override
	public List<SearchResponse> search(SearchRequest request) throws Exception{

		List<SearchResponse> response = new ArrayList<>();

		EligibilityDetails queryBuilder = new EligibilityDetails();

		String planName = request.getPlanName();
		if (planName != null && !planName.equals("")) {
			queryBuilder.setPlanName(planName);
		}

		String planStatus = request.getPlanStatus();
		if (planStatus != null && !planStatus.equals("")) {
			queryBuilder.setPlanStatus(planStatus);
		}

		LocalDate planStartDate = request.getPlanStartDate();
		if (planStartDate != null) {
			queryBuilder.setPlanStartDate(planStartDate);
		}

		LocalDate planEndDate = request.getPlanEndDate();
		if (planEndDate != null) {
			queryBuilder.setPlanEndDate(planEndDate);
		}

		Example<EligibilityDetails> example = Example.of(queryBuilder);

		List<EligibilityDetails> entities = eligibilityDetailsRepo.findAll(example);
		for (EligibilityDetails entity : entities) {
			SearchResponse sr = new SearchResponse();
			BeanUtils.copyProperties(entity, sr);
			response.add(sr);

		}
		return response;
	}

	@Override
	public void generateExcel(HttpServletResponse response) throws Exception {

		List<EligibilityDetails> entities = eligibilityDetailsRepo.findAll();

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		HSSFRow hederRow = sheet.createRow(0);
		hederRow.createCell(0).setCellValue("Name");
		hederRow.createCell(1).setCellValue("Email");
		hederRow.createCell(2).setCellValue("Mobile");
		hederRow.createCell(3).setCellValue("Gender");
		hederRow.createCell(4).setCellValue("SSN");

		int i = 1;
		for (EligibilityDetails entity : entities) {

			HSSFRow dataRow = sheet.createRow(i);
			dataRow.createCell(0).setCellValue(entity.getName());
			dataRow.createCell(1).setCellValue(entity.getEmail());
			dataRow.createCell(2).setCellValue(String.valueOf(entity.getMobile()));
			dataRow.createCell(3).setCellValue(String.valueOf(entity.getGender()));
			dataRow.createCell(4).setCellValue(entity.getSsn());
			i++;
		}

		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();

	}

	@Override
	public void generatePdf(HttpServletResponse response) throws Exception {

		List<EligibilityDetails> entities = eligibilityDetailsRepo.findAll();

		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());
		document.open();
		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(18);
		font.setColor(Color.BLUE);

		Paragraph p = new Paragraph("List of Users", font);
		p.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(p);

		PdfPTable table = new PdfPTable(5);
		table.setWidthPercentage(100f);
		table.setWidths(new float[] { 1.5f, 3.5f, 3.0f, 3.0f, 1.5f });
		table.setSpacingBefore(10);

		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(5);

		Font font1 = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.WHITE);

		cell.setPhrase(new Phrase("Name", font1));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Email", font1));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Phone", font1));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Gender", font1));
		table.addCell(cell);

		cell.setPhrase(new Phrase("SSN", font1));
		table.addCell(cell);

		for (EligibilityDetails entity : entities) {
			table.addCell(entity.getName());
			table.addCell(entity.getEmail());
			table.addCell(String.valueOf(entity.getMobile()));
			table.addCell(String.valueOf(entity.getGender()));
			table.addCell(String.valueOf(entity.getSsn()));
		}
		document.add(table);
		document.close();
	}

}
