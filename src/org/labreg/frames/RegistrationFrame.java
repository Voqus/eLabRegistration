package org.labreg.frames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import org.labreg.actions.ButtonListener;
import org.labreg.database.DBManager;

/**
 * @author Voqus
 */
public final class RegistrationFrame extends JFrame
{

	/* Variable declaration -- START */
	private JTextField labNameField;
	private JTextField totalRecordsField;
	private JComboBox<Object> timeComboBox, labComboBox;
	private JButton submitButton, cancelButton;
	public static final JLabel _notificationLabel = new JLabel("* Please select some laboratory to activate the submit button.", JLabel.CENTER);

	private Connection con = DBManager.connect();
	private PreparedStatement _pStatement = null;
	private ResultSet _rSet = null;

	private List<String> labList = new ArrayList<>();
	private List<String> dataList = new ArrayList<>();
	/* Variable declaration -- END */

	public RegistrationFrame()
	{
		
		super("e-LabRegistration Application");
		init();
	} // Constructor

	/**
	 * Initializes the GUI of the application
	 */
	public void init()
	{
		setLayout(new FlowLayout());
		
		// Creating a panel for the record fields
		JPanel labRegPanel = new JPanel();
		// Creating another panel for the laboratory selection
		JPanel labSelectionPanel = new JPanel();

		submitButton = new JButton("Submit");
		// Disabling the submit button in order to deal with some bugs that came up
		submitButton.setEnabled(false);
		cancelButton = new JButton("Cancel");

		// Find all the laboratory names from the database that matches the semester of the user 
		try
		{
			con = DBManager.connect();
			_pStatement = con.prepareStatement("SELECT DISTINCT LabName FROM LabData WHERE Semester=?");
			_pStatement.setString(1, ButtonListener._studentLogged.getSemester());
			_rSet = _pStatement.executeQuery();

			while (_rSet.next())
			{
				// Acquired a lock on _labHours variable in case its been asked be different instances of program or different thread.
				synchronized (labList)
				{
					// Pass in the results found into a list
					labList.add(_rSet.getString("LabName"));
				} // synchronized
			} // while
		}
		catch (Exception e)
		{
			// If something is wrong, return the error log.
			e.printStackTrace();
		} // try
		finally
		{
			// Try to close all the possible open resources.
			try
			{
				if (con != null)
					con.close();
				
				if (_pStatement != null)
					_pStatement.close();
				
				if (_rSet != null)
					_rSet.close();
				
			} catch (SQLException se)
			{
				// If something is wrong, return the error log.
				se.printStackTrace();
			} // try
		} // finally

		labSelectionPanel.add(new JLabel("Selected Laboratory: "));
		labComboBox = new JComboBox<Object>(labList.toArray());
		labSelectionPanel.add(labComboBox);

		labRegPanel.add(new JLabel("Laboratory Name: "));
		labNameField = new JTextField(15);
		labNameField.setText(String.valueOf(labComboBox.getSelectedItem()));
		labNameField.setEditable(false);
		labRegPanel.add(labNameField);

		labRegPanel.add(new JLabel("Students Registered: "));
		totalRecordsField = new JTextField(15);
		totalRecordsField.setEditable(false);
		labRegPanel.add(totalRecordsField);

		// Find all the possible records from the student list where the laboratory name
		// is the same with the selection from drop-down box.
		try
		{
			con = DBManager.connect();
			_pStatement = con.prepareStatement("SELECT COUNT(*) FROM StudentList WHERE LabName=?");
			_pStatement.setString(1, String.valueOf(labComboBox.getSelectedItem()));
			_rSet = _pStatement.executeQuery();

			if (_rSet.next())
				totalRecordsField.setText(_rSet.getString("COUNT(*)"));
		} catch (SQLException | HeadlessException exc)
		{
			// If something is wrong, return the error log.
			exc.printStackTrace();
		}// catch
		finally
		{
			// Try to close all the possible open resources.
			try
			{
				if (con != null)
					con.close();

				if (_pStatement != null)
					_pStatement.close();

				if (_rSet != null)
					_rSet.close();

			}
			catch (SQLException se)
			{
				// If something is wrong, return the error log.
				se.printStackTrace();
			} // try
		} // finally

		try
		{
			con = DBManager.connect();
			_pStatement = con.prepareStatement("SELECT Hour FROM LabData WHERE LabName=?");
			_pStatement.setString(1, labList.get(labList.size() - 1));
			_rSet = _pStatement.executeQuery();
			while (_rSet.next())
			{
				// Acquired a lock on _labHours variable in case its been asked be different instances of program or different thread.
				synchronized (dataList)
				{
					dataList.add(_rSet.getString("Hour"));
				} // synchronized
			} // while
		}
		catch (Exception e)
		{
			// If something is wrong, return the error log.
			e.printStackTrace();
		} // try
		finally
		{
			// Try to close all the possible open resources.
			try
			{
				if (con != null)
					con.close();

				if (_pStatement != null)
					_pStatement.close();

				if (_rSet != null)
					_rSet.close();

			} catch (SQLException se)
			{
				// If something is wrong, return the error log.
				se.printStackTrace();
			} // try
		} // finally
		labRegPanel.add(new JLabel("Laboratory Timetable: "));
		timeComboBox = new JComboBox<Object>(dataList.toArray());
		labRegPanel.add(timeComboBox);

		// Initialize the action listener and pass in the components to handle
		ButtonListener buttonListener = new ButtonListener(labNameField, totalRecordsField, timeComboBox, labComboBox, submitButton, cancelButton);
		submitButton.addActionListener(buttonListener);
		cancelButton.addActionListener(buttonListener);
		labComboBox.addActionListener(buttonListener);
		timeComboBox.addActionListener(buttonListener);

		// Setting a panel for the buttons
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());

		buttonPanel.add(submitButton);
		buttonPanel.add(cancelButton);

		JPanel main2Panel = new JPanel();
		JPanel mainPanel = new JPanel();
		main2Panel.setLayout(new BorderLayout());
		mainPanel.setLayout(new BorderLayout());

		// Setting a mix of panels the desirable GUI setup
		JPanel containerPanel = new JPanel();
		containerPanel.setLayout(new BorderLayout());
		containerPanel.add(labSelectionPanel, BorderLayout.NORTH);
		containerPanel.add(labRegPanel, BorderLayout.CENTER);
		containerPanel.setBorder(BorderFactory.createTitledBorder("Laboratory Time Classification Form: "));
		main2Panel.add(containerPanel, BorderLayout.CENTER);
		main2Panel.add(buttonPanel, BorderLayout.SOUTH);
		mainPanel.add(main2Panel, BorderLayout.CENTER);
		mainPanel.add(_notificationLabel, BorderLayout.SOUTH);
		add(mainPanel);// add the final panel to the top-level container
		pack(); // do automatic resize of the window
		// Set the minimum size by taking the size from the pack() and adding 30 width to it
		setMinimumSize(new Dimension(this.getWidth() + 30, this.getHeight()));
		// Set the default operation to close and terminate the process of this application
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);// Set the frame to the center of the screen
		// Set the frame's visibility to true
		setVisible(true);
	} // init
} // RegistrationFrame
