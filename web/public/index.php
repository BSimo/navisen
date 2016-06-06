<?php
/*
 * NAVISEN Project
 * Projet de fin d'année CIR2
 * SIMONNEAUX Benoît & TROTIN Eddy
 * Mai 2016
 *
 */

// Controleur principal

/* --- Test Déconnexion --- */
if(isset($_GET['logout'])) {
  setcookie('sid', null, -1, '/');
  header('Location: /');
  exit;
}

/* --- Submodule: PDO --- */
require_once '../models/DB.class.php';
require_once '../models/Secure.class.php';

$db_data = include '../config/db.php';
$db = new DB($db_data['dsn'], $db_data['host'], $db_data['login'], $db_data['pass'], $db_data['db']);

/* --- Submodule: Authentification System --- */
require_once '../controllers/auth.php';
$auth = new Auth($db);


/* --- Routeur --- */
$p = isset($_GET['p']) ? $_GET['p'] : 'index';

if($p == 'ajax') {

  /* --- Submodule: AJAX --- */
  require_once '../controllers/ajax.php';
  $ajax = new AJAX($db);

} else if(file_exists('../views/p/' . $p . '.php')) {

  /* --- Include the view --- */
  include '../views/p/' . $p . '.php';

} else {

  /* --- 404 Not Found... --- */
  include '../views/404.php';

}

?>
