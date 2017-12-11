package nimble;

import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class Reports
{
	public static WebDriver d;
	public static Properties elements;
	public static ExtentTest test;
	
	public static String receiveddate;
	public static String duedate;
	
	public Reports()
	{
		
	}
	
	public Reports(Properties elements, ExtentTest test)
	{
		this.elements = elements;
		this.test = test;
		this.d = Worker.d;
	}
	
	
	
	public static boolean articlehistory(String tabname, String jid, String aid, String stage) throws Exception
	{
		Worker worker = new Worker(elements, test);
		String info[] = ElementLoader.getTabDetail(tabname);
		boolean flag = false;
		if(Worker.go(tabname))
		{
			if(Worker.filter(tabname, "Journal ID", "Filters", jid)&&(Worker.filter(tabname, "Stage", "Filters", stage)))
			{
				if(Worker.verifytaskin(tabname, jid, aid, stage))
				{
					String receiveddatepath =  elements.getProperty(info[1]).trim()+"//table//tr[td[2]/div='"+jid.toUpperCase()+"' and td[3]/div='"+aid+"' and td[4]/div='"+stage+"']/td[6]/div";
					String duedatepath =  elements.getProperty(info[1]).trim()+"//table//tr[td[2]/div='"+jid.toUpperCase()+"' and td[3]/div='"+aid+"' and td[4]/div='"+stage+"']/td[7]/div";
					receiveddate = d.findElement(By.xpath(receiveddatepath)).getText();
					duedate = d.findElement(By.xpath(duedatepath)).getText();
					flag = true;
				}
			}
		}
			return flag;
	}
	
	
	public static boolean verifyreportdata(String stage, String value, String index) throws Exception
	{
		boolean flag = false;
		Worker worker = new Worker(elements, test);
		String element = "//tr[td[1]//span='"+stage+"' and td["+index+"]//span='"+value.split("=")[1].trim()+"']"; 
		if(value.equalsIgnoreCase("date"))
		{
			receiveddate = ElementLoader.returndate(receiveddate, "dd-MM-yyyy hh:mm:ss", "yyyy-MM-dd hh:mm:ss a");
			duedate = ElementLoader.returndate(duedate, "dd-MM-yyyy hh:mm:ss", "yyyy-MM-dd hh:mm:ss a");
			element = "//tr[td[1]//span='CU' and td[2]//span[text()='"+receiveddate+"'] and td[4]//span[text()='"+duedate+"']]";
		}
		
		if(Helper.waitTo(element, value, d))
		{		
			test.log(LogStatus.PASS, "<b>"+value.split("=")[1].trim()+"<span style='color:green'> Report - Verification : Pass</span></b>");
			flag = true;
		}
		else
		{
			test.log(LogStatus.INFO, test.addBase64ScreenShot(Helper.EncodeImage(d, Helper.imageLocation+"/"+index+".jpg")));
		}
			return flag;
	}
	
	
}
