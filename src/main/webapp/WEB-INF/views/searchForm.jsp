<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html >
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1.0"> </meta>
<!-- Bootstrap -->

<link href="webjars/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet" media="screen"> </link>

<title>Search Form</title>
</head>
<body>
    <script src="assets/js/jquery-1.12.4.min.js"></script>
    <script src="webjars/bootstrap/3.3.7/js/bootstrap.min.js"></script>
   
    
    <div class="container">
    <spring:url value="/doSearch" var="actionUrl" />
    
    <form:form method="post" class="form-horizontal" modelAttribute="searchForm" action="${actionUrl}">
        <fieldset>
        <legend>Search Vocabulary</legend>
		

		<spring:bind path="term">
		  <div class="form-group ${status.error ? 'has-error' : ''}">
			<label class="col-sm-2 control-label">Search Term</label>
			<div class="col-sm-10">
				<form:input path="term" class="form-control" id="term" placeholder="Term" />
				<form:errors path="term" class="control-label" />
			</div>
		  </div>
		</spring:bind>
		
		<spring:bind path="vocabulary">
			<div class="form-group ${status.error ? 'has-error' : ''}">
				<label class="col-sm-2 control-label">Vocabulary</label>
				<div class="col-sm-5">
					<form:select path="vocabulary" class="form-control"> 
						<form:options items="${vocabularyList}" />
					</form:select>
					<form:errors path="vocabulary" class="control-label" />
				</div>
				<div class="col-sm-5"></div>
			</div>
		</spring:bind> 
		
		<form:hidden path="lang"  />
		
		<spring:bind path="format">
			<div class="form-group ${status.error ? 'has-error' : ''}">
				<label class="col-sm-2 control-label">Format</label>
				<div class="col-sm-5">
					<form:select path="format" class="form-control">
						<form:option value="NONE" label="--- Select ---" />
						<form:options items="${formatList}" />
					</form:select>
					<form:errors path="format" class="control-label" />
				</div>
				<div class="col-sm-5"></div>
			</div>
		</spring:bind>
		
		</fieldset>
		
		<div class="form-actions">
		  <div class="col-sm-offset-2 col-sm-10">
			<button type="submit" class="btn-lg btn-primary pull-right">Search</button>
		  </div>
		</div> 
	</form:form>
	</div>
	
</body>
</html>
