<?php

class DB {

	private $pdo;
	private $nbReq;

	public function __construct($dsn, $host, $login, $pass, $db) {
		try {
			$this->pdo = new PDO($dsn . ':host=' . $host . ';dbname='.$db, $login, $pass, array(
				PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC
			));
		} catch (PDOException $e) {
			throw new Exception('Unable to connect to {' . $driver . ':' . $host . '} on database {' . $db . '} with user {' . $login . '}.');
		}
	}

	public function getNbReq() {
		return $this->nbReq;
	}

	public function query($query) {
		$this->nbReq++;

		return $this->pdo->query($query);
	}

	public function arrQuery($query, $multi = false) {
		$this->nbReq++;

		$req = $this->pdo->query($query);
		$data = array();

		if (!$multi) $data= $req->fetch();
		else {
			$i = 0;
			while($fetch = $req->fetch()) {
				$data[$i] = $fetch;
				$i++;
			}
		}
		$req->closeCursor();

		return $data;
	}

	public function arrQueryByIndex($query, $index) {
		$this->nbReq++;

		$req = $this->pdo->query($query);
		$data = array();

		while($fetch = $req->fetch()) $data[$fetch[$index]] = $fetch;

		$req->closeCursor();
		return $data;
	}

	public function arrQueryByGroup($query, $group) {
		$this->nbReq++;

		$req = $this->pdo->query($query);
		$data = array();

		while($fetch = $req->fetch()) $data[$fetch[$group]][] = $fetch;

		$req->closeCursor();
		return $data;
	}

	public function prepare($query, $array) {
		$this->nbReq++;

		$req = $this->pdo->prepare($query);
		$req->execute($array);
		return $req;
	}

	public function arrPrepare($query, $array, $multi = false) {
		$this->nbReq++;

		$req = $this->pdo->prepare($query);
		$req->execute($array);

		$data = array();

		if (!$multi) $data = $req->fetch();
		else {
			$i = 0;
			while($fetch = $req->fetch()) {
				$data[$i] = $fetch;
				$i++;
			}
		}

		$req->closeCursor();
		return $data;
	}

	public function arrPrepareByIndex($query, $array, $index) {
		$this->nbReq++;

		$req = $this->pdo->prepare($query);
		$req->execute($array);
		$data = array();

		while($fetch = $req->fetch()) $data[$fetch[$index]] = $fetch;

		$req->closeCursor();
		return $data;
	}

	public function arrPrepareByGroup($query, $array, $index) {
		$this->nbReq++;

		$req = $this->pdo->prepare($query);
		$req->execute($array);
		$data = array();

		while($fetch = $req->fetch()) $data[$fetch[$index]][] = $fetch;

		$req->closeCursor();
		return $data;
	}

	public function queryNoReturn($query, $array = array()) {
		$this->nbReq++;

		if(count($array) > 0) {
			$req = $this->pdo->prepare($query);
			$req->execute($array);
		} else {
			$req = $this->pdo->query($query);
		}

		$req->closeCursor();
	}
}
