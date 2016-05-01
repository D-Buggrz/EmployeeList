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

package com.travelers.objects;

// [START example]
public class Employee {
// [START employee]
  private String employeeLastName;
  private String employeeFirstName;
  private String phoneNumber;
  private String building;
  private String location;
  private Long id;
  private String createdBy;
  private String createdById;
  
// [END employee]
// [START keys]
  public static final String EMPLOYEE_LAST_NAME = "employeeLastName";
  public static final String EMPLOYEE_FIRST_NAME = "employeeFirstName";
  public static final String PHONE_NUMBER = "phoneNumber";
  public static final String BUILDING = "building";
  public static final String LOCATION = "location";
  public static final String ID = "id";
  public static final String CREATED_BY = "createdBy";
  public static final String CREATED_BY_ID = "createdById";
  
  
// [END keys]
// [START constructor]
  // We use a Builder pattern here to simplify and standardize construction of Employee objects.
  private Employee(Builder builder) {
    this.employeeLastName = builder.employeeLastName;
    this.employeeFirstName = builder.employeeFirstName;
    this.phoneNumber = builder.phoneNumber;
    this.building = builder.building;
    this.location = builder.location;
    this.id = builder.id;
    this.createdBy = builder.createdBy;
    this.createdById = builder.createdById;
  }
// [END constructor]
// [START builder]
  public static class Builder {
	  private String employeeLastName;
	  private String employeeFirstName;
	  private String phoneNumber;
	  private String building;
	  private String location;
	  private Long id;
	  private String createdBy;
	  private String createdById;

    public Builder employeeLastName(String employeeLastName) {
      this.employeeLastName = employeeLastName;
      return this;
    }
	public Builder employeeFirstName(String employeeFirstName) {
        this.employeeFirstName = employeeFirstName;
        return this;
    }
    
    public Builder phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }
    
    public Builder building(String building) {
    	this.building = building;
    	return this;
    }
    
    public Builder location(String location) {
        this.location = location;
        return this;
    }

    public Builder id(Long id) {
        this.id = id;
        return this;
      }
    
    public Builder createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }
    
    public Builder createdById(String createdById) {
        this.createdById = createdById;
        return this;
    }
    
    public Employee build() {
        return new Employee(this);
    }
    
  }

	public String getEmployeeLastName() {
	return employeeLastName;
}

	public void setEmployeeLastName(String employeeLastName) {
		this.employeeLastName = employeeLastName;
	}
	
	public String getEmployeeFirstName() {
		return employeeFirstName;
	}
	
	public void setEmployeeFirstName(String employeeFirstName) {
		this.employeeFirstName = employeeFirstName;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getBuilding() {
		return building;
	}
	
	public void setBuilding(String building) {
		this.building = building;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedById() {
		return createdById;
	}

	public void setCreatedById(String createdById) {
		this.createdById = createdById;
	}
	
// [END builder]
  @Override
  public String toString() {
    return
        "employeeLastName: " + employeeLastName + ", employeeFirstName: " + employeeFirstName + ", phoneNumber: " + phoneNumber
        + ", building: " + building.toString() + ", location: " + location
        + ", createdBy: " + createdBy+ ", createdById: " + createdById;
  }
}
// [END example]