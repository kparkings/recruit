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
		<div style="margin-top: 10px">Dear ${firstname}</div>
		
		<p>
		To reduce the number of calls you receive from recruiters we kindly ask you to confirm your current availability. We send this email once per two weeks when you 
		are activelly looking and once a month when you have indicated that you are on a project. 
		<p>
			
		<div>	
			<button class="btn-arenella btn-arenella-green" style="cursor:pointer"><a style="display:inline-block;width:100%;height:100%;color:black;" href="https://www.arenella-ict.com/public/candidate/${candidateId}/availabilitytoken/${summaryEmailIdToken}?isAvailable=true">Available</a></button>
			<button class="btn-arenella btn-arenella-red" style="cursor:pointer"><a style="display:inline-block;width:100%;height:100%;color:black;" href="https://www.arenella-ict.com/public/candidate/${candidateId}/availabilitytoken/${summaryEmailIdToken}?isAvailable=false">Unavailable</a></button>
		</div>
		<P>
			<a href="https://www.arenella-ict.com">https://www.arenella-ict.com</a>
		</P>
		
		<div> 
			<p>Kind regards</p>
			<p>Arenella-ICT </p>
		</div>
		
	</body>
</html>