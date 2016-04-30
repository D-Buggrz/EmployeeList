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

import com.travelers.location.objects.Location;
import com.travelers.location.objects.Result;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// [START example]
public class CloudSqlDao implements LocationDao {
// [START constructor]
  private static final BasicDataSource dataSource = new BasicDataSource();

  /**
   * A data access object for Locationshelf using a Google Cloud SQL server for storage.
   */
  public CloudSqlDao(final String url) throws SQLException {

    dataSource.setUrl(url);
    final String createTableSql = "CREATE TABLE IF NOT EXISTS locations ( id INT NOT NULL "
        + "AUTO_INCREMENT, createdBy VARCHAR(255), createdById VARCHAR(255), "
        + "uuid VARCHAR(255), locationName VARCHAR(255), description VARCHAR(255), conferenceRooms VARCHAR(255), "
        + "locationType VARCHAR(255), currentMeetingAgenda VARCHAR(255), imageURLs VARCHAR(255), videoConferencingEnabled VARCHAR(255), "
        + "roomCapacity VARCHAR(255), "
        + "PRIMARY KEY (id))";

    try (Connection conn = dataSource.getConnection()) {
      conn.createStatement().executeUpdate(createTableSql);
    }
  }
// [END constructor]
// [START create]
  @Override
  public Long createLocation(Location location) throws SQLException {
    final String createLocationString = "INSERT INTO locations "
        + "(createdBy, createdById, uuid, locationName, description, conferenceRooms, locationType, currentMeetingAgenda, imageURLs, videoConferencingEnabled, roomCapacity) "
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    try (Connection conn = dataSource.getConnection();
        final PreparedStatement createLocationStmt = conn.prepareStatement(createLocationString,
            Statement.RETURN_GENERATED_KEYS)) {
      createLocationStmt.setString(1, location.getCreatedBy());
      createLocationStmt.setString(2, location.getCreatedById());
      createLocationStmt.setString(3, location.getUuid());
      createLocationStmt.setString(4, location.getLocationName());
      createLocationStmt.setString(5, location.getDescription());
      createLocationStmt.setString(6, location.getConferenceRooms());
      createLocationStmt.setString(7, location.getLocationType());
      createLocationStmt.setString(8, location.getCurrentMeetingAgenda());
      createLocationStmt.setString(9, location.getImageURLs());
      createLocationStmt.setString(10, location.getVideoConferencingEnabled());
      createLocationStmt.setString(11, location.getRoomCapacity());
      createLocationStmt.executeUpdate();
      try (ResultSet keys = createLocationStmt.getGeneratedKeys()) {
        keys.next();
        return keys.getLong(1);
      }
    }
  }
// [END create]
// [START read]
  @Override
  public Location readLocation(Long locationId) throws SQLException {
    final String readLocationString = "SELECT * FROM locations WHERE id = ?";
    try (Connection conn = dataSource.getConnection();
        PreparedStatement readLocationStmt = conn.prepareStatement(readLocationString)) {
      readLocationStmt.setLong(1, locationId);
      try (ResultSet keys = readLocationStmt.executeQuery()) {
        keys.next();
        return new Location.Builder()
            .id(keys.getLong(Location.ID))
            .createdBy(keys.getString(Location.CREATED_BY))
            .createdById(keys.getString(Location.CREATED_BY_ID))
            .uuid(keys.getString(Location.UUID))
            .locationName(keys.getString(Location.LOCATION_NAME))
            .description(keys.getString(Location.DESCRIPTION))
            .conferenceRooms(keys.getString(Location.CONFERENCE_ROOMS))
            .locationType(keys.getString(Location.LOCATION_TYPE))
            .currentMeetingAgenda(keys.getString(Location.CURRENT_MEETING_AGENDA))
            .imageURLs(keys.getString(Location.IMAGE_URLS))
            .videoConferencingEnabled(keys.getString(Location.VIDEO_CONFERENCING_ENABLED))
            .roomCapacity(keys.getString(Location.ROOM_CAPACITY))
            .build();
      }
    }
  }
// [END read]
// [START update]
  @Override
  public void updateLocation(Location location) throws SQLException {
    final String updateLocationString = "UPDATE locations SET createdBy = ?, createdById = ?, uuid = ?,"
    		+ " locationName = ?, description = ?, conferenceRooms = ?, locationType = ?, currentMeetingAgenda = ?,"
    		+ " imageURLs = ?, videoConferencingEnabled = ?, roomCapacity = ? WHERE id = ?";
    try (Connection conn = dataSource.getConnection();
        PreparedStatement updateLocationStmt = conn.prepareStatement(updateLocationString)) {
    	updateLocationStmt.setString(1, location.getCreatedBy());
    	updateLocationStmt.setString(2, location.getCreatedById());
    	updateLocationStmt.setString(3, location.getUuid());
    	updateLocationStmt.setString(4, location.getLocationName());
    	updateLocationStmt.setString(5, location.getDescription());
    	updateLocationStmt.setString(6, location.getConferenceRooms());
    	updateLocationStmt.setString(7, location.getLocationType());
    	updateLocationStmt.setString(8, location.getCurrentMeetingAgenda());
    	updateLocationStmt.setString(9, location.getImageURLs());
    	updateLocationStmt.setString(10, location.getVideoConferencingEnabled());
    	updateLocationStmt.setString(11, location.getRoomCapacity());
    	updateLocationStmt.setLong(12, location.getId());
      updateLocationStmt.executeUpdate();
    }
  }
// [END update]
// [START delete]
  @Override
  public void deleteLocation(Long locationId) throws SQLException {
    final String deleteLocationString = "DELETE FROM locations WHERE id = ?";
    try (Connection conn = dataSource.getConnection();
        PreparedStatement deleteLocationStmt = conn.prepareStatement(deleteLocationString)) {
      deleteLocationStmt.setLong(1, locationId);
      deleteLocationStmt.executeUpdate();
    }
  }
// [END delete]
// [START listlocations]
  @Override
  public Result<Location> listLocations(String cursor) throws SQLException {
    int offset = 0;
    if (cursor != null && !cursor.equals("")) {
      offset = Integer.parseInt(cursor);
    }
    final String listLocationsString = "SELECT SQL_CALC_FOUND_ROWS id, createdBy, createdById, "
    		+ "uuid, locationName, description, conferenceRooms, locationType, currentMeetingAgenda, "
    		+ "imageURLs, videoConferencingEnabled, roomCapacity"
    		+ " FROM locations ORDER BY locationType ASC, locationName ASC "
        + "LIMIT 10 OFFSET ?";
    try (Connection conn = dataSource.getConnection();
        PreparedStatement listLocationsStmt = conn.prepareStatement(listLocationsString)) {
      listLocationsStmt.setInt(1, offset);
      List<Location> resultLocations = new ArrayList<>();
      try (ResultSet rs = listLocationsStmt.executeQuery()) {
        while (rs.next()) {
          Location location = new Location.Builder()
	          .id(rs.getLong(Location.ID))
	          .createdBy(rs.getString(Location.CREATED_BY))
	          .createdById(rs.getString(Location.CREATED_BY_ID))
	          .uuid(rs.getString(Location.UUID))
	          .locationName(rs.getString(Location.LOCATION_NAME))
	          .description(rs.getString(Location.DESCRIPTION))
	          .conferenceRooms(rs.getString(Location.CONFERENCE_ROOMS))
	          .locationType(rs.getString(Location.LOCATION_TYPE))
	          .currentMeetingAgenda(rs.getString(Location.CURRENT_MEETING_AGENDA))
	          .imageURLs(rs.getString(Location.IMAGE_URLS))
	          .videoConferencingEnabled(rs.getString(Location.VIDEO_CONFERENCING_ENABLED))
	          .roomCapacity(rs.getString(Location.ROOM_CAPACITY))
              .build();
          resultLocations.add(location);
        }
      }
      try (ResultSet rs = conn.createStatement().executeQuery("SELECT FOUND_ROWS()")) {
        int totalNumRows = 0;
        if (rs.next()) {
          totalNumRows = rs.getInt(1);
        }
        if (totalNumRows > offset + 10) {
          return new Result<>(resultLocations, Integer.toString(offset + 10));
        } else {
          return new Result<>(resultLocations);
        }
      }
    }
  }
// [END listlocations]
// [START listbyuser]
  @Override
  public Result<Location> listLocationsByType(String locationType, String startCursor) throws SQLException {
    int offset = 0;
    if (startCursor != null && !startCursor.equals("")) {
      offset = Integer.parseInt(startCursor);
    }
    final String listLocationsString = "SELECT SQL_CALC_FOUND_ROWS  id, createdBy, createdById, "
    		+ "uuid, locationName, description, conferenceRooms, locationType, currentMeetingAgenda, "
    		+ "imageURLs, videoConferencingEnabled, roomCapacity FROM locations WHERE locationType = ? "
        + "ORDER BY locationType ASC, locationName ASC LIMIT 10 OFFSET ?";
    try (Connection conn = dataSource.getConnection();
        PreparedStatement listLocationsStmt = conn.prepareStatement(listLocationsString)) {
      listLocationsStmt.setString(1, locationType);
      listLocationsStmt.setInt(2, offset);
      List<Location> resultLocations = new ArrayList<>();
      try (ResultSet rs = listLocationsStmt.executeQuery()) {
        while (rs.next()) {
          Location location = new Location.Builder()
			  .id(rs.getLong(Location.ID))
	          .createdBy(rs.getString(Location.CREATED_BY))
	          .createdById(rs.getString(Location.CREATED_BY_ID))
	          .uuid(rs.getString(Location.UUID))
	          .locationName(rs.getString(Location.LOCATION_NAME))
	          .description(rs.getString(Location.DESCRIPTION))
	          .conferenceRooms(rs.getString(Location.CONFERENCE_ROOMS))
	          .locationType(rs.getString(Location.LOCATION_TYPE))
	          .currentMeetingAgenda(rs.getString(Location.CURRENT_MEETING_AGENDA))
	          .imageURLs(rs.getString(Location.IMAGE_URLS))
	          .videoConferencingEnabled(rs.getString(Location.VIDEO_CONFERENCING_ENABLED))
	          .roomCapacity(rs.getString(Location.ROOM_CAPACITY))
              .build();
          resultLocations.add(location);
        }
      }
      try (ResultSet rs = conn.createStatement().executeQuery("SELECT FOUND_ROWS()")) {
        int totalNumRows = 0;
        if (rs.next()) {
          totalNumRows = rs.getInt(1);
        }
        if (totalNumRows > offset + 10) {
          return new Result<>(resultLocations, Integer.toString(offset + 10));
        } else {
          return new Result<>(resultLocations);
        }
      }
    }
  }
// [END listbyuser]
}
// [END example]
