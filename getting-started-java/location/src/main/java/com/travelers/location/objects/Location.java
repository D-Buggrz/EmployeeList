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

package com.travelers.location.objects;

// [START example]
public class Location {
// [START location]
  private String uuid;
  private String locationName;
  private String description;
  private String conferenceRooms;
  private String locationType;
  private String currentMeetingAgenda;
  private String imageURLs;
  private String videoConferencingEnabled;
  private String roomCapacity;
  private Long id;
  private String createdBy;
  private String createdById;
  
// [END location]
// [START keys]
  public static final String UUID = "uuid";
  public static final String LOCATION_NAME = "locationName";
  public static final String DESCRIPTION = "description";
  public static final String CONFERENCE_ROOMS = "conferenceRooms";
  public static final String LOCATION_TYPE = "locationType";
  public static final String CURRENT_MEETING_AGENDA = "currentMeetingAgenda";
  public static final String IMAGE_URLS = "imageURLs";
  public static final String VIDEO_CONFERENCING_ENABLED = "videoConferencingEnabled";
  public static final String ROOM_CAPACITY = "roomCapacity";
  public static final String ID = "id";
  public static final String CREATED_BY = "createdBy";
  public static final String CREATED_BY_ID = "createdById";
  
  
// [END keys]
// [START constructor]
  // We use a Builder pattern here to simplify and standardize construction of Location objects.
  private Location(Builder builder) {
	  
    this.uuid = builder.uuid;
    this.locationName = builder.locationName;
    this.description = builder.description;
    this.conferenceRooms = builder.conferenceRooms;
    this.locationType = builder.locationType;
    this.currentMeetingAgenda = builder.currentMeetingAgenda;
    this.imageURLs = builder.imageURLs;
    this.videoConferencingEnabled = builder.videoConferencingEnabled;
    this.roomCapacity = builder.roomCapacity;
    this.id = builder.id;
    this.createdBy = builder.createdBy;
    this.createdById = builder.createdById;
  }
// [END constructor]
// [START builder]
  public static class Builder {
	  private String uuid;
	  private String locationName;
	  private String description;
	  private String conferenceRooms;
	  private String locationType;
	  private String currentMeetingAgenda;
	  private String imageURLs;
	  private String videoConferencingEnabled;
	  private String roomCapacity;
	  private Long id;
	  private String createdBy;
	  private String createdById;

    public Builder uuid(String uuid) {
      this.uuid = uuid;
      return this;
    }
    
    public Builder locationName(String locationName) {
        this.locationName = locationName;
        return this;
    }
    
    public Builder description(String description) {
        this.description = description;
        return this;
    }
    
    public Builder conferenceRooms(String conferenceRooms) {
    	this.conferenceRooms = conferenceRooms;
    	return this;
    }
    
    public Builder locationType(String locationType) {
        this.locationType = locationType;
        return this;
    }
    
    public Builder currentMeetingAgenda(String currentMeetingAgenda) {
        this.currentMeetingAgenda = currentMeetingAgenda;
        return this;
    }
    
    public Builder imageURLs(String imageURLs) {
    	this.imageURLs = imageURLs;
    	return this;
    }
    
    public Builder videoConferencingEnabled(String videoConferencingEnabled) {
        this.videoConferencingEnabled = videoConferencingEnabled;
        return this;
    }

    public Builder roomCapacity(String roomCapacity) {
        this.roomCapacity = roomCapacity;
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
    
    public Location build() {
        return new Location(this);
    }
    
  }

	public String getUuid() {
		return uuid;
	}
		
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
		
	public String getLocationName() {
		return locationName;
	}
		
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
		
	public String getDescription() {
		return description;
	}
		
	public void setDescription(String description) {
		this.description = description;
	}
		
	public String getConferenceRooms() {
		return conferenceRooms;
	}
		
	public void setConferenceRooms(String conferenceRooms) {
		this.conferenceRooms = conferenceRooms;
	}
		
	public String getLocationType() {
		return locationType;
	}
		
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
		
	public String getCurrentMeetingAgenda() {
		return currentMeetingAgenda;
	}
	
	public void setCurrentMeetingAgenda(String currentMeetingAgenda) {
		this.currentMeetingAgenda = currentMeetingAgenda;
	}
		
	public String getImageURLs() {
		return imageURLs;
	}
		
	public void setImageURLs(String imageURLs) {
		this.imageURLs = imageURLs;
	}
		
	public String getVideoConferencingEnabled() {
		return videoConferencingEnabled;
	}
	
	public void setVideoConferencingEnabled(String videoConferencingEnabled) {
		this.videoConferencingEnabled = videoConferencingEnabled;
	}
		
	public String getRoomCapacity() {
		return roomCapacity;
	}
		
	public void setRoomCapacity(String roomCapacity) {
		this.roomCapacity = roomCapacity;
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
        "uuid: " + uuid + ", locationName: " + locationName + ", description: " + description
        + ", conferenceRooms: " + conferenceRooms.toString() + ", locationType: " + locationType
        + ", currentMeetingAgenda: " + currentMeetingAgenda + ", imageURLs: " + imageURLs.toString()
        + ", videoConferencingEnabled: " + videoConferencingEnabled+ ", roomCapacity: " + roomCapacity
        + ", createdBy: " + createdBy+ ", createdById: " + createdById;
  }
}
// [END example]