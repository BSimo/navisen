<?php

/*
 * Controlleur AJAX
 * Gère les réponses aux requêtes faites à /ajax/*
 * Doit retourner un texte au format JSON
 */
class AJAX {

  private $db;

  public function __construct($db) {
    $this->db = $db;

    static::printHeader();

    $data = isset($_POST['data']) ? json_decode($_POST['data']) : null;
    $this->analyze($data);
  }

  /*
   * Analyze the POST data, and redirect the data by the URI
   */
  private function analyze($data) {
    $p2 = isset($_GET['p2']) ? $_GET['p2'] : '';
    if($p2 == 'retrievePoints') {
      $this->analyseRetrievePoints();
    } else {
      static::printError("Not Found");
    }
  }

  private function analyseRetrievePoints() {
    $reqPoints = $this->db->query("SELECT * FROM point", true);
    $reqNeighbors = $this->db->query("SELECT * FROM point_neighbor", true);

    $ret = new stdClass();
    $ret->points = $reqPoints->fetchAll();
    $ret->neighbors = $reqNeighbors->fetchAll();

    static::printData($ret);
  }

  private static function printHeader() {
    header('Content-Type: application/json');
  }

  private static function printError($msg) {
    $ret = new stdClass();
    $ret->type = "error";
    $ret->error_msg = $msg;

    static::printData($ret);
  }

  private static function printData($data) {
    echo json_encode($data, JSON_PRETTY_PRINT);

    exit();
  }
}
