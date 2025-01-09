package com.ebay.driver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.ebay.pages.EBayBookScenarioPage;
import com.ebay.utils.ExcelDataManipulator;

public class Driver {

	public static Properties prop;
	public static WebDriver driver;
	public static String env;
	public static String url;
	public static String browserType;
	public static String isHeadless;
	public static long implicitWaitInSeconds;
	public static long pageLoadWaitInSeconds;
	public static ExtentSparkReporter sparkReport;
	public static ExtentReports extentReports;
	public static ExtentTest extentTest;
	public static Logger log;
	public static String testClassName;
	public static EBayBookScenarioPage eBayBookScenarioPage;

	// to load all the environment or execution variables. Reading values from
	// Config property file
	public void initializeProperties() {
		try {
			FileInputStream fip = new FileInputStream(
					"C:\\Users\\shama\\Documents\\Shamanth\\Professional\\Selenium Projects\\Ebay\\src\\test\\resources\\config.properties");
			prop = new Properties();
			prop.load(fip);
			env = prop.getProperty("env");
			browserType = prop.getProperty("browserType");
			isHeadless = prop.getProperty("isHeadless");
			try {
				implicitWaitInSeconds = Long.parseLong(prop.getProperty("implicitWaitInSeconds").trim());
				pageLoadWaitInSeconds = Long.parseLong(prop.getProperty("pageLoadWaitInSeconds").trim());
			} catch (NumberFormatException e) {
				log.info("NumberFormatException Handled; default values passed");
				implicitWaitInSeconds = 30;
				pageLoadWaitInSeconds = 60;
			}

			log.info("implicitWaitInSeconds: " + implicitWaitInSeconds);
			log.info("pageLoadWaitInSeconds: " + pageLoadWaitInSeconds);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			log.fatal("Parsing error. Failure while converting String values to long");
			e.printStackTrace();
		}
	}

	// Browser initiation based on user input
	public void launchBrowser() {
		try {
			switch (browserType.toLowerCase()) {
			case "chrome":
				driver = new ChromeDriver();
				break;

			case "firefox":
				driver = new FirefoxDriver();
				break;

			case "edge":
				driver = new EdgeDriver();
				break;

			default:
				System.out.println("Incorrect browser type value keyed in config properties file");
				break;
			}
			log.info("Selected Browser Type is: " + browserType);
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWaitInSeconds));
			driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadWaitInSeconds));
			driver.manage().window().maximize();
			// driver.get(getURL());

		} catch (TimeoutException e) {
			log.error("Encountered Timeout error while launching the application initially; "
					+ "reopening webpage with increased page load timeout seconds");
			driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadWaitInSeconds * 2));
			// driver.get(getURL());
		}

		catch (Exception e) {
			e.printStackTrace();
			log.error("Failed to launch the web application");
		}
	}

	// Initializing the execution
	@BeforeSuite
	public void setUp() {
		log = LogManager.getLogger("Assessment");
		log.info("Execution Started");
		initializeProperties();
		ExcelDataManipulator.storeDataToHashMapFromExcel();
		generateExtentReport();
	}

	public void generateExtentReport() {
		try {
			Date currentTS = new Date();

			sparkReport = new ExtentSparkReporter(
					"./Reports/Automation Execution Report" + currentTS.getTime() + ".html");
			extentReports = new ExtentReports();
			extentReports.attachReporter(sparkReport);
			sparkReport.config().setDocumentTitle("Ebay Automation Report");
			sparkReport.config().setEncoding("utf-8");
			sparkReport.config().setReportName(env + " environment Report");
		} catch (Exception e) {
			log.error("Failed to generate the report");
			e.printStackTrace();
		}
	}

	@AfterSuite
	public void tearDown() {
		extentReports.flush();
		log.info("Execution Complete");
	}

}
