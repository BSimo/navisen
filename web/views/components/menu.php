<a id="btn_menu"class="btn-floating btn-large">
	<img src="img/icons/menu.png">
</a>

<ul id="slide-out" class="side-nav full menu top_nav collapsible collapsible-accordion">
	<li class="menu_title">
		Navisen
	</li>
	<li id="where_am_i_menu">
		<a>
			<i class="material-icons">my_location</i>
			<p>Se localiser</p>
		</a>
	</li>
	<li id="events_list_menu">
		<a data-target="modal2" class="modal-trigger">
			<i class="material-icons">location_on</i>
			<p>Points d'intêret</p>
		</a>
	</li>

	<?php if(!$auth->isAuth()) { ?>
		<li id="login_menu">
			<a data-target="modal3" class="modal-trigger">
				<i class="material-icons">https</i>
				<p>Connexion Admin</p>
			</a>
		</li>
	<?php } else { ?>
		<li id="logout_menu">
				<a href="/?logout">
					<i class="material-icons">exit_to_app</i>
					<p>Déconnexion</p>
				</a>
		</li>
	<?php } ?>

</ul>

<a href="#" data-activates="slide-out" class="button-collapse button-collapse-left">
	<i class="large mdi-navigation-menu"></i>
</a>
