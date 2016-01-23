package org.labreg.frames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.JTableHeader;
import net.proteanit.sql.DbUtils;
import org.labreg.actions.ButtonListener;
import org.labreg.actions.MenuListener;
import org.labreg.database.DBManager;

/**
 * @author Voqus
 */
public final class ContentFrame
{

	/* Variable declaration -- START */
	public static JFrame frame = null;
	private final JTable _labTable = new JTable()
	{
		@Override
		public boolean isCellEditable(int row, int column)
		{
			// Sets restriction so the cells wont be editable
			return false;
		} // isCellEditable
	}; // JTable

	private JMenuBar _menuBar = null;
	private JMenu _programMenu = null;
	private JMenuItem _exportItem = null;
	private JMenuItem _exitItem = null;

	private JComboBox<Object> _profLabSelector;

	private Connection _con = null;
	private PreparedStatement _pStatement = null;
	private ResultSet _resSet = null;
	/* Variable declaration -- END */

	public ContentFrame()
	{
		// Initialize the frame object and set title to it
		frame = new JFrame("e-LabRegistration Application");
		// Initialize the GUI components of the application
		init();
	}

	/**
	 * Initializes the GUI of the application
	 */
	public void init()
	{
		frame.setLayout(new FlowLayout());
		// Set the panel for the laboratory selection of the professor
		JPanel labSelectionPanel = new JPanel();

		// Setting the menu of the application
		_menuBar = new JMenuBar();
		_programMenu = new JMenu("Program");
		_exportItem = new JMenuItem("Export as Excel");
		_programMenu.add(_exportItem);
		_menuBar.add(_programMenu);

		// Adding a separator for the program menu
		_programMenu.addSeparator();
		_exitItem = new JMenuItem("Exit");
		_programMenu.add(_exitItem);

		// Adding the menu bar to the application frame
		frame.setJMenuBar(_menuBar);

		// Selects the unique names from the database that are registered under the full name of the Professor
		try
		{
			_con = DBManager.connect();
			_pStatement = _con.prepareStatement("SELECT DISTINCT LabName FROM LabData WHERE Professor=?");
			_pStatement.setString(1, ButtonListener._professorLogged.getName() + " " + ButtonListener._professorLogged.getLastName());
			_resSet = _pStatement.executeQuery();

			// while there is a result add it to the list
			while (_resSet.next())
			{
				// pass in the results found to a list
				ButtonListener._professorLogged.addLaboratory(_resSet.getString("LabName"));
			} // while

		} catch (Exception e)
		{
			// If something is wrong, return the error log.
			e.printStackTrace();
		}//try
		finally
		{
			// Try to close all the possible open resources
			try
			{
				if (_con != null)
					_con.close();

				if (_pStatement != null)
					_pStatement.close();

				if (_resSet != null)
					_resSet.close();

			} catch (SQLException se)
			{
				// If something is wrong, return the error log.
				se.printStackTrace();
			} // try
		} // finally

		labSelectionPanel.add(new JLabel("Select Laboratory: "));
		// Convert the list to array to initialize the drop-down box
		_profLabSelector = new JComboBox<Object>(ButtonListener._professorLogged.laboratoryList.toArray());
		// add the drop-down box to the panel
		labSelectionPanel.add(_profLabSelector);

		// Select the student records that correspond to the Laboratory selected
		try
		{
			_con = DBManager.connect();
			_pStatement = _con.prepareStatement("SELECT Name,LastName,AM,Semester,Hour FROM StudentList WHERE LabName=?");
			_pStatement.setString(1, String.valueOf(_profLabSelector.getSelectedItem()));
			_resSet = _pStatement.executeQuery();

			// If result found, use the rs2xml library to pass the data into the JTable
			_labTable.setModel(DbUtils.resultSetToTableModel(_resSet));
		} catch (SQLException | HeadlessException e)
		{
			// If something is wrong, return the error log.
			JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} // try
		finally
		{
			// Try to close all the possible open resources
			try
			{
				if (_con != null)
					_con.close();

				if (_pStatement != null)
					_pStatement.close();

				if (_resSet != null)
					_resSet.close();

			} catch (SQLException se)
			{
				// If something is wrong, return the error log.
				se.printStackTrace();
			} // try
		} // finally

		// Set the header of the JTable
		JTableHeader tableHeader = _labTable.getTableHeader();
		_labTable.setRowHeight(24);
		// Setting the preferred width for the hour column
		_labTable.getColumn("Hour").setPreferredWidth(150);

		// Initialize the action listener for the drop-down box button
		ButtonListener buttonListener = new ButtonListener(_labTable, _profLabSelector);
		_profLabSelector.addActionListener(buttonListener);

		// Initialize the action listener for the menu
		MenuListener menuListener = new MenuListener(_labTable, _exportItem, _exitItem);
		_exportItem.addActionListener(menuListener);
		_exitItem.addActionListener(menuListener);

		// Setting the panel for the table of the application and its header
		JPanel tablePanel = new JPanel(new BorderLayout());
		tablePanel.add(tableHeader, BorderLayout.NORTH);
		tablePanel.add(_labTable, BorderLayout.CENTER);

		// Setting a panel as the main panel of the application to include the lab selection and the table
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(labSelectionPanel, BorderLayout.NORTH);
		mainPanel.add(tablePanel, BorderLayout.CENTER);

		// Adding the mainPanel to the top-level container
		frame.add(mainPanel);
		frame.pack(); // do automatic resize of the window
		// Set the minimum size by taking the size from the pack() and adding 50 width and 200 height pixels
		frame.setMinimumSize(new Dimension(frame.getWidth() + 50, frame.getHeight() + 200));
		frame.setLocationRelativeTo(null); // Set the frame to the center of the screen
		// Set the default operation to close and terminate the process of this application
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		// Set the frame's visibility to true
		frame.setVisible(true);
	} // init
} // ContentFrame
