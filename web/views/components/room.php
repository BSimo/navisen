	<div id="roomModal" class="modal bottom-sheet">

		<div class="modal-content">

			<div>
			  <a id="exit_point_config_panel" class="btn-floating btn-large waves-effect waves-light red"><i class="material-icons">add</i></a>
				<h4>Room n°45</h4>
				<p>CIR project room</p>
				<a id="go_btn" class="btn-floating btn-large waves-effect waves-light red"><img src="img/icons/go.png" alt=""/></a>
			</div>


			<div class="mark_admin" style="display: none;">
				<div id="mark_form" class="input-field col s12">
					<div>
						<h5>Chemin vers :</h5>
						<select multiple>
						</select>
					</div>
				</div>
				<div class="input-field col s6">
					<input id="last_name" type="text" class="validate">
					<label for="last_name">Nom de la salle</label>
				</div>
				<div id="mark_desc_div" class="row">
					<form class="col s12">
						<div class="row">
							<div class="input-field col s12">
							<textarea id="mark_desc" class="materialize-textarea"></textarea>
							<label for="mark_desc">Textarea</label>
							</div>
						</div>
					</form>
				</div>

				<button id="submit_point_config_panel" class="btn waves-effect waves-light">Submit
	   			<i class="material-icons right">send</i>
	  		</button>
				<div id="qrcode" style="display: none;"></div>
			</div>

		</div>
	</div>
