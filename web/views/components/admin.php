  <button data-target="admin_panel_modal" class="btn admin_switch modal-trigger" onclick="function (){$('#admin_panel_modal').display='none';}">Admin panel</button>
  
  <div id = "admin_panel_modal" class="modal bottom-sheet">
  	<div  id="admin_panel" class="modal-content">
  		
  		<a id="exit_admin_panel" class="btn-floating btn-large waves-effect waves-light red"><i class="material-icons">add</i></a>
  		
  		<div>
  			<form action="#">
  				<p>
  					<input type="checkbox" id="displayVertex" checked="checked"/>
  					<label for="displayVertex">Afficher points de passage</label>
  				</p>

  				<p>
  					<input type="checkbox" id="displayEdges" />
  					<label for="displayEdges">Afficher les lien entre les points</label>
  				</p>

  				<p>
  					<input type="checkbox" id="displayPOI" />
  					<label for="displayPOI">Afficher points d'interets</label>
  				</p>
  			</form>



  			<div class="switch">
  				<label>
  					<input type="checkbox" class="edit_points">
  					<span class="lever"></span>
  					Mode ajout de point
  				</label>
  			</div>
  		</div>

  		<div id = "legend">
  			<p><img src="img/map/blue_dot.png">Salles</p>
  			<p><img src="img/map/green_dot.png">Angles et jonctions</p>
  		</div>

  	</div>
  </div>