package nimble;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;






import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class Worker {

	public static Properties elements;
	public static ExtentTest test;
	public static WebDriver d;
	public static Actions actions;
	
	public Worker() {		
	}
	
	public Worker(Properties elements, ExtentTest test) throws Exception
	{
		this.elements = elements;
		this.test = test;
	}
	
	public static void intializeActions()
	{
		actions = new Actions(d);
	}
	
	public static boolean openurl(String url, String element)
	{
		boolean flag = false;
		try
		{
			d = Configurator.browser("firefox");
			d.get(url);
			flag = seeelement(element);
		}
		catch(Exception e)
		{
			test.log(LogStatus.ERROR, e.getMessage());
		}
			return flag;
	}
	
	public static boolean refresh(String element)
	{
		boolean flag = false;
		try
		{
			d.navigate().to(d.getCurrentUrl());
			flag = Helper.waitFor(elements, element, d);
		}
		catch(Exception e)
		{
			test.log(LogStatus.ERROR, e.getMessage());
		}
			return flag;
	}
	
	public static boolean verifyinput(String element, String text)	{
		boolean flag = false;
		try	{
			if(Helper.waitTo(element, "Input With Content ["+text+"]", d))	{
				if(text.equalsIgnoreCase(ElementLoader.inputValue(element, d)))	{
					flag = true;
				}
			}
		}
		catch(Exception e)	{
			test.log(LogStatus.ERROR, e.getMessage());
		}
		return flag;
	}
	
	public static boolean focus(String action, String value)
	{
		int val = Integer.parseInt(value);
		boolean flag = false;
		try
		{
			wait("5");
			if(!action.equalsIgnoreCase("main"))
			{
				if(Helper.click(elements, action, d))
				{
					d.switchTo().window(d.getWindowHandles().toArray()[val].toString());
					flag = true;
				}
			}
			else
			{
				Thread.sleep(3000);
				d.switchTo().window(d.getWindowHandles().toArray()[val].toString());
				flag = true;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			test.log(LogStatus.ERROR, e.getMessage());
		}
		return flag;
	}
	
	
	public static boolean pageaction(String todo)
	{
		boolean flag = false;
		try
		{
			if(todo.equalsIgnoreCase("forward"))
			{
				d.navigate().forward();
				flag = true;
			}
			else if(todo.equalsIgnoreCase("backward"))
			{
				d.navigate().back();
				flag = true;
			}
			else if(todo.equalsIgnoreCase("close"))
			{
				d.close();
			}
		}
		catch(Exception e)
		{
			test.log(LogStatus.ERROR, e.getMessage());
		}
			return flag;
	}
	
	public static boolean navigateto(String url, String element)
	{
		boolean flag = false;
		try
		{
			d.navigate().to(url);
			flag = Helper.waitFor(elements, element, d);
		}
		catch(Exception e)
		{
			test.log(LogStatus.ERROR, e.getMessage());
		}
			return flag;
	}
	
	public static boolean typevalue(String element, String textToType)
	{
		boolean flag= false;
		try
		{
			flag = Helper.typeText(elements, element, textToType, d);
		}
		catch(Exception e)
		{
			test.log(LogStatus.ERROR, e.getMessage());
		}
			return flag;
	}
	
	public static boolean verifytext(String element, String expectedText) throws Exception
	{
		boolean flag = false;
		if(Helper.waitFor(elements, element, d))
		{
			String actualText = ElementLoader.getText(elements.getProperty(element.toUpperCase()).trim(), d);
			if(actualText.equals(expectedText))
			{
				flag = true;
				test.log(LogStatus.PASS, "<b>"+StringUtils.capitalize(element)+" :<span style='color:green'> Content Verified</span></b>");
			}
			else
			{
				test.log(LogStatus.WARNING, "<b>Expected: <span style='color:green'>"+expectedText+"</span></b>");
				test.log(LogStatus.WARNING, "<b>Actuals : <span style='color:red'>"+actualText+"</span></b>");
			}
		}
			return flag;
	}
	
	public static boolean login(String url, String username, String password) throws Exception
	{		
		boolean flag = false;
		d = Configurator.browser("firefox");
		try
		{
			d.get(url);
			if(Helper.typeText(elements, "USERNAME", username, d)&&(Helper.typeText(elements, "PASSWORD", password, d)))
			{
				if(Helper.click(elements, "LOGINSUBMIT", d))
				{
					flag = seeelement("LOGINPANEL");
				}
			}
		}
		catch(Exception e)
		{
			test.log(LogStatus.ERROR, e.getMessage());
		}
			return flag;
	}
	
	
	public static boolean wait(String InSeconds) throws NumberFormatException, InterruptedException
	{
		boolean flag = false;
		try
		{
			Thread.sleep(Integer.parseInt(InSeconds+"000"));
			flag = true;
		}
		catch(Exception e)
		{
			test.log(LogStatus.ERROR, e.getMessage());
		}
			return flag;
	}
	
	
	public static boolean go(String tabname)
	{
		boolean flag = false;
		try
		{
			if(tabname.equalsIgnoreCase("teamgroups")||tabname.equalsIgnoreCase("teams")||tabname.equalsIgnoreCase("jobflows")||tabname.equalsIgnoreCase("journals")||tabname.equalsIgnoreCase("complexity")||tabname.equalsIgnoreCase("process")||tabname.equalsIgnoreCase("users")||tabname.equalsIgnoreCase("roles"))
			{
				Helper.click(elements, "ADMIN", d);
			}
			
			String info[] = ElementLoader.getTabDetail(tabname);
			if(Helper.click(elements, (String)info[0],  d))
			{
				if(tabname.equalsIgnoreCase("modifyissues")) 					/*Will Click Issue Expand In Modify Articles*/			
				{
					Helper.click(elements, "MODIFYARTICLESEXPAND", d);
				}
				
				if(tabname.equalsIgnoreCase("issues"))							/*Will Click Issue Expand In Login*/
				{
					Helper.click(elements, "LOGINEXPAND", d);
				}
				
				if(tabname.equalsIgnoreCase("grabtask"))
				{
					Helper.click(elements, "GRABTASKBUTTON", d);
				}
				
				if(seeelement((String)info[1]))
				{
					flag = Helper.verifyError(elements, "ERROR", d);
				}
			}	
		}
		catch(Exception e)
		{
			test.log(LogStatus.ERROR, e.getMessage());
		}
			return flag;
	}
	
	public static boolean clickbutton(String element)
	{
		boolean flag = false;
		try
		{
			flag = Helper.click(elements, element, d);
			Helper.verifyError(elements, "ERROR", d);
		}
		catch(Exception e)
		{
			test.log(LogStatus.ERROR, e.getMessage());
		}
			return flag;
	}
	
	public static boolean click(String element, String label)
	{
		boolean flag = false;
		try
		{
			if(Helper.waitTo(element, label, d))
			{
				d.findElement(By.xpath(element)).click();
				flag = true;
			}
		}
		catch(Exception e)
		{
			test.log(LogStatus.ERROR, "<b>"+StringUtils.capitalize(label)+"<span style='color:red'> Click Failed</span></b>");
		}
			return flag;
	}
	
	
	public static boolean selectfromlist(String index, String value)
	{
		String element = ElementLoader.allCombination("li", value);
		String label="Trigger";
		boolean flag = false;
		try
		{
			(new WebDriverWait(d, 20)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(elements.getProperty("DROPDOWNTRIGGER").trim()+"["+index+"]")));
			d.findElement(By.xpath(elements.getProperty("DROPDOWNTRIGGER").trim()+"["+index+"]")).click();
			label= value;
			wait("3");
			(new WebDriverWait(d, 20)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(element)));
			d.findElement(By.xpath(element)).click();
			test.log(LogStatus.PASS, "<b>"+value+" : <span style='color:green'>Selected</span></b>");
			flag = Helper.verifyError(elements, "ERROR", d);
		}
		catch(Exception e)
		{
			test.log(LogStatus.FAIL, "<b>"+label+": <span style='color:red'> Fail</span></b>");
		}
			return flag;
	}
	
	
	
	public static boolean verifytaskin(String tabname, String jid, String aid, String stage) throws Exception
	{
		String[] tabInfo = ElementLoader.getSelectionInfo(tabname, jid, aid, stage);
		boolean flag = false;
		try
		{
			(new WebDriverWait(d, 20)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(tabInfo[0])));			
			test.log(LogStatus.PASS, "<b>"+tabInfo[1]+ " In "+StringUtils.capitalize(tabname)+": Found</b>");
			flag = true;
		}
		catch(Exception e)
		{
			test.log(LogStatus.FAIL, "<b>"+tabInfo[1]+" In "+StringUtils.capitalize(tabname)+": Not Found</b>");
			test.log(LogStatus.INFO, test.addBase64ScreenShot(Helper.EncodeImage(d, Helper.imageLocation+"/verifyTask.jpg")));
		}
			return flag;
	}
	
	
	public static boolean verifytaskout(String tabname, String jid, String aid, String stage) throws Exception
	{
		String[] tabInfo = ElementLoader.getSelectionInfo(tabname, jid, aid, stage);
		boolean flag = false;
		try
		{
			(new WebDriverWait(d, 10)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(tabInfo[0])));
			test.log(LogStatus.FAIL, "<b>"+tabInfo[1]+" In "+StringUtils.capitalize(tabname)+": Should Not Display</b>");
			test.log(LogStatus.INFO, test.addBase64ScreenShot(Helper.EncodeImage(d, Helper.imageLocation+"/verifyTaskout.jpg")));
		}
		catch(Exception e)
		{
			test.log(LogStatus.PASS, "<b>"+tabInfo[1]+ " In "+StringUtils.capitalize(tabname)+":<span style='color:green'> Cleared</span></b>");
			flag = true;
		}
			return flag;
	}
	
	
	public static boolean select(String tabname, String jid, String aid, String stage)
	{
		boolean flag = false;
		String[] selectionInfo = ElementLoader.getSelectionInfo(tabname, jid, aid, stage);
		try
		{
			flag = click(selectionInfo[0], selectionInfo[1]+" In "+tabname.toUpperCase());
			test.log(LogStatus.PASS, "<b>"+selectionInfo[1]+ " In "+StringUtils.capitalize(tabname)+":<span style='color:green'> Selected</span></b>");
		}
		catch(Exception e)
		{
			Worker.test.log(LogStatus.ERROR, e.getMessage());
		}
			return flag;
	}
	
	public static boolean selectinadmin(String tabname, String value) throws Exception
	{
		boolean flag = false;
		String[] selection= ElementLoader.getadminselectioninfo(tabname, value);
		try
		{
			if(Helper.waitTo(selection[0], selection[1], d))
			{
				flag = click(selection[0], selection[1]);
				test.log(LogStatus.PASS, "<b>"+selection[1]+":<span style='color:green'> Selected</span></b>");
			}
		}
		catch(Exception e)
		{
			test.log(LogStatus.ERROR, e.getMessage());
		}
			return flag;
	}
	
	public static boolean seeelement(String element) throws Exception
	{
		boolean flag = false;
		flag = Helper.waitFor(elements, element, d);
		if(flag)
		{
			test.log(LogStatus.PASS, "<b>"+StringUtils.capitalize(element)+" Display :<span style='color:green'> True</span></b>");
		}
		else
		{
			test.log(LogStatus.FAIL, "<b>"+StringUtils.capitalize(element)+"<b> Display :<span style='color:red'> Fail</span></b>");
			test.log(LogStatus.INFO, test.addBase64ScreenShot(Helper.EncodeImage(d, Helper.imageLocation+"/"+element+".jpg")));
	    }
			return flag;
	}
	
	
	public static boolean dontseeelement(String element)
	{
		boolean flag = false;
		try
		{
			new WebDriverWait(d, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(elements.getProperty(element).trim())));
			test.log(LogStatus.FAIL, "<b>"+element+": <span style='color:red'> should not display </span></b>");
			test.log(LogStatus.INFO, test.addBase64ScreenShot(Helper.EncodeImage(d, Helper.imageLocation+"/"+element+".jpg")));
		}
		catch(Exception e)
		{
			flag = true;
		}
			return flag;
	}
	
	public static boolean logout(String username)
	{
		boolean flag = false;
		String label = username;
		username =  "//span[text()='"+username+" ']";
		try
		{
			if(click(username, label))
			{	
				if(Helper.click(elements, "logout", d))
				{
					flag = seeelement("loginform");
				}
			}
		}
		catch(Exception e)
		{
			test.log(LogStatus.ERROR, e.getMessage());
		}
			return flag;
	}
	
	public static boolean menu(String tabname, String element, String keyword) throws Exception
	{
		String tabInfo[] = ElementLoader.getTabDetail(tabname);
		boolean flag = false;
		try
		{
			
			String path = elements.getProperty(tabInfo[1]).trim()+elements.getProperty(element.toUpperCase().trim());
			if(keyword.equalsIgnoreCase("enabled")||keyword.equalsIgnoreCase("enable")||keyword.equalsIgnoreCase("disabled")||keyword.equalsIgnoreCase("disable"))
			{
				path = elements.getProperty(tabInfo[1]).trim()+elements.getProperty(element.toUpperCase().trim())+"/ancestor::div[1]";
				if(Helper.waitTo(path, element, d))
				{
					if(d.findElement(By.xpath(path)).getAttribute("class").contains("disabled")&&(keyword.equalsIgnoreCase("disabled")||keyword.equalsIgnoreCase("disable")))
					{
						test.log(LogStatus.INFO, "<b>"+StringUtils.capitalize(element)+" Disabled : <span style='color:green'> True</span></b>");
						flag = true;
					}
					else if(!d.findElement(By.xpath(path)).getAttribute("class").contains("disabled")&&(keyword.equalsIgnoreCase("enabled")||keyword.equalsIgnoreCase("enable")))
					{
						test.log(LogStatus.INFO, "<b>"+StringUtils.capitalize(element)+" Enabled : <span style='color:green'> True</span></b>");
						flag = true;
					}
					else
					{
						test.log(LogStatus.FAIL, test.addBase64ScreenShot(Helper.EncodeImage(d, Helper.imageLocation+"/"+keyword+".jpg")));
					}
				}
			}
			else if(keyword.equalsIgnoreCase("ispresent"))
			{
				flag = Helper.waitTo(path, element, d);
				if(flag)
				{
					test.log(LogStatus.PASS, "<b>"+StringUtils.capitalize(element)+" Display : <span style='color:green'> True</span></b>");
				}
			}
			else if(keyword.equalsIgnoreCase("hide"))
			{
				try
				{
					(new WebDriverWait(d, 20)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(element)));
					test.log(LogStatus.PASS, "<b>"+StringUtils.capitalize(element)+" Hide : <span style='color:red'> Fail</span></b>");
				}
				catch(Exception e)
				{
					test.log(LogStatus.PASS, "<b>"+StringUtils.capitalize(element)+" Hide : <span style='color:green'> Pass</span></b>");
				}
			}
			else if(keyword.equalsIgnoreCase("click"))
			{
				flag = click(path, element);
			}
		}
		catch(Exception e)
		{
			test.log(LogStatus.ERROR, e.getMessage());
		}
			return flag;
	}
	
	public static boolean datepicker(String index, String day)
	{
		boolean flag = false;
		if(click(elements.getProperty("DATE").trim()+"["+index+"]", StringUtils.capitalize(index)+" Date Picker"))
		{
			String element = ElementLoader.xpath("span", day)[0]+"//ancestor::td[contains(@class,'x-datepicker-active')]";
			if(day.equalsIgnoreCase("today"))
			{
				element = ElementLoader.xpath("span", day)[0];
			}
			try
			{
				(new WebDriverWait(d, 20)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(element)));
				if(click(element, day))
				{
					test.log(LogStatus.PASS, "<b>"+StringUtils.capitalize(day)+" Date : <span style='color:green'> Selected</span></b>");
					flag = true;
				}
			}
			catch(Exception e)
			{
				flag = click(elements.getProperty("DROPDOWNTRIGGER").trim()+"["+index+"]", day);
				test.log(LogStatus.PASS, "<b>"+"Date "+StringUtils.capitalize(day)+" is in : <span style='color:red'> Disable Mode </span></b>");
			}
		}
			return flag;
	}
	
	public static boolean verifyselected(String index, String value)
	{
		String elementpath = "(//table[2]//li[text()='"+value+"'])["+index+"]";
		boolean flag = false;
		try
		{
			flag = Helper.waitTo(elementpath, StringUtils.capitalize(value), d);
			if(flag)
			{
				test.log(LogStatus.PASS, "<b>Article <span style='color:green'>"+value+" Selected</span></b>");
			}
			else
			{
				test.log(LogStatus.FAIL, test.addBase64ScreenShot(Helper.EncodeImage(d, Helper.imageLocation+"/"+value+".jpg")));
			}
		}
		catch(Exception e)
		{
			test.log(LogStatus.ERROR, e.getMessage());
		}
			return flag;
	}	
	
	
	public static boolean moveavailable(String index, String value)
	{
		String elementpath = "(//table[1]//li[text()='"+value+"'])["+index+"]";
		String addbutton	= "("+elements.getProperty("ADDBUTTON").trim()+")["+index+"]";
		boolean flag = false;
		try
		{
			if(click(elementpath, StringUtils.capitalize(value)))
			{
				if(click(addbutton, "ADDBUTTON"))
				{
					if(verifyselected(index, value))
					{
						test.log(LogStatus.PASS, "<b>Article <span style='color:green'>"+value+" Moved</span></b>");
						flag = true;
					}
				}
			}
		}
		catch(Exception e)
		{
			test.log(LogStatus.ERROR, e.getMessage());
		}
			return flag;
	}
	
	public static boolean movefromlist(String fromindex, String toindex, String button, String value)
	{
		String fromPath = "//td["+fromindex+"]//li[text()='"+value+"']";
		String toPath =  "//td["+toindex+"]//li[text()='"+value+"']";
		String buttonname	= elements.getProperty(button.toUpperCase().trim()+"BUTTON").trim();
		boolean flag = false;
		try
		{
			if(click(fromPath, StringUtils.capitalize(value)))
			{
				if(click(buttonname, button.toUpperCase().trim()+"BUTTON"))
				{
					if(Helper.waitTo(toPath, value+" In Selected", d))
					{
						test.log(LogStatus.PASS, "<b>Article <span style='color:green'>"+value+" Moved</span></b>");
						flag = true;
					}
				}
			}
		}
		catch(Exception e)
		{
			test.log(LogStatus.ERROR, e.getMessage());
		}
			return flag;
	}
	
	
	public static boolean checkelement(String element, String keyword) throws Exception
	{
		boolean flag = false;
		try
		{
			if(Helper.waitTo(elements.getProperty(element.toUpperCase().trim())+"/ancestor::div[1]", element, d))
			{
				if(d.findElement(By.xpath(elements.getProperty(element.toUpperCase().trim())+"/ancestor::div[1]")).getAttribute("class").contains("disabled")&&(keyword.equalsIgnoreCase("disabled")||keyword.equalsIgnoreCase("disable")))
				{
					test.log(LogStatus.INFO, "<b>"+StringUtils.capitalize(element)+" Disabled : <span style='color:green'> True</span></b>");
					flag = true;
				}
				else if(!d.findElement(By.xpath(elements.getProperty(element.toUpperCase().trim())+"/ancestor::div[1]")).getAttribute("class").contains("disabled")&&(keyword.equalsIgnoreCase("enabled")||keyword.equalsIgnoreCase("enable")))
				{
					test.log(LogStatus.INFO, "<b>"+StringUtils.capitalize(element)+" Enabled : <span style='color:green'> True</span></b>");
					flag = true;
				}
				else
				{
					test.log(LogStatus.FAIL, test.addBase64ScreenShot(Helper.EncodeImage(d, Helper.imageLocation+"/"+keyword+".jpg")));
				}
			}
		}
		catch(Exception e)
		{
			test.log(LogStatus.ERROR, e.getMessage());
		}
			return flag;
	}
	
	
	public static boolean verifyissuedata(String tabname, String jid, String aid, String stage, String tspages) throws Exception
	{
		boolean flag = false;
		String info[] = ElementLoader.getTabDetail(tabname);
		String element = elements.getProperty(info[1]).trim()+"//table//tr[td[2]/div='"+jid.toUpperCase()+"' and td[3]/div='"+aid+"' and td[4]/div='"+stage+"' and td[8]/div='"+tspages+"']";
		String label = "["+jid.toUpperCase()+"-"+aid+"-"+stage+"-"+tspages+"]";
		if(Helper.waitTo(element, label, d))
		{
			test.log(LogStatus.PASS, "<b>"+label+" : <span style='color:green'> Found </span></b>");
			flag = true;
		}
			return flag;
	}
	
	
	public static boolean verifyarticledata(String tabname, String jid, String aid, String stage, String mspages, String tspages, String figures, String tables) throws Exception
	{
		boolean flag = false;
		String info[] = ElementLoader.getTabDetail(tabname);
		String element = elements.getProperty(info[1]).trim()+"//table//tr[td[2]/div='"+jid.toUpperCase()+"' and td[3]/div='"+aid+"' and td[4]/div='"+stage+"' and td[8]/div='"+mspages+"' and td[9]/div='"+tspages+"' and td[10]/div='"+figures+"' and td[11]/div='"+tables+"']";
		String label = "["+jid.toUpperCase()+"-"+aid+"-"+stage+"-"+mspages+"-"+tspages+"-"+figures+"-"+tables+"]";
		if(Helper.waitTo(element, label, d))
		{
			test.log(LogStatus.PASS, "<b>"+label+" : <span style='color:green'> Found </span></b>");
			flag = true;
		}
			return flag;
	}
	
	public static boolean verifymytask(String jid, String aid, String stage, String value) throws Exception
	{
		boolean flag = false;
		String info[] = ElementLoader.mytaskdata(value);
		String element = elements.getProperty("MYTASKPANEL").trim()+"//table//tr[td[3]/div/a=' "+jid.toUpperCase()+" ' and td[4]/div/a=' "+aid+" ' and td[8]/div='"+stage+"' and td["+info[0]+"]/div=' "+info[1]+" ' or td["+info[0]+"]/div=' "+info[1]+"' or td["+info[0]+"]/div='"+info[1]+" ' or td["+info[0]+"]/div='"+info[1]+"']";
		if(Helper.waitTo(element, "["+jid.toUpperCase()+"-"+aid+"-"+stage.toUpperCase()+"]", d))
		{
			test.log(LogStatus.PASS, "<b>["+jid.toUpperCase()+"-"+aid+"-"+stage.toUpperCase()+"]<span style='color:green'> " +value+" - PASS <span></b>");
			flag = true;
		}
			return flag;
	}
	
	
	public static boolean verifytaskreport(String jid, String aid, String stage, String value) throws Exception
	{
		boolean flag = false;
		String info[] = ElementLoader.taskreportdata(value);
		String element = elements.getProperty("TASKREPORTPANEL").trim()+"//table//tr[td[5]/div/a=' "+jid.toUpperCase()+" ' and td[6]/div/a=' "+aid+" ' and td[12]/div='"+stage+"' and td["+info[0]+"]/div='"+info[1]+"']";
		if(Helper.waitTo(element, "["+jid.toUpperCase()+"-"+aid+"-"+stage.toUpperCase()+"]", d))
		{
			test.log(LogStatus.PASS, "<b>["+jid.toUpperCase()+"-"+aid+"-"+stage.toUpperCase()+"]<span style='color:green'> "+value+" - PASS <span></b>");
			flag = true;
		}
			return flag;
	}
	
	
	public static boolean verifyassigntask(String jid, String aid, String stage, String value) throws Exception
	{
		boolean flag = false;
		String info[] = ElementLoader.assigntaskdata(value);
		String element = elements.getProperty("ASSIGNTASKPANEL").trim()+"//table//tr[td[6]/div/a=' "+jid.toUpperCase()+" ' and td[7]/div/a=' "+aid+" ' and td[12]/div='"+stage+"' and td["+info[0]+"]/div='"+info[1]+"']";
		if(Helper.waitTo(element, "["+jid.toUpperCase()+"-"+aid+"-"+stage.toUpperCase()+"]", d))
		{
			flag = true;
			test.log(LogStatus.PASS, "<b>["+jid.toUpperCase()+"-"+aid+"-"+stage.toUpperCase()+"]<span style='color:green'> "+value+" - PASS <span></b>");
		}
			return flag;
	}
	
	public static boolean mytask(String jid, String aid, String stage, String toClick)
	{
		boolean flag= false;
		String tabInfo[] = ElementLoader.getSelectionInfo("mytask", jid, aid, stage);
		String button  = ElementLoader.mytaskrules(tabInfo[0], toClick);
		if(click(button, toClick))
		{
			if(Helper.verifyError(elements, "ERROR", d))
			{
				test.log(LogStatus.PASS, "<b>Task: "+tabInfo[1]+" <span style='color:green'>: "+ StringUtils.capitalize(toClick)+" Pass</b>");
				flag = true;
			}	
		}
			return flag;
	}
	
	
	public static boolean verifycolor(String tabname, String jid, String aid, String stage, String colorname)
	{
		boolean flag = false;
		try
		{
			String info[] = ElementLoader.getSelectionInfo(tabname, jid, aid, stage);
			if(Helper.waitTo(info[0], info[1], d))
			{
				String rgba = d.findElement(By.xpath(info[0]+"/td[1]/div")).getCssValue("color");
				if(colorname.equalsIgnoreCase("red"))
				{
					if(rgba.equalsIgnoreCase(elements.getProperty("REDCOLOR").trim()))
					{
						test.log(LogStatus.PASS, "<b><span style='color:red'>"+info[1]+" - in Red Color</span></b>");
						flag = true;
					}
					else
					{
						test.log(LogStatus.FAIL, info[1]+ " Not In Red Color");
						test.log(LogStatus.INFO, test.addBase64ScreenShot(Helper.EncodeImage(d, Helper.imageLocation+"/notinredcolor.jpg")));
					}
				}
				else if(colorname.equalsIgnoreCase("black"))
				{
					if(rgba.equalsIgnoreCase(elements.getProperty("BLACKCOLOR").trim()))
					{
						test.log(LogStatus.PASS, "<b><span style='color:black'>"+info[1]+" - in Black Color</span></b>");
						flag = true;
					}
					else
					{
						test.log(LogStatus.FAIL, info[1]+ " Not In Black Color");
						test.log(LogStatus.INFO, test.addBase64ScreenShot(Helper.EncodeImage(d, Helper.imageLocation+"/notinblackcolor.jpg")));
					}
				}
			}
		}
		catch(Exception e)
		{
			test.log(LogStatus.ERROR, e.getMessage());
		}
			return flag;
	}
	
	
	public static boolean taskinfo(String tabname, String jid, String aid, String stage) throws Exception
	{
		boolean flag = false;
		String element = ElementLoader.getSelectionInfo("TASKREPORT", jid, aid, stage)[0]+"//img[@data-qtip='Information']";
		if(tabname.equalsIgnoreCase("assigntask"))
		{
			element =  elements.getProperty("ASSIGNTASKPANEL").trim()+"//table//tr[td[6]//a=' "+jid.toUpperCase()+" 'and td[7]//a=' "+aid+" ' and td[12]/div='"+stage+"']//img[@data-qtip='Information']";
		}
		
		if(click(element, "["+jid.toUpperCase()+"-"+aid+"-"+stage+"]"))
		{
			if(Helper.waitTo("//*[text()='"+jid.toUpperCase()+" - "+aid+"']", "Notes: ["+jid.toUpperCase()+" - "+aid+"]", d))
			{
				test.log(LogStatus.PASS, "<b>Notes: ["+jid.toUpperCase()+" - "+aid+"] : <span style='color:green'>Displayed</span></b>");
			}
		}
			return flag;
	}
	
	
	public static boolean task(String tabname, String jid, String aid, String stage, String option) throws Exception
	{
		boolean flag = false;
		String element = ElementLoader.getSelectionInfo(tabname.toUpperCase(), jid, aid, stage)[0]+"//img[@data-qtip='Revoke']";
		if(option.equalsIgnoreCase("reassign"))
		{
			element =  ElementLoader.getSelectionInfo(tabname.toUpperCase(), jid, aid, stage)[0]+"//img[@data-qtip='ReAssign']";
		}
		
		if(click(element, "["+jid.toUpperCase()+"-"+aid+"-"+stage+"]"))
		{
			test.log(LogStatus.PASS, "<b>["+jid.toUpperCase()+" - "+aid+"-"+stage.toUpperCase()+"] : <span style='color:green'>Revoke Button Clicked</span></b>");
			flag = true;
		}
			return flag;
	}
	
	public static boolean movetopages(String tabname, String page)
	{
		String tabInfo[] = ElementLoader.getTabDetail(tabname);
		boolean flag = false;
		try
		{
			String element = elements.getProperty(tabInfo[1]).trim()+elements.getProperty("LASTPAGE").trim();
			if(page.equalsIgnoreCase("first"))
			{
				element =	elements.getProperty(tabInfo[1]).trim()+elements.getProperty("FIRSTPAGE").trim();
			}
				flag = click(element, StringUtils.capitalize(page)+" Page In "+StringUtils.capitalize(tabname));
		}
		catch(Exception e)
		{
			test.log(LogStatus.ERROR, e.getMessage());
		}
			return flag;
	}
	
	public static boolean hasgraphics(String value)
	{
		boolean flag = false;
		try
		{
			if(value.equalsIgnoreCase("no"))
			{
				flag = dontseeelement("NEXT");
			}
			else
			{
				wait("5");
				if(dontseeelement("NEXT"))
				{
					if(Helper.click(elements, "HASGRAPHICS", d))
					{
						wait("5");
						flag = Helper.click(elements, "NEXT", d);
					}
				}
				else
				{
					flag = Helper.click(elements, "NEXT", d);
				}
			}
		}
		catch(Exception e)
		{
			Worker.test.log(LogStatus.ERROR, e.getMessage());
		}
			return flag;
	}
	
	public static boolean assigntouser(String empid)
	{
		String element = "(//td[3]//div[text()='"+empid+"']/ancestor::td[2]//td[1]//div/div)[1]";
		boolean flag = false;
		try
		{
			if(Helper.waitTo(element, empid, d))
			{
				if(click(element, empid))
				{	
					test.log(LogStatus.PASS, "<b>Assigned - ["+StringUtils.capitalize(empid)+"] : <span style='color:green'>Pass</span></b>");
					flag = true;
				}
			}
		}
		catch(Exception e)
		{
			test.log(LogStatus.ERROR, e.getMessage());
		}
		return flag;
	}
	
	
	public static boolean createfile(String path, String jid, String aid, String stage, String filename, String filecontent) throws IOException
	{
		boolean flag = false;
		String url = path+"\\"+jid.toUpperCase()+"\\"+aid.toUpperCase()+"\\"+stage.toUpperCase()+"\\"+filename;
		//url = ElementLoader.alignFilePath(url);
		File fileObject = new File(url);
		if(!fileObject.getParentFile().exists())
		{
			fileObject.getParentFile().mkdirs();
			fileObject.createNewFile();
		}
		try
		{
			Writer write = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileObject), "UTF-8"));
			write.write(filecontent);
			write.close();
			flag = true;
			test.log(LogStatus.PASS, "<b>["+url+"]<span style='color:green'> File Dropped </span></b>");
		}
		catch(Exception e)
		{
			test.log(LogStatus.ERROR, e.getMessage());
		}
			return flag;
	}
	
	public static boolean seefile(String filelocation)
	{
		boolean flag = false;
		try
		{
			filelocation = ElementLoader.alignFilePath(filelocation);
			File file = new File(filelocation);
			if(file.exists())
			{
				test.log(LogStatus.PASS, "<b>[ "+new File(filelocation).getName()+" ]<span style='color:green'> - Found </span></b>");
				flag = true;
			}
		}
		catch(Exception e)
		{
			test.log(LogStatus.FAIL, "<b>[ "+new File(filelocation).getName()+" ]<span style='color:red'> - Not Found </span></b>");
		}
			return flag;
	}
	
	
	
	public static boolean filter(String tabname, String gridname, String filtertype, String select)
	{
		intializeActions();
		String tabInfo[] = ElementLoader.getTabDetail(tabname);
		boolean flag = false;
		try
		{
			String element = elements.getProperty(tabInfo[1]).trim()+"//span[text()='"+gridname+"']";
			if(Helper.waitTo(element, gridname, d))
			{
				WebElement elementName = ElementLoader.getElement(element+"/parent::div", d);
				actions.moveToElement(elementName).build().perform();
				if(click(element+"/following-sibling::div", gridname))
				{
					WebElement ele = ElementLoader.getElement("//div[@id[starts-with(.,'menu-')] and contains(@id,'body')]/parent::div[not (contains(@style,'visibility: hidden'))]//span[text()='"+StringUtils.capitalize(filtertype)+"']", d);
					actions.moveToElement(ele).build().perform();
					String valueOfFilter ="//span[text()='"+select+"']//preceding-sibling::img";
					if(ElementLoader.scrollIntoElement(valueOfFilter, d))
					{
						wait("2");
						flag = click(valueOfFilter, select);
						wait("3");
						if(flag)
						{	
							flag = Helper.click(elements, tabInfo[0], d);
						}
						wait("4");
						test.log(LogStatus.PASS, "<b>"+select+"<span style='color:green'> - Filtered</span></b>");
					}
				}	
			}
		}
		catch(Exception e)
		{
			test.log(LogStatus.ERROR, e.getMessage());
		}
			return flag;
	}
	
	
	public static boolean adminfilter(String tabname, String gridname, String filtertype, String select)
	{
		intializeActions();
		String tabInfo[] = ElementLoader.getTabDetail(tabname);
		boolean flag = false;
		try
		{
			String element = elements.getProperty(tabInfo[1]).trim()+"//span[text()='"+gridname+"']";
			if(Helper.waitTo(element, gridname, d))
			{
				WebElement elementName = ElementLoader.getElement(element+"/parent::div", d);
				actions.moveToElement(elementName).build().perform();
				if(click(element+"/following-sibling::div", gridname))
				{
					WebElement ele = ElementLoader.getElement("//div[@id[starts-with(.,'menu-')] and contains(@id,'body')]/parent::div[not (contains(@style,'visibility: hidden'))]//span[text()='"+StringUtils.capitalize(filtertype)+"']", d);
					actions.moveToElement(ele).build().perform();
					String valueOfFilter ="//span[text()='"+select+"']//preceding-sibling::img";
					if(ElementLoader.scrollIntoElement(valueOfFilter, d))
					{
						wait("2");
						flag = click(valueOfFilter, select);
						wait("3");
						if(flag)
						{	
							flag = Helper.click(elements, "ADMIN", d);
						}
					}
				}	
			}
		}
		catch(Exception e)
		{
			test.log(LogStatus.ERROR, e.getMessage());
		}
			return flag;
	}
	
	public static boolean verifypageoption(String tabname, String option)
	{
		String tabInfo[] = ElementLoader.getTabDetail(tabname);
		boolean flag = false;
		try
		{
			String element = elements.getProperty(tabInfo[1]).trim()+elements.getProperty(option.toUpperCase()).trim();
			if(Helper.waitTo(element, StringUtils.capitalize(option), d))
			{
				test.log(LogStatus.PASS, "<b>"+StringUtils.capitalize(option)+"<span style='color:green'> - Display : Pass</span></b>");
				flag = true;
			}
		}
		catch(Exception e)
		{
			test.log(LogStatus.ERROR, e.getMessage());
		}
			return flag;
	}
	
	public static boolean verifygrid(String tabname, String gridname, String option) throws Exception
	{
		boolean flag = false;
		String tabInfo[] = ElementLoader.getTabDetail(tabname);
		String element = elements.getProperty(tabInfo[1]).trim()+"//span[text()='"+gridname+"']";
		try
		{
			if(!option.equalsIgnoreCase("hide"))
			{
				if(Helper.waitTo(element, gridname, d))
				{
					flag = true;
					test.log(LogStatus.PASS, "<b>"+StringUtils.capitalize(gridname)+"<span style='color:green'> - Display : Pass</span></b> ");
				}
			}
			else
			{
				(new WebDriverWait(d, 20)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(element)));
				test.log(LogStatus.FAIL, "<b>"+StringUtils.capitalize(gridname)+"<span style='color:red'> - Hide : Fail</span></b> ");
			}
		}
		catch(Exception e)
		{
			flag = true;
			test.log(LogStatus.PASS, "<b>"+StringUtils.capitalize(gridname)+"<span style='color:green'> - Hide : Pass</span></b> ");
		}
			return flag;
	}
	
	public static boolean verifylabel(String label) throws Exception
	{
		boolean flag = false;
		String element = "//label[text()='"+label+"']/ancestor::table[1][not(contains(@style,'display: none'))]";
		if(Helper.waitTo(element, label, d))
		{
			test.log(LogStatus.PASS, "<b>"+StringUtils.capitalize(label)+"<span style='color:green'> - Display : Pass</span></b>");
			flag = true;
		}
			return flag;
	}
	
	
	public static boolean verifydropdownwarning(String index, String display) throws Exception
	{
		boolean flag = false;
		String element =elements.getProperty("DROPDOWNTRIGGER").trim()+"["+index+"]"+"/ancestor::td[1]/following-sibling::td[@style='display: none;']"; 
		if(display.equalsIgnoreCase("yes"))
		{
			try
			{
				element = elements.getProperty("DROPDOWNTRIGGER").trim()+"["+index+"]"+"/ancestor::td[1]/following-sibling::td[@style='']";
				(new WebDriverWait(d, 10)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(element)));
				test.log(LogStatus.PASS, "<b>Field "+index+"<span style='color:green'> - Warning Display : Pass</span></b>");
				flag = true;
			}
			catch(Exception e)
			{
				test.log(LogStatus.FAIL, "<b>Field "+index+"<span style='red'> - Warning Display : Fail</span></b>");
				test.log(LogStatus.INFO, test.addBase64ScreenShot(Helper.EncodeImage(d, Helper.imageLocation+"/"+index+".jpg")));
			}
		}
		else
		{
			try
			{
				(new WebDriverWait(d, 10)).until(ExpectedConditions.presenceOfElementLocated(By.xpath(element)));
				test.log(LogStatus.PASS, "<b>Field "+index+"<span style='color:green'> - Warning Hide : Pass</span></b>");
				flag = true;
			}
			catch(Exception e)
			{
				test.log(LogStatus.FAIL, "<b>Field "+index+"<span style='color:red'> - Warning Hide : Fail</span></b>");
				test.log(LogStatus.INFO, test.addBase64ScreenShot(Helper.EncodeImage(d, Helper.imageLocation+"/"+index+".jpg")));
			}
		}
			return flag;
	}
	
	
	public static boolean verifytextboxwarning(String attribute, String index, String display) throws Exception
	{
		boolean flag = false;
		String elementPath = ElementLoader.returnElement(attribute, index);
		String element =elementPath+"/ancestor::td[1]/following-sibling::td[@style='display: none;']"; 
		if(display.equalsIgnoreCase("yes"))
		{
			try
			{
				element = elementPath+"/ancestor::td[1]/following-sibling::td[@style='']";
				(new WebDriverWait(d, 10)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(element)));
				test.log(LogStatus.PASS, "<b>Field "+index+"<span style='color:green'> - Warning Display : Pass</span></b>");
				flag = true;
			}
			catch(Exception e)
			{
				test.log(LogStatus.FAIL, "<b>Field "+index+"<span style='red'> - Warning Display : Fail</span></b>");
				test.log(LogStatus.INFO, test.addBase64ScreenShot(Helper.EncodeImage(d, Helper.imageLocation+"/"+index+".jpg")));
			}
		}
		else
		{
			try
			{
				(new WebDriverWait(d, 10)).until(ExpectedConditions.presenceOfElementLocated(By.xpath(element)));
				test.log(LogStatus.PASS, "<b>Field "+index+"<span style='color:green'> - Warning Hide : Pass</span></b>");
				flag = true;
			}
			catch(Exception e)
			{
				test.log(LogStatus.FAIL, "<b>Field "+index+"<span style='red'> - Warning Hide : Fail</span></b>");
				test.log(LogStatus.INFO, test.addBase64ScreenShot(Helper.EncodeImage(d, Helper.imageLocation+"/"+index+".jpg")));
			}
		}
			return flag;
	}
	
	
	public static boolean dialog(String dialogname, String elementname, String actionname)
	{
		boolean flag = false;
		try
		{
			String element = elements.getProperty(dialogname.toUpperCase()).trim()+elements.getProperty(elementname.toUpperCase()).trim();
			if(Helper.waitTo(element, elementname, d))
			{
				if(actionname.equalsIgnoreCase("click"))
				{
					flag  = click(element, elementname);
				}
				else if(actionname.equalsIgnoreCase("enable")||actionname.equalsIgnoreCase("enabled")||actionname.equalsIgnoreCase("disable")||actionname.equalsIgnoreCase("disabled"))
				{
					flag = Helper.enabledisable(element, actionname, d);
				}
				else if(actionname.equalsIgnoreCase("ispresent"))
				{
					if(Helper.waitTo(element, elementname, d))
					{
						test.log(LogStatus.PASS, "<b>"+StringUtils.capitalize(elementname)+"<span style='color:green'> - Display : Pass</span></b>");
						flag = true;
					}
				}
			}
		}
		catch(Exception e)
		{
			test.log(LogStatus.ERROR, e.getMessage());
		}
			return flag;
	}
	
	public static boolean element(String attribute, String index, String option) throws Exception
	{
		boolean flag = false;
		String[] attrib = attribute.split("=");
		String elementPath = ElementLoader.returnElement(attribute, index);
		try
		{
			if(!option.equalsIgnoreCase("hide"))
			{	
				(new WebDriverWait(d, 20)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(elementPath)));
				if(option.equalsIgnoreCase("click"))
				{
					Helper.verifyError(elements, "ERROR", d);
					flag = click(elementPath, attribute);
				}
				else if(option.contains("type")||option.contains("Type")||option.contains("TYPE"))
				{
					String value[] = option.trim().split("=");
					if(ElementLoader.typeOn(elementPath, value[1], value[0], d))
					{	
						test.log(LogStatus.PASS, "<b>"+StringUtils.capitalize(attrib[1])+"<span style='color:green'> - "+StringUtils.capitalize(value[0])+" : Pass</span></b>");
						flag = true;
					}
					else
					{
						test.log(LogStatus.FAIL, "<b>"+StringUtils.capitalize(attrib[1])+"<span style='color:red'> - "+StringUtils.capitalize(value[0])+" : Fail</span></b>");
					}
				}
				else if(option.equalsIgnoreCase("enable")||option.equalsIgnoreCase("enabled")||option.equalsIgnoreCase("disable")||option.equalsIgnoreCase("disabled"))
				{
					flag = Helper.enabledisable(elementPath, option, d);
				}
				else if(option.equalsIgnoreCase("ispresent"))
				{
					if(Helper.waitTo(elementPath, attribute, d))
					{
						test.log(LogStatus.PASS, "<b>"+attrib[1]+"<span style='color:green'> - Display : Pass</span></b>");
						flag = true;
					}
					
				}
				else if(option.equalsIgnoreCase("mousehover"))
				{
					if(Helper.waitTo(elementPath, attribute, d))
					{
						Actions action = new Actions(d);
						WebElement ele = d.findElement(By.xpath(elementPath));
						action.moveToElement(ele).perform();
						flag = true;
					}
					else
					{
						test.log(LogStatus.FAIL, "<b>"+attrib[1]+"<span style='color:red'> : Mousehover Failed</span></b>");
					}
				}
				else if(option.equalsIgnoreCase("doubleclick"))
				{
					try
					{
						Actions action = new Actions(d);
						WebElement ele=d.findElement(By.xpath(elementPath));
						action.doubleClick(ele).perform();
						flag = true;
					}
					catch(Exception e)
					{
						test.log(LogStatus.INFO, test.addBase64ScreenShot(Helper.EncodeImage(d, Helper.imageLocation+"/"+elementPath+".jpg")));
					}
				}
			}	
			else 
			{
				try
				{
					(new WebDriverWait(d, 20)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(elementPath)));
					test.log(LogStatus.FAIL, "<b>"+index+"<span style='color:red'> - Hide : Fail</span></b>");
					test.log(LogStatus.INFO, test.addBase64ScreenShot(Helper.EncodeImage(d, Helper.imageLocation+"/"+elementPath+".jpg")));
				}
				catch(Exception e)
				{
					test.log(LogStatus.PASS, "<b>"+StringUtils.capitalize(attribute)+"<span style='color:green'> - Hide : Pass</span></b>)");
				}
			}
		}
		catch(Exception e)
		{
			test.log(LogStatus.ERROR, e.getMessage());
		}
			return flag;
	}
	
	public static boolean verifycheckbox(String flagname)
	{
		boolean flag = false;
		String element = "//div[text()='"+flagname+"']//preceding::td[1]//input";
		try
		{
			if(Helper.waitTo(element, flagname, d))
			{
				WebElement webElement = ElementLoader.getElement(element, d);
				flag = webElement.isSelected();
				test.log(LogStatus.PASS, "<b>"+StringUtils.capitalize(flagname)+" Checked :<span style='color:green'> "+flag+"</span></b>");
			}
		}
		catch(Exception e)
		{
			test.log(LogStatus.ERROR, e.getMessage());
		}
			return flag;
	}
	
	public static boolean currentprocess(String aid, String process, String keyword)
	{
		boolean flag = false;
		String element = "//table//tr[td[1]/div/a=' "+aid+" ' and td[2]/div='"+process+"']//img[@data-qtip='Action']";
		try
		{
			flag = Helper.waitTo(element, process, d);
			if(keyword.equalsIgnoreCase("click")&&(flag))
			{
				if(click(element, process))
				{
					flag = true;
					test.log(LogStatus.PASS, "<b>"+StringUtils.capitalize(process)+" Clicked In Edit Window : <span style='color:green'> "+flag+"</span></b>");
				}
			}
		}
		catch(Exception e)
		{
			test.log(LogStatus.ERROR, e.getMessage());
		}
		return flag;
	}
	
	public static boolean verifymytaskin(String jid, String aid, String stage, String value) throws Exception
	{
		boolean flag = false;
		String info[] = ElementLoader.mytaskdata(value);
		String element = elements.getProperty("MYTASKPANEL").trim()+"//table//tr[td[3]/div/a=' "+jid.toUpperCase()+" ' and td[4]/div/a=' "+aid+" ' and td[8]/div='"+stage+"' and td["+info[0]+"]/div=' "+info[1]+" ' or td["+info[0]+"]/div=' "+info[1]+"' or td["+info[0]+"]/div='"+info[1]+" ' or td["+info[0]+"]/div='"+info[1]+"']";
		try
		{
			(new WebDriverWait(d, 10)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(element)));
			test.log(LogStatus.PASS, "<b>["+jid.toUpperCase()+"-"+aid+"-"+stage.toUpperCase()+"]<span style='color:green'> " +value+" found - PASS <span></b>");
			flag = true;
		}
		catch(Exception e)
		{
			test.log(LogStatus.FAIL, "<b>["+jid.toUpperCase()+"-"+aid+"-"+stage.toUpperCase()+"]<span style='color:red'> " +value+" Not found - FAIL <span></b>");
			test.log(LogStatus.INFO, test.addBase64ScreenShot(Helper.EncodeImage(d, Helper.imageLocation+"/verifyMyTaskin.jpg")));
		}
			return flag;
	}
	
	public static boolean verifymytaskout(String jid, String aid, String stage, String value) throws Exception
	{
		boolean flag = false;
		String info[] = ElementLoader.mytaskdata(value);
		String element = elements.getProperty("MYTASKPANEL").trim()+"//table//tr[td[3]/div/a=' "+jid.toUpperCase()+" ' and td[4]/div/a=' "+aid+" ' and td[8]/div='"+stage+"' and td["+info[0]+"]/div=' "+info[1]+" ' or td["+info[0]+"]/div=' "+info[1]+"' or td["+info[0]+"]/div='"+info[1]+" ' or td["+info[0]+"]/div='"+info[1]+"']";
		try
		{
			(new WebDriverWait(d, 10)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(element)));
			test.log(LogStatus.FAIL, "<b>["+jid.toUpperCase()+"-"+aid+"-"+stage.toUpperCase()+"]<span style='color:green'> " +value+" found - FAIL <span></b>");
			test.log(LogStatus.INFO, test.addBase64ScreenShot(Helper.EncodeImage(d, Helper.imageLocation+"/verifyMyTaskout.jpg")));
		}
		catch(Exception e)
		{
			test.log(LogStatus.PASS, "<b>["+jid.toUpperCase()+"-"+aid+"-"+stage.toUpperCase()+"]<span style='color:green'> " +value+" Not found - PASS <span></b>");
			flag = true;
		}
			return flag;
	}
	
	public static boolean verifysize(String source, String destination)
	{
		boolean flag = false;
		double byte1 = 0;
		double byte2 = 0;
		double kb1 = 0;
		double kb2 = 0;
		try
		{
			File src =new File(source);
			if(src.exists())
			{
				byte1 = src.length();
				kb1 = byte1 * 1024;
				
			}
			File des =new File(destination);
			if(des.exists())
			{
				byte2 = des.length();
				kb2 = byte2 * 1024;
			}
			if(byte1==byte2)
			{
				flag = true;
				test.log(LogStatus.PASS,"<b> File Size Matched <span style='color:green'></span></b>" );
			}
		}
		catch(Exception e)
		{
			test.log(LogStatus.ERROR, e.getMessage());
		}
		return flag;
	}
	
	public static boolean verifyadmintaskin(String tabname, String value) throws Exception
	{
		String[] tabInfo = ElementLoader.getadminselectioninfo(tabname, value);
		boolean flag = false;
		try
		{
			(new WebDriverWait(d, 20)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(tabInfo[0])));			
			test.log(LogStatus.PASS, "<b>"+tabInfo[1]+ " In "+StringUtils.capitalize(tabname)+": Found</b>");
			flag = true;
		}
		catch(Exception e)
		{
			test.log(LogStatus.FAIL, "<b>"+tabInfo[1]+" In "+StringUtils.capitalize(tabname)+": Not Found</b>");
			test.log(LogStatus.INFO, test.addBase64ScreenShot(Helper.EncodeImage(d, Helper.imageLocation+"/verifyTask.jpg")));
		}
			return flag;
	}
	
	public static boolean verifyadmintaskout(String tabname, String value) throws Exception
	{
		String[] tabInfo = ElementLoader.getadminselectioninfo(tabname, value);
		boolean flag = false;
		try
		{
			(new WebDriverWait(d, 10)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(tabInfo[0])));
			test.log(LogStatus.FAIL, "<b>"+tabInfo[1]+" In "+StringUtils.capitalize(tabname)+": Should Not Display</b>");
			test.log(LogStatus.INFO, test.addBase64ScreenShot(Helper.EncodeImage(d, Helper.imageLocation+"/verifyTaskout.jpg")));
		}
		catch(Exception e)
		{
			test.log(LogStatus.PASS, "<b>"+tabInfo[1]+ " In "+StringUtils.capitalize(tabname)+":<span style='color:green'> Cleared</span></b>");
			flag = true;
		}
			return flag;
	}
	
	public static boolean grabtask(String jid, String aid, String stage, String toClick)
	{
		boolean flag= false;
		String tabInfo[] = ElementLoader.getSelectionInfo("grabtask", jid, aid, stage);
		String button  = ElementLoader.grabtaskrules(tabInfo[0], toClick);
		if(click(button, toClick))
		{
			if(Helper.verifyError(elements, "ERROR", d))
			{
				test.log(LogStatus.PASS, "<b>Task: "+tabInfo[1]+" <span style='color:green'>: "+ StringUtils.capitalize(toClick)+" Pass</b>");
				flag = true;
			}	
		}
			return flag;
	}
	
	public static boolean verifygrabtask(String jid, String aid, String stage, String value) throws Exception
	{
		boolean flag = false;
		String info[] = ElementLoader.grabtaskdata(value);
		String element = elements.getProperty("GRABTASKPANEL").trim()+"//table//tr[td[5]/div/a=' "+jid.toUpperCase()+" ' and td[6]/div/a=' "+aid+" ' and td[11]/div='"+stage+"' and td["+info[0]+"]/div='"+info[1]+"']";
		if(Helper.waitTo(element, "["+jid.toUpperCase()+"-"+aid+"-"+stage.toUpperCase()+"]", d))
		{
			flag = true;
			test.log(LogStatus.PASS, "<b>["+jid.toUpperCase()+"-"+aid+"-"+stage.toUpperCase()+"]<span style='color:green'> "+value+" - PASS <span></b>");
		}
			return flag;
	}
	
	public static boolean verifypagecount(String tabname)
	{
		boolean flag = false;
		try
		{
		String tabInfo[] = ElementLoader.getTabDetail(tabname);
		String element = elements.getProperty(tabInfo[1]).trim()+elements.getProperty("PAGECOUNT").trim();
		(new WebDriverWait(d, 20)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(element)));
		String value = d.findElement(By.xpath(element)).getText();
		String a = value.substring(19);
		float b = Float.parseFloat(a)/25;
		b = (int) Math.ceil(b);
		WebElement ele = d.findElement(By.xpath("//div[contains(text(),'of "+Math.round(b)+"')]"));
		test.log(LogStatus.PASS,"<b> Page Count "+b+" :<span style='color:green'> Displayed</span></b>");
		flag = true;
		}
		catch(Exception e)
		{
			test.log(LogStatus.ERROR, e.getMessage());
		}
		return flag;
	}
	
	public static boolean urlcontains(String value)
	{
		boolean flag = false;
		try
		{
			String val = d.getCurrentUrl();
			if(val.contains(value))
			{
				Worker.test.log(LogStatus.PASS,"<b>  "+value+" :<span style='color:green'> - customer switched</span></b>");
				flag = true;
			}
			else
			{
				Worker.test.log(LogStatus.FAIL, Worker.test.addBase64ScreenShot(Helper.EncodeImage(d, Helper.imageLocation+"/"+value+".jpg")));
			}
		}
		catch(Exception e)
		{
			test.log(LogStatus.ERROR, e.getMessage());
		}
		return flag;
	}
	
	public static boolean copyfile(String source , String destination)
	{
		boolean flag = false;
		try
		{
			File src = new File(source);
			File des = new File(destination);
			if(des.exists())
			{
				FileUtils.deleteDirectory(des);
			}
			Thread.sleep(1000);
			FileUtils.copyDirectory(src, des);
			flag = true;
			Worker.test.log(LogStatus.PASS,"<b>  "+destination+" :<span style='color:green'> - File Copied Here</span></b>");
			
		}
		catch(Exception e)
		{
			test.log(LogStatus.ERROR, e.getMessage());
		}
		return flag;
	}
	
	public static boolean hidefile(String filepath) throws Exception{
		boolean flag = false;
		try {
			Path file = Paths.get(filepath);
			Files.setAttribute(file, "dos:hidden", true);
			Worker.test.log(LogStatus.PASS,"<b>  "+filepath+" :<span style='color:green'> - File Hided Successfully</span></b>");
			flag = true;
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	public static boolean deletefile(String filepath) throws Exception{
		boolean flag = false;
		try{
    		File file = new File(filepath);
    		if(file.delete()){
    			Worker.test.log(LogStatus.PASS,"<b>  "+file.getName()+" :<span style='color:green'> - is deleted successfully</span></b>");
    			flag = true;
    		}else{
    			Worker.test.log(LogStatus.PASS,"<b>  "+file.getName()+" :<span style='color:green'> - Delete operation is failed.</span></b>");
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
		return flag;
	}
	
}
	
