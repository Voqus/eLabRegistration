package org.labreg.actions;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import net.proteanit.sql.DbUtils;
import org.labreg.base.Professor;
import org.labreg.base.Student;
import org.labreg.database.DBManager;
import org.labreg.frames.ContentFrame;
import org.labreg.frames.LoginFrame;
import org.labreg.frames.RegistrationFrame;

/**
 * @author Voqus
 */
public class ButtonListener implements ActionListener
{
	/* Variable declaration -- START */
	private JTextField _usernameField, _labField, _totalRecordField;
	private JPasswordField _passwordField;
	private JButton _connectButton, _cancelButton, _submitButton, _cancel2Button;

	private JTable _labTable;
	private JComboBox<Object> _hourLabBox, _labSelector, _profLabSelector;

	private final List<String> _labHours = new ArrayList<>();

	private Connection _con = DBManager.connect();
	private PreparedStatement _pStatement = null;
	private ResultSet _resSet = null;

	public static Student _studentLogged = null;
	public static Professor _professorLogged = null;
	/* Variable declaration -- END */

	public ButtonListener(final JTextField usernameField, final JPasswordField passwordField, final JButton connectButton, final JButton cancelButton)
	{
		this._usernameField = usernameField;
		this._passwordField = passwordField;
		this._connectButton = connectButton;
		this._cancelButton = cancelButton;
	} // LoginListener Constructor

	public ButtonListener(final JTextField labField, final JTextField totalRecordField, final JComboBox<Object> hourLabBox, final JComboBox<Object> labSelector, final JButton submitButton, final JButton _cancelButton)
	{
		this._labField = labField;
		this._totalRecordField = totalRecordField;
		this._hourLabBox = hourLabBox;
		this._submitButton = submitButton;
		this._cancel2Button = _cancelButton;
		this._labSelector = labSelector;
	} // RegistrationFrame Constructor

