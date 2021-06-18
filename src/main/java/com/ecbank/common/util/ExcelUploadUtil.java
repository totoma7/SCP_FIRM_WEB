package com.ecbank.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.HtmlUtils;

@Component
public class ExcelUploadUtil {

	/** Buffer size */
	public static int BUFFER_SIZE = 8192;

	public static String SEPERATOR = File.separator;

	public static String FILE_BASE_PATH = "";

	protected static Log logger = LogFactory.getLog(ExcelUploadUtil.class);

	public static List<Map<String,Object>> readExcelFile(HttpServletRequest request) throws Exception{
		return readExcelFile(request, 1, null);
	}

	public static List<Map<String,Object>> readLargeExcelFile(HttpServletRequest request,String[] colNames) throws Exception{
		return readExcelFile(request, 1, colNames);
	}

	@Value("${file.upload.temp.path}")
	public void setFileBasePath(String path) {
	    FILE_BASE_PATH = path;
	}


	@SuppressWarnings("resource")
	public static List<Map<String,Object>> readExcelFile(HttpServletRequest request, int headerCnt, String[] colNames) throws Exception{
		List<Map<String,Object>> responseData = new ArrayList<Map<String,Object>>();
		String strFileUploadPath = FILE_BASE_PATH;
		String strServerSubPath = "EXCEL_TEMP";
		String strUPloadDirPath = WebUtil.filePathBlackList(strFileUploadPath + SEPERATOR + strServerSubPath);

		// 디레토리가 없다면 생성
		File dir = new File(strUPloadDirPath);
		if (!dir.isDirectory()) {
			dir.mkdirs();
		}

		MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest) request;

		Iterator<?> fileIter = mptRequest.getFileNames();

		String strFileLoc = (String) fileIter.next();
		MultipartFile mFile = mptRequest.getFile(strFileLoc);
		String tmp = mFile.getOriginalFilename();

		if (tmp.lastIndexOf("\\") >= 0) {
			tmp = tmp.substring(tmp.lastIndexOf("\\") + 1);
		}

		if ("".equals(tmp)) { // 파일명이 없다면
			throw new Exception("파일이 없습니다.") ;
		}

		String strOrgFileName = HtmlUtils.htmlEscape(tmp); // fileName Xss 처리

		int pos = strOrgFileName.lastIndexOf( "." );
		String ext = strOrgFileName.substring( pos + 1 );

		if (mFile.getSize() > 0) {
			// 설정한 path에 파일저장
			File serverFile = new File(WebUtil.filePathBlackList(strUPloadDirPath + SEPERATOR + strOrgFileName));
			mFile.transferTo(serverFile);
		}

		int colSize = -1;
		if( colNames != null ) {
			colSize = colNames.length;
		}

		File tmpFile = new File(WebUtil.filePathBlackList(strUPloadDirPath + SEPERATOR + strOrgFileName));

		FileInputStream fis=new FileInputStream(tmpFile);

