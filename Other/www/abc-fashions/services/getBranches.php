<?php

$userId = $_GET['userId'];

$link = mysqli_connect("localhost","root","","abc-fashions") or die("Error " . mysqli_error($link));

$result = mysqli_query($link,"SELECT * FROM branch") or die(mysqli_error());
 
$results = array();
    
	while($row =mysqli_fetch_assoc($result))
    {
		  $results[] = $row;
    }
	

$named_array = array(
    "getBranchResponse" => $results
);

echo json_encode($named_array);


mysqli_close($link);

?>