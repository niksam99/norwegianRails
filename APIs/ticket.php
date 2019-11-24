<?php
require_once 'include/DB_Functions.php';
$db = new DB_Functions();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['unique_id']) && isset($_POST['from']) && isset($_POST['to']) && isset($_POST['time_from']) && isset($_POST['time_to']) && isset($_POST['travel_time']) && isset($_POST['track']) && isset($_POST['price']) && isset($_POST['date'])) {
    $unique_id = $_POST['unique_id'];
    $from = $_POST['from'];
    $to = $_POST['to'];
    $time_from = $_POST['time_from'];
    $time_to = $_POST['time_to'];
    $travel_time = $_POST['travel_time'];
    $track = $_POST['track'];
    $price = $_POST['price'];
    $date = $_POST['date'];



   $insert = $db->buyTicket($unique_id,$from,$to,$time_from,$time_to,$travel_time,$track,$price,$date);
    $subtract= $db->subtractWallet($unique_id ,$price );
    
    
    if ($insert != false && $subtract != false) {

        $response["error"] = FALSE;
        $response["unique_id"] = $insert["unique_id"];
        $response["from"] = $insert["_from"];
        $response["to"] = $insert["_to"];
        $response["time_from"] = $insert["time_from"];
        $response["time_to"] = $insert["time_to"];
        $response["travel_time"] = $insert["travel_time"];
        $response["track"] = $insert["track"];
        $response["price"] = $insert["price"];
        $response["date"] = $insert["_date"];
        $response["wallet_amount"] = $subtract["wallet_amount"];
        echo json_encode($response);

    } else {
        // user is not found with the credentials
        $response["error"] = TRUE;
        $response["message"] = "Something went Wrong";
        echo json_encode($response);
    }
    

	

    
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters is missing!";
    echo json_encode($response);
}
?>
