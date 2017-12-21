<?php

$userId = $_GET['userId'];
$qrCode = $_GET['qrCode'];
$brCode = $_GET['branchId'];

$link = mysqli_connect("localhost","root","","abc-fashions") or die("Error " . mysqli_error($link));

$productId=0;
$price=0.00;
$productMainObj = new stdClass();

/////get product details
$result = mysqli_query($link,"SELECT p.productId,p.productName,p.description,p.price,c.categoryName,p.imageUrl FROM product p LEFT OUTER JOIN category c ON p.categoryId=c.categoryId WHERE p.qrCode='$qrCode'") or die(mysqli_error());
  
$productObj = new stdClass();
      
while($row =mysqli_fetch_assoc($result))
{
		  $productObj = $row;
		  $productId = $row["productId"];
		  $price = $row["price"];
}

$productMainObj->details = $productObj;

/////get available stock
$availableStock = mysqli_query($link,"SELECT s.stockId,si.sizeName FROM stock s LEFT OUTER JOIN size si ON s.sizeId = si.sizeId WHERE s.branchId='$brCode' AND s.productId='$productId' AND s.count > 0") or die(mysqli_error());
		
$sizes = array();
$i = 0;
		
while($row =mysqli_fetch_assoc($availableStock))
{
		  
		  	/////get offers
			$oprice=$price;
			$stockOffer = mysqli_query($link,"SELECT discountPerc,discountPrice FROM `offer` WHERE stockId='$row[stockId]' AND branchId='$brCode' AND startDate <= (SELECT CURDATE()) and endDate >= (SELECT CURDATE())") or die(mysqli_error());
			
			while($rowP =mysqli_fetch_assoc($stockOffer))
			{
					if ($rowP["discountPerc"] > 0)
					$oprice = $price - ($price * $rowP["discountPerc"]);
					else if ($rowP["discountPrice"] > 0)
					$oprice = $price - $rowP["discountPrice"];		
			}

			
			$sizes [$i]["stockId"]= $row['stockId'];
   		    $sizes [$i]["sizeName"]= $row['sizeName'];
			$sizes [$i]["price"]= $oprice;

    		$i++;

}
	

$productMainObj->availableStock = $sizes;

echo json_encode($productMainObj);


mysqli_close($link);

?>