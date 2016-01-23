package org.labreg.base;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Voqus
 */
public final class Professor extends User
{

	/* Variable declaration -- START */
	public List<String> laboratoryList;
	/* Variable declaration -- END */

	public Professor(final String name, final String lastName)
	{
		this.name = name;
		this.lastName = lastName;

		// Initialize the list
		laboratoryList = new ArrayList<>();
	} // Constructor

	/**
	 * Adds a laboratory to the list and returns <code>true</code>/ <code>false</code> if it succeeded or not.
	 *
	 * @param lab
	 * @return
	 */
	public synchronized boolean addLaboratory(final String lab)
	{
		return laboratoryList.add(lab);
	} // addLaboratory

	/**
	 * Clears the list of laboratories.
	 */
	public synchronized void clearLaboratories()
	{
		laboratoryList.clear();
	} // clearLaboratories
} // Professor
