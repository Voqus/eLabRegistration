package org.labreg;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import org.labreg.frames.LoginFrame;

/**
 * @author Voqus
 */
public class Launcher
{
	// Main method of the program
	public static void main(String[] args)
	{
		// try to change the look and feel to nimbus
		try
		{
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
			{
				if ("Nimbus".equals(info.getName()))
				{
					UIManager.setLookAndFeel(info.getClassName());
					break;
				} // if
			} // for
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
		{
			// If something is wrong, return the error log.
			e.printStackTrace();
		} // catch

		// Initialize the login frame GUI window
		SwingUtilities.invokeLater(() ->
		{
			new LoginFrame();
		}); // invokeLater
	} // main
}
