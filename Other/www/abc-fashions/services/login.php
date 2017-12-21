<?php

$user=$_GET["username"];
$pass=$_GET["pass"];

$link = mysqli_connect("localhost","root","","abc-fashions") or die("Error " . mysqli_error($link));

$result = mysqli_query($link,"SELECT * FROM user WHERE email = '$user' AND password = '$pass'") or die(mysqli_error());
 
$userObj = NULL;
    
	while($row =mysqli_fetch_assoc($result))
    {
        $userObj = $row;
    }
	
echo json_encode($userObj);

mysqli_close($link);

?>