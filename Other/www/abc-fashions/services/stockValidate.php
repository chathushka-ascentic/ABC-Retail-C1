<?php

$userId = $_GET['userId'];
$stockId = $_GET['stockId'];
$orderQty = $_GET['orderQty'];


$link = mysqli_connect("localhost","root","","abc-fashions") or die("Error " . mysqli_error($link));

$stockObj = new stdClass();
$stockCount=0;

$availableStock = mysqli_query($link,"SELECT count FROM stock WHERE stockId='$stockId'") or die(mysqli_error()); 
while($row =mysqli_fetch_assoc($availableStock))
{
		  $stockCount = $row["count"];
}

if($stockCount >= $orderQty)
{
	$stockObj->status = "200";
}
else
{
	$stockObj->status = "201";
}

echo json_encode($stockObj);

mysqli_close($link);


?>