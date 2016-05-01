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
<!-- [START view] -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<div class="container">
  <h3>Employee</h3>
  <div class="btn-group">
    <a href="/update?id=${employee.id}" class="btn btn-primary btn-sm">
      <i class="glyphicon glyphicon-edit"></i>
      Edit employee
    </a>
    <a href="/delete?id=${employee.id}" class="btn btn-danger btn-sm">
      <i class="glyphicon glyphicon-trash"></i>
      Delete employee
    </a>
  </div>

  <div class="media">
    <div class="media-body">
      <h4 class="employee-uuid">
        ${fn:escapeXml(employee.uuid)}<br/>
        <small>${fn:escapeXml(employee.employeeName)}</small><br/>
        <small>${fn:escapeXml(employee.employeeType)}</small>
      </h4>
      <p class="employee-description">Description:  ${fn:escapeXml(employee.description)}</p>
      <p class="employee-conferenceRooms">Conference Rooms:  ${fn:escapeXml(employee.conferenceRooms)}</p>
      <p class="employee-currentMeetingAgenda">Current Meeting Agenda:  ${fn:escapeXml(employee.currentMeetingAgenda)}</p>
      <p class="employee-imageURLs">Image URLs:  ${fn:escapeXml(employee.imageURLs)}</p>
      <p class="employee-videoConferencingEnabled">Is Video Conferencing Enabled?  ${fn:escapeXml(employee.videoConferencingEnabled)}</p>
      <p class="employee-roomCapacity">Room Capacity:  ${fn:escapeXml(employee.roomCapacity)}</p> 
      <small class="employee-added-by">Added by
        ${fn:escapeXml(not empty employee.createdBy?employee.createdBy:'Anonymous')}</small>
    </div>
  </div>
</div>
<!-- [END view] -->