	public ButtonListener(final JTable _labTable, final JComboBox<Object> profLabSelector)
	{
		this._labTable = _labTable;
		this._profLabSelector = profLabSelector;
	} // ContentFrame Constructor

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// If the action belongs to _connectButton
		if (e.getSource().equals(_connectButton))
		{
			// Using SwingWorker to work with different thread on the background because it causes visual bugs on GUI.
			SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>()
			{
				@Override
				protected Void doInBackground() throws Exception
				{
					// If the fields are empty, return with error message.
					if (_usernameField.getText().isEmpty() || _passwordField.getText().isEmpty())
					{
						JOptionPane.showMessageDialog(null, "The form contains empty fields.", "Error", JOptionPane.ERROR_MESSAGE);
						return null;
					} // if

					// Confirm the validity of the data given with those of the student's database.
					try
					{
						String sqlQuery = "SELECT * FROM Student WHERE Username=? AND Password=?";
						_con = DBManager.connect();
						_pStatement = _con.prepareStatement(sqlQuery);
						_pStatement.setString(1, _usernameField.getText());
						_pStatement.setString(2, _passwordField.getText());
						_resSet = _pStatement.executeQuery();

						if (_resSet.next())
						{
							// if the data given were found then open the registration GUI.
							_studentLogged = new Student(_resSet.getString("Name"), _resSet.getString("LastName"), _resSet.getString("Semester"), Integer.valueOf(_resSet.getString("AM")));
							// close the connection of the database since it opens new instance afterwards,
							// the finally block is not executed to close the connection from there.
							_con.close();

							// Initialize the registration frame GUI window.
							SwingUtilities.invokeLater(() ->
							{
								new RegistrationFrame();
								// destroy the login frame GUI window, no longer needed.
								LoginFrame.frame.dispose();
							}); // invokeLater
						} // if
						else
						{
							// Confirm the validity of the data given with those of the student's database.
							try
							{
								_pStatement = _con.prepareStatement("SELECT * FROM Professor WHERE Username=? AND Password=?");
								_pStatement.setString(1, _usernameField.getText());
								_pStatement.setString(2, _passwordField.getText());
								_resSet = _pStatement.executeQuery();

								if (_resSet.next())
								{
									// If the data given were found then open the content window.
									_professorLogged = new Professor(_resSet.getString("Name"), _resSet.getString("LastName"));
									// close the connection of the database.
									_con.close();

									// Initialize the content frame GUI window
									SwingUtilities.invokeLater(() ->
									{
										new ContentFrame();
										// Destroy the login frame GUI window, no longer needed.
										LoginFrame.frame.dispose();
									}); // invokeLater
								} else
								{
									// If the data given were mistaken, return the error log.
									JOptionPane.showMessageDialog(null, "The data given were invalid, or there is no such account.", "Error", JOptionPane.ERROR_MESSAGE);
									return null;
								} // if
							} catch (SQLException | HeadlessException exc)
							{
								// If something is wrong, return the error.
								JOptionPane.showMessageDialog(null, exc, "Error", JOptionPane.ERROR_MESSAGE);
								exc.printStackTrace();
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
						} // if
					}
					catch (SQLException | HeadlessException exc)
					{
						// If something is wrong, return the error log.
						JOptionPane.showMessageDialog(null, exc, "Error", JOptionPane.ERROR_MESSAGE);
						exc.printStackTrace();
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
					return null;
				} // doInBackground
			};
			// Execute the worker for background work
			worker.execute();
		} // if

		// If the action belongs to drop-down _labSelector
		if (e.getSource().equals(_labSelector))
		{
			// Setting the _submitButton active, because of the reason described at class RegistrationFrame @ line:61
			_submitButton.setEnabled(true);
			// Setting the notification label to null
			RegistrationFrame._notificationLabel.setText(null);

			// Take the laboratory schedule from the database of the selected laboratory.
			try
			{
				_con = DBManager.connect();
				_pStatement = _con.prepareStatement("SELECT Hour FROM LabData WHERE LabName=?");
				_pStatement.setString(1, String.valueOf(_labSelector.getSelectedItem()));
				_resSet = _pStatement.executeQuery();

				// Acquired a lock on _labHours variable in case its been asked be different instances of program or different thread.
				synchronized (_labHours)
				{
					_labHours.clear();
				} // synchronized
				while (_resSet.next())
				{
					// Required a lock on _labHours as before for the same reason.
					synchronized (_labHours)
					{
						_labHours.add(_resSet.getString("Hour"));
					} // synchronized
				} // while
				// Removing all the items existing in drop-down box.
				_hourLabBox.removeAllItems();
				_hourLabBox.setModel(new DefaultComboBoxModel<Object>());

				// For each element that exists in the list, add it on the drop-down box.
				for (Object obj : _labHours)
					_hourLabBox.addItem(obj);

				// Set the name of the laboratory from the selected laboratory.
				_labField.setText(String.valueOf(_labSelector.getSelectedItem()));
			} catch (SQLException | HeadlessException exc)
			{

				// If something is wrong, return the error log.
				JOptionPane.showMessageDialog(null, exc, "Error", JOptionPane.ERROR_MESSAGE);
				exc.printStackTrace();
			} // try
			finally
			{
				// Try to close all the possible open resources.
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

			// Take the studentList from the database of the selected laboratory from the drop-down box
			try
			{
				_con = DBManager.connect();
				_pStatement = _con.prepareStatement("SELECT COUNT(*) FROM StudentList WHERE LabName=?");
				_pStatement.setString(1, String.valueOf(_labSelector.getSelectedItem()));
				_resSet = _pStatement.executeQuery();

				if (_resSet.next())
				{
					// If found, add it on total record field with the number accumulated.
					_totalRecordField.setText(_resSet.getString("COUNT(*)"));
				} // if
			} catch (SQLException | HeadlessException exc)
			{
				// If something is wrong, return the error log.
				JOptionPane.showMessageDialog(null, exc, "Error", JOptionPane.ERROR_MESSAGE);
				exc.printStackTrace();
			} // try
			finally
			{
				// Try to close all the possible open resources.
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
		} // if

		// If the action belongs to drop-down box
		if (e.getSource().equals(_hourLabBox))
		{
			// Take all the student records of which the name of the laboratory and the day/time is what we selected.
			try
			{
				_con = DBManager.connect();
				_pStatement = _con.prepareStatement("SELECT COUNT(*) FROM StudentList WHERE LabName=? AND Hour=?");
				_pStatement.setString(1, String.valueOf(_labField.getText()));
				_pStatement.setString(2, String.valueOf(_hourLabBox.getSelectedItem()));
				_resSet = _pStatement.executeQuery();

				if (_resSet.next())
				{
					// If found, add it on total record field with the number accumulated.
					_totalRecordField.setText(_resSet.getString("COUNT(*)"));
				} // if
			} catch (SQLException | HeadlessException exc)
			{
				// If something is wrong, return the error log.
				JOptionPane.showMessageDialog(null, exc, "Error", JOptionPane.ERROR_MESSAGE);
				exc.printStackTrace();
			}// try
			finally
			{
				// Try to close all the possible open resources.
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
		} // if

		// If the action belongs to _submitButton
		if (e.getSource().equals(_submitButton))
		{
			// Using SwingWorker to work with different thread on the background because it causes visual bugs on GUI.
			SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>()
			{
				@Override
				protected Void doInBackground() throws Exception
				{
					// If the student records in a laboratory exceed 25 seats, then return error log.
					if (Integer.valueOf(_totalRecordField.getText()) >= 25)
					{
						JOptionPane.showMessageDialog(null, "The laboratory is already full." + " Please select another time/day or contact with the professor of this laboratory.", "Number of seats exceeded", JOptionPane.ERROR_MESSAGE);
						return null;
					} // if

					// Find out if the student trying to register already exists in the list.
					try
					{
						_con = DBManager.connect();
						_pStatement = _con.prepareStatement("SELECT * FROM Student WHERE AM IN(SELECT AM FROM StudentList WHERE AM=? AND LabName=?)");
						_pStatement.setString(1, String.valueOf(_studentLogged.getAM()));
						_pStatement.setString(2, String.valueOf(_labSelector.getSelectedItem()));
						_resSet = _pStatement.executeQuery();

						if (_resSet.next())
						{
							// If the student exists, then return error log.
							JOptionPane.showMessageDialog(null, "You have already registered in this laboratory.", "Registration exists", JOptionPane.ERROR_MESSAGE);
							return null;
						} // if
					} catch (SQLException | HeadlessException exc)
					{
						// If something is wrong, return the error log.
						JOptionPane.showMessageDialog(null, exc, "Error", JOptionPane.ERROR_MESSAGE);
						exc.printStackTrace();
					} // try
					finally
					{
						// Try to close all the possible open resources.
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

					// Insert the student in the laboratory he selected.
					try
					{
						_con = DBManager.connect();
						_pStatement = _con.prepareStatement("INSERT INTO StudentList(Name,LastName,AM,LabName,Hour,Semester) VALUES(?,?,?,?,?,?)");
						_pStatement.setString(1, _studentLogged.getName()); // Name
						_pStatement.setString(2, _studentLogged.getLastName()); // LastName
						_pStatement.setString(3, String.valueOf(_studentLogged.getAM())); // AM
						_pStatement.setString(4, String.valueOf(_labSelector.getSelectedItem())); // LabName
						_pStatement.setString(5, _hourLabBox.getSelectedItem().toString()); // Hour
						_pStatement.setString(6, _studentLogged.getSemester()); // Semester
						_pStatement.executeUpdate();

						_totalRecordField.setText(String.valueOf(Integer.valueOf(_totalRecordField.getText()) + 1));
						JOptionPane.showMessageDialog(null, "You were registered successfully in the laboratory: " + _hourLabBox.getSelectedItem().toString(), "Registered Successfully", JOptionPane.INFORMATION_MESSAGE);
					} catch (SQLException | HeadlessException exc)
					{
						JOptionPane.showMessageDialog(null, exc, "Error", JOptionPane.ERROR_MESSAGE);
						// If something is wrong, return the error log.
						exc.printStackTrace();
					} // try
					finally
					{
						// Try to close all the possible open resources.
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
					return null;
				} // doInBackground
			};
			worker.execute();
		}

		// If the action belongs to drop-down professor box
		if (e.getSource().equals(_profLabSelector))
		{
			// Select all the student records that were registered in the laboratory selected
			try
			{
				_con = DBManager.connect();
				_pStatement = _con.prepareStatement("SELECT Name,LastName,AM,Semester,Hour FROM StudentList WHERE LabName=?");
				_pStatement.setString(1, String.valueOf(_profLabSelector.getSelectedItem()));
				_resSet = _pStatement.executeQuery();

				// If result found, use the rs2xml library to pass the data into the JTable
				_labTable.setModel(DbUtils.resultSetToTableModel(_resSet));
				// Setting preferred width for the hour column
				_labTable.getColumn("Hour").setPreferredWidth(150);

			} catch (SQLException | HeadlessException exc)
			{
				// If something is wrong, return the error log.
				exc.printStackTrace();
			} // try 
			finally
			{
				// Try to close all the possible open resources.
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
		} // if

		// If the action belongs to _cancelButton
		if (e.getSource().equals(_cancelButton))
		{
			System.exit(0);
		} // if

		// If the action belongs to _cancel2Button
		if (e.getSource().equals(_cancel2Button))
		{
			JOptionPane.showMessageDialog(null, "Logged out successfully.", "Logged out", JOptionPane.INFORMATION_MESSAGE);
			System.exit(0);
		} // if
	} // actionListener
} // ButtonListener
