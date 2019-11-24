<?php

class DB_Functions {

    private $conn;

    // constructor
    function __construct() {
        require_once 'DB_Connect.php';
        // connecting to database
        $db = new Db_Connect();
        $this->conn = $db->connect();
    }

    // destructor
    function __destruct() {
        
    }

    /**
     * Storing new user
     * returns user details
     */
    public function storeUser($name, $email, $password) {
        $uuid = uniqid('', true);
        $hash = $this->hashSSHA($password);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt

        $stmt = $this->conn->prepare("INSERT INTO users(unique_id, name, email, encrypted_password, salt, created_at) VALUES(?, ?, ?, ?, ?, NOW())");
        $stmt->bind_param("sssss", $uuid, $name, $email, $encrypted_password, $salt); 
        $result = $stmt->execute();
        $stmt->close();
        $wallet_amount = 0;
        $stmt1 = $this->conn->prepare("INSERT INTO wallet(unique_id, wallet_amount) VALUES(?, ?)");
        $stmt1->bind_param("si",$uuid,$wallet_amount);
        $result1 = $stmt1->execute();
        $stmt1->close();

        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM users WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();

            return $user;
        } else {
            return false;
        }
    }
	
	 /**
     * Add to wallet
     */
	 public function addWallet($unique_id, $wallet_amount) { 
		$stmt = $this->conn->prepare("INSERT INTO wallet(unique_id, wallet_amount) VALUES(?, ?)");
        $stmt->bind_param("si",$unique_id,$wallet_amount);
        $result = $stmt->execute();
        $stmt->close();
		
        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM wallet WHERE unique_id = ?");
            $stmt->bind_param("s", $unique_id);
            $stmt->execute();
            $wallet_value = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $wallet_value;
        } else {
            return false;
        }
    }

    /**
     * Buy Ticket
     */
    public function buyTicket($unique_id,$from,$to,$time_from,$time_to,$travel_time,$track,$price,$date) { 
        $stmt = $this->conn->prepare("INSERT INTO  ticket (unique_id, _from, _to,time_from,time_to,travel_time,track,price,_date) VALUES (?,?,?,?,?,?,?,?,?)");
        $stmt->bind_param("sssssssss",$unique_id,$from,$to,$time_from,$time_to,$travel_time,$track,$price,$date);
        $result = $stmt->execute();
        $stmt->close();
        
        //check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM ticket WHERE unique_id = ?");
            $stmt->bind_param("s", $unique_id);
            $stmt->execute();
            $value = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $value;
        } else {
            return false;
        }
    }
	 /**
     * updateWallet
     */
	 public function updateWallet($unique_id, $wallet_amount) { 
		$stmt = $this->conn->prepare("SELECT * FROM wallet WHERE unique_id = ?");
        $stmt->bind_param("s", $unique_id);
		$stmt->execute();
        $value = $stmt->get_result()->fetch_assoc();
        $stmt->close();
		$wallet_amount = $wallet_amount + $value["wallet_amount"];
		$stmt = $this->conn->prepare("UPDATE wallet SET wallet_amount = ? WHERE unique_id = ?");
        $stmt->bind_param("is", $wallet_amount,$unique_id);
		$result = $stmt->execute();
        $stmt->close();
		 // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM wallet WHERE unique_id = ?");
            $stmt->bind_param("s", $unique_id);
            $stmt->execute();
            $wallet_update = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $wallet_update;
        } else {
            return false;
        }
		
       
    }

    public function subtractWallet($unique_id, $price) { 
        $stmt = $this->conn->prepare("SELECT * FROM wallet WHERE unique_id = ?");
        $stmt->bind_param("s", $unique_id);
        $stmt->execute();
        $value = $stmt->get_result()->fetch_assoc();
        $stmt->close();
        $value["wallet_amount"] = $value["wallet_amount"] - (int)$price ;
        $stmt = $this->conn->prepare("UPDATE wallet SET wallet_amount = ? WHERE unique_id = ?");
        $stmt->bind_param("is", $value["wallet_amount"],$unique_id);
        $result = $stmt->execute();
        $stmt->close();
         // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM wallet WHERE unique_id = ?");
            $stmt->bind_param("s", $unique_id);
            $stmt->execute();
            $wallet_update = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $wallet_update;
        } else {
            return false;
        }
        
       
    }

    public function getAllTicket($unique_id) {
        $stmt = $this->conn->prepare("SELECT * FROM ticket WHERE unique_id = ? ORDER BY id desc");
        $stmt->bind_param("s", $unique_id);
        $stmt->execute();
        $ticket = $stmt->get_result();
        $stmt->close();
        return $ticket;
    }

    
    public function getWallet($unique_id){
      $stmt = $this->conn->prepare("SELECT * FROM wallet WHERE unique_id = ?");
        $stmt->bind_param("s", $unique_id);
        $stmt->execute();
        $value = $stmt->get_result()->fetch_assoc();
        $stmt->close();
        return $value;  
    }

    //Train Search
    public function searchTrainFrom ($from){
            $stmt = $this->conn->prepare("SELECT * FROM train WHERE place = ?");
            $stmt->bind_param("s", $from);
            $stmt->execute();
            $value = $stmt->get_result()->fetch_assoc();
            $stmt->close();
        return $value;
    }
    public function searchTrainTo ($to){
            $stmt = $this->conn->prepare("SELECT * FROM train WHERE place = ?");
            $stmt->bind_param("s", $to);
            $stmt->execute();
            $value = $stmt->get_result()->fetch_assoc();
            $stmt->close();
        return $value;
    }
    public function cancelTicket ($id){
        $stmt = $this->conn->prepare( "DELETE FROM ticket WHERE id = ? ");
        $stmt->bind_param("i", $id);
        $result = $stmt->execute();
        $stmt->close();
        return $result;  
    }

    /**
     * Get user by email and password
     */
    public function getUserByEmailAndPassword($email, $password) {

        $stmt = $this->conn->prepare("SELECT * FROM users WHERE email = ?");

        $stmt->bind_param("s", $email);

        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();

            // verifying user password
            $salt = $user['salt'];
            $encrypted_password = $user['encrypted_password'];
            $hash = $this->checkhashSSHA($salt, $password);
            // check for password equality
            if ($encrypted_password == $hash) {
                // user authentication details are correct
                return $user;
            }
        } else {
            return NULL;
        }
    }
	
	
	
    /**
     * Check user is existed or not
     */
    public function isUserExisted($email) {
        $stmt = $this->conn->prepare("SELECT email from users WHERE email = ?");

        $stmt->bind_param("s", $email);

        $stmt->execute();

        $stmt->store_result();

        if ($stmt->num_rows > 0) {
            // user existed 
            $stmt->close();
            return true;
        } else {
            // user not existed
            $stmt->close();
            return false;
        }
    }
	public function isWalletExisted($unique_id) {
        $stmt = $this->conn->prepare("SELECT unique_id from wallet WHERE unique_id = ?");

        $stmt->bind_param("s", $unique_id);

        $stmt->execute();

        $stmt->store_result();

        if ($stmt->num_rows > 0) {
            // user existed 
            $stmt->close();
            return true;
        } else {
            // user not existed
            $stmt->close();
            return false;
        }
    }
    /**
     * Encrypting password
     * @param password
     * returns salt and encrypted password
     */
    public function hashSSHA($password) {

        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }

    /**
     * Decrypting password
     * @param salt, password
     * returns hash string
     */
    public function checkhashSSHA($salt, $password) {

        $hash = base64_encode(sha1($password . $salt, true) . $salt);

        return $hash;
    }

}

?>
