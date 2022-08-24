<?php
if (!isset($_POST)) {
    $response = array('status' => 'failed', 'data' => null);
    sendJsonResponse($response);
    die();
}

include_once("dbconnect.php");
$foodid = $_POST['id'];
$foodname = addslashes($_POST['name']);
$foodprice = addslashes($_POST['price']);
$fooddesc = $_POST['desc'];

$updatefood = "UPDATE `tbl_foods` SET `food_name`= '$foodname', `food_price`= '$foodprice', `food_desc`= '$fooddesc' WHERE id = '$foodid'";    

if ($conn->query($updatefood)){
    $response = array('status' => 'success', 'data' => null);    
}else{
    $response = array('status' => 'failed', 'data' => null);
}

sendJsonResponse($response);

function sendJsonResponse($sentArray)
{
    header('Content-Type: application/json');
    echo json_encode($sentArray);
}

?>