<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html >
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1.0"> </meta>
<!-- Bootstrap -->

<link href="webjars/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet" media="screen"> </link>

<title>Search Results</title>
</head>
<body>
    <script src="assets/js/jquery-1.12.4.min.js"></script>
    <script src="webjars/bootstrap/3.3.7/js/bootstrap.min.js"></script> 
    
    
    <c:if test="${empty conceptList}" > 
    <h2>No Results Found</h2>
    
    </c:if>
    
    <c:if test="${! empty conceptList}" >
    <h2>Number of Concepts Found: ${fn:length(conceptList)}</h2>
    
    <div classs="container-fluid">
    <c:forEach var="concept" items="${conceptList}" >
    <hr />
    <div class="container"> 
    <div class="row">
      <div class="col-sm-2 text-right" style="background-color:white;">Uri</div>
      <div class="col-sm-10" > ${concept.uri}</div>
    </div>
    <div class="row">
      <div class="col-sm-2 text-right" style="background-color:white;">PrefLabel</div>
      <div class="col-sm-10" >${concept.label}</div>
    </div> 
    
    <c:if test="${! empty concept.altLabelList }" >
    <div class="row">
      <div class="col-sm-2 text-right" style="background-color:white;">AltLabel</div>
      <div class="col-sm-10" >
        <div class="row">
        <c:forEach var="item" items="${concept.altLabelList}" >
        <div class="col-sm-12"><c:out value="${item}" /></div> 
        </c:forEach>
        </div>
      </div>
    </div>
    </c:if> 
    </div>
    <hr />
    </c:forEach>
    
    </div> 
    </c:if>
	
</body>
</html>
