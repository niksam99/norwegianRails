<?php
require_once 'include/DB_Functions.php';
$db = new DB_Functions();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['unique_id'])) {

    $unique_id = $_POST['unique_id'];
    $result = $db->getAllTicket($unique_id);

    $response["ticket"] = array();
 
            // looping through result and preparing tasks array
            while ($ticket = $result->fetch_assoc()) {
                $tmp = array();
                $tmp["id"] = $ticket["id"];
                $tmp["from"] = $ticket["_from"];
                $tmp["to"] = $ticket["_to"];
                $tmp["time_from"] = $ticket["time_from"];
                $tmp["time_to"] = $ticket["time_to"];
                $tmp["travel_time"] = $ticket["travel_time"];
                $tmp["track"] = $ticket["track"];
                $tmp["price"] = $ticket["price"];
                $tmp["date"] = $ticket["_date"];
                array_push($response["ticket"], $tmp);
            }


     echo json_encode($response);
    
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters missing";
    echo json_encode($response);
}
?>
