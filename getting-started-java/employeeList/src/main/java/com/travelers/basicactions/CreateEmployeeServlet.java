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
// [START annotations]
@MultipartConfig
@WebServlet(name = "create", urlPatterns = {"/create"})
public class CreateEmployeeServlet extends HttpServlet {
// [END annotations]
  private static final Logger logger = Logger.getLogger(CreateEmployeeServlet.class.getName());

  // [START setup]
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
      IOException {
    req.setAttribute("action", "Add");          // Part of the Header in form.jsp
    req.setAttribute("destination", "create");  // The urlPattern to invoke (this Servlet)
    req.setAttribute("page", "form");           // Tells base.jsp to include form.jsp
    req.getRequestDispatcher("/base.jsp").forward(req, resp);
  }
  // [END setup]

  // [START formpost]
  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
      IOException {
    EmployeeDao dao = (EmployeeDao) this.getServletContext().getAttribute("dao");
    String createdByString = "";
    String createdByIdString = "";
    if (req.getSession().getAttribute("token") != null) { // Does the user have a logged in session?
      createdByString = (String) req.getSession().getAttribute("userEmail");
      createdByIdString = (String) req.getSession().getAttribute("userId");
    }

    
    Employee employee = new Employee.Builder()
        .createdBy(createdByString)
        .createdById(createdByIdString)
        .employeeLastName(req.getParameter(Employee.EMPLOYEE_LAST_NAME))
        .employeeFirstName(req.getParameter(Employee.EMPLOYEE_FIRST_NAME))
        .phoneNumber(req.getParameter(Employee.PHONE_NUMBER))
        .building(req.getParameter(Employee.BUILDING))
        .location(req.getParameter(Employee.LOCATION))
        .build();
    try {
      Long id = dao.createEmployee(employee);
      logger.log(Level.INFO, "Created employee {0}", employee);
      logger.log(Level.INFO, "Content Type: " + req.getContentType());
      if (req.getContentType() != null && "application/json".equalsIgnoreCase(req.getContentType())
      		|| "json".equalsIgnoreCase(req.getParameter("format"))) {
      	//Use GSon to convert your objects to a String.
      	resp.getWriter().write(new Gson().toJson(employee));
      	resp.flushBuffer();
      }
      else {
    	  resp.sendRedirect("/read?id=" + id.toString());   // read what we just wrote
      }
    } catch (Exception e) {
      throw new ServletException("Error creating employee", e);
    }
  }
  // [END formpost]
}
// [END example]
