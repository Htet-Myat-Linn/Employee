package models;

public class Employee {
	private int emp_id; 
	private String name;
	private String address;
	private long salary;
	
	public Employee() {}
	
	public Employee(int emp_id, String name, String address, long salary) {
		super();
		this.emp_id = emp_id;
		this.name = name;
		this.address = address;
		this.salary = salary;
	}
	public int getEmp_id() {
		return emp_id;
	}
	public void setEmp_id(int emp_id) {
		this.emp_id = emp_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public long getSalary() {
		return salary;
	}
	public void setSalary(long salary) {
		this.salary = salary;
	}
	
	
}
