<?php
if (!isset($_POST)) {
    $response = array('status' => 'failed', 'data' => null);
    sendJsonResponse($response);
    die();
}
include_once("dbconnect.php");
$foodid = $_POST['id'];
$sqldel = "DELETE FROM `tbl_foods` WHERE id = '$foodid'";
if ($conn->query($sqldel) === TRUE) {
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