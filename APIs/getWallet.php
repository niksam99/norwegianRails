<?php
require_once 'include/DB_Functions.php';
$db = new DB_Functions();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['unique_id'])) {

    $unique_id = $_POST['unique_id'];
    $wallet = $db->getWallet($unique_id);
    $response["error"] = FALSE;
    $response["unique_id"] = $wallet["unique_id"];
    $response["wallet_amount"]= $wallet["wallet_amount"];
    echo json_encode($response);
    
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters missing";
    echo json_encode($response);
}
?>
