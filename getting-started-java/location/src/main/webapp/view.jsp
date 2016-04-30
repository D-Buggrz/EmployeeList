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
  <h3>Location</h3>
  <div class="btn-group">
    <a href="/update?id=${location.id}" class="btn btn-primary btn-sm">
      <i class="glyphicon glyphicon-edit"></i>
      Edit location
    </a>
    <a href="/delete?id=${location.id}" class="btn btn-danger btn-sm">
      <i class="glyphicon glyphicon-trash"></i>
      Delete location
    </a>
  </div>

  <div class="media">
    <div class="media-body">
      <h4 class="location-uuid">
        ${fn:escapeXml(location.uuid)}<br/>
        <small>${fn:escapeXml(location.locationName)}</small><br/>
        <small>${fn:escapeXml(location.locationType)}</small>
      </h4>
      <p class="location-description">Description:  ${fn:escapeXml(location.description)}</p>
      <p class="location-conferenceRooms">Conference Rooms:  ${fn:escapeXml(location.conferenceRooms)}</p>
      <p class="location-currentMeetingAgenda">Current Meeting Agenda:  ${fn:escapeXml(location.currentMeetingAgenda)}</p>
      <p class="location-imageURLs">Image URLs:  ${fn:escapeXml(location.imageURLs)}</p>
      <p class="location-videoConferencingEnabled">Is Video Conferencing Enabled?  ${fn:escapeXml(location.videoConferencingEnabled)}</p>
      <p class="location-roomCapacity">Room Capacity:  ${fn:escapeXml(location.roomCapacity)}</p> 
      <small class="location-added-by">Added by
        ${fn:escapeXml(not empty location.createdBy?location.createdBy:'Anonymous')}</small>
    </div>
  </div>
</div>
<!-- [END view] -->
