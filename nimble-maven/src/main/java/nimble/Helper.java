package nimble;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.LogStatus;

public class Helper 
{
	public static String imageLocation;
	
	public Helper(String imageLocation)
	{
		this.imageLocation = imageLocation;
	}
	
	public static boolean click(Properties properties, String element, WebDriver d)
	{
		boolean flag = false;
		try
		{
			if(waitFor(properties, element, d))
			{
				WebElement webelement = d.findElement(By.xpath(properties.getProperty(element.toUpperCase()).trim()));
				webelement.click();
				flag = true;
			}	
		}
		catch(Exception e)
		{
			Worker.test.log(LogStatus.ERROR, e.getMessage());
		}
		return flag;
	}
	
	
	public static boolean waitFor(Properties properties, String element, WebDriver d) throws Exception
	{
		boolean flag = false;
		try
		{
			(new WebDriverWait(d, 60)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(properties.getProperty(element.toUpperCase()).trim())));
			flag = true;
		}
		catch (Exception ex) 
		{
			Worker.test.log(LogStatus.FAIL, "<b>"+StringUtils.capitalize(element)+"<span style='color:red'> : Not Found </span></b>");
			Worker.test.log(LogStatus.FAIL, Worker.test.addBase64ScreenShot(Helper.EncodeImage(d, Helper.imageLocation+"/"+element+".jpg")));
			
		}
			return flag;
	}
	
	
	
	public static boolean waitTo(String element, String label,  WebDriver d) throws Exception
	{
		boolean flag = false;
		try
		{
			(new WebDriverWait(d, 60)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(element)));
			flag = true;
		}
		catch (Exception ex) 
		{
			Worker.test.log(LogStatus.FAIL, "<b>"+StringUtils.capitalize(label)+"<span style='color:red'> : Not Found</span></b>");
			Worker.test.log(LogStatus.FAIL, Worker.test.addBase64ScreenShot(Helper.EncodeImage(d, Helper.imageLocation+"/"+label+".jpg")));
		}
			return flag;
	}
	
	
	public static boolean typeText(Properties properties, String element, String text, WebDriver d) throws Exception
	{
		boolean flag = false;
		try
		{
			flag = waitFor(properties, element, d);
			if(flag)
			{
				d.findElement(By.xpath(properties.getProperty(element.toUpperCase()).trim())).clear();
				Thread.sleep(3000);
				d.findElement(By.xpath(properties.getProperty(element.toUpperCase()).trim())).sendKeys(text);
			}
		}
		catch(Exception e)
		{
			Worker.test.log(LogStatus.FAIL, "<b>"+StringUtils.capitalize(element)+" Not Allowed To Type");
			Worker.test.log(LogStatus.INFO, Worker.test.addBase64ScreenShot(EncodeImage(d, imageLocation+"/"+element+".jpg")));
		}
			return flag;
	}
	
	public static boolean verifyError(Properties properties, String element, WebDriver d)
	{
		boolean flag = false;
		try
		{
			element = properties.getProperty(element.toUpperCase()).trim(); 
			(new WebDriverWait(d, 5)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(element)));
			Worker.test.log(LogStatus.INFO, Worker.test.addBase64ScreenShot(EncodeImage(d, imageLocation+"/Error.jpg")));
			click(properties, "OKBUTTON", d);
		}
		catch(Exception e)
		{
			flag = true;
		}
			return flag;
	}
	
	public static String EncodeImage(WebDriver d, String imgpath) throws Exception
	{
		String image = "";
		try
		{
			imgpath  = ElementLoader.saveFile(imgpath, 1);
			File imgfile = new File(captureScreen(d, imgpath));
			FileInputStream imageFile = new FileInputStream(imgpath);
            byte imageData[] = new byte[(int) imgfile.length()];
            imageFile.read(imageData);
            byte[] base64EncodedByteArray = Base64.encodeBase64(imageData);
            image = new String(base64EncodedByteArray);
	    }
		catch(Exception e)
		{
			e.printStackTrace();
		}
			return "data:image/png;base64,"+image;
	}
	
	
	public static String captureScreen(WebDriver d, String imagePath)
	{
		TakesScreenshot oScn = (TakesScreenshot) d;
	    File oScnShot = oScn.getScreenshotAs(OutputType.FILE);
	    File oDest = new File(imagePath);
	    try 
	    {
	    	FileUtils.copyFile(oScnShot, oDest);
	    } 
	    catch (IOException e) 
	    {
	    	System.out.println(e.getMessage());
	    }
	    	return imagePath;
	}
	
	public static boolean enabledisable(String element, String keyword, WebDriver d) throws Exception
	{
		boolean flag = false;
		try
		{
			String label ="";
			Matcher matcher = Pattern.compile("[\"'](.+)[\"']").matcher(element);
			if(matcher.find())
			{
				label = matcher.group(1);
			}
			if(Helper.waitTo(element.trim()+"/ancestor::div[1]", matcher.group(1), d))
			{
				if(d.findElement(By.xpath(element.trim()+"/ancestor::div[1]")).getAttribute("class").contains("disabled")&&(keyword.equalsIgnoreCase("disabled")||keyword.equalsIgnoreCase("disable")))
				{
					Worker.test.log(LogStatus.INFO, "<b>"+label+" Disabled : <span style='color:green'> True</span></b>");
					flag = true;
				}
				else if(!d.findElement(By.xpath(element.trim()+"/ancestor::div[1]")).getAttribute("class").contains("disabled")&&(keyword.equalsIgnoreCase("enabled")||keyword.equalsIgnoreCase("enable")))
				{
					Worker.test.log(LogStatus.INFO, "<b>"+label+" Enabled : <span style='color:green'> True</span></b>");
					flag = true;
				}
				else
				{
					Worker.test.log(LogStatus.INFO, Worker.test.addBase64ScreenShot(Helper.EncodeImage(d, Helper.imageLocation+"/"+label+".jpg")));
				}
			}
		}
		catch(Exception e)
		{
			Worker.test.log(LogStatus.ERROR, e.getMessage());
		}
			return flag;
	}
	  
}
