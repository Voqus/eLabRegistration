package org.labreg.frames;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import org.labreg.actions.ButtonListener;

/**
 * @author Voqus
 */
public final class LoginFrame
{
	/* Variable declaration -- START */
	private static JTextField _usernameField;
	private static JPasswordField _passwordField;
	private static JButton connectButton, cancelButton;

	public static JFrame frame = null;
	/* Variable declaration -- END */

	public LoginFrame()
	{
		// Initialize the frame object and set the title to it
		frame = new JFrame("e-LabRegistration Application");
		init();
	} // Constructor

	/**
	 * Initializes the GUI of the application
	 */
	public void init()
	{
		frame.setLayout(new FlowLayout());

		// Setting a panel as the main panel of the application
		JPanel mainPanel = new JPanel();
		// Setting the layout of the panel to GridLayout of 3 rows and 3 columns
		mainPanel.setLayout(new GridLayout(3, 3));
		_usernameField = new JTextField(15);
		_passwordField = new JPasswordField(15);

		mainPanel.add(new JLabel("Username: "));
		mainPanel.add(_usernameField);
		mainPanel.add(new JLabel("Password: "));
		mainPanel.add(_passwordField);

		connectButton = new JButton("Connect");
		cancelButton = new JButton("Cancel");

		// Initialize the action listener and pass in the components to handle
		ButtonListener buttonListener = new ButtonListener(_usernameField, _passwordField, connectButton, cancelButton);
		connectButton.addActionListener(buttonListener);
		cancelButton.addActionListener(buttonListener);

		// Adding 2 buttons on the main panel
		mainPanel.add(connectButton);
		mainPanel.add(cancelButton);

		// Setting a border on the main panel
		mainPanel.setBorder(BorderFactory.createTitledBorder("Login application form:"));

		// Adding the mainPanel to the top-level container
		frame.add(mainPanel);
		frame.pack(); // do automatic resize of the window
		// Setting the minimum size of the window using the current size taken from pack method
		frame.setMinimumSize(new Dimension(frame.getWidth(), frame.getHeight()));
		// Setting default operation to close and terminate the process of the application
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // Set the frame to the center of the screen
		frame.setLocationRelativeTo(null);
		// Set the frame's visibility to true
		frame.setVisible(true);
	} // init
} // LoginFrame
