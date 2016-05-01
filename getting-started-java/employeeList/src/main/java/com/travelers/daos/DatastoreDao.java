/*
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

import com.google.cloud.datastore.Cursor;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.OrderBy;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.travelers.objects.Employee;
import com.travelers.objects.Result;

import java.util.ArrayList;
import java.util.List;

// [START example]
public class DatastoreDao implements EmployeeDao {

// [START constructor]
  private Datastore datastore;
  private KeyFactory keyFactory;

  public DatastoreDao() {
    datastore = DatastoreOptions.defaultInstance().service(); // Authorized Datastore service object
    keyFactory = datastore.newKeyFactory().kind("Employee"); // Is used for creating keys later
  }
// [END constructor]
  // [START entityToEmployee]
  public Employee entityToEmployee(Entity entity) {
    return new Employee.Builder()                                     // Convert to Employee form
		.employeeLastName(entity.getString(Employee.EMPLOYEE_LAST_NAME))
		.employeeFirstName(entity.getString(Employee.EMPLOYEE_FIRST_NAME))
		.phoneNumber(entity.getString(Employee.PHONE_NUMBER))
		.building(entity.getString(Employee.BUILDING))
		.location(entity.getString(Employee.LOCATION))   
        .id(entity.key().id())
        .createdBy(entity.contains(Employee.CREATED_BY) ? entity.getString(Employee.CREATED_BY) : "")
        .createdById(
            entity.contains(Employee.CREATED_BY_ID) ? entity.getString(Employee.CREATED_BY_ID) : "")
        .build();
  }
  // [END entityToEmployee]
// [START create]
  @Override
  public Long createEmployee(Employee employee) {
    IncompleteKey key = keyFactory.newKey();          // Key will be assigned once written
    FullEntity<IncompleteKey> incEmployeeEntity = Entity.builder(key)  // Create the Entity
		.set(Employee.EMPLOYEE_LAST_NAME, employee.getEmployeeLastName())
		.set(Employee.EMPLOYEE_FIRST_NAME, employee.getEmployeeFirstName())
		.set(Employee.PHONE_NUMBER, employee.getPhoneNumber())
		.set(Employee.BUILDING, employee.getBuilding())
		.set(Employee.LOCATION, employee.getLocation())
        .set(Employee.CREATED_BY, employee.getCreatedBy())
        .set(Employee.CREATED_BY_ID, employee.getCreatedById())
        .build();
    Entity employeeEntity = datastore.add(incEmployeeEntity); // Save the Entity
    return employeeEntity.key().id();                     // The ID of the Key
  }
// [END create]
// [START read]
  @Override
  public Employee readEmployee(Long employeeId) {
    Entity employeeEntity = datastore.get(keyFactory.newKey(employeeId)); // Load an Entity for Key(id)
    return entityToEmployee(employeeEntity);
  }
// [END read]
// [START update]
  @Override
  public void updateEmployee(Employee employee) {
    Key key = keyFactory.newKey(employee.getId());  // From a employee, create a Key
    Entity entity = Entity.builder(key)         // Convert Employee to an Entity
    		.set(Employee.EMPLOYEE_LAST_NAME, employee.getEmployeeLastName())
    		.set(Employee.EMPLOYEE_FIRST_NAME, employee.getEmployeeFirstName())
    		.set(Employee.PHONE_NUMBER, employee.getPhoneNumber())
    		.set(Employee.BUILDING, employee.getBuilding())
    		.set(Employee.LOCATION, employee.getLocation())
            .set(Employee.CREATED_BY, employee.getCreatedBy())
            .set(Employee.CREATED_BY_ID, employee.getCreatedById())
        .build();
    datastore.update(entity);                   // Update the Entity
  }
// [END update]
// [START delete]
  @Override
  public void deleteEmployee(Long employeeId) {
    Key key = keyFactory.newKey(employeeId);        // Create the Key
    datastore.delete(key);                      // Delete the Entity
  }
// [END delete]
// [START entitiesToEmployees]
  public List<Employee> entitiesToEmployees(QueryResults<Entity> resultList) {
    List<Employee> resultEmployees = new ArrayList<>();
    while (resultList.hasNext()) {  // We still have data
      resultEmployees.add(entityToEmployee(resultList.next()));      // Add the Employee to the List
    }
    return resultEmployees;
  }
// [END entitiesToEmployees]
// [START listemployees]
  @Override
  public Result<Employee> listEmployees(String startCursorString) {
    Cursor startCursor = null;
    if (startCursorString != null && !startCursorString.equals("")) {
      startCursor = Cursor.fromUrlSafe(startCursorString);    // Where we left off
    }
    Query<Entity> query = Query.entityQueryBuilder()          // Build the Query
        .kind("Employee")                                         // We only care about Employees
        .limit(100)                                            // Only show 10 at a time
        .startCursor(startCursor)                             // Where we left off
        .orderBy(OrderBy.asc(Employee.EMPLOYEE_LAST_NAME)) // Use default Index "lastName" and "employeeFirstName"
        .build();
    QueryResults<Entity> resultList = datastore.run(query);   // Run the query
    List<Employee> resultEmployees = entitiesToEmployees(resultList);     // Retrieve and convert Entities
    Cursor cursor = resultList.cursorAfter();                 // Where to start next time
    if (cursor != null && resultEmployees.size() == 100) {         // Are we paging? Save Cursor
      String cursorString = cursor.toUrlSafe();               // Cursors are WebSafe
      return new Result<>(resultEmployees, cursorString);
    } else {
      return new Result<>(resultEmployees);
    }
  }
// [END listemployees]
// [START listbyuser]
  @Override
  public Result<Employee> listEmployeesByType(String employeeType, String startCursorString) {
    Cursor startCursor = null;
    if (startCursorString != null && !startCursorString.equals("")) {
      startCursor = Cursor.fromUrlSafe(startCursorString);    // Where we left off
    }
    Query<Entity> query = Query.entityQueryBuilder()          // Build the Query
        .kind("Employee")                                         // We only care about Employees
        //.filter(PropertyFilter.eq(Employee.EMPLOYEE_TYPE, employeeType))// Only for this employee type
        .limit(100)                                            // Only show 100 at a time
        .startCursor(startCursor)                             // Where we left off
        // a custom datastore index is required since you are filtering by one property
        // but ordering by another
        .orderBy(OrderBy.asc(Employee.EMPLOYEE_LAST_NAME))
        .build();
    QueryResults<Entity> resultList = datastore.run(query);   // Run the Query
    List<Employee> resultEmployees = entitiesToEmployees(resultList);     // Retrieve and convert Entities
    Cursor cursor = resultList.cursorAfter();                 // Where to start next time
    if (cursor != null && resultEmployees.size() == 100) {         // Are we paging? Save Cursor
      String cursorString = cursor.toUrlSafe();               // Cursors are WebSafe
      return new Result<>(resultEmployees, cursorString);
    } else {
      return new Result<>(resultEmployees);
    }
  }
// [END listbyuser]
}
// [END example]
