<!-- [START_EXCLUDE] -->
<!--
Copyright 2015 Google Inc. All Rights Reserved.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
-->
<!-- [END_EXCLUDE] -->
<!-- [START form] -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<div class="container">
  <h3>
    <c:out value="${action}" /> location
  </h3>

  <form method="POST" action="${destination}" enctype="multipart/form-data">

    <div class="form-group">
      <label for="uuid">UUID</label>
      <input type="text" name="uuid" id="uuid" value="${fn:escapeXml(location.uuid)}" class="form-control" />
    </div>

    <div class="form-group">
      <label for="locationName">Location Name</label>
      <input type="text" name="locationName" id="locationName" value="${fn:escapeXml(location.locationName)}" class="form-control" />
    </div>
    
    <div class="form-group">
      <label for="description">Description</label>
      <textarea name="description" id="description" class="form-control">${fn:escapeXml(location.description)}</textarea>
    </div>
    
    <div class="form-group">
      <label for="conferenceRooms">Conference Rooms</label>
      <input type="text" name="conferenceRooms" id="conferenceRooms" value="${fn:escapeXml(location.conferenceRooms)}" class="form-control" />
    </div>
  
  	<div class="form-group">
      <label for="locationType">Location Type</label>
      <input type="text" name="locationType" id="locationType" value="${fn:escapeXml(location.locationType)}" class="form-control" />
    </div>
    
    <div class="form-group">
      <label for="currentMeetingAgenda">Current Meeting Agenda</label>
      <input type="text" name="currentMeetingAgenda" id="currentMeetingAgenda" value="${fn:escapeXml(location.currentMeetingAgenda)}" class="form-control" />
    </div>
    
    <div class="form-group">
      <label for="imageURLs">Image URLs</label>
      <input type="text" name="imageURLs" id="imageURLs" value="${fn:escapeXml(location.imageURLs)}" class="form-control" />
    </div>
    
    <div class="form-group">
      <label for="videoConferencingEnabled">Is Video Conferencing Enabled?</label>
      <input type="text" name="videoConferencingEnabled" id="videoConferencingEnabled" value="${fn:escapeXml(location.videoConferencingEnabled)}" class="form-control" />
    </div>
    
    <div class="form-group">
      <label for="roomCapacity">Room Capacity</label>
      <input type="text" name="roomCapacity" id="roomCapacity" value="${fn:escapeXml(location.roomCapacity)}" class="form-control" />
    </div>
    
    <div class="form-group ${isCloudStorageConfigured ? '' : 'hidden'}">
      <label for="image">Cover Image</label>
      <input type="file" name="file" id="file" class="form-control" />
    </div>
  
  	<div class="form-group hidden">
  		<input type="hidden" name="id" value="${location.id}" />
  	</div> 

    <button type="submit" class="btn btn-success">Save</button>
  </form>
</div>
<!-- [END form] -->