		if ("xlsx".equals(ext)) {
			XSSFWorkbook workbook=new XSSFWorkbook(fis);
			int rowindex=0;
			int columnindex=0;
			XSSFSheet sheet=workbook.getSheetAt(0);
			int rows=sheet.getPhysicalNumberOfRows();

			// header에 빈 라인 이 있는지 check
			int headerEmptyRowCnt = 0;
			for( int i=0; i < headerCnt; i++ ) {
				XSSFRow row=sheet.getRow(i);
				if( row == null) {
					headerEmptyRowCnt++;
				}
			}
			rows = rows + headerEmptyRowCnt;


			for(rowindex=headerCnt;rowindex<rows;rowindex++){
				Map<String,Object> mapRow = new HashMap<>();
				//행을읽는다
			    XSSFRow row=sheet.getRow(rowindex);
			    if(row !=null){
			        //셀의 수
			        int cells=row.getPhysicalNumberOfCells();
			        for(columnindex=0;columnindex<=cells;columnindex++){
			        	//셀값을 읽는다
			            XSSFCell cell=row.getCell(columnindex);
			            String value="";
			            //셀이 빈값일경우를 위한 널체크
			            if(cell==null){
			                continue;
			            }else{
			            	CellType type=cell.getCellTypeEnum();

			            	if (type == CellType.NUMERIC) {

			            		if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {

			            			Date date = cell.getDateCellValue();
			            			value=new SimpleDateFormat("yyyyMMdd").format(date);
			            		} else {
			            			//double tempDouble = cell.getNumericCellValue();
			            			//logger.debug( " double value = " + Double.toString(tempDouble));
			            			//logger.debug( " double value 2= " + Double.valueOf(tempDouble));
			            			DataFormatter dataFormatter = new DataFormatter();
			            			String tempVal =dataFormatter.formatCellValue(cell);
			            			//logger.debug( "getNumericCellValue =" + cell.getNumericCellValue());
			            			//logger.debug( "tempVal =" + tempVal);
			            			if( tempVal != null) {
			            				tempVal.replace(",",  "");
			            			}
			            			//value=cell.getNumericCellValue()+"";
			            			value = tempVal;

			            		}
			            	} else if (type == CellType.STRING) {
			            		value=cell.getStringCellValue()+"";
			            	} else {
			            		value=cell.getStringCellValue()+"";
			            	}
			            }

			            if( value != null) {
				            value = value.replaceAll("　", " ");

				            value = value.trim();
			            }
			            logger.debug( "============ value =" + value);
			            if( colNames != null) {
			            	if( columnindex < colSize) {
			            		mapRow.put(colNames[columnindex], value);
			            	} else {
			            		mapRow.put("CELL_"+columnindex, value);
			            	}
			            } else {
			            	mapRow.put("CELL_"+columnindex, value);
			            }
			        }
			    }
			    responseData.add(mapRow);
			}

		} else if ("xls".equals(ext)) {
			HSSFWorkbook workbook=new HSSFWorkbook(fis);
			int rowindex=0;
			int columnindex=0;
			HSSFSheet sheet=workbook.getSheetAt(0);
			int rows=sheet.getPhysicalNumberOfRows();

			// header에 빈 라인 이 있는지 check
			int headerEmptyRowCnt = 0;
			for( int i=0; i < headerCnt; i++ ) {
				HSSFRow row=sheet.getRow(i);
				if( row == null) {
					headerEmptyRowCnt++;
				}
			}
			rows = rows + headerEmptyRowCnt;

			for(rowindex=headerCnt;rowindex<rows;rowindex++){
				Map<String,Object> mapRow = new HashMap<>();
				//행을 읽는다
			    HSSFRow row=sheet.getRow(rowindex);

			    //logger.info("rowIdx =" + rowindex);
			    if(row !=null){
			        //셀의 수
			        int cells=row.getPhysicalNumberOfCells();
			        for(columnindex=0;columnindex<=cells;columnindex++){
			            //셀값을 읽는다
			            HSSFCell cell=row.getCell(columnindex);
			            String value="";
			            //셀이 빈값일경우를 위한 널체크
			            if(cell==null){
			                continue;
			            }else{
			            	CellType type=cell.getCellTypeEnum();

			            	if (type == CellType.NUMERIC) {
			            		if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
			            			Date date = cell.getDateCellValue();
			            			value=new SimpleDateFormat("yyyyMMdd").format(date);
			            		} else {
			            			//value=(int)cell.getNumericCellValue()+"";
			            			DataFormatter dataFormatter = new DataFormatter();
			            			String tempVal =dataFormatter.formatCellValue(cell);

			            			if( tempVal != null) {
			            				tempVal.replace(",",  "");
			            			}

			            			value = tempVal;
			            		}
			            	} else if (type == CellType.STRING) {
			            		value=cell.getStringCellValue()+"";
			            	} else {
			            		value=cell.getStringCellValue()+"";
			            	}
			            }

			            if( value != null) {
				            value = value.replaceAll("　", " ");
				            value = value.trim();
			            }

			            if( colNames != null) {
			            	//logger.debug("colSize = " + colSize + ", columnindex = " + columnindex);
			            	if( columnindex < colSize) {
			            		mapRow.put(colNames[columnindex], value);
			            	} else {
			            		mapRow.put("CELL_"+columnindex, value);
			            	}
			            } else {
			            	mapRow.put("CELL_"+columnindex, value);
			            }
			        }
			    }
			    responseData.add(mapRow);
			}
		} else {
			responseData.add(null);
		}

		// 파일 삭제
		if(tmpFile.delete()){

		} else { // 파일 삭제 실패

		}

		return responseData;
	}
}
