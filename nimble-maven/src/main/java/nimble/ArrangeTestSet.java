package nimble;

import java.util.ArrayList;

/*
 * Test comment
 * 
 * 
 * 
 */
import java.util.List;

import org.jopendocument.dom.spreadsheet.Sheet;

public class ArrangeTestSet 
{
static Sheet sheet;
	
	public static List<Integer> serializeExecution(String from, String to) throws Exception
	{
		
		int n = Configurator.sheets().getSheetCount();
		List<Integer> range = new ArrayList<Integer>();
		
		/* Adds all Sheets to the list */
		
		if(from.equalsIgnoreCase("ALL")&&(to==null))
		{
//			System.out.println("Entered All Sheets Execution Section");
			for(int i=1;i<Configurator.sheets().getSheetCount();i++)
			{
				range.add(i);
			}
		}
		
		/* Will add specific single sheet*/   
		
		else if(!from.equalsIgnoreCase("all")&&(from!=null)&&(to==null))
		{
//			System.out.println("Entered Specific Sheet Execution Section");
			range.add(1);
			for(int i=2;i<n;i++)
			{
				if(Configurator.sheets().getSheet(i).getName().equalsIgnoreCase(from))
				{
					range.add(i);
				}
			}
		}
		
		/*  Will add sheets between ranges  */
		
		else if((!from.equalsIgnoreCase("all")&&(from!=null)&&(to!=null)))
		{
//			System.out.println("Entered Sheets between ranges Execution Section");
			int start =0;
			range.add(1);
			for(int i=2;i<n;i++)
			{
				if(Configurator.sheets().getSheet(i).getName().equalsIgnoreCase(from))
				{
					start = i;
					range.add(start);
					start = start+1;
				}
			}
			for(;start<n;start++)
			{
				if(!Configurator.sheets().getSheet(start).getName().equalsIgnoreCase(to))
				{
					range.add(start);
				}
				else
				{
					range.add(start);
					break;
				}
			}
		}
		
		
			return range;
	}
}
