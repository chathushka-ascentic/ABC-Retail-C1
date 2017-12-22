<?php 
	
		$link = mysqli_connect("localhost","root","","abc-fashions") or die("Error " . mysqli_error($link));
		
		if(isset($_POST['submit1']))
		{
			if(empty($_POST['BName']) || empty($_POST['Addr']) || empty($_POST['Cont']) || empty($_POST['Lat']) || empty($_POST['Lon'])){
			phpAlert("All fields required!!!"); 	
			}
			else
			{
		
			$BName=$_POST['BName'];
			$Addr=$_POST['Addr'];
			$Cont=$_POST['Cont'];
			$Lat=$_POST['Lat'];
			$Lon=$_POST['Lon'];

			mysqli_query($link,"INSERT INTO `branch`(`branchName`, `latitude`, `longitude`, `address`, `contactNo`) VALUES ('$BName','$Lat','$Lon','$Addr','$Cont')") or die(mysqli_error());
			
			phpAlert("Successfully Added"); 	

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

    <div class="account" id="div2" align="center">
    
    	<div class="logo" style="background-image:url('images/logo.png');" align="right"></div>
			
		<div class="accfrm">
			<form action="?" method="post" >
			<table>
			<tbody>
				<tr><td><h4>Branch Name</h4></td><td><input type="text" id="BName" name="BName"   /></td></tr>
                <tr><td><h4>Address</h4></td><td><input type="text" id="Addr" name="Addr"   /></td></tr>
                <tr><td><h4>Contact No</h4></td><td><input type="text" id="Cont" name="Cont"   /></td></tr>
                <tr><td><h4>Latitude</h4></td><td><input type="text" id="Lat" name="Lat"   /></td></tr>
                <tr><td><h4>Longitude</h4></td><td><input type="text" id="Lon" name="Lon"   /></td></tr>
	
				<tr><td></td><td><input class="button" type="submit" value="Add Branch" name="submit1"/></td></tr>
			</tbody>
			</table>
			</form>
		</div>
	</div>
    </div>


</body>

</html>
