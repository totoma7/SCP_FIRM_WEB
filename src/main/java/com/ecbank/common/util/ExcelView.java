package com.ecbank.common.util;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.servlet.view.AbstractView;

public class ExcelView extends AbstractView {

	/** The content type for an Excel response */
	private static final String CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	/** The extension to look for existing templates */
	private static final String EXTENSION = ".xlsx";
	private String url;

	/**	 * Default Constructor.	 * Sets the content type of the view to "application/vnd.ms-excel".	 */
	public ExcelView() {
		setContentType(CONTENT_TYPE);
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	protected boolean generatesDownloadContent() {
		return true;
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest req, HttpServletResponse res) throws Exception {

		Workbook workbook;

		ByteArrayOutputStream baos = createTemporaryOutputStream();

		workbook = new XSSFWorkbook();

		buildExcelDocument(model, workbook, req, res);

		workbook.write(baos);

		writeToResponse(res, baos);

	}


	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// #1 엑셀 출력용 데이터
        String _excelName = (String)model.get("FILE_NAME");
        @SuppressWarnings("unchecked")
        List<Map<String,Object>> _sheets = (List<Map<String,Object>>) model.get("SHEETS"); //SHEET.HEADER(String[][]), SHEET.LIST(List<Map>)


        // #1.1 파일 설정
        String _fileName = createFileName(_excelName.concat("_").concat(DateUtil.getToday()));
        setFileNameToResponse(request, response, _fileName);


        // #2 Cell Style & Font
        Font fntTitle = workbook.createFont();
        fntTitle.setBold(true);
        fntTitle.setFontHeightInPoints((short) 36);
        fntTitle.setColor(HSSFColor.HSSFColorPredefined.LIGHT_ORANGE.getIndex());

        Font fntHeader = workbook.createFont();
        fntHeader.setBold(true);
        fntHeader.setFontHeightInPoints((short) 14);
        fntHeader.setColor(HSSFColor.HSSFColorPredefined.BROWN.getIndex());

        Font fntValue = workbook.createFont();
        fntValue.setFontHeightInPoints((short) 10);
        fntValue.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());

