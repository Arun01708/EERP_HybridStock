package utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelFileUtil {

	XSSFWorkbook wb; // global variable

	// constructor for reading excel path
	public ExcelFileUtil(String Excelpath) throws Throwable {
		FileInputStream fi = new FileInputStream(Excelpath);
		wb = new XSSFWorkbook(fi);

	}

	//1st - methods for counting no of rows in sheet
	public int rowcount(String sheetName) {
		return wb.getSheet(sheetName).getLastRowNum();
	}

	// 2nd - methods for reading cell data

	public String getCellData(String sheetname, int row, int column) {
		String data; // local variables ( declare inside the methods)
		if (wb.getSheet(sheetname).getRow(row).getCell(column).getCellType() == CellType.NUMERIC) {
			int celldata = (int) wb.getSheet(sheetname).getRow(row).getCell(column).getNumericCellValue();

			data = String.valueOf(celldata);
		} else {
			data = wb.getSheet(sheetname).getRow(row).getCell(column).getStringCellValue();
		}
		return data;
	}

	// 3rd - method for writing results into new workbook
	public void setCellData(String sheetname, int row, int column, String Status, String WriteExcel) throws Throwable
	{
		// get sheet from workbook
		XSSFSheet ws = wb.getSheet(sheetname);

		// get row from sheet
		XSSFRow rowNum = ws.getRow(row);

		// create cell in a row
		XSSFCell cell = rowNum.createCell(column);

		// write status into cell
		cell.setCellValue(Status);

		// Formatting
		if (Status.equalsIgnoreCase("Pass")) 
		{
			// create an object
			XSSFCellStyle style = wb.createCellStyle();
			XSSFFont font = wb.createFont();
			
			// colour the text with green
			font.setColor(IndexedColors.GREEN.getIndex());
			
			// bold the text
			font.setBold(true);
			
			style.setFont(font);
			rowNum.getCell(column).setCellStyle(style);
			
			}
		else if(Status.equalsIgnoreCase("Fail")) {

			XSSFCellStyle style = wb.createCellStyle();
			XSSFFont font = wb.createFont();
			
			// colour the text with red
			font.setColor(IndexedColors.RED.getIndex());
			
			// bold the text
			font.setBold(true);
			
			style.setFont(font);
			rowNum.getCell(column).setCellStyle(style);
		}

	
	else if (Status.equalsIgnoreCase("Blocked")) {
		

		XSSFCellStyle style = wb.createCellStyle();
		XSSFFont font = wb.createFont();
		
		// colour the text with blue
		font.setColor(IndexedColors.BLUE.getIndex());
		
		// bold the text
		font.setBold(true);
		
		style.setFont(font);
		rowNum.getCell(column).setCellStyle(style);
	}
		
		FileOutputStream fo = new FileOutputStream(WriteExcel);
		wb.write(fo);
}
}
