<?php


  /*$data ='{"OrderInsertRequest":{"orderTotal":47.5,"branchId":"1","paidBy":2,"orderList":[{"product":{"categoryName":"Dresses","availableStock":[{"stockId":1,"sizeName":"S","price":9.5},{"stockId":2,"sizeName":"M","price":5}],"price":10,"productName":"ASOS Mini Cross Front Origami Dress","imageUrl":"https:\/\/cdnb.lystit.com\/photos\/2011\/10\/11\/asos-collection-red-asos-petite-exclusive-origami-pleated-dress-product-1-2186184-841596785.jpeg","description":"Color - Red\r\nV - Neck","productId":1},"qty":5,"stock":{"stockId":1,"sizeName":"S","price":9.5}}],"orderId":0,"orderDate":"2017-12-19","userId":"1"}}';
  
  $data1 = json_decode($data);*/
  
  $data1 = json_decode(file_get_contents('php://input'));

  //header
  $userId = $data1->OrderInsertRequest->userId;
  $orderDate = $data1->OrderInsertRequest->orderDate;
  $orderTotal = $data1->OrderInsertRequest->orderTotal;
  $payId = $data1->OrderInsertRequest->paidBy;
  $branchId = $data1->OrderInsertRequest->branchId;
  $orderList = $data1->OrderInsertRequest->orderList;

$link = mysqli_connect("localhost","root","","abc-fashions") or die("Error " . mysqli_error($link));

$orderObj = new stdClass();

if(mysqli_query($link,"INSERT INTO `order`(`userId`, `orderDate`, `orderTotal`, `payId`, `branchId`) VALUES ('$userId','$orderDate','$orderTotal','$payId','$branchId')"))
{
	$orderId = mysqli_insert_id($link);
	
	 //detail
	foreach($orderList as $item) 
	{ 
   
	   $productId = $item->product->productId;	
	   $qty = $item->qty;	
	   $stockId = $item->stock->stockId;
	   $price = $item->stock->price;
	   //$size = $item->stock->sizeName;
	   
	   	if(mysqli_query($link,"INSERT INTO `orderdetail`(`orderId`, `productId`, `stockId`, `qty`, `price`) VALUES ('$orderId','$productId','$stockId','$qty','$price')"))
		{
		  mysqli_query($link,"UPDATE `stock` SET count=(count - '$qty') WHERE `stockId`='$stockId'");
		  $orderObj->status = "200";
		}
		else
		{
		  $orderObj->status = "202";
		  break;
		}
	}

}
else
{
	$orderObj->status = "202";
}

echo json_encode($orderObj);

mysqli_close($link);


?>