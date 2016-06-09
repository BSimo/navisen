		<script type="text/javascript" src="js/reader_html5-qrcode.min.js"></script>
		<script type="text/javascript" src="js/reader_jsqrcode-combined.min.js"></script>
		<script type="text/javascript" src="js/writer_qrcode.js"></script>
		<script type="text/javascript" src="js/leaflet.js"></script>
		<script type="text/javascript" src="js/materialize/bin/materialize.min.js"></script>
		<script type="text/javascript">
			var is_admin = <?= $auth->isAuth() ? 'true' : 'false'; ?>;
		</script>
		<script type="text/javascript" src="js/components/locate.js"></script>

		<script type="text/javascript" src="js/components/ws.js"></script>
		<script type="text/javascript" src="js/components/ajax.js"></script>
		<script type="text/javascript" src="js/components/mark.js"></script>
		<script type="text/javascript" src="js/components/slide.js"></script>
		<script type="text/javascript" src="js/components/room.js"></script>
		<script type="text/javascript" src="js/components/map.js"></script>
		<script type="text/javascript" src="js/components/menu.js"></script>
		<?php if($auth->isAuth()) { ?>
			<script type="text/javascript" src="js/components/admin.js"></script>
		<?php } ?>
		<script type="text/javascript" src="js/index.js"></script>
  </body>
</html>
