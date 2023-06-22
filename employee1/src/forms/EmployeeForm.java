package forms;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.mysql.jdbc.PreparedStatement;

import config.DBConfig;
import models.Employee;
import services.EmployeeService;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class EmployeeForm {

	public JFrame frmEmployeeEntry;
	private JTextField txtName;
	private JTextField txtAddress;
	private JTextField txtSearch;
	private EmployeeService employeeService;
	private Employee employee;
	private JTextField txtSalary;
	private JTable tblEmployee;
	private DefaultTableModel dtm = new DefaultTableModel();

	private final DBConfig dbConfig = new DBConfig();
	private JButton btnShowAll;
	private JButton btnUpdate;
	public int r;
	private JButton btnDelete;
	private JButton btnSearch;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EmployeeForm window = new EmployeeForm();
					window.frmEmployeeEntry.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public EmployeeForm() {
		initialize();
		this.initializeDependency();
		this.setTableDesign();
        this.loadAllEmployees();
	}

	private void initializeDependency() {
		this.employeeService = new EmployeeService();
	}

	private void setTableDesign() {
		dtm.addColumn("ID");
		dtm.addColumn("Name");
		dtm.addColumn("Address");
		dtm.addColumn("Salary");
		this.tblEmployee.setModel(dtm);
	}


	private void loadAllEmployees() {
		this.dtm = (DefaultTableModel) this.tblEmployee.getModel();
		this.dtm.getDataVector().removeAllElements();
		this.dtm.fireTableDataChanged();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

            String query = "SELECT * FROM emp";

            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
            	Object[] dataRow = {
            			rs.getInt("emp_id"),
            			rs.getString("name"),
            			rs.getString("address"),
            			rs.getString("salary")
            	};
            	
            	dtm.addRow(dataRow);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

		this.tblEmployee.setModel(dtm);
	}

	private void resetFormData() {
		txtName.setText("");
		txtAddress.setText("");
		txtSalary.setText("");
	}
	private void loadAllEmployees1() {
	    this.dtm = (DefaultTableModel) this.tblEmployee.getModel();
	    this.dtm.getDataVector().removeAllElements();
	    this.dtm.fireTableDataChanged();
	    String searchValue = txtSearch.getText().trim();
	    try (PreparedStatement ps = (PreparedStatement) this.dbConfig.getConnection().prepareStatement("SELECT * FROM emp WHERE name LIKE ?")) {
	        ps.setString(1, searchValue + "%");
	        ResultSet rs = ps.executeQuery();
	        while (rs.next()) {
	            Object[] dataRow = {
	                rs.getInt("emp_id"),
	                rs.getString("name"),
	                rs.getString("address"),
	                rs.getString("salary")
	            };
	            dtm.addRow(dataRow);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    this.tblEmployee.setModel(dtm);
	}


	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmEmployeeEntry = new JFrame();
		frmEmployeeEntry.setTitle("Employee Entry");
		frmEmployeeEntry.setBounds(100, 100, 1000, 500);
		frmEmployeeEntry.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmEmployeeEntry.getContentPane().setLayout(null);

		JLabel lblName = new JLabel("Name");
		lblName.setHorizontalAlignment(SwingConstants.LEFT);
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblName.setBounds(47, 39, 85, 29);
		frmEmployeeEntry.getContentPane().add(lblName);

		txtName = new JTextField();
		txtName.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtName.setColumns(10);
		txtName.setBounds(47, 78, 193, 29);
		frmEmployeeEntry.getContentPane().add(txtName);

		JLabel lblAddress = new JLabel("Address");
		lblAddress.setHorizontalAlignment(SwingConstants.LEFT);
		lblAddress.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblAddress.setBounds(47, 126, 85, 29);
		frmEmployeeEntry.getContentPane().add(lblAddress);

		txtAddress = new JTextField();
		txtAddress.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtAddress.setColumns(10);
		txtAddress.setBounds(47, 165, 193, 29);
		frmEmployeeEntry.getContentPane().add(txtAddress);

		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Employee employee = new Employee();
				employee.setName(txtName.getText());
				employee.setAddress(txtAddress.getText());
				employee.setSalary(Long.parseLong(txtSalary.getText()));

				if (!employee.getName().isBlank() && !employee.getAddress().isBlank() && employee.getSalary() != 0) {

					employeeService.createEmployee(employee);
					resetFormData();
					loadAllEmployees();

				} else {
					JOptionPane.showMessageDialog(null, "Enter Required Field");
				}
			}
		});
		btnSave.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnSave.setBounds(47, 309, 193, 29);
		frmEmployeeEntry.getContentPane().add(btnSave);

		txtSearch = new JTextField();
		txtSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				loadAllEmployees1();
			}
		});
		txtSearch.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				btnSearch.setEnabled(true);
			}
		});
		txtSearch.setToolTipText("");
		txtSearch.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtSearch.setColumns(10);
		txtSearch.setBounds(575, 78, 165, 29);
		frmEmployeeEntry.getContentPane().add(txtSearch);

		 btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadAllEmployees1();
				txtSearch.selectAll();
			}
		});
		btnSearch.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnSearch.setBounds(750, 78, 85, 29);
		btnSearch.setEnabled(false);
		frmEmployeeEntry.getContentPane().add(btnSearch);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(276, 132, 662, 292);
		frmEmployeeEntry.getContentPane().add(scrollPane);

		tblEmployee = new JTable();
		tblEmployee.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				 r = tblEmployee.getSelectedRow();
                txtName.setText((String) tblEmployee.getValueAt(r, 1));
                txtAddress.setText((String) tblEmployee.getValueAt(r, 2));
                txtSalary.setText((String) tblEmployee.getValueAt(r, 3));
                btnSave.setEnabled(false);
                btnUpdate.setEnabled(true);
                btnDelete.setEnabled(true);
                
					
			}
		});
		tblEmployee.setFont(new Font("Tahoma", Font.PLAIN, 15));
		scrollPane.setViewportView(tblEmployee);

		JLabel lblSalary = new JLabel("Salary");
		lblSalary.setHorizontalAlignment(SwingConstants.LEFT);
		lblSalary.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblSalary.setBounds(47, 212, 85, 29);
		frmEmployeeEntry.getContentPane().add(lblSalary);

		txtSalary = new JTextField();
		txtSalary.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtSalary.setColumns(10);
		txtSalary.setBounds(47, 251, 193, 29);
		frmEmployeeEntry.getContentPane().add(txtSalary);
		
		btnShowAll = new JButton("Show All");
		btnShowAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadAllEmployees();	
				txtSearch.selectAll();
				btnSearch.setEnabled(false);
				}
		});
		btnShowAll.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnShowAll.setBounds(845, 78, 93, 29);
		frmEmployeeEntry.getContentPane().add(btnShowAll);
		
		btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//employeeService.updateEmployee((int)tblEmployee.getValueAt(r, 0),);
               String Name =txtName.getText();
               String Address=txtAddress.getText();
               long Salary=Long.parseLong(txtSalary.getText());
               int id= (int) tblEmployee.getValueAt(r, 0);
               employeeService.updateEmployee(id, Name, Address, Salary);
               loadAllEmployees();	
				txtSearch.selectAll();
				btnSave.setEnabled(true);
				btnUpdate.setEnabled(false);
				btnDelete.setEnabled(false);
				Clear();
               
			}
		});
		btnUpdate.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnUpdate.setBounds(47, 359, 193, 29);
		btnUpdate.setEnabled(false);
		frmEmployeeEntry.getContentPane().add(btnUpdate);
		
		btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(JOptionPane.showConfirmDialog(null,"Are you sure you want to delete?","Confrim",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION)
				{	
					int id= (int) tblEmployee.getValueAt(r, 0);
					 employeeService.DeleteEmployee(id);
					 loadAllEmployees();
					 btnDelete.setEnabled(false);
					 Clear();
				}
				
			}
		});
		btnDelete.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnDelete.setBounds(47, 405, 193, 29);
		btnDelete.setEnabled(false);
		frmEmployeeEntry.getContentPane().add(btnDelete);

	}
	public void Clear()
	{
		txtName.setText("");
		txtAddress.setText("");
		txtSalary.setText("");
		txtName.requestFocus(true);
	}
}
