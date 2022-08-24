<?php
if (!isset($_POST)) {
    $response = array('status' => 'failed', 'data' => null);
    sendJsonResponse($response);
    die();
}
include_once("dbconnect.php");
$sqlloadfoods = "SELECT * FROM tbl_foods";
$result = $conn->query($sqlloadfoods);
if ($result->num_rows > 0) {
    //do something
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