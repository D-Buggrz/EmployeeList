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
<!-- [START list] -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<div class="container">
  <h3>Employees</h3>
  <a href="/create" class="btn btn-success btn-sm">
    <i class="glyphicon glyphicon-plus"></i>
    Add employee
  </a>
  <c:choose>
  <c:when test="${empty employees}">
  <p>No employees found</p>
  </c:when>
  <c:otherwise>
  <c:forEach items="${employees}" var="employee">
  <div class="media">
    <a href="/read?id=${employee.id}">
      <div class="media-body">
        <h4>${fn:escapeXml(employee.uuid)}</h4>        
        <p>${fn:escapeXml(employee.employeeType)}</p>
        <p>${fn:escapeXml(employee.employeeName)}</p>
      </div>
    </a>
  </div>
  </c:forEach>
  <c:if test="${not empty cursor}">
  <nav>
    <ul class="pager">
      <li><a href="?cursor=${fn:escapeXml(cursor)}">More</a></li>
    </ul>
  </nav>
  </c:if>
  </c:otherwise>
  </c:choose>
</div>
<!-- [END list] -->
