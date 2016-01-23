package org.labreg.base;

/**
 * @author Voqus
 */
public final class Student extends User
{

	/* Variable declaration -- START */
	private String semester;
	private int AM;
	/* Variable declaration -- END */

	public Student(final String name, final String lastName, final String semester, final int AM)
	{
		this.name = name;
		this.lastName = lastName;
		this.semester = semester;
		this.AM = AM;
	} // Constructor

	/**
	 * Sets the semester of the student
	 * 
	 * @param semester
	 */
	public void setSemester(final String semester)
	{
		this.semester = semester;
	} // setSemester

	/**
	 * Returns the semester of the student
	 * 
	 * @return
	 */
	public String getSemester()
	{
		return semester;
	} // getSemester

	/**
	 * Sets the registration number of the student
	 * 
	 * @param AM
	 */
	public void setAM(final int AM)
	{
		this.AM = AM;
	} // setAM

	/**
	 * Returns the registration number of the student
	 * 
	 * @return
	 */
	public int getAM()
	{
		return AM;
	} // getAM
} // Student
