<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html lang="en">

<body>
	<p>Test Links</p>
	
	<a href="/gerrit/config/host">Show Host</a>
	<form action="/gerrit/config/changeHost" method="POST">
		Hostname: <input type="text" name="host" /> <input type="submit"
			value="Submit" />
	</form>
	<a href="">Change Host</a>
	<a href="/gerrit/config/reviewersToIgnore">Show List Of Reviewers
		To Ignore</a>
	<a href="/gerrit/config/changeReviewersToIgnore">Change List Of
		Reviewers To Ignore</a>

	<a href="/gerrit/review/open/all">Get All Open Reviews</a>
	<a href="/gerrit/review/open/.*">Get All Merged Reviews Matching</a>
	<a href="/gerrit/review/open/20150101/20151201">Get All Merged
		Reviews Between</a>
	<a href="/gerrit/review/open/.*/20150101/20151201">Get All Merged
		Reviews Between Matching</a>
</body>
</html>