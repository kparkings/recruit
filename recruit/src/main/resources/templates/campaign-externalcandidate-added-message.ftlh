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
		A recruiter using the <a href="https://www.arenella-ict.com">Arenella-ICT</a> platform has added you to their private area as an External Candidate. An External Candidate is a Candidate not registered on the platform but whom 
		the recruiter wishes to follow and contact as part of a recruitment campaign.
		</p>
	
		<p>
		Recruiters are informed that they must obtain permission before adding a Candidates details. Hopefully that is the case. However to be sure Arenella-ICT is only storing peoples data where 
		they are in full agreement, when an External Candidate is added we send this email to ask for your confirmation.
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
					<td><b>Relating to role:</b></td>
					<td>${campaignOrRole}</td>
				</tr>
			</table>
		</div>
		
		<div>
			<p>The following information is stored</p>
			<ul>
				<li>First Name</li>
				<li>Surname</li>
				<li>Email</li>
				<li>Country</li>
				<li>Job Title</li>
			</ul>
		</div>
		
		<div>
			<b>Please could you confirm if you would like your details to be stored in the Arenella-ICT platform</b>
			<br>
			<br>
			<a href="https://www.arenella-ict.com/public/campaign/accept-ecr/${externalCandidateId}">Yes - I am happy for my details to be stored for 1 year</a>
			<br>
			<a href="https://www.arenella-ict.com/public/campaign/refuse-ecr/${externalCandidateId}">No - Please delete my information from the platform</a>
			
			<p>
			In the event no choice is selected External Candidates details are automatically deleted from the system after a period of 7 days.
			<p>
			
		</div>
		
		<div>
			<b>Replying:</b>
			<p>Please do not reply directly to this email.</p>
			<p>You can instead reply to the recruiter using their email address: ${recruiterEmail} or contact the Arenella-ICT admin at admin@arenella-ict.com</p>
		</div>
		
		<div>
			<p>Sincerely</p>
			<p>Arenella-ICT</p>
		</div>
	
		<P>
			<a href="https://www.arenella-ict.com">https://www.arenella-ict.com</a>
		</P>
		
	</body>
</html>