<?php


  //$data ='{"CustomerInsertRequest":{"email":"fff1@gmail.com","cust_pwd":"12345","fullname":"ffff","userId":0}}';
  //$data1 = json_decode($data);
  
  $data1 = json_decode(file_get_contents('php://input'));

  $FullName = $data1->CustomerInsertRequest->fullname;
  $Email = $data1->CustomerInsertRequest->email;
  $Pass = $data1->CustomerInsertRequest->cust_pwd;


$link = mysqli_connect("localhost","root","","abc-fashions") or die("Error " . mysqli_error($link));

$result = mysqli_query($link,"SELECT COUNT(*) AS cc FROM user WHERE email = '$Email'",0);
$result1=mysqli_fetch_assoc($result);

$userObj = new stdClass();

if($result1['cc'] > 0)
{
	$userObj-> status = "201";
	$userObj->userId = 0;
}
else
{
if(mysqli_query($link,"INSERT INTO user (`fullname`,`email`,`password`) VALUES('$FullName', '$Email', '$Pass')"))
{
	$last_id = mysqli_insert_id($link);

	$userObj->status = "200";
	$userObj->userId = $last_id;

}
else
{
	$userObj->status = "202";
	$userObj->userId = 0;

}
}

echo json_encode($userObj);

mysqli_close($link);


?>