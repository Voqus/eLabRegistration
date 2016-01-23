package org.labreg.base;

/**
 * @author Voqus
 */
public class User
{

	protected String name;
	protected String lastName;

	public User(final String name, final String lastName)
	{
		this.name = name;
		this.lastName = lastName;
	} // Constructor

	public User()
	{} // Default Constructor

	/**
	 * Sets the name of the user
	 * 
	 * @param name
	 */
	public void setName(final String name)
	{
		this.name = name;
	} // setName

	/**
	 * Returns the name of the user
	 * 
	 * @return
	 */
	public String getName()
	{
		return name;
	} // getName

	/**
	 * Sets the last name of the user
	 * 
	 * @param lastName
	 */
	public void setLastName(final String lastName)
	{
		this.lastName = lastName;
	} // setLastName

	/**
	 * Returns the last name of the user
	 * 
	 * @return
	 */
	public String getLastName()
	{
		return lastName;
	} // getLastName
} // User
