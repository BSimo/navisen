<?php
	require_once __DIR__ . '/../components/header.php';
	require_once __DIR__ . '/../components/map.php';
	require_once __DIR__ . '/../components/menu.php';
	require_once __DIR__ . '/../components/slide.php';
	require_once __DIR__ . '/../components/room.php';
	require_once __DIR__ . '/../components/events_list.php';
	require_once __DIR__ . '/../components/locate.php';
	require_once __DIR__ . '/../components/login_form.php';
	if($auth->isAuth())
		require_once __DIR__ . '/../components/admin.php';

	require_once __DIR__ . 	'/../components/footer.php';
?>
