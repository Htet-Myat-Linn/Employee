package services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import config.DBConfig;
//import entities.Brand;
import models.Employee;

public class EmployeeService {
//    private final EmployeeMapper employeeMapper;
    private final DBConfig dbConfig;

    public EmployeeService() {
        this.dbConfig = new DBConfig();
    }

    public void createEmployee(Employee employee) {
        try {

            PreparedStatement ps = this.dbConfig.getConnection()
            		.prepareStatement("INSERT INTO emp (name, address, salary) VALUES (?, ?, ?)");

            ps.setString(1, employee.getName());
            ps.setString(2, employee.getAddress());
            ps.setInt(3, (int)employee.getSalary());            
            ps.executeUpdate();
            ps.close();

            
           
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
    public void updateEmployee(int id,String Name,String Address,long Salary) {
        try {

            PreparedStatement ps = this.dbConfig.getConnection()
                    .prepareStatement("UPDATE emp SET name =?, address=?, salary=?  WHERE emp_id = ?");

            ps.setString(1, Name);
            ps.setString(2, Address);
            ps.setLong(3, Salary);           
            ps.setInt(4, id);
            ps.executeUpdate();
            ps.close();
            
            JOptionPane.showMessageDialog(null, "Successfully Update!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void DeleteEmployee(int id) {
        try {

            PreparedStatement ps = this.dbConfig.getConnection()
                    .prepareStatement("DELETE FROM emp WHERE emp_id = ?");
         
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
            
            JOptionPane.showMessageDialog(null, "Successfully Delete!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


   }
