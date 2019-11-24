<?php
require_once 'include/DB_Functions.php';
$db = new DB_Functions();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['id'])) {

    $id = $_POST['id'];
   $result = $db->cancelTicket($id);
    $response["error"] = FALSE;
    echo json_encode($response);

    
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters missing";
    echo json_encode($response);
}
?>