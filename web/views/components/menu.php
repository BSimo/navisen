<a id="btn_menu"class="btn-floating btn-large">
	<img src="img/icons/menu.png">
</a>

<ul id="slide-out" class="side-nav full menu top_nav collapsible collapsible-accordion">
	<li class="menu_title">
		Navisen
	</li>
	<li id="where_am_i_menu">
		<a>
			<img src="img/icons/where_am_i.png"/>
			<p>Se localiser</p>
		</a>
	</li>
	<li id="events_list_menu">
		<a data-target="modal2" class="modal-trigger">
				<img src="img/icons/events_list.png">
				<p>Points d'intêret</p>
		</a>
	</li>

	<?php if(!$auth->isAuth()) { ?>
		<li id="login_menu">
			<a data-target="modal3" class="modal-trigger">
					<img src="img/icons/unlocked.png"/>
					<p>Connexion Admin</p>
			</a>
		</li>
	<?php } else { ?>
		<li id="logout_menu">
				<a href="/?logout">
					<img src="img/icons/unlocked.png"/>
					<p>Déconnexion</p>
				</a>
		</li>
	<?php } ?>

</ul>

<a href="#" data-activates="slide-out" class="button-collapse button-collapse-left">
	<i class="large mdi-navigation-menu"></i>
</a>
