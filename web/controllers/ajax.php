<?php
/*
 * Controlleur AJAX
 * Gère les réponses aux requêtes faites à /ajax/*
 * Doit retourner un texte au format JSON
 */
class AJAX {

  private $db;
  private $auth;

  public function __construct($db, $auth) {
    $this->db = $db;
    $this->auth = $auth;

    static::printHeader();

    $data = isset($_POST['data']) ? json_decode($_POST['data']) : null;
    $this->analyze($data);
  }

  /*
   * Analyze the POST data, and redirect the data by the URI
   */
  private function analyze($data) {
    $p2 = isset($_GET['p2']) ? $_GET['p2'] : '';
    $p3 = isset($_GET['p3']) ? $_GET['p3'] : null;

    if($p2 == 'retrievePoints') {
      $this->analyseRetrievePoints();
    } else if($this->auth->isAuth() && $p2 == 'newPoint') {
      $this->analyseNewPoint($data);
    } else if($p2 == 'getPoint') {
      $this->analyseGetPoint($p3);
    } else if($this->auth->isAuth() && $p2 == 'updatePoint') {
      $this->analyseUpdatePoint($p3, $data);
    } else if($this->auth->isAuth() && $p2 == 'deletePoint') {
      $this->analyseDeletePoint($p3);
    } else if($this->auth->isAuth() && $p2 == 'newNeighbor') {
      $this->analyseNewNeighbor($data);
    } else if($this->auth->isAuth() && $p2 == 'deleteNeighbor') {
      $this->analyseDeleteNeighbor($data);
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

  private function analyseNewPoint($data) {
      if($data == null ||
          !property_exists($data, 'floor') ||
          !property_exists($data, 'x') ||
          !property_exists($data, 'y'))
      {
        static::printError("Missing some datas");
      }

      $reqFloor = $this->db->arrPrepare("SELECT * FROM floor WHERE floor_id = ?", array($data->floor));

      if(!$reqFloor)
        static::printError("Unknown floor");

      $reqAdd = $this->db->queryNoReturn("INSERT INTO point (floor_id, point_pos_x, point_pos_y) VALUES (?, ?, ?)", array($data->floor, $data->x, $data->y));

      $ret = new stdClass();
      $ret->type = "success";
      $ret->point_id = $this->db->lastInsertId();

      static::printData($ret);
  }

  private function analyseGetPoint($id) {
    if($id == null)
      static::printError("Missing point_id");

    if(!$this->checkIfPointExist($id))
      static::printError("Unknown point");

    $ret = new stdClass();
    $ret->type = "success";
    $ret->point = $this->db->arrPrepare("SELECT * FROM point WHERE point_id = ?", array($id));

    static::printData($ret);
  }

  private function analyseUpdatePoint($id, $data) {
    if($id == null)
      static::printError("Missing point_id");

    if(!$this->checkIfPointExist($id))
      static::printError("Unknown point");

    if($data == null)
      static::printError("Missing some datas");

    if(property_exists($data, 'name'))
      $reqUpd = $this->db->queryNoReturn("UPDATE point SET point_name = ? WHERE point_id = ?", array($data->name, $id));

    if(property_exists($data, 'x'))
      $reqUpd = $this->db->queryNoReturn("UPDATE point SET point_pos_x = ? WHERE point_id = ?", array($data->x, $id));

    if(property_exists($data, 'y'))
      $reqUpd = $this->db->queryNoReturn("UPDATE point SET point_pos_y = ? WHERE point_id = ?", array($data->y, $id));

    $ret = new stdClass();
    $ret->type = "success";
    $ret->point = $this->db->arrPrepare("SELECT * FROM point WHERE point_id = ?", array($id));

    static::printData($ret);
  }

  private function analyseDeletePoint($id) {
    if($id == null)
      static::printError("Missing point_id");

    if(!$this->checkIfPointExist($id))
      static::printError("Unknown point");

    $reqDelete = $this->db->queryNoReturn("DELETE FROM point WHERE point_id = ?", array($id));

    $ret = new stdClass();
    $ret->type = "success";

    static::printData($ret);
  }

  private static function analyseNewNeighbor($data) {
    if($data == null ||
        !property_exists($data, 'point_id') ||
        !property_exists($data, 'neighbor_id'))
    {
      static::printError("Missing some datas");
    }

    if(!$this->checkIfPointExist($data->point_id) || !$this->checkIfPointExist($data->neighbor_id))
      static::printError("Unknown point or neighbor");

    if($this->checkIfNeighborExist($data->point_id, $data->neighbor_id))
      static::printError("Relation already exist");

    $reqNew = $this->db->queryNoReturn("INSERT INTO point_neighbor (point_id, neighbor_id) VALUES (?, ?)", array($point_id, $neighbor_id));

    $ret = new stdClass();
    $ret->type = "success";

    static::printData($ret);
  }

  private static function analyseDeleteNeighbor($data) {
    if($data == null ||
        !property_exists($data, 'point_id') ||
        !property_exists($data, 'neighbor_id'))
    {
      static::printError("Missing some datas");
    }

    if(!$this->checkIfPointExist($data->point_id) || !$this->checkIfPointExist($data->neighbor_id))
      static::printError("Unknown point or neighbor");

    if(!$this->checkIfNeighborExist($data->point_id, $data->neighbor_id))
      static::printError("Relation already not exist");

    $reqDelete = $this->db->queryNoReturn("DELETE FROM point_neighbor
      WHERE (point_id = ? AND neighbor_id = ?)
      OR (point_id = ? AND neighbor_id = ?)", array($point, $neighbor, $neighbor, $point));

    $ret = new stdClass();
    $ret->type = "success";

    static::printData($ret);

  }

  private function checkIfPointExist($id) {
    $reqPoint = $this->db->arrPrepare("SELECT * FROM point WHERE point_id = ?", array($id));

    if(!$reqPoint)
      return false;
    return true;
  }

  private function checkIfNeighborExist($point, $neighbor) {
    $reqNeighbor = $this->db->arrPrepare("SELECT * FROM point_neighbor
      WHERE (point_id = ? AND neighbor_id = ?)
      OR (point_id = ? AND neighbor_id = ?)", array($point, $neighbor, $neighbor, $point));

    if(!$reqNeighbor)
      return false;
    return true;
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
