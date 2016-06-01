<?php
/*
 * NAVISEN Project
 * Projet de fin d'année CIR2
 * SIMONNEAUX Benoît & TROTIN Eddy
 * Mai 2016
 *
 */

// Controleur principal

/* --- PDO --- */

require_once '../models/DB.class.php';

$db = include '../config/db.php';
$pdo = new DB($db['dsn'], $db['host'], $db['login'], $db['pass'], $db['db']);

/* --- Routeur --- */
$p1 = isset($_GET['p']) ? $_GET['p'] : 'index';

if(file_exists('../views/p/' . $p1 . '.php')) {
  include '../views/p/' . $p1 . '.php';
} else
  include '../views/404.php';


?>
