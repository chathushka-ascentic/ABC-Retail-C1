	<?php
		
			$link = mysqli_connect("localhost","root","","abc-fashions") or die("Error " . mysqli_error($link));
			$od=$_GET['orderId'];
					
	?>
				
							
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
<link rel="stylesheet" type="text/css" href="default.css"  />

<title>Order Detail</title>
</head>

<body id="prof" class="prof">
		

<div class="header">
 
	<div class="logo" style="background-image:url('images/logo.png');"></div>
	 
	<div class="nm" >
		<h2><span style="font-weight:lighter"><?php echo "Order Id : $od"; ?></span></h2>
	</div> 

</div>



<div class="body" >


<div class="order">
<table class="orderD"> 
		<tbody>
		<tr ><th class="od1">Item Name</th><th class="od2">Size</th><th class="od3">Qty</th><th class="od4">Price</th></tr>
		<?php
		$qr = mysqli_query($link,"SELECT * FROM `orderdetail` ord LEFT OUTER JOIN stock st ON ord.stockId=st.stockId LEFT OUTER JOIN product p ON ord.productId=p.productId LEFT OUTER JOIN size si ON st.sizeId=si.sizeId WHERE ord.orderId='$od'"); 
		
		 while($od=mysqli_fetch_assoc($qr)){ ?>
		<tr>
        <td align="left"><?php echo $od['productName']; ?></td>
        <td align="center"><?php echo $od['sizeName']; ?></td>
        <td align="center"><?php echo $od['qty']; ?></td>
		<td align="right">$ <?php echo number_format($od['price'],2); ?></td>
		</tr>
		<?php } ?>
		
</tbody>
</table>
</div>


</div>

</body>

</html>
				

