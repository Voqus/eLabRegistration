package org.labreg.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.labreg.frames.ContentFrame;

/**
 * @author Voqus
 */
public final class MenuListener implements ActionListener
{

	/* Variable declaration -- START */
	private final JMenuItem _exportItem, _exitItem;
	private final JTable _labTable;
	/* Variable declaration -- END */

	public MenuListener(final JTable labTable, final JMenuItem exportItem, final JMenuItem exitItem)
	{
		this._labTable = labTable;
		this._exportItem = exportItem;
		this._exitItem = exitItem;
	} // Constructor

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// If action belongs to _exportItem
		if (e.getSource().equals(_exportItem))
		{
			// Open new panel to choose the path of the file that it's going to be saved at.
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Save as..");
			fileChooser.setSelectedFile(new File("Student_List.xls"));
			fileChooser.setFileFilter(new FileNameExtensionFilter("Excel 97-2003 Workbook (*.xls)", ".xls"));

			if (fileChooser.showSaveDialog(ContentFrame.frame) == JFileChooser.APPROVE_OPTION)
			{
				File fileToSave = fileChooser.getSelectedFile();

				// Try to convert the JTable data to the file specified
				if (FileExport.toExcel(_labTable, fileToSave))
				{
					JOptionPane.showMessageDialog(fileChooser, "File saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
				} else
				{
					JOptionPane.showMessageDialog(fileChooser, "Something went wrong, the file didn't get saved.", "Failure", JOptionPane.ERROR_MESSAGE);
					return;
				} // if
			} // if
		} // if

		// If the action belongs to _exitItem
		if (e.getSource().equals(_exitItem))
		{
			System.exit(0);
		} // if
	}

}
