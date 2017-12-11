package nimble;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ElementLoader 
{
	public static String[] getTabDetail(String tabname)
	{
		String elementPath[] = new String[2];
		if(tabname.equalsIgnoreCase("mytask"))
		{
			elementPath[0] = "MYTASK";
			elementPath[1] = "MYTASKPANEL";
		}
		else if(tabname.equalsIgnoreCase("login"))
		{
			elementPath[0] ="LOGIN";
			elementPath[1] ="LOGINPANEL";
		}
		else if(tabname.equalsIgnoreCase("issues"))
		{
			elementPath[0] ="LOGIN";
			elementPath[1] ="ISSUESPANEL";
		}
		else if(tabname.equalsIgnoreCase("assigntask"))
		{
			elementPath[0] ="ASSIGNTASK";
			elementPath[1] ="ASSIGNTASKPANEL";
		}
		else if(tabname.equalsIgnoreCase("taskreport"))
		{
			elementPath[0] ="TASKREPORT";
			elementPath[1] ="TASKREPORTPANEL";
		}
		else if(tabname.equalsIgnoreCase("modifyarticles"))
		{
			elementPath[0] ="MODIFYARTICLES";
			elementPath[1] ="MODIFYARTICLESPANEL";
		}
		else if(tabname.equalsIgnoreCase("modifyissues"))
		{
			elementPath[0] ="MODIFYARTICLES";
			elementPath[1] ="MODIFYISSUESPANEL";
		}
		else if(tabname.equalsIgnoreCase("reports"))
		{
			elementPath[0] ="REPORTS";
			elementPath[1] ="REPORTARTICLE";
		}
		else if(tabname.equalsIgnoreCase("grabtask"))
		{
			elementPath[0] ="GRABTASK";
			elementPath[1] ="GRABTASKPANEL";
		}
		else if(tabname.equalsIgnoreCase("revoketask"))
		{
			elementPath[0] ="REVOKETASK";
			elementPath[1] ="REVOKETASKPANEL";
		}
		else if(tabname.equalsIgnoreCase("teamgroups"))
		{
			elementPath[0] ="TEAMGROUP";
			elementPath[1] ="TEAMGROUPLIST";
		}
		else if(tabname.equalsIgnoreCase("teams"))
		{
			elementPath[0] ="TEAM";
			elementPath[1] ="TEAMLIST";
		}
		else if(tabname.equalsIgnoreCase("jobflows"))
		{
			elementPath[0] ="JOBFLOW";
			elementPath[1] ="JOBFLOWLIST";
		}
		else if(tabname.equalsIgnoreCase("journals"))
		{
			elementPath[0] ="JOURNAL";
			elementPath[1] ="JOURNALLIST";
		}
		else if(tabname.equalsIgnoreCase("complexity"))
		{
			elementPath[0] ="COMPLEXITY";
			elementPath[1] ="COMPLEXITYLIST";
		}
		else if(tabname.equalsIgnoreCase("process"))
		{
			elementPath[0] ="PROCESS";
			elementPath[1] ="PROCESSLIST";
		}
		else if(tabname.equalsIgnoreCase("users"))
		{
			elementPath[0] ="USER";
			elementPath[1] ="USERLIST";
		}
		else if(tabname.equalsIgnoreCase("roles"))
		{
			elementPath[0] ="ROLE";
			elementPath[1] ="ROLELIST";
		}
			return elementPath;
	}
	
	public static String allCombination(String tag, String value)
	{
		String finalPath = "//"+tag+"[text()='"+value.toLowerCase()+"']|//"+tag+"[text()='"+value.toUpperCase()+"']|//"+tag+"[text()='"+org.apache.commons.lang.StringUtils.capitalize(value)+"']|//"+tag+"[text()='"+value+"']";
		return finalPath;
	}
	
	
	public static String alignFilePath(String oldString)
	{
		String filename = oldString.replace("\\", "/");
		return filename;
	}
	
	public static String saveFile(String filePath, int i)
	{
		File file = new File(filePath);
		if(file.exists())
		{
			saveFile(file.getParent()+"/"+FilenameUtils.getBaseName(file.getName()+i+".jpg"), i++);
		}
			return filePath;
	}
	
	
	public static boolean typeOn(String elementPath, String tobetyped, String value, WebDriver d)
	{
		boolean flag = false;
		if(value.equalsIgnoreCase("typenot"))
		{
			try
			{
				d.findElement(By.xpath(elementPath)).clear();
				Thread.sleep(3000);
				d.findElement(By.xpath(elementPath)).sendKeys(tobetyped);
			}
			catch(Exception e)
			{
				flag = true;
			}
		}
		else if(value.equalsIgnoreCase("type"))
		{
			try
			{
				d.findElement(By.xpath(elementPath)).clear();
				Thread.sleep(3000);
				d.findElement(By.xpath(elementPath)).sendKeys(tobetyped);
				flag = true;
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
		}
			return flag;
	}
	
	public static String returndate(String date, String oldpattern, String newpattern) throws ParseException
	{
		Date dateobj = new SimpleDateFormat(oldpattern).parse(date); 
		String dateString2 = new SimpleDateFormat(newpattern).format(dateobj); 
		return dateString2;
	}
	
	public static boolean scrollIntoElement(String elementPath, WebDriver d)
	{
		boolean flag = false;
		try
		{
			elementPath = "\"" + elementPath + "\"";
			Thread.sleep(3000);
			JavascriptExecutor js = (JavascriptExecutor) d;
			js.executeScript("var e = document.evaluate("+elementPath+",document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;"
					+ "e.scrollIntoView();");
			flag = true;
		}
		catch(Exception e)
		{
			System.out.println("Scroll Into View: "+e.getMessage());
		}
			return flag;
	}
	
	
	public static String inputValue(String element, WebDriver d) throws Exception	{
		try	{
				element = "\"" + element + "\"";
				Thread.sleep(3000);
				JavascriptExecutor js = (JavascriptExecutor) d;
				return (String) js.executeScript("var e = document.evaluate("+element+",document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;"
					+ "return e.value;");
			}
		catch(Exception e)	{
			e.printStackTrace();
			throw new Exception("Unable to retrieve text from "+element);
		}
	}
	
	public static String[] getSelectionInfo(String tabname, String jid, String aid, String stage)
	{
		String tabInfo[] = getTabDetail(tabname);
		String info[] = new String[2];
		if(tabname.equalsIgnoreCase("login"))
		{
			info[0] = Worker.elements.getProperty(tabInfo[1]).trim()+"//table//tr[td[2]/div='"+jid.toUpperCase()+"' and td[3]/div='"+aid+"' and td[4]/div='"+stage+"']/td[1]/div/div";
			info[1] = "["+jid.toUpperCase()+"-"+aid+"-"+stage+"]";
		}
		else if(tabname.equalsIgnoreCase("issues"))
		{
			info[0] = Worker.elements.getProperty(tabInfo[1]).trim()+"//table//tr[td[2]/div='"+jid.toUpperCase()+"' and td[3]/div='"+aid+"' and td[4]/div='"+stage+"']/td[1]/div/div";
			info[1] = "["+jid.toUpperCase()+"-"+aid+"-"+stage+"]";
		}
		else if(tabname.equalsIgnoreCase("assigntask"))
		{
			info[0] = Worker.elements.getProperty(tabInfo[1]).trim()+"//table//tr[td[6]//a=' "+jid.toUpperCase()+" 'and td[7]//a=' "+aid+" ' and td[12]/div='"+stage+"']/td[1]/div/div";
			info[1] = "["+jid.toUpperCase()+"-"+aid+"-"+stage+"]";
		}
		else if(tabname.equalsIgnoreCase("modifyarticles"))
		{
			info[0] = Worker.elements.getProperty(tabInfo[1]).trim()+"//table//tr[td[2]/div='"+jid.toUpperCase()+"' and td[3]/div='"+aid+"' and td[4]/div='"+stage+"']/td[1]/div/div";
			info[1] = "["+jid.toUpperCase()+"-"+aid+"-"+stage+"]";
		}
		else if(tabname.equalsIgnoreCase("modifyissues"))
		{
			info[0] = Worker.elements.getProperty(tabInfo[1]).trim()+"//table//tr[td[2]/div='"+jid.toUpperCase()+"' and td[3]/div='"+aid+"' and td[4]/div='"+stage+"']/td[1]/div/div";
			info[1] = "["+jid.toUpperCase()+"-"+aid+"-"+stage+"]";
		}
		else if(tabname.equalsIgnoreCase("mytask"))
		{
			info[0] = Worker.elements.getProperty(tabInfo[1]).trim()+"//table//tr[td[3]/div/a=' "+jid.toUpperCase()+" ' and td[4]/div/a=' "+aid+" ' and td[8]/div='"+stage+"']";
			info[1] = "["+jid.toUpperCase()+"-"+aid+"-"+stage+"]";
		}
		else if(tabname.equalsIgnoreCase("taskreport"))
		{
			info[0] = Worker.elements.getProperty(tabInfo[1]).trim()+"//table//tr[td[5]/div/a=' "+jid.toUpperCase()+" ' and td[6]/div/a=' "+aid+" ' and td[12]/div='"+stage+"']";
			info[1] = "["+jid.toUpperCase()+"-"+aid+"-"+stage+"]";
		}
		else if(tabname.equalsIgnoreCase("grabtask"))
		{
			info[0] = Worker.elements.getProperty(tabInfo[1]).trim()+"//table//tr[td[5]/div/a=' "+jid.toUpperCase()+" ' and td[6]/div/a=' "+aid+" ' and td[11]/div='"+stage+"']";
			info[1] = "["+jid.toUpperCase()+"-"+aid+"-"+stage+"]";
		}
		else if(tabname.equalsIgnoreCase("revoketask"))
		{
			info[0] = Worker.elements.getProperty(tabInfo[1]).trim()+"//table//tr[td[4]/div/a=' "+jid.toUpperCase()+" ' and td[5]/div/a=' "+aid+" ' and td[10]/div='"+stage+"']";
			info[1] = "["+jid.toUpperCase()+"-"+aid+"-"+stage+"]";
		}
			return info;
	}
	
	
	public static String[] getadminselectioninfo(String tabname, String value)
	{
		String tabInfo[] = getTabDetail(tabname);
		String values[] = new String[3];
		String val[] = value.split("=");
		if(val[1].trim().contains(","))
		{
			String input[] = val[1].trim().split(",");
			if(tabname.equalsIgnoreCase("complexity"))
			{
				values[0] = Worker.elements.getProperty(tabInfo[1]).trim()+"//table//tr[td[2]/div='"+input[0].trim()+"' and td[3]/div='"+input[1].trim()+"']/td[1]/div/div";
				values[1] = "["+input[0]+"]"+" - "+"["+input[1]+"]";
			}
		}
		else
		{
			if(tabname.equalsIgnoreCase("teamgroups"))
			{
				values[0] = Worker.elements.getProperty(tabInfo[1]).trim()+"//table//tr[td[2]/div='"+val[1]+"']/td[1]/div/div";
				values[1] = "["+val[1]+"]";
			}
			else if(tabname.equalsIgnoreCase("teams"))
			{
				values[0] = Worker.elements.getProperty(tabInfo[1]).trim()+"//table//tr[td[2]/div='"+val[1]+"']/td[1]/div/div";
				values[1] = "["+val[1]+"]";
			}
			else if(tabname.equalsIgnoreCase("jobflows"))
			{
				values[0] = Worker.elements.getProperty(tabInfo[1]).trim()+"//table//tr[td[2]/div='"+val[1]+"']/td[1]/div/div";
				values[1] = "["+val[1]+"]";
			}
			else if(tabname.equalsIgnoreCase("journals"))
			{
				values[0] = Worker.elements.getProperty(tabInfo[1]).trim()+"//table//tr[td[2]/div='"+val[1]+"']/td[1]/div/div";
				values[1] = "["+val[1]+"]";
			}
			else if(tabname.equalsIgnoreCase("process"))
			{
				values[0] = Worker.elements.getProperty(tabInfo[1]).trim()+"//table//tr[td[2]/div='"+val[1]+"']/td[1]/div/div";
				values[1] = "["+val[1]+"]";
			}
			else if(tabname.equalsIgnoreCase("roles"))
			{
				values[0] = Worker.elements.getProperty(tabInfo[1]).trim()+"//table//tr[td[2]/div='"+val[1]+"']/td[1]/div/div";
				values[1] = "["+val[1]+"]";
			}
			else if(tabname.equalsIgnoreCase("users"))
			{
				values[0] = Worker.elements.getProperty(tabInfo[1]).trim()+"//table//tr[td[2]/div='"+val[1]+"']/td[1]/div/div";
				values[1] = "["+val[1]+"]";
			}
		}
			return values;
	}
	
	
	public static String[] mytaskdata(String value)
	{
		String  val[] = value.split("=");
		String[] index =new String[2];
		if(val[0].trim().equalsIgnoreCase("flag"))
		{
			index[0] = "10";
			index[1] = val[1].trim();	
		}
		else if(val[0].trim().equalsIgnoreCase("status"))
		{
			index[0] = "11";
			index[1] = val[1].trim();
		}
		else if(val[0].equalsIgnoreCase("figures")||val[0].equalsIgnoreCase("Figures"))
		{
			index[0] = "6";
			index[1] = val[1].trim();
		}
		else if(val[0].equalsIgnoreCase("process"))
		{
			index[0] = "7";
			index[1] = val[1].trim();
		}
			return index;
	}
	
	
	public static String[] taskreportdata(String value)
	{
		String  val[] = value.split("=");
		String[] index =new String[3];
		if(val[0].trim().equalsIgnoreCase("flag"))
		{
			index[0] = "24";
			index[1] = val[1].trim()+" ";
		}
		else if(val[0].trim().equalsIgnoreCase("status"))
		{
			index[0] = "26";
			index[1] = val[1].trim();
		}
		else if(val[0].trim().equalsIgnoreCase("process"))
		{
			index[0] = "14";
			index[1] = val[1].trim();
		}
			
			return index;
	}
	
	public static String[] assigntaskdata(String value)
	{
		String  val[] = value.split("=");
		String[] index =new String[2];
		if(val[0].trim().equalsIgnoreCase("flag"))
		{
			index[0] = "14";
			index[1] = val[1].trim()+" ";	
		}
		else if(val[0].trim().equalsIgnoreCase("status"))
		{
			index[0] = "15";
			index[1] = val[1].trim();
		}
		else if(val[0].trim().equalsIgnoreCase("process"))
		{
			index[0] = "11";
			index[1] = val[1].trim();
		}
			return index;
	}
	
	public static String[] grabtaskdata(String value)
	{
		String  val[] = value.split("=");
		String[] index =new String[2];
		if(val[0].trim().equalsIgnoreCase("flag"))
		{
			index[0] = "13";
			index[1] = val[1].trim()+" ";	
		}
		else if(val[0].trim().equalsIgnoreCase("status"))
		{
			index[0] = "14";
			index[1] = val[1].trim();
		}
			return index;
	}
	
	public static String mytaskrules(String elementPath, String toClick)
	{
		String value = elementPath.substring(0, elementPath.length()-1)+"";
		
		if(toClick.equalsIgnoreCase("start"))
		{
			elementPath = value+ " and td[11]/div='Assigned']//img[@data-qtip='Start']";
		}
		else if(toClick.equalsIgnoreCase("complete"))
		{
			elementPath = value+ " and td[11]/div='WIP']//img[@data-qtip='Complete']";
		}
		else if(toClick.equalsIgnoreCase("pause"))
		{
			elementPath = value+ " and td[11]/div='WIP']//img[@data-qtip='Pause']";
		}
		else if(toClick.equalsIgnoreCase("continue"))
		{
			elementPath = value+" and td[11]/div='Held']//img[@data-qtip='Continue']";
		}
		else if(toClick.equalsIgnoreCase("information"))
		{
			elementPath = value+ "]//img[@data-qtip='Information']";
		}
		else if(toClick.equalsIgnoreCase("download"))
		{
			elementPath = value+ " and td[11]/div='WIP']//img[@data-qtip='Download']";
		}
		else if(toClick.equalsIgnoreCase("figures")||toClick.equalsIgnoreCase("figure"))
		{
			elementPath = value+ " and td[6]/div !='0']//td[6]/div";
		}
		else if(toClick.equalsIgnoreCase("subtask"))
		{
			elementPath = value+ " and td[11]/div='WIP']//img[@data-qtip='Subtask']";
		}
			return elementPath;
	}
	
	public static String grabtaskrules(String elementPath, String toClick)
	{
		String value = elementPath;
		
		if(toClick.equalsIgnoreCase("assign"))
		{
			elementPath = value+ "//img[@data-qtip='Assign']";
		}
		else if(toClick.equalsIgnoreCase("information"))
		{
			elementPath = value+ "//img[@data-qtip='Information']";
		}
			return elementPath;
	}
	
	
	
	public static WebElement getElement(String path, WebDriver d)
	{
		WebElement element = d.findElement(By.xpath(path));
		return element;
	}
	
	
	public static String returnElement(String attrib, String index)
	{
		String tag ="*";
		String val[] = attrib.split("=");
		if(attrib.contains(":"))
		{
			String input[] =attrib.split(":");
			tag =input[0].trim();
			val = input[1].split("=");
		}
		String element = "(//"+tag+"[@"+val[0].trim()+"='"+val[1].trim()+"'])";
		if(!index.equals("0"))
		{	
			element ="(//"+tag+"[@"+val[0].trim()+"='"+val[1].trim()+"'])["+index+"]";
		}
		if(attrib.contains("text()"))
		{
			element = element.replace("@", "");
		}
			return element;
	}
	
	public static String getText(String path, WebDriver d) throws InterruptedException
	{
		Thread.sleep(3000);
		String text = d.findElement(By.xpath(path)).getText();
		return text;
	}
	
	public static String[] xpath(String tag, String text)
	{
		String[] values = new String[2];
		values[0]= "//"+tag+"[text()='"+text+"']";
		values[1]= text;
		return values;
	}
	
	public static String concat(String a1, String b1)
	{
		return a1+b1;
	}
	
	public static String issuerules(String isresupply)
	{
		String windowname ="running-task-edit-association-articles";
		
		if(isresupply.equalsIgnoreCase("yes"))
		{
			windowname ="running-task-edit-association-resupply";
		}
			return "//*[contains(@id,'"+windowname+"')]";
	}
	
}
