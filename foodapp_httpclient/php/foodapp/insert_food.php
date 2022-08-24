<?php
if (!isset($_POST)) {
    $response = array('status' => 'failed', 'data' => null);
    sendJsonResponse($response);
    die();
}
include_once("dbconnect.php");
$foodname = addslashes($_POST['name']);
$foodprice = addslashes($_POST['price']);
$fooddesc = $_POST['desc'];

$sqlinsert = "INSERT INTO `tbl_foods`(`food_name`, `food_price`, `food_desc`) VALUES ('$foodname','$foodprice','$fooddesc')";

if ($conn->query($sqlinsert) === TRUE) {
    $response = array('status' => 'success', 'data' => null);
    sendJsonResponse($response);
} else {
    $response = array('status' => 'failed', 'data' => null);
    sendJsonResponse($response);
}

function sendJsonResponse($sentArray)
{
    header('Content-Type: application/json');
    echo json_encode($sentArray);
}
?>