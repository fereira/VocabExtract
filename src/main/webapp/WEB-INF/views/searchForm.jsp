<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1.0"> </meta>
<!-- Bootstrap -->

<link href="webjars/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet" media="screen"> </link>

<title>Search Form</title>
</head>
<body>
    <script src="http://code.jquery.com/jquery.js"></script>
    <script src="webjars/bootstrap/3.3.7/js/bootstrap.min.js"></script>
   
    
    <div class="container">
    <spring:url value="/users" var="actionUrl" />
    <form:form method="post" class="form-horizontal" modelAttribute="searchForm" action="${actionUrl}">
        <fieldset>
        <legend>Search Vocabulary</legend>
		<spring:bind path="vocabulary">
		  <div class="form-group ${status.error ? 'has-error' : ''}">
			<label class="col-sm-2 control-label">Vocabulary</label>
			<div class="col-sm-10">
				<form:input path="vocabulary type="text" class="form-control"
                                id="vocabulary" placeholder="Vocabulary" />
				<form:errors path="vocabulary" class="control-label" />
			</div>
		  </div>
		</spring:bind>

		<spring:bind path="term">
		  <div class="form-group ${status.error ? 'has-error' : ''}">
			<label class="col-sm-2 control-label">Search Term</label>
			<div class="col-sm-10">
				<form:input path="term" class="form-control"
                                id="term" placeholder="Term" />
				<form:errors path="term" class="control-label" />
			</div>
		  </div>
		</spring:bind> 
		
		<form:hidden path="lang" type="hidden" /> <!-- bind to searchForm.lang -->
		<form:hidden path="format" type="hidden" /> <!-- bind to searchForm.format -->
		</fieldset>
		<div class="form-group">
		  <div class="col-sm-offset-2 col-sm-10">
			<button type="submit" class="btn-lg btn-primary pull-right">Search</button>
		  </div>
		</div> 
	</form:form>
	</div>
	
</body>
</html>
