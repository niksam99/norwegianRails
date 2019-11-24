<?php
require_once 'include/DB_Functions.php';
$db = new DB_Functions();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['unique_id']) && isset($_POST['wallet_amount'])) {

    $unique_id = $_POST['unique_id'];
    $wallet_amount = $_POST['wallet_amount'];
    
	
	if ($db->isWalletExisted($unique_id)) {
        //update wallet
		$wallet_update = $db->updateWallet($unique_id, $wallet_amount);
		if ($wallet_update != false) {
        $response["error"] = FALSE;
        $response["unique_id"] = $wallet_update["unique_id"];
        $response["wallet_amount"]= $wallet_update["wallet_amount"];
        echo json_encode($response);
    } else {
        $response["error"] = TRUE;
        $response["error_msg"] = "Something went wrong in update!";
		echo json_encode($response);
    }
    } else {
        $wallet_value = $db->addWallet($unique_id, $wallet_amount);
		if ($wallet_value != false) {
        $response["error"] = FALSE;
        $response["unique_id"] = $wallet_value["unique_id"];
        $response["wallet_amount"]= $wallet_value["wallet_amount"];
        echo json_encode($response);
    } else {
        $response["error"] = TRUE;
        $response["error_msg"] = "Something went wrong!";
		echo json_encode($response);
    }
    }
	

    
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters unique_id or wallet_amount is missing!";
    echo json_encode($response);
}
?>
