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
		<div style="margin-top: 10px">Dear ${firstname}</div>
		
		<p>
		Your password has been reset. This is because a request was sent via the Arenella-ICT website for the password 
		associated with your email address to be reset. 
		</p>
	
		<P>
			<a href="https://www.arenella-ict.com">https://www.arenella-ict.com</a>
		</P>
	
		<h3>Your login details are </h3>
	
		<div>
			<table>
				<tr>
					<td><b>Username</b></td>
					<td>${userId}</td>
				</tr>
				<tr>
					<td><b>Password</b></td>
					<td>${password}</td>
				</tr>
			</table>
		</div>
		
		<div> 
			<p>Kind regards</p>
			<p>Kevin</p>
		</div>
		
	</body>
</html>