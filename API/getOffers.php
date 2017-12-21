<?php

$userId = $_GET['userId'];
$brCode = $_GET['branchId'];

$link = mysqli_connect("localhost","root","","abc-fashions") or die("Error " . mysqli_error($link));


$results = array();

		  
/////get offers

			$stockOffer = mysqli_query($link,"SELECT `desc`,`desc1`,`offerId` FROM `offer` WHERE branchId='$brCode' AND startDate <= (SELECT CURDATE()) and endDate >= (SELECT CURDATE())") or die(mysqli_error());

			
			while($row =mysqli_fetch_assoc($stockOffer))
			{
				$results[] = $row;
			}


$named_array = array(
    "getOffersResponse" => $results
);

echo json_encode($named_array);

mysqli_close($link);

?>