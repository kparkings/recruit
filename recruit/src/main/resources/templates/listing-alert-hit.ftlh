<!doctype html>
<html lang="en">
	<head>
	    <meta charset="UTF-8">
	    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
	    <meta http-equiv="X-UA-Compatible" content="ie=edge">
	    <title>Arenella-ICT</title>
	    <style type="text/css">
	    <#include "/styles.css">
	    </style>
	    <#setting number_format="computer">
	</head>
	<body>
		<div style="margin-top: 10px">Dear User</div>
		
		<p>
		We are pleased to inform you that a potentially interesting role has been added to the Arenella-ICT jobboard. You are receiving this email as you 
		previously set-up an email alert for these types of roles.
		</p>
		
		<table>
			<tr>
				<td>Role</td>
				<td>${listingTitle}</td>
			</tr>
			<tr>
				<td>Link</td>
				<td><a href="https://www.arenella-ict.com/listing/${listingId}">View role</a></td>
			</tr>
		</table>
		
		<p>
		If you no longer wish to receive these alerts you can click on the following link: <a href="https://www.arenella-ict.com/public/listing-alert/${alertId}/_delete">unsubscribe</a>
		</p>
		
		<div> 
			<p>Kind regards</p>
			<p>Kevin</p>
			<P>
			<a href="https://www.arenella-ict.com/email/">https://www.arenella-ict.com</a>
			</P>
		</div>
		
	</body>
</html>