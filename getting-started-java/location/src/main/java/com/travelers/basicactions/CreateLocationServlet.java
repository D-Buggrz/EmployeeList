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
import com.google.gson.GsonBuilder;
import com.travelers.daos.LocationDao;
import com.travelers.location.objects.Location;

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
public class CreateLocationServlet extends HttpServlet {
// [END annotations]
  private static final Logger logger = Logger.getLogger(CreateLocationServlet.class.getName());

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
    LocationDao dao = (LocationDao) this.getServletContext().getAttribute("dao");
    String createdByString = "";
    String createdByIdString = "";
    if (req.getSession().getAttribute("token") != null) { // Does the user have a logged in session?
      createdByString = (String) req.getSession().getAttribute("userEmail");
      createdByIdString = (String) req.getSession().getAttribute("userId");
    }

    
    Location location = new Location.Builder()
        .createdBy(createdByString)
        .createdById(createdByIdString)
        .uuid(req.getParameter(Location.UUID))
        .locationName(req.getParameter(Location.LOCATION_NAME))
        .description(req.getParameter(Location.DESCRIPTION))
        .conferenceRooms(req.getParameter(Location.CONFERENCE_ROOMS))
        .locationType(req.getParameter(Location.LOCATION_TYPE))
        .currentMeetingAgenda(req.getParameter(Location.CURRENT_MEETING_AGENDA))
        .imageURLs(req.getParameter(Location.IMAGE_URLS))
        .videoConferencingEnabled(req.getParameter(Location.VIDEO_CONFERENCING_ENABLED))
        .roomCapacity(req.getParameter(Location.ROOM_CAPACITY))
        .build();
    try {
      Long id = dao.createLocation(location);
      logger.log(Level.INFO, "Created location {0}", location);
      logger.log(Level.INFO, "Content Type: " + req.getContentType());
      if (req.getContentType() != null && "application/json".equalsIgnoreCase(req.getContentType())
      		|| "json".equalsIgnoreCase(req.getParameter("format"))) {
      	//Use GSon to convert your objects to a String.
      	resp.getWriter().write(new Gson().toJson(location));
      	resp.flushBuffer();
      }
      else {
    	  resp.sendRedirect("/read?id=" + id.toString());   // read what we just wrote
      }
    } catch (Exception e) {
      throw new ServletException("Error creating location", e);
    }
  }
  // [END formpost]
}
// [END example]
