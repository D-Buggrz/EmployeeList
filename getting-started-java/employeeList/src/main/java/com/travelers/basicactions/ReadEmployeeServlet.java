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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// [START example]
@SuppressWarnings("serial")
@WebServlet(name = "read", value = "/read")
public class ReadEmployeeServlet extends HttpServlet {
// [START init]
  private final Logger logger = Logger.getLogger(ReadEmployeeServlet.class.getName());
// [END init]
  
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,
      ServletException {
    Long id = Long.decode(req.getParameter("id"));
    EmployeeDao dao = (EmployeeDao) this.getServletContext().getAttribute("dao");
    try {
    	Employee employee = dao.readEmployee(id);
      // [START log]
      logger.log(Level.INFO, "Read employee with id {0}", id);
      // [END log]
      req.setAttribute("employee", employee);
      req.setAttribute("page", "view");
      if (req.getContentType() != null && "application/json".equalsIgnoreCase(req.getContentType())
      		|| "json".equalsIgnoreCase(req.getParameter("format"))) {
      	//Use GSon to convert your objects to a String.
      	resp.getWriter().write(new Gson().toJson(employee));
      	resp.flushBuffer();
      }
      else {
    	  req.getRequestDispatcher("/base.jsp").forward(req, resp);
      }
    } catch (Exception e) {
      throw new ServletException("Error reading employee", e);
    }
  }
}
// [END example]
