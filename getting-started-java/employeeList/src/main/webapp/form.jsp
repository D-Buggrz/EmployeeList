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
    <c:out value="${action}" /> employee
  </h3>

  <form method="POST" action="${destination}" enctype="multipart/form-data">

    <div class="form-group">
      <label for="uuid">UUID</label>
      <input type="text" name="uuid" id="uuid" value="${fn:escapeXml(employee.uuid)}" class="form-control" />
    </div>

    <div class="form-group">
      <label for="employeeName">Employee Name</label>
      <input type="text" name="employeeName" id="employeeName" value="${fn:escapeXml(employee.employeeName)}" class="form-control" />
    </div>
    
    <div class="form-group">
      <label for="description">Description</label>
      <textarea name="description" id="description" class="form-control">${fn:escapeXml(employee.description)}</textarea>
    </div>
    
    <div class="form-group">
      <label for="conferenceRooms">Conference Rooms</label>
      <input type="text" name="conferenceRooms" id="conferenceRooms" value="${fn:escapeXml(employee.conferenceRooms)}" class="form-control" />
    </div>
  
  	<div class="form-group">
      <label for="employeeType">Employee Type</label>
      <input type="text" name="employeeType" id="employeeType" value="${fn:escapeXml(employee.employeeType)}" class="form-control" />
    </div>
    
    <div class="form-group">
      <label for="currentMeetingAgenda">Current Meeting Agenda</label>
      <input type="text" name="currentMeetingAgenda" id="currentMeetingAgenda" value="${fn:escapeXml(employee.currentMeetingAgenda)}" class="form-control" />
    </div>
    
    <div class="form-group">
      <label for="imageURLs">Image URLs</label>
      <input type="text" name="imageURLs" id="imageURLs" value="${fn:escapeXml(employee.imageURLs)}" class="form-control" />
    </div>
    
    <div class="form-group">
      <label for="videoConferencingEnabled">Is Video Conferencing Enabled?</label>
      <input type="text" name="videoConferencingEnabled" id="videoConferencingEnabled" value="${fn:escapeXml(employee.videoConferencingEnabled)}" class="form-control" />
    </div>
    
    <div class="form-group">
      <label for="roomCapacity">Room Capacity</label>
      <input type="text" name="roomCapacity" id="roomCapacity" value="${fn:escapeXml(employee.roomCapacity)}" class="form-control" />
    </div>
    
    <div class="form-group ${isCloudStorageConfigured ? '' : 'hidden'}">
      <label for="image">Cover Image</label>
      <input type="file" name="file" id="file" class="form-control" />
    </div>
  
  	<div class="form-group hidden">
  		<input type="hidden" name="id" value="${employee.id}" />
  	</div> 

    <button type="submit" class="btn btn-success">Save</button>
  </form>
</div>
<!-- [END form] -->
