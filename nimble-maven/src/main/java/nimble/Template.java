package nimble;

import java.io.File;

import com.relevantcodes.extentreports.ExtentReports;

public class Template {
	public static  ExtentReports createReporter(ExtentReports reports, String reportLocation,String sprintName) {
		reports = new ExtentReports(reportLocation+"/Nimble_"+sprintName+"_TestResult.html");
		reports.addSystemInfo("Host Name", "Nimble");
		reports.addSystemInfo("Environment", "Automation Testing Environment");
		reports.addSystemInfo("Tester Name", "Raja C");
		reports.addSystemInfo("Browser", "Firefox 46.0.1");
		reports.loadConfig(new File("./resources/ExtentConfig.xml"));
		return reports; 
	}
}