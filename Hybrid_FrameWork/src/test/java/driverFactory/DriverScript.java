package driverFactory;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import commonFunctions.FunctionLibrary;
import utilities.ExcelFileUtil;

public class DriverScript {

	// global variables declaration
	WebDriver driver;
	String inputpath = "./FileInput/DataEngine.xlsx"; // provide the path of DataEngine file
	String outputpath = "./FileOutput/HybridResults.xlsx"; // provide the path of result file

	// used for extent report
	ExtentReports report;
	ExtentTest logger;
	String TCSheet = "MasterTestcases"; // store Master sheet into one variables

	@Test
	public void startTest() throws Throwable {

		// local variables
		String Module_status = "";
		String Module_new = "";

		// create object for execel file util class
		ExcelFileUtil xl = new ExcelFileUtil(inputpath);

		// iterate all test cases in TCSheet (Master Sheet)
		for (int i = 1; i <= xl.rowcount(TCSheet); i++) {

			if (xl.getCellData(TCSheet, i, 2).equalsIgnoreCase("Y")) // execute the testcase which are flag to "Y"
			{
				// store each sheet into one variables
				String TCModule = xl.getCellData(TCSheet, i, 1);

				// define the path of html
				report = new ExtentReports(
						"./target/ExtentReports/" + TCModule + FunctionLibrary.generateDate() + ".html");
				logger = report.startTest(TCModule); // Precondition for extent report

				// iterate all rows in every sheet
				for (int j = 1; j <= xl.rowcount(TCModule); j++) {

					// read each cell from TCModule
					String Description = xl.getCellData(TCModule, j, 0);
					String ObjectType = xl.getCellData(TCModule, j, 1);
					String Ltype = xl.getCellData(TCModule, j, 2);
					String Lvalue = xl.getCellData(TCModule, j, 3);
					String TestData = xl.getCellData(TCModule, j, 4);
					try {

						if (ObjectType.equalsIgnoreCase("startBrowser")) {
							driver = FunctionLibrary.startBrowser();
							logger.log(LogStatus.INFO, Description);
						}
						if (ObjectType.equalsIgnoreCase("openUrl")) {
							FunctionLibrary.openUrl();
							logger.log(LogStatus.INFO, Description);
						}
						if (ObjectType.equalsIgnoreCase("waitForElement")) {
							FunctionLibrary.waitForElement(Ltype, Lvalue, TestData);
							logger.log(LogStatus.INFO, Description);
						}
						if (ObjectType.equalsIgnoreCase("typeAction")) {
							FunctionLibrary.typeAction(Ltype, Lvalue, TestData);
							logger.log(LogStatus.INFO, Description);
						}
						if (ObjectType.equalsIgnoreCase("clickAction")) {
							FunctionLibrary.clickAction(Ltype, Lvalue);
							logger.log(LogStatus.INFO, Description);
						}
						if (ObjectType.equalsIgnoreCase("validateTitle")) {
							FunctionLibrary.validateTitle(TestData);
							logger.log(LogStatus.INFO, Description);
						}
						if (ObjectType.equalsIgnoreCase("closeBrowser")) {
							FunctionLibrary.closeBrowser();
							logger.log(LogStatus.INFO, Description);
						}

						if (ObjectType.equalsIgnoreCase("dropDownAction")) {

							FunctionLibrary.dropDownAction(Ltype, Lvalue, TestData);
							logger.log(LogStatus.INFO, Description);
						}

						if (ObjectType.equalsIgnoreCase("captureStock")) {
							FunctionLibrary.captureStock(Ltype, Lvalue);
							logger.log(LogStatus.INFO, Description);

						}

						if (ObjectType.equalsIgnoreCase("stockTable")) {
							FunctionLibrary.stockTable();
							logger.log(LogStatus.INFO, Description);
						}

						if (ObjectType.equalsIgnoreCase("captureSup")) {
							FunctionLibrary.captureSup(Ltype, Lvalue);
							logger.log(LogStatus.INFO, Description);
						}

						if (ObjectType.equalsIgnoreCase("supplierTable")) {
							FunctionLibrary.supplierTable();
							logger.log(LogStatus.INFO, Description);
						}

						if(ObjectType.equalsIgnoreCase("captureCus")) {
							FunctionLibrary.captureCus(Ltype, Lvalue);
							logger.log(LogStatus.INFO, Description);
						}
						
						if(ObjectType.equalsIgnoreCase("customerTable")) {
							FunctionLibrary.customerTable();
							logger.log(LogStatus.INFO, Description);
						}
					
						
						// write as pass into status cell in TCModule Sheet
						xl.setCellData(TCModule, j, 5, "Pass", outputpath);
						Module_status = "True";
						logger.log(LogStatus.PASS, Description);

					} catch (Throwable t) {

						// to catch all the error we are getting
						System.out.println(t.getMessage());

						// write as fail into status cell in TCModule Sheet
						xl.setCellData(TCModule, j, 5, "Fail", outputpath);
						logger.log(LogStatus.FAIL, Description);
						Module_new = "False";

						// take screen shot
						File screen = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
						FileUtils.copyFile(screen, new File(
								"./target/Screenshot/" + Description + FunctionLibrary.generateDate() + ".png"));
					}
					if (Module_status.equalsIgnoreCase("True")) {
						xl.setCellData(TCSheet, i, 3, "Pass", outputpath);
					}
					if (Module_new.equalsIgnoreCase("False")) {
						xl.setCellData(TCSheet, i, 3, "Fail", outputpath);
					}
					// Post condition for extent report
					report.endTest(logger);
					report.flush();

				}

			} else {
				// write as blocked for testcase flag to N
				xl.setCellData(TCSheet, i, 3, "Blocked", outputpath);

			}
		}

	}

}
