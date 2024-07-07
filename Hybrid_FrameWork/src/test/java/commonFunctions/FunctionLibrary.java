package commonFunctions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

import com.relevantcodes.extentreports.ReporterType;

public class FunctionLibrary {

	// global variable
	public static WebDriver driver;
	public static Properties conpro; // to access key value from properties file

	// 1st - Method for Launching Browser
	public static WebDriver startBrowser() throws Throwable {

		// initiate object for Properties file
		conpro = new Properties();
		// load property file here
		conpro.load(new FileInputStream("./PropertyFiles/Enviromnet.properties"));

		// for launching chrome browser [execute only when key matching with given value
		if (conpro.getProperty("Browser").equalsIgnoreCase("chrome")) {
			driver = new ChromeDriver();
			driver.manage().window().maximize();

		} else if (conpro.getProperty("Browser").equalsIgnoreCase("firefox")) {
			driver = new FirefoxDriver();
		} else {

			Reporter.log("Browser value is not matching ", true);

		}
		return driver;
	}

	// 2nd - method for launching url

	public static void openUrl() {
		driver.get(conpro.getProperty("Url"));
	}

	// 3rd - method to wait for any webelement in a page
	public static void waitForElement(String LocatorType, String LocatorValue, String TestData) {

		WebDriverWait mywait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(TestData)));

		if (LocatorType.equalsIgnoreCase("id")) {
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.id(LocatorValue)));
		}

		if (LocatorType.equalsIgnoreCase("name")) {
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.name(LocatorValue)));

			if (LocatorType.equalsIgnoreCase("xpath")) {
				mywait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(LocatorValue)));
			}
		}
	}

	// 4th - method for any textbox
	public static void typeAction(String LocatorType, String LocatoreValue, String testData) {

		if (LocatorType.equalsIgnoreCase("id")) {

			driver.findElement(By.id(LocatoreValue)).clear(); // clear textbox
			driver.findElement(By.id(LocatoreValue)).sendKeys(testData);
		}

		if (LocatorType.equalsIgnoreCase("xpath")) {

			driver.findElement(By.xpath(LocatoreValue)).clear(); // clear textbox
			driver.findElement(By.xpath(LocatoreValue)).sendKeys(testData);
		}

		if (LocatorType.equalsIgnoreCase("name")) {

			driver.findElement(By.name(LocatoreValue)).clear(); // clear textbox
			driver.findElement(By.name(LocatoreValue)).sendKeys(testData);
		}

	}

	// 5th - method for any buttons,checkboxes,radiobuttons,links and images
	public static void clickAction(String LocatorType, String LocatorValue) {

		if (LocatorType.equalsIgnoreCase("xpath")) {
			driver.findElement(By.xpath(LocatorValue)).click();
		}
		if (LocatorType.equalsIgnoreCase("name")) {
			driver.findElement(By.name(LocatorValue)).click();
		}
		if (LocatorType.equalsIgnoreCase("id")) {
			driver.findElement(By.id(LocatorValue)).sendKeys(Keys.ENTER);
		}
	}

	// 6th- method for validae Title
	public static void validateTitle(String Expected_Title) {

		String Actual_Title = driver.getTitle(); // run time title

		try {
			Assert.assertEquals(Actual_Title, Expected_Title, "Title is not matching");
		} catch (AssertionError a) {
			System.out.println(a.getMessage());
		}
	}

	// 7th-method for closing
	public static void closeBrowser() {
		driver.quit();
	}

	// 8th - method for generate date format
	public static String generateDate() {

		Date date = new Date();
		DateFormat df = new SimpleDateFormat("YYYY_MM_DD hh_mm_ss");
		return df.format(date);
	}

	// 9th - method for listboxes
	public static void dropDownAction(String LocatorType, String LocatorValue, String testData) {
		if (LocatorType.equalsIgnoreCase("id")) {
			// convert testdata string type into integer
			int value = Integer.parseInt(testData);
			Select element = new Select(driver.findElement(By.id(LocatorValue)));
			element.selectByIndex(value);
		}
		if (LocatorType.equalsIgnoreCase("name")) {
			// convert testdata string type into integer
			int value = Integer.parseInt(testData);
			Select element = new Select(driver.findElement(By.name(LocatorValue)));
			element.selectByIndex(value);
		}
		if (LocatorType.equalsIgnoreCase("xpath")) {
			// convert testdata string type into integer
			int value = Integer.parseInt(testData);
			Select element = new Select(driver.findElement(By.xpath(LocatorValue)));
			element.selectByIndex(value);
		}
	}

	// 10th - method for capturing stock Number into Notepad
	public static void captureStock(String LocatorType, String LocatorValue) throws Throwable {
		String StockNum = "";
		if (LocatorType.equalsIgnoreCase("name")) {
			StockNum = driver.findElement(By.name(LocatorValue)).getAttribute("value");

		}
		if (LocatorType.equalsIgnoreCase("id")) {
			StockNum = driver.findElement(By.id(LocatorValue)).getAttribute("value");

		}
		if (LocatorType.equalsIgnoreCase("xpath")) {
			StockNum = driver.findElement(By.xpath(LocatorValue)).getAttribute("value");

		}
		// create notepad
		FileWriter fw = new FileWriter("./CaptureData/stockNumber.txt");
		// allocate memory for file
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(StockNum);
		bw.flush();
		bw.close();
	}

	// 11th - method to validate stock Number in table
	public static void stockTable() throws Throwable {

		// read stock number from notepad
		FileReader fr = new FileReader("./CaptureData/stockNumber.txt");
		BufferedReader br = new BufferedReader(fr); // allocate memory
		String Exp_Data = br.readLine();
		// if search textbox already displayed don't click Search panel button
		if (!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())
			// if search textbox not displayed click search p[anel button
			driver.findElement(By.xpath(conpro.getProperty("search-panel"))).click();
		// clear text in search textbox
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
		// enter sctock number into search textbox
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(Exp_Data);
		// click search button
		driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
		Thread.sleep(5000);

		String Act_Data = driver
				.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[8]/div/span/span")).getText();

		Reporter.log(Act_Data + "  " + Exp_Data, true);

		try {
			Assert.assertEquals(Act_Data, Exp_Data, "Stock number Should not Match");
		} catch (AssertionError a) {
			Reporter.log(a.getMessage(), true);

		}

	}

	// 12th - method for supplier number to capture into notepad

	public static void captureSup(String LocatorType, String LocatorValue) throws Throwable {

		String SupplierNum ="";
		if(LocatorType.equalsIgnoreCase("name"))
		{
			SupplierNum =driver.findElement(By.name(LocatorValue)).getAttribute("value");
			
		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			SupplierNum =driver.findElement(By.id(LocatorValue)).getAttribute("value");
			
		}
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			SupplierNum =driver.findElement(By.xpath(LocatorValue)).getAttribute("value");
			
		}
		//create notepad
		FileWriter fw = new FileWriter("./CaptureData/supplierNumber.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(SupplierNum);
		bw.flush();
		bw.close();
	}

	// 13th - method to verify supplier number in supplier table
	public static void supplierTable() throws Throwable {

		// read stock number from notepad
		FileReader fr = new FileReader("./CaptureData/supplierNumber.txt");
		BufferedReader br = new BufferedReader(fr);
		String Exp_Data = br.readLine();
		// if search textbox already displayed dont click search panel button
		if (!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())
			// if search textbox not displayed click search p[anel button
			driver.findElement(By.xpath(conpro.getProperty("search-panel"))).click();
		// clear text in search textbox
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
		// enter sctock number into search textbox
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(Exp_Data);
		driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
		Thread.sleep(3000);
		String Act_Data = driver
				.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[6]/div/span/span")).getText();
		Reporter.log(Act_Data + "        " + Exp_Data, true);
		try {
			Assert.assertEquals(Act_Data, Exp_Data, "Stock number Should not Match");
		} catch (AssertionError a) {
			Reporter.log(a.getMessage(), true);
		}

	}

	// 14th - method for capture customer number into notepad
	public static void captureCus(String LocatorType, String LocatorValue) throws Throwable {

		String CustomerNum = "";
		if (LocatorType.equalsIgnoreCase("name")) {
			CustomerNum = driver.findElement(By.name(LocatorValue)).getAttribute("value");

		}
		if (LocatorType.equalsIgnoreCase("id")) {
			CustomerNum = driver.findElement(By.id(LocatorValue)).getAttribute("value");

		}
		if (LocatorType.equalsIgnoreCase("xpath")) {
			CustomerNum = driver.findElement(By.xpath(LocatorValue)).getAttribute("value");

		}
		// create notepad
		FileWriter fw = new FileWriter("./CaptureData/CustomerNumber.txt");
		// allocate memory for file
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(CustomerNum);
		bw.flush();
		bw.close();

	}

	// 15th - method to verify customer number in customer table
	public static void customerTable() throws Throwable {
		// read stock number from notepad
		FileReader fr = new FileReader("./CaptureData/CustomerNumber.txt");
		BufferedReader br = new BufferedReader(fr); // allocate memory
		String Exp_Data = br.readLine();
		// if search textbox already displayed don't click Search panel button
		if (!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())
			// if search textbox not displayed click search p[anel button
			driver.findElement(By.xpath(conpro.getProperty("search-panel"))).click();
		// clear text in search textbox
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
		// enter sctock number into search textbox
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(Exp_Data);
		// click search button
		driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
		Thread.sleep(5000);

		String Act_Data = driver
				.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[5]/div/span/span")).getText();

		Reporter.log(Act_Data + "  " + Exp_Data, true);

		try {
			Assert.assertEquals(Act_Data, Exp_Data, "Customer number Should not Match");
		} catch (AssertionError a) {
			Reporter.log(a.getMessage(), true);

		}

	}

}
