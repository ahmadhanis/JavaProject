<?php
error_reporting(0);
include_once("dbconnect.php");
$foodname = addslashes($_GET['foodname']);
$sqlloadfoods = "SELECT * FROM tbl_foods WHERE food_name LIKE '%$foodname%'";
$result = $conn->query($sqlloadfoods);
if ($result->num_rows > 0) {
    $foods["foods"] = array();
    while ($row = $result->fetch_assoc()) {
        $foodlist = array();
        $foodlist['id'] = $row['id'];
        $foodlist['food_name'] = $row['food_name'];
        $foodlist['food_price'] = $row['food_price'];
        $foodlist['food_desc'] = $row['food_desc'];
        array_push($foods["foods"],$foodlist);
    }
    $response = array('status' => 'success', 'data' => $foods);
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