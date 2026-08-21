<!doctype html>
<html lang="en">
	<head>
	    <meta charset="UTF-8">
	    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
	    <meta http-equiv="X-UA-Compatible" content="ie=edge">
	    <title>Arenella-ICT</title>
	    <#setting number_format="computer">
	</head>
	<body>
		<div style="margin-top: 10px">Dear ${recipientName}</div>
		
		<p>
		The following message has been send via a Recruiter on the Arenella-ICT platform 
		</p>
	
		<div>
			<h3>Recruiter Details:</h3>
			<table>
				<tr>
					<td><b>Recruiter Name:</b></td>
					<td>${recruiterName}</td>
				</tr>
				<tr>
					<td><b>Recruiter email:</b></td>
					<td>${recruiterEmail}</td>
				</tr>
				<tr>
					<td><b>Relating to:</b></td>
					<td>${campaignOrRole}</td>
				</tr>
			</table>
		</div>
		
		<div>
			<h3>Message </h3>
			<div>
				${message}
			</div>
		</div>
	
		<P>
			<a href="https://www.arenella-ict.com">https://www.arenella-ict.com</a>
		</P>
		
	</body>
</html>