        CellStyle csTitle = workbook.createCellStyle();
        csTitle.setFillBackgroundColor(HSSFColor.HSSFColorPredefined.LIGHT_BLUE.getIndex());
        csTitle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.BROWN.getIndex());
        csTitle.setFont(fntTitle);
        csTitle.setAlignment(HorizontalAlignment.CENTER);

        CellStyle csHeader = workbook.createCellStyle();
        csHeader.setFont(fntHeader);
        csHeader.setAlignment(HorizontalAlignment.CENTER);

        CellStyle csDate = workbook.createCellStyle();
        csDate.setFont(fntValue);
        csDate.setAlignment(HorizontalAlignment.CENTER);
        csDate.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));

        CellStyle csDateLeft = workbook.createCellStyle();
        csDateLeft.setFont(fntValue);
        csDateLeft.setAlignment(HorizontalAlignment.LEFT);
        csDateLeft.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));

        CellStyle csAmt = workbook.createCellStyle();
        csAmt.setFont(fntValue);
        csAmt.setAlignment(HorizontalAlignment.RIGHT);
        csAmt.setDataFormat((short) 4);

        CellStyle csAmtLeft = workbook.createCellStyle();
        csAmtLeft.setFont(fntValue);
        csAmtLeft.setAlignment(HorizontalAlignment.LEFT);
        csAmtLeft.setDataFormat((short) 4);

        CellStyle csQty = workbook.createCellStyle();
        csQty.setFont(fntValue);
        csQty.setAlignment(HorizontalAlignment.RIGHT);
        csQty.setDataFormat((short) 3);

        CellStyle csTxtLeft = workbook.createCellStyle();
        csTxtLeft.setFont(fntValue);
        csTxtLeft.setAlignment(HorizontalAlignment.LEFT);

        CellStyle csTxtRight = workbook.createCellStyle();
        csTxtRight.setFont(fntValue);
        csTxtRight.setAlignment(HorizontalAlignment.RIGHT);

        CellStyle csTxtCenter = workbook.createCellStyle();
        csTxtCenter.setFont(fntValue);
        csTxtCenter.setAlignment(HorizontalAlignment.CENTER);

        // #3 엑셀처리 POI 개체 생성
    	for (Map<String, Object> _sheet:_sheets) {
    		String _docTitle = (String)_sheet.get("DOC_TITLE");
            String [][] _header = (String[][])_sheet.get("HEADER");

    		@SuppressWarnings("unchecked")
			List<Map<String,Object>> _list 	= (List<Map<String,Object>>) _sheet.get("LIST"); //SHEET.HEADER(String[][]), SHEET.LIST(List<Map>)

    		Sheet sheet1 = workbook.createSheet(_docTitle+" (" + _list.size() + "건)");
	        sheet1.setFitToPage(true);

	        Row xlsRow1;
	        Cell cell1 = null;
	        int _lastHdRow = _header.length-1;

	        // #4 타이틀 생성

	        int row1 = 0;
	        int column1 = 0;


	        xlsRow1 = sheet1.createRow(row1++);
	        setCellInfo(xlsRow1.createCell(column1++), csTitle, _docTitle);
	        sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, _header[_lastHdRow].length-1));

	        // #5 타이틀 생성
	        for (String [] headLine:_header) {
	        	xlsRow1 = sheet1.createRow(row1++);
		        column1 = 0;
		        for (String _headCol : headLine) {
		        	String [] _title = _headCol.split("\\|");
		        	String title = _title[0];
		        	setCellInfo(xlsRow1.createCell(column1), csHeader, title);

		        	if (_title.length > 1) {
		        		int _firstRow 	= row1-1;
			        	int _lastRow 	= row1-1;
			        	int _firstCol 	= column1;
			        	int _lastCol	= column1 + Integer.parseInt(_title[1]) - 1;

			        	sheet1.addMergedRegion(new CellRangeAddress(_firstRow, _lastRow, _firstCol, _lastCol));
		        		column1 += Integer.parseInt(_title[1]);
		    	    } else {
		    	    	column1++;
		    	    }
		        }
		        if (row1 ==_header.length) break;
	        }


	        for (Map<String,Object> pr : _list) {
	            Row r = sheet1.createRow(row1++);
	            column1 = 0;
	            for (String _oneHeader : _header[_lastHdRow]) {
	            	String [] _column = _oneHeader.split("\\|");

	            	String columnName = "";
	            	columnName = _column[0];
	                cell1 = r.createCell(column1++);
	                if (_column.length > 1) {
	            		if ("LEFT".equals(_column[1])) {
	            			setCellInfo(cell1, csTxtLeft, pr.get(columnName));
	            		} else if ("RIGHT".equals(_column[1])) {
	            			setCellInfo(cell1, csTxtRight, pr.get(columnName));
	            		} else if ("CENTER".equals(_column[1])) {
	            			setCellInfo(cell1, csTxtCenter, pr.get(columnName));
	            		} else {
	            			setCellInfo(cell1, csTxtLeft, pr.get(columnName));
	            		}
	            	} else {
	            		if (columnName.endsWith("AMT") || columnName.endsWith("PRICE")) {
		                    setCellInfo(cell1, csAmt,     pr.get(columnName), CellType.NUMERIC);
		                } else if (columnName.endsWith("QTY") || columnName.endsWith("CNT") || "SEQ".equalsIgnoreCase(columnName)) {
		                    setCellInfo(cell1, csQty,     pr.get(columnName), CellType.NUMERIC);
		                } else {
		                	setCellInfo(cell1, csTxtLeft, pr.get(columnName));
		                }
	            	}
	            }
	        }
	        for(int i = 0; i < _header[_lastHdRow].length; i++) {
	            sheet1.autoSizeColumn(i);
	            sheet1.setColumnWidth(i, (int)Math.round(sheet1.getColumnWidth(i) * 1.5));
	        }
    	}

	}


    private void setFileNameToResponse(HttpServletRequest request, HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
        response.setCharacterEncoding("UTF-8");
        String userAgent = request.getHeader("User-Agent");
        if (userAgent.indexOf("MSIE 5.5") >= 0) {
            response.setContentType(CONTENT_TYPE);
            response.setHeader("Content-Disposition","filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");
        } else {
            response.setHeader("Content-Disposition","attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");
        }
    }

    private String createFileName(String key) {
        return new StringBuilder(key).append(EXTENSION).toString();
    }

    private void setCellInfo(Cell cell, CellStyle cellStyle, Object value) {
        setCellInfo(cell, cellStyle, value, CellType.STRING);
    }

    private void setCellInfo(Cell cell, CellStyle cellStyle, Object value, CellType cellType) {
        cell.setCellStyle(cellStyle);

        if(value == null) cell.setCellType(cellType);
        else {
            cell.setCellType(cellType);
            if(cellType == CellType.NUMERIC) {
                cell.setCellValue(Double.parseDouble(String.valueOf(value)));
            } else {
                cell.setCellValue(String.valueOf(value));
            }
        }
    }
}
