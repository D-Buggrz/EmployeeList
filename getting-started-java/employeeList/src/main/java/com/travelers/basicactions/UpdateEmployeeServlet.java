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

package com.travelers.basicactions;

import com.google.gson.Gson;
import com.travelers.daos.EmployeeDao;
import com.travelers.objects.Employee;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// [START example]
@SuppressWarnings("serial")
@MultipartConfig
@WebServlet(name = "update", value = "/update")
public class UpdateEmployeeServlet extends HttpServlet {

	  private static final Logger logger = Logger.getLogger(UpdateEmployeeServlet.class.getName());
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
      IOException {
    EmployeeDao dao = (EmployeeDao) this.getServletContext().getAttribute("dao");
    try {
      Employee employee = dao.readEmployee(Long.decode(req.getParameter("id")));
      req.setAttribute("employee", employee);
      req.setAttribute("action", "Edit");
      req.setAttribute("destination", "update");
      req.setAttribute("page", "form");
      req.getRequestDispatcher("/base.jsp").forward(req, resp);
    } catch (Exception e) {
      throw new ServletException("Error loading employee for editing", e);
    }
  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
      IOException {
    EmployeeDao dao = (EmployeeDao) this.getServletContext().getAttribute("dao");
    try {
      Employee oldEmployee = dao.readEmployee(Long.decode(req.getParameter("id")));
      Employee employee = new Employee.Builder()
    	.id(Long.decode(req.getParameter("id")))
        .createdBy(oldEmployee.getCreatedBy())
        .createdById(oldEmployee.getCreatedById())
  	  	.employeeLastName(req.getParameter(Employee.EMPLOYEE_LAST_NAME))
  	  	.employeeFirstName(req.getParameter(Employee.EMPLOYEE_FIRST_NAME))
  	  	.phoneNumber(req.getParameter(Employee.PHONE_NUMBER))
  	  	.building(req.getParameter(Employee.BUILDING))
  	  	.location(req.getParameter(Employee.LOCATION))
        .build();
      dao.updateEmployee(employee);
      logger.log(Level.INFO, "Content Type: " + req.getContentType());
      if (req.getContentType() != null && "application/json".equalsIgnoreCase(req.getContentType())
        		|| "json".equalsIgnoreCase(req.getParameter("format"))) {
        	//Use GSon to convert your objects to a String.
        	resp.getWriter().write(new Gson().toJson(employee));
        	resp.flushBuffer();
        }
        else {
        	resp.sendRedirect("/read?id=" + req.getParameter("id"));
        }
    } catch (Exception e) {
      throw new ServletException("Error updating employee", e);
    }
  }
}
// [END example]
