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

<title>Search Results</title>
</head>
<body>
    <script src="assets/js/jquery-1.12.4.min.js"></script>
    <script src="webjars/bootstrap/3.3.7/js/bootstrap.min.js"></script> 
    
    <div class="container">
    ${results}
	</div>
	
</body>
</html>
