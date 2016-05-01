/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.travelers.daos;

import org.apache.commons.dbcp2.BasicDataSource;

import com.travelers.objects.Employee;
import com.travelers.objects.Result;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// [START example]
public class CloudSqlDao implements EmployeeDao {
// [START constructor]
  private static final BasicDataSource dataSource = new BasicDataSource();

  /**
   * A data access object for Employee using a Google Cloud SQL server for storage.
   */
  public CloudSqlDao(final String url) throws SQLException {

    dataSource.setUrl(url);
    final String createTableSql = "CREATE TABLE IF NOT EXISTS employees ( id INT NOT NULL "
        + "AUTO_INCREMENT, createdBy VARCHAR(255), createdById VARCHAR(255), "
        + "employeeLastName VARCHAR(255), employeeFirstName VARCHAR(255), phoneNumber VARCHAR(255), building VARCHAR(255), "
        + "location VARCHAR(255), "
        + "PRIMARY KEY (id))";

    try (Connection conn = dataSource.getConnection()) {
      conn.createStatement().executeUpdate(createTableSql);
    }
  }
// [END constructor]
// [START create]
  @Override
  public Long createEmployee(Employee employee) throws SQLException {
    final String createEmployeeString = "INSERT INTO employees "
        + "(createdBy, createdById, employeeLastName, employeeFirstName, phoneNumber, building, location) "
        + "VALUES (?, ?, ?, ?, ?, ?, ?)";
    try (Connection conn = dataSource.getConnection();
        final PreparedStatement createEmployeeStmt = conn.prepareStatement(createEmployeeString,
            Statement.RETURN_GENERATED_KEYS)) {
      createEmployeeStmt.setString(1, employee.getCreatedBy());
      createEmployeeStmt.setString(2, employee.getCreatedById());
      createEmployeeStmt.setString(3, employee.getEmployeeLastName());
      createEmployeeStmt.setString(4, employee.getEmployeeFirstName());
      createEmployeeStmt.setString(5, employee.getPhoneNumber());
      createEmployeeStmt.setString(6, employee.getBuilding());
      createEmployeeStmt.setString(7, employee.getLocation());
      createEmployeeStmt.executeUpdate();
      try (ResultSet keys = createEmployeeStmt.getGeneratedKeys()) {
        keys.next();
        return keys.getLong(1);
      }
    }
  }
// [END create]
// [START read]
  @Override
  public Employee readEmployee(Long employeeId) throws SQLException {
    final String readEmployeeString = "SELECT * FROM employees WHERE id = ?";
    try (Connection conn = dataSource.getConnection();
        PreparedStatement readEmployeeStmt = conn.prepareStatement(readEmployeeString)) {
      readEmployeeStmt.setLong(1, employeeId);
      try (ResultSet keys = readEmployeeStmt.executeQuery()) {
        keys.next();
        return new Employee.Builder()
            .id(keys.getLong(Employee.ID))
            .createdBy(keys.getString(Employee.CREATED_BY))
            .createdById(keys.getString(Employee.CREATED_BY_ID))
      	  	.employeeLastName(keys.getString(Employee.EMPLOYEE_LAST_NAME))
      	  	.employeeFirstName(keys.getString(Employee.EMPLOYEE_FIRST_NAME))
      	  	.phoneNumber(keys.getString(Employee.PHONE_NUMBER))
      	  	.building(keys.getString(Employee.BUILDING))
      	  	.location(keys.getString(Employee.LOCATION))
            .build();
      }
    }
  }
// [END read]
// [START update]
  @Override
  public void updateEmployee(Employee employee) throws SQLException {
    final String updateEmployeeString = "UPDATE employees SET createdBy = ?, createdById = ?, employeeLastName = ?,"
    		+ " employeeFirstName = ?, phoneNumber = ?, building = ?, location = ? WHERE id = ?";
    try (Connection conn = dataSource.getConnection();
        PreparedStatement updateEmployeeStmt = conn.prepareStatement(updateEmployeeString)) {
    	updateEmployeeStmt.setString(1, employee.getCreatedBy());
    	updateEmployeeStmt.setString(2, employee.getCreatedById());
		updateEmployeeStmt.setString(3, employee.getEmployeeLastName());
		updateEmployeeStmt.setString(4, employee.getEmployeeFirstName());
	  	updateEmployeeStmt.setString(5, employee.getPhoneNumber());
	  	updateEmployeeStmt.setString(6, employee.getBuilding());
	  	updateEmployeeStmt.setString(7, employee.getLocation());
    	updateEmployeeStmt.setLong(8, employee.getId());
      updateEmployeeStmt.executeUpdate();
    }
  }
// [END update]
// [START delete]
  @Override
  public void deleteEmployee(Long employeeId) throws SQLException {
    final String deleteEmployeeString = "DELETE FROM employees WHERE id = ?";
    try (Connection conn = dataSource.getConnection();
        PreparedStatement deleteEmployeeStmt = conn.prepareStatement(deleteEmployeeString)) {
      deleteEmployeeStmt.setLong(1, employeeId);
      deleteEmployeeStmt.executeUpdate();
    }
  }
// [END delete]
// [START listemployees]
  @Override
  public Result<Employee> listEmployees(String cursor) throws SQLException {
    int offset = 0;
    if (cursor != null && !cursor.equals("")) {
      offset = Integer.parseInt(cursor);
    }
    final String listEmployeesString = "SELECT SQL_CALC_FOUND_ROWS id, createdBy, createdById, "
    		+ "employeeLastName, employeeFirstName, phoneNumber, building, location"
    		+ " FROM employees ORDER BY employeeLastName ASC, employeeFirstName ASC "
        + "LIMIT 100 OFFSET ?";
    try (Connection conn = dataSource.getConnection();
        PreparedStatement listEmployeesStmt = conn.prepareStatement(listEmployeesString)) {
      listEmployeesStmt.setInt(1, offset);
      List<Employee> resultEmployees = new ArrayList<>();
      try (ResultSet rs = listEmployeesStmt.executeQuery()) {
        while (rs.next()) {
          Employee employee = new Employee.Builder()
			.id(rs.getLong(Employee.ID))
			.createdBy(rs.getString(Employee.CREATED_BY))
			.createdById(rs.getString(Employee.CREATED_BY_ID))
			.employeeLastName(rs.getString(Employee.EMPLOYEE_LAST_NAME))
			.employeeFirstName(rs.getString(Employee.EMPLOYEE_FIRST_NAME))
			.phoneNumber(rs.getString(Employee.PHONE_NUMBER))
			.building(rs.getString(Employee.BUILDING))
			.location(rs.getString(Employee.LOCATION))
            .build();
          resultEmployees.add(employee);
        }
      }
      try (ResultSet rs = conn.createStatement().executeQuery("SELECT FOUND_ROWS()")) {
        int totalNumRows = 0;
        if (rs.next()) {
          totalNumRows = rs.getInt(1);
        }
        if (totalNumRows > offset + 100) {
          return new Result<>(resultEmployees, Integer.toString(offset + 100));
        } else {
          return new Result<>(resultEmployees);
        }
      }
    }
  }
// [END listemployees]
// [START listbyuser]
  @Override
  public Result<Employee> listEmployeesByType(String employeeType, String startCursor) throws SQLException {
    int offset = 0;
    if (startCursor != null && !startCursor.equals("")) {
      offset = Integer.parseInt(startCursor);
    }
    final String listEmployeesString = "SELECT SQL_CALC_FOUND_ROWS  id, createdBy, createdById, "
    		+ "employeeLastName, employeeFirstName, phoneNumber, building, location FROM employees WHERE employeeType = ? "
        + "ORDER BY employeeLastName ASC, employeeFirstName ASC LIMIT 100 OFFSET ?";
    try (Connection conn = dataSource.getConnection();
        PreparedStatement listEmployeesStmt = conn.prepareStatement(listEmployeesString)) {
      listEmployeesStmt.setString(1, employeeType);
      listEmployeesStmt.setInt(2, offset);
      List<Employee> resultEmployees = new ArrayList<>();
      try (ResultSet rs = listEmployeesStmt.executeQuery()) {
        while (rs.next()) {
          Employee employee = new Employee.Builder()
				.id(rs.getLong(Employee.ID))
		        .createdBy(rs.getString(Employee.CREATED_BY))
		        .createdById(rs.getString(Employee.CREATED_BY_ID))
				.employeeLastName(rs.getString(Employee.EMPLOYEE_LAST_NAME))
				.employeeFirstName(rs.getString(Employee.EMPLOYEE_FIRST_NAME))
				.phoneNumber(rs.getString(Employee.PHONE_NUMBER))
				.building(rs.getString(Employee.BUILDING))
				.location(rs.getString(Employee.LOCATION))
              .build();
          resultEmployees.add(employee);
        }
      }
      try (ResultSet rs = conn.createStatement().executeQuery("SELECT FOUND_ROWS()")) {
        int totalNumRows = 0;
        if (rs.next()) {
          totalNumRows = rs.getInt(1);
        }
        if (totalNumRows > offset + 100) {
          return new Result<>(resultEmployees, Integer.toString(offset + 100));
        } else {
          return new Result<>(resultEmployees);
        }
      }
    }
  }
// [END listbyuser]
}
// [END example]
