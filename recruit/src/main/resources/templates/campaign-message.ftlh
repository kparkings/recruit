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
		The following message has been sent from a Recruiter via the Arenella-ICT platform 
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
				<pre>${message}</pre>
			</div>
		</div>
		
		<div>
			<h3>Please do not reply directly to this email. You can reply to the recruiter using their email address: ${recruiterEmail} </h3>
		</div>
	
		<P>
			<a href="https://www.arenella-ict.com">https://www.arenella-ict.com</a>
		</P>
		
		<div>
			<p>
			As part of the terms and conditions of using Arenella-ICT Recruiters must ask permission from candidates before adding them into their personal 
			area of the platform. If you have not given permission or would no longer like to be contacted by this recruiter relating to this specific role you can 
			click here: <a href="https://www.arenella-ict.com/public/campaign/refuse-ecr/${externalCandidateId}">unsubscribe</a>
			</p>
		</div>
	</body>
</html>