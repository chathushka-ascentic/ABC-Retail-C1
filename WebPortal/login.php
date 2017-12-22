<?php 
	
		$link = mysqli_connect("localhost","root","","abc-fashions") or die("Error " . mysqli_error($link));
		
		//login
		if(isset($_POST['submit']))
		{
			if(empty($_POST['User']) || empty($_POST['Password'])){
			phpAlert("Username or Password is Invalid !!!"); 				
			}
			else
			{
		
			$user=$_POST['User'];
			$pass=$_POST['Password'];
			
			$query = mysqli_query($link,"SELECT * FROM user WHERE email = '$user' AND password = '$pass'" ); 
			$result=mysqli_fetch_assoc($query);
			$rows = mysqli_num_rows($query);
			
			if($rows == 1)
			{
				phpAlert("Logging in..."); 	
				header('Location: profile.php?userId='.$result['userId']);
			}
			else
			{
				phpAlert("Please create an account"); 	
			}
			
			}
		}
		
		//create an account
		if(isset($_POST['submit1']))
		{
			if(empty($_POST['FName']) || empty($_POST['Email']) || empty($_POST['Password1']) || empty($_POST['Password2'])){
			phpAlert("All fields required!!!"); 	
			}
			elseif (($_POST['Password1']) != ($_POST['Password2'])){
			phpAlert("Passwords won't match"); 			}
			else
			{
		
			$FName=$_POST['FName'];
			$Email=$_POST['Email'];
			$Pass=$_POST['Password1'];
			
			$result = mysqli_query($link,"SELECT COUNT(*) AS cc FROM user WHERE email = '$Email'",0);
			$result1=mysqli_fetch_assoc($result);
			
			if($result1['cc'] > 0)
			{
				phpAlert("You have alrady created an account"); 	
			}
			else
			{
				mysqli_query($link,"INSERT INTO user (`fullname`,`email`,`password`) VALUES('$FName', '$Email', '$Pass')");
				phpAlert("Account created. Please login"); 	

			}
			}
		}
		
		function phpAlert($msg) {
    		echo '<script type="text/javascript">alert("' . $msg . '")</script>';
		}
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
<link rel="stylesheet" type="text/css" href="default.css"  />
<title>ABC Fashions</title>
</head>

<body>

	<div class="wrapper" id="wrapper">

	<div class="login" id="div1" align="right">
		
        <div class="logo" style="background-image:url('images/logo.png');" align="right"></div>

		<div class="logfrm">
			<form action="?" method="post" >
			<table>
			<tbody>
				<tr><td><h4>Username</h4></td><td><input type="text" id="User" name="User"   /></td></tr>
				<tr><td><h4>Password</h4></td><td><input type="password" id="Password" name="Password"  /></td></tr>
				<tr><td></td><td><input class="button" type="submit" value="Login" name="submit"/></td></tr>
			</tbody>
			</table>
			</form>
		</div>
	</div>
    
    <div class="line"></div>

    <div class="account" id="div2" align="center">
			
		<div class="accfrm">
			<form action="?" method="post" >
			<table>
			<tbody>
				<tr><td><h4>Full Name</h4></td><td><input type="text" id="FName" name="FName"   /></td></tr>
                <tr><td><h4>Email</h4></td><td><input type="text" id="Email" name="Email"   /></td></tr>
				<tr><td><h4>Password</h4></td><td><input type="password" id="Password1" name="Password1"  /></td></tr>
                <tr><td><h4>Confirm Password</h4></td><td><input type="password" id="Password2" name="Password2"  /></td></tr>
				<tr><td></td><td><input class="button" type="submit" value="Create an Account" name="submit1"/></td></tr>
			</tbody>
			</table>
			</form>
		</div>
	</div>
    </div>


</body>

</html>
