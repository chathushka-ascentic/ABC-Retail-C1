	<?php
		
			$link = mysqli_connect("localhost","root","","abc-fashions") or die("Error " . mysqli_error($link));

		
			$ud=$_GET['userId'];
						
			$query = mysqli_query($link,"SELECT * FROM user WHERE userId='$ud'");
			
			$nm=mysqli_fetch_assoc($query) 
					
	?>
				
							
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
<link rel="stylesheet" type="text/css" href="default.css"  />

<title>Orders</title>
</head>

<body id="prof" class="prof">
		

<div class="header">
 
	<div class="logo" style="background-image:url('images/logo.png');"></div>
	 
	<div class="nm" >
		<h2><span style="font-weight:lighter">User: </span><?php echo $nm['fullname'] ; ?></h2>
	</div> 

</div>



<div class="body" >


<div class="order">
<table class="orderH"> 
<tbody>
	<tr ><th class="or1">Order Id</th><th class="or2">Order Date</th><th class="or3">Branch</th><th class="or4">Payment Method</th><th class="or5">Total Value</th><th class="or6">Details</th></tr>
	<?php
	
	 $quer = 
	 mysqli_query($link,"SELECT * FROM `order` ord LEFT OUTER JOIN payment p ON ord.payId=p.payId LEFT OUTER JOIN branch b ON ord.branchId=b.branchId WHERE ord.userId = '$ud' ORDER BY orderId DESC"); 
	 while($id = mysqli_fetch_assoc($quer)){ ?>
	    
    <tr> 
    <td align="center"><?php echo $a=$id['orderId']; ?></td>   
	<td align="center"><?php echo $id['orderDate']; ?></td> 
    <td align="left"><?php echo $id['branchName']; ?></td>
    <td align="left"><?php echo $id['methodName']; ?></td>
	<td align="right">$ <?php echo number_format($id['orderTotal'],2) ?></td>
    <td align="center"><a href="javascript:window.open('orderDetail.php?orderId= <?php echo $a; ?>', 'Order Details', 'width=1000,height=650');">Show more...</a></td>
    </tr>
	<?php }?>
</tbody>
</table>
</div>


</div>

</div>

<div class="footer"><h4><i>Thank you for shopping with us!!!</i></h4></div>


</body>

</html>
				

