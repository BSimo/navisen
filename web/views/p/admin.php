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

require_once __DIR__ . '/../components/map.php';
require_once __DIR__ . '/../components/menu.php';
require_once __DIR__ . '/../components/slide.php';
require_once __DIR__ . '/../components/room.php';
require_once __DIR__ . '/../components/events_list.php';
require_once __DIR__ . '/../components/admin.php';
require_once __DIR__ . '/../components/login_form.php';

require_once __DIR__ . '/../components/footer_admin.php';
?>
