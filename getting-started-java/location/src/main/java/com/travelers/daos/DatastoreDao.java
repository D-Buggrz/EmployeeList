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

import com.travelers.location.objects.Location;
import com.travelers.location.objects.Result;

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

import java.util.ArrayList;
import java.util.List;

// [START example]
public class DatastoreDao implements LocationDao {

// [START constructor]
  private Datastore datastore;
  private KeyFactory keyFactory;

  public DatastoreDao() {
    datastore = DatastoreOptions.defaultInstance().service(); // Authorized Datastore service object
    keyFactory = datastore.newKeyFactory().kind("Location"); // Is used for creating keys later
  }
// [END constructor]
  // [START entityToLocation]
  public Location entityToLocation(Entity entity) {
    return new Location.Builder()                                     // Convert to Location form
   		.uuid(entity.getString(Location.UUID))
   		.locationName(entity.getString(Location.LOCATION_NAME))
   		.description(entity.getString(Location.DESCRIPTION))
    	.conferenceRooms(entity.getString(Location.CONFERENCE_ROOMS))
    	.locationType(entity.getString(Location.LOCATION_TYPE))    	
        .currentMeetingAgenda(entity.getString(Location.CURRENT_MEETING_AGENDA))
        .imageURLs(entity.getString(Location.IMAGE_URLS))
        .videoConferencingEnabled(entity.getString(Location.VIDEO_CONFERENCING_ENABLED))
        .roomCapacity(entity.getString(Location.ROOM_CAPACITY))    
        .id(entity.key().id())
        .createdBy(entity.contains(Location.CREATED_BY) ? entity.getString(Location.CREATED_BY) : "")
        .createdById(
            entity.contains(Location.CREATED_BY_ID) ? entity.getString(Location.CREATED_BY_ID) : "")
        .build();
  }
  // [END entityToLocation]
// [START create]
  @Override
  public Long createLocation(Location location) {
    IncompleteKey key = keyFactory.newKey();          // Key will be assigned once written
    FullEntity<IncompleteKey> incLocationEntity = Entity.builder(key)  // Create the Entity
        .set(Location.UUID, location.getUuid())           // Add Property ("uuid", location.getUuid())
        .set(Location.LOCATION_NAME, location.getLocationName())
        .set(Location.DESCRIPTION, location.getDescription())
        .set(Location.CONFERENCE_ROOMS, location.getConferenceRooms())
        .set(Location.LOCATION_TYPE, location.getLocationType())
        .set(Location.CURRENT_MEETING_AGENDA, location.getCurrentMeetingAgenda())
        .set(Location.IMAGE_URLS, location.getImageURLs())
        .set(Location.VIDEO_CONFERENCING_ENABLED, location.getVideoConferencingEnabled())
        .set(Location.ROOM_CAPACITY, location.getRoomCapacity())
        .set(Location.CREATED_BY, location.getCreatedBy())
        .set(Location.CREATED_BY_ID, location.getCreatedById())
        .build();
    Entity locationEntity = datastore.add(incLocationEntity); // Save the Entity
    return locationEntity.key().id();                     // The ID of the Key
  }
// [END create]
// [START read]
  @Override
  public Location readLocation(Long locationId) {
    Entity locationEntity = datastore.get(keyFactory.newKey(locationId)); // Load an Entity for Key(id)
    return entityToLocation(locationEntity);
  }
// [END read]
// [START update]
  @Override
  public void updateLocation(Location location) {
    Key key = keyFactory.newKey(location.getId());  // From a location, create a Key
    Entity entity = Entity.builder(key)         // Convert Location to an Entity
            .set(Location.UUID, location.getUuid())           // Add Property ("uuid", location.getUuid())
            .set(Location.LOCATION_NAME, location.getLocationName())
            .set(Location.DESCRIPTION, location.getDescription())
            .set(Location.CONFERENCE_ROOMS, location.getConferenceRooms())
            .set(Location.LOCATION_TYPE, location.getLocationType())
            .set(Location.CURRENT_MEETING_AGENDA, location.getCurrentMeetingAgenda())
            .set(Location.IMAGE_URLS, location.getImageURLs())
            .set(Location.VIDEO_CONFERENCING_ENABLED, location.getVideoConferencingEnabled())
            .set(Location.ROOM_CAPACITY, location.getRoomCapacity())
            .set(Location.CREATED_BY, location.getCreatedBy())
            .set(Location.CREATED_BY_ID, location.getCreatedById())
        .build();
    datastore.update(entity);                   // Update the Entity
  }
// [END update]
// [START delete]
  @Override
  public void deleteLocation(Long locationId) {
    Key key = keyFactory.newKey(locationId);        // Create the Key
    datastore.delete(key);                      // Delete the Entity
  }
// [END delete]
// [START entitiesToLocations]
  public List<Location> entitiesToLocations(QueryResults<Entity> resultList) {
    List<Location> resultLocations = new ArrayList<>();
    while (resultList.hasNext()) {  // We still have data
      resultLocations.add(entityToLocation(resultList.next()));      // Add the Location to the List
    }
    return resultLocations;
  }
// [END entitiesToLocations]
// [START listlocations]
  @Override
  public Result<Location> listLocations(String startCursorString) {
    Cursor startCursor = null;
    if (startCursorString != null && !startCursorString.equals("")) {
      startCursor = Cursor.fromUrlSafe(startCursorString);    // Where we left off
    }
    Query<Entity> query = Query.entityQueryBuilder()          // Build the Query
        .kind("Location")                                         // We only care about Locations
        .limit(100)                                            // Only show 10 at a time
        .startCursor(startCursor)                             // Where we left off
        .orderBy(OrderBy.asc(Location.LOCATION_NAME)) // Use default Index "locationType" and "locationName"
        .build();
    QueryResults<Entity> resultList = datastore.run(query);   // Run the query
    List<Location> resultLocations = entitiesToLocations(resultList);     // Retrieve and convert Entities
    Cursor cursor = resultList.cursorAfter();                 // Where to start next time
    if (cursor != null && resultLocations.size() == 100) {         // Are we paging? Save Cursor
      String cursorString = cursor.toUrlSafe();               // Cursors are WebSafe
      return new Result<>(resultLocations, cursorString);
    } else {
      return new Result<>(resultLocations);
    }
  }
// [END listlocations]
// [START listbyuser]
  @Override
  public Result<Location> listLocationsByType(String locationType, String startCursorString) {
    Cursor startCursor = null;
    if (startCursorString != null && !startCursorString.equals("")) {
      startCursor = Cursor.fromUrlSafe(startCursorString);    // Where we left off
    }
    Query<Entity> query = Query.entityQueryBuilder()          // Build the Query
        .kind("Location")                                         // We only care about Locations
        .filter(PropertyFilter.eq(Location.LOCATION_TYPE, locationType))// Only for this location type
        .limit(10)                                            // Only show 100 at a time
        .startCursor(startCursor)                             // Where we left off
        // a custom datastore index is required since you are filtering by one property
        // but ordering by another
        .orderBy(OrderBy.asc(Location.LOCATION_NAME))
        .build();
    QueryResults<Entity> resultList = datastore.run(query);   // Run the Query
    List<Location> resultLocations = entitiesToLocations(resultList);     // Retrieve and convert Entities
    Cursor cursor = resultList.cursorAfter();                 // Where to start next time
    if (cursor != null && resultLocations.size() == 100) {         // Are we paging? Save Cursor
      String cursorString = cursor.toUrlSafe();               // Cursors are WebSafe
      return new Result<>(resultLocations, cursorString);
    } else {
      return new Result<>(resultLocations);
    }
  }
// [END listbyuser]
}
// [END example]
