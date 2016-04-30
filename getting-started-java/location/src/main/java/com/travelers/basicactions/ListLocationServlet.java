/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.travelers.basicactions;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.travelers.daos.CloudSqlDao;
import com.travelers.daos.DatastoreDao;
import com.travelers.daos.LocationDao;
import com.travelers.location.objects.Location;
import com.travelers.location.objects.Result;
import com.travelers.util.CloudStorageHelper;

// [START example]
// a url pattern of "" makes this servlet the root servlet
@WebServlet(name = "list", urlPatterns = {"", "/locations"}, loadOnStartup = 1)
@SuppressWarnings("serial")
public class ListLocationServlet extends HttpServlet {

  private static final Logger logger = Logger.getLogger(ListLocationServlet.class.getName());

  @Override
  public void init() throws ServletException {
    LocationDao dao = null;
    CloudStorageHelper storageHelper = new CloudStorageHelper();

    // Creates the DAO based on the Context Parameters
    String storageType = this.getServletContext().getInitParameter("location.storageType");
    switch (storageType) {
      case "datastore":
        dao = new DatastoreDao();
        break;
      case "cloudsql":
        try {
          dao = new CloudSqlDao(this.getServletContext().getInitParameter("sql.url"));
        } catch (SQLException e) {
          throw new ServletException("SQL error", e);
        }
        break;
      default:
        throw new IllegalStateException(
            "Invalid storage type. Check if location.storageType property is set.");
    }
    this.getServletContext().setAttribute("dao", dao);
    this.getServletContext().setAttribute("storageHelper", storageHelper);
    this.getServletContext().setAttribute(
        "isAuthConfigured",            // Hide login when auth is not configured.
        !Strings.isNullOrEmpty(getServletContext().getInitParameter("location.clientID")));
    this.getServletContext().setAttribute(
        "isCloudStorageConfigured",    // Hide upload when Cloud Storage is not configured.
        !Strings.isNullOrEmpty(getServletContext().getInitParameter("location.bucket")));
  }

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,
      ServletException {
    LocationDao dao = (LocationDao) this.getServletContext().getAttribute("dao");
    String startCursor = req.getParameter("cursor");
    List<Location> locations = null;
    String endCursor = null;
    try {
      Result<Location> result = dao.listLocations(startCursor);
      logger.log(Level.INFO, "Retrieved list of all locations");
      locations = result.result;
      endCursor = result.cursor;
    } catch (Exception e) {
      throw new ServletException("Error listing locations", e);
    }
    req.getSession().getServletContext().setAttribute("locations", locations);
    StringBuilder locationNames = new StringBuilder();
    for (Location location : locations) {
      locationNames.append(location.getUuid() + " ");
    }
    req.setAttribute("cursor", endCursor);
    req.setAttribute("page", "list");
    
    logger.log(Level.INFO, "Loaded locations: " + locationNames.toString());
    logger.log(Level.INFO, "Content Type: " + req.getContentType());
    if (req.getContentType() != null && "application/json".equalsIgnoreCase(req.getContentType())
    		|| "json".equalsIgnoreCase(req.getParameter("format"))) {
    	//Use GSon to convert your objects to a String.
    	resp.getWriter().write(new Gson().toJson(locations));
    	resp.flushBuffer();
    } else {
	    req.getRequestDispatcher("/base.jsp").forward(req, resp);
    }
  }
}
// [END example]
