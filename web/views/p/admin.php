<?php

if(isset($_POST['submit_login'])) {
  $username = isset($_POST['name']) ? $_POST['name'] : null;
  $password = isset($_POST['pass']) ? $_POST['pass'] : null;

  if($username != null && $password != null && !$auth->loginUser($username, $password))
    header("Location: /?error_login");
}

if(!$auth->isAuth())
  header("Location: /");

require_once __DIR__ . '/../components/header.php';

require_once __DIR__ . '/../components/menu.php';

require_once __DIR__ . '/../components/footer.php';
?>
