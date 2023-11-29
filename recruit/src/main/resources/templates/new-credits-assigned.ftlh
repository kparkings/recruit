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
		<div style="margin-top: 10px">Dear ${recipientName}</div>
		
		<p>
		We are pleased to inform you that you have been assigned this weeks credits. With credit based access you can use your credits to perform actions on the site. 
		Once your credits have been used you will need to wait until the following week when you will be assigned new Credits. For unlimited access you can select a paid 
		subscription option.
		</p>
		
		<table>
			<tr>
				<td>Suggestions</td>
				<td>${CREDITS_CURRICULUM}</td>
				<td>Download CVs of Candidates from the Suggestion page.</td>
			</tr>
			<tr>
				<td>Job Board</td>
				<td>${CREDITS_LISTINGS}</td>
				<td>Advertise your open positions to attract Candidates, including those not yet registered with the site.</td>
			</tr>
			<tr>
				<td>Marketplace</td>
				<td>${CREDITS_MARKETPLACE}</td>
				<td>Request Candidates from other Recruiters.</td>
			</tr>
		</table>
		<P>
			<a href="https://www.arenella-ict.com/email/">https://www.arenella-ict.com</a>
		</P>
		
		<div> 
			<p>Kind regards</p>
			<p>Kevin</p>
		</div>
		<p class="disclaimer">
			Please be informed messages and attachemnts are sent by unverified sources. 
			We recommend always keeping your anti-virus programs up to date and not no enable macros or dynamic content if 
			prompted to do so when viewing any file on the internet.
		</p>
	</body>
</html>