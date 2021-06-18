package com.ecbank.common.util;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.ibm.icu.math.BigDecimal;

public class ExcelUtil {

	protected static final Log logger = LogFactory.getLog(ExcelUtil.class);

	private String[] excelHeader; //= {};
	private String[] columnKey;
	private ServletOutputStream out;
	private int rowCnt = 0;
	private int pageSize = 10000;
	private SXSSFWorkbook workbook;
	private int totalCnt = 0;
	private Sheet sheet;
	private Row headerRow;
	private List<Map<String,Object>> dataList;
	private Map<String,Object> partData;


	public ExcelUtil(){
		this.workbook = new SXSSFWorkbook(this.pageSize);
	}

	public ExcelUtil(int pageSize){
		this.workbook = new SXSSFWorkbook(pageSize);
	}

	public void setHeader(String[] header){
		this.excelHeader = header;
	}

	public void setCoulumn(String[] column){
		this.columnKey = column;
	}

	public void setHeaderData(String[][] headerData) {

		this.excelHeader = headerData[0];
		this.columnKey = headerData[1];
	}

	public void setRowNumber(int rowCnt){
		this.rowCnt = rowCnt;
	}

	public void setTotalCnt(int totalCnt){
		this.totalCnt = totalCnt;
	}

	public void createSheet(){
		sheet = this.workbook.createSheet();
	}

	public void createWorkSheetHeader(int rowCnt){

		setRowNumber(rowCnt);

		headerRow = sheet.createRow(this.rowCnt++);

		for (int j=0;j<excelHeader.length;j++) {
			Cell headerCell = headerRow.createCell(j);
			headerCell.setCellValue(excelHeader[j]);
		}

	}

	public void setExcelDataList(List<Map<String,Object>> dataList){
		this.dataList = dataList;

		for (Map<String,Object> itemMap : dataList) {
			Row bodyRow = sheet.createRow(this.rowCnt++);
			for (int c=0;c<columnKey.length;c++) {
				Cell cell = bodyRow.createCell(c);
				if (itemMap.get(columnKey[c]) instanceof BigDecimal) {
					cell.setCellValue(Double.parseDouble(ObjectUtils.toString(itemMap.get(columnKey[c]), "")));
				} else {
					cell.setCellValue( ObjectUtils.toString(itemMap.get(columnKey[c]), "") );
				}
			}
		}

	}

	public void writeWorkbook(ServletOutputStream stream) throws IOException{
		this.workbook.write(stream);
		closeWorkbook();

	}


	public void writeWorkbook(HttpServletRequest request, HttpServletResponse response, String fileName) throws IOException{
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Set-Cookie", "fileDownload=true; path=/");
		response.setHeader("Content-Disposition","attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + ".xlsx\"");
		ServletOutputStream out = response.getOutputStream();

		try {

			this.workbook.write(out);
			closeWorkbook();
		} catch( Exception e) {
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Content-Type","text/html; charset=utf-8");

			out = null;
			try {
				out = response.getOutputStream();
				byte[] data = new String("fail..").getBytes();
				out.write(data, 0, data.length);
			} catch(Exception ignore) {
				logger.error(ignore.getLocalizedMessage(),ignore);
			} finally {
				if(out != null) try { out.close(); } catch(Exception ignore) {}
			}
		} finally {
			if( out != null ) {
		        try {
		        	out.flush();
		        	out.close();
		        } catch (Exception fex) {
		        }
			}
	    }

	}

	public void closeWorkbook() throws IOException{

		if( this.workbook != null ) {
			this.workbook.close();
		}
	}



	public ServletOutputStream getOuputStream(HttpServletResponse response) throws IOException{
		out = response.getOutputStream();
		return out;
	}


	public void excelDownload(){

	}


}
