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

package com.travelers.basicactions;

import com.google.gson.Gson;
import com.travelers.daos.EmployeeDao;
import com.travelers.objects.Employee;
import com.travelers.objects.Result;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// [START example]
@SuppressWarnings("serial")
@WebServlet(name = "listbytype", value = "/employees/mine")
public class ListByTypeServlet extends HttpServlet {

  private static final Logger logger = Logger.getLogger(ListByTypeServlet.class.getName());

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,
        ServletException {
    EmployeeDao dao = (EmployeeDao) this.getServletContext().getAttribute("dao");
    String startCursor = req.getParameter("cursor");
    List<Employee> employees = null;
    String endCursor = null;
    try {
      Result<Employee> result =
          dao.listEmployeesByType((String) req.getSession().getAttribute("employeeType"), startCursor);
      employees = result.result;
      endCursor = result.cursor;
    } catch (Exception e) {
      throw new ServletException("Error listing employees", e);
    }
    req.getSession().getServletContext().setAttribute("employees", employees);
    StringBuilder employeeNames = new StringBuilder();
    for (Employee employee : employees) {
      employeeNames.append(employee.getEmployeeLastName() + "," + employee.getEmployeeFirstName() + "|");
    }
    logger.log(Level.INFO, "Loaded employees: |" + employeeNames.toString()
        + " for type " + (String) req.getSession().getAttribute("employeeType"));
    req.getSession().setAttribute("cursor", endCursor);
    req.getSession().setAttribute("page", "list");
    logger.log(Level.INFO, "Content Type: " + req.getContentType());
    if (req.getContentType() != null && "application/json".equalsIgnoreCase(req.getContentType())
    		|| "json".equalsIgnoreCase(req.getParameter("format"))) {
    	//Use GSon to convert your objects to a String.
    	resp.getWriter().write(new Gson().toJson(employees));
    	resp.flushBuffer();
    }
    else {
    	req.getRequestDispatcher("/base.jsp").forward(req, resp);
    }
  }
}
// [END example]
