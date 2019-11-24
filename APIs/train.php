<?php
require_once 'include/DB_Functions.php';
$db = new DB_Functions();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['from']) && isset($_POST['to'])) {

    $from = $_POST['from'];
    $to = $_POST['to'];
    $trainFrom = $db->searchTrainFrom($from);
    $trainTo = $db->searchTrainTo($to);
    $response["id_from"] = $trainFrom["id"];
    $response["place_from"] = $trainFrom["place"];
    $response["track_1_from"] = $trainFrom["track_1"];
    $response["track_2_from"] = $trainFrom["track_2"];
    $response["id_to"] = $trainTo["id"];
    $response["place_to"] = $trainTo["place"];
    $response["track_1_to"] = $trainTo["track_1"];
    $response["track_2_to"] = $trainTo["track_2"];
    echo json_encode($response);
    

	

    
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters from or to is missing!";
    echo json_encode($response);
}
?>
