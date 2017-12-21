<?php


  //$data ='{"CustomerUpdateRequest":{"contactNo1":"0777111111", "contactNo2":"0777222222","address":"panadura","fullname":"ffff" , "email":"nipunaaa@gmail.com"}}';
  //$data1 = json_decode($data);
  
  $data1 = json_decode(file_get_contents('php://input'));

  $FullName = $data1->CustomerUpdateRequest->fullname;
  $con1 = $data1->CustomerUpdateRequest->contactNo1;
  $con2 = $data1->CustomerUpdateRequest->contactNo2;
  $addr = $data1->CustomerUpdateRequest->address;
  $Email = $data1->CustomerUpdateRequest->email;


$link = mysqli_connect("localhost","root","","abc-fashions") or die("Error " . mysqli_error($link));

$userObj = new stdClass();

if(mysqli_query($link,"UPDATE user SET `fullname`='$FullName' , `contactno1` = '$con1' , `contactno2` = '$con2' , address = '$addr' WHERE email = '$Email'"))
{
	$userObj->status = "200";

}
else
{
	$userObj->status = "202";
}

echo json_encode($userObj);

mysqli_close($link);


?>