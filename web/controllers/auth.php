<?php

/*
 * Controlleur d'Authentification
 * Gère l'authentification des utilisateurs
 * Vérification de l'utilisateur et Création des tokens d'authentification
 */
class Auth {

  private $db;
  private $token;
  private $user_id = null;
  private $user = null;

  public function __construct($db) {
      $this->db = $db;

      $this->token = isset($_COOKIE['sid']) ? $_COOKIE['sid'] : null;
      if($this->token != null)
        $this->retrieveUserData();
  }

  public function isAuth() {
      return $this->user_id != null;
  }

  public function retrieveUserData() {
    $req_token = $this->db->arrPrepare("SELECT user_id FROM user_auth_token WHERE token = ?", array($this->token));
    if(!isset($req_token['user_id'])) // Bad Token
      return;

    $this->user_id = $req_token['user_id'];
    $req_user = $this->db->arrPrepare("SELECT * FROM user WHERE id = ?", array($this->user_id));
    $this->user = $req_user;
  }

  public function loginUser($username, $password) {
    $req = $this->db->arrPrepare("SELECT * FROM user WHERE user_name = ?", array($username));

    if(count($req) == 0)
      return;

    if(!Secure::testPassword($password, $req['password']))
      return;

    $token = $this->generateAuthToken($req['user_id']);

    setcookie('sid', $token, time() + 3600, '/');

    $this->retrieveUserData();
  }

  public function generateAuthToken($user_id) {
    do {
      $token = sha1(Secure::randStr(32));
    } while ($this->db->arrPrepare("SELECT COUNT(*) as count FROM user_auth_token WHERE token = ?", array($token))['count'] > 0);

    $this->db->queryNoReturn("INSERT INTO user_auth_token (user_id, token) VALUES (?, ?)", array($user_id, $token));

    return $token;
  }

  public function getUser() {
    return $this->user;
  }
}
