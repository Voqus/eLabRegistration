package org.labreg.actions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import javax.swing.JTable;
import javax.swing.table.TableModel;

/**
 * @author Voqus
 */
public class FileExport
{
	/**
	 * Converts the JTable to a <code>file</code>, returns <code>true</code>/<code>false</code> if it succeeds or not.
	 * 
	 * @param table
	 * @param file
	 * @return
	 */
	public static boolean toExcel(JTable table, File file)
	{
		try
		{
			TableModel tableModel = table.getModel();
			// Get the columns and rows and write them to a file.
			try (BufferedWriter excel = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "ISO-8859-7")))
			{ // Change the charset encoding for greek characters
				int i, j;
				for (i = 0; i < tableModel.getColumnCount(); i++)
				{
					// Write the column headers
					excel.write(tableModel.getColumnName(i) + "\t");
				} // for
				excel.write("\n"); // new line
				
				for (i = 0; i < tableModel.getRowCount(); i++)
				{// rows
					for (j = 0; j < tableModel.getColumnCount(); j++)
					{// columns
						// take the rows and columns and write them to the file
						excel.write(tableModel.getValueAt(i, j).toString() + "\t");
					} // for
					excel.write("\n"); // new line
				} // for
			}
			return true; // return true if it succeeded reading & writing the file
		} catch (IOException e)
		{
			// If something is wrong, return the error log.
			e.printStackTrace();
			return false; // return false if something went wrong and didn't manage to finish
		} // try
	} // toExcel
} // FileExport
