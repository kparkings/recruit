<!doctype html>
<html lang="en">
	<head>
	    <meta charset="UTF-8">
	    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
	    <meta http-equiv="X-UA-Compatible" content="ie=edge">
	    <title>Arenella-ICT</title>
	    <#include "/styles.css">
	</head>
	<body>
		<div style="margin-top: 10px">Dear ${recipientName}</div>
		
		<p>
		We are pleased to inform you that you have received matches for one or more of your Candidate Search Alerts. 
		</p>
	
		<div>
			<table>
				<tr>
					<th>Alert Name</th>
					<th>Candidate Id</th>
					<th>Role Sought</th>
					<th>Accuracy</th>
				</tr>
				<#list alerts as alertId, alert>
	  				<#list alert as match>
		    			<tr>
		  					<td>${match.alertName}</td>
		  					<td class="td-centered">${match.candidateId}</td>
		  					<td>${match.roleSought}</td>
		  					<td class="td-centered">${match.accuracy}</td>
		  				</tr>
	    			</#list>
	    		</#list>
			</table>
		</div>
		
		<P>
			<a href="https://www.arenella-ict.com">https://www.arenella-ict.com</a>
		</P>
	
		
		<div> 
			<p>Kind regards</p>
			<p>Kevin</p>
		</div>
		
	</body>
</html>