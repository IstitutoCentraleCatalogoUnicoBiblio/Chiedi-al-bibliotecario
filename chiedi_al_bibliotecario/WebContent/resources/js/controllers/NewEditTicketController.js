assistenzaApp
		.registerCtrl(
				'NewEditTicketController',
				[
		'$scope',
		'$translate',
		'$routeParams',
		'$location',
		'$filter',
		'$timeout',
		"$route",
		"ApiServices",
		"LocalDataServices",
		function($scope, $translate, $routeParams, $location,
				$filter, $timeout, $route, ApiServices,
				LocalDataServices) {
			console.log("NewEditTicketController");
			$scope.controller = "new_ticket";
			$scope.tickets = LocalDataServices.getTickets();
			$scope.user = LocalDataServices.getUser();
			$scope.categories = LocalDataServices
					.getCategories();
			$scope.faqs = LocalDataServices.getFaqs();
			$scope.biblioteche = LocalDataServices
					.getBiblioteche();
			$scope.conf_polo = LocalDataServices.getConfPolo();
			$scope.applicationBuild = LocalDataServices
					.getBuild();
			LocalDataServices.setTickets([]);
			LocalDataServices.saveFiltersToDetail(null);

			$scope.reset = function() {
				$route.reload();
			}
			$scope.changeLanguage = function(key) {
				$translate.use(key);
			};
			$scope.logout = function() {
				LocalDataServices.logout();
				$location.path("/")
			};

			var init = function() {
				$scope.isEditingTicket = false;
				$scope.dismissAlert();
				$scope.initCat = "";//$scope.categories[0].id;
				$scope.isLoading = false;
				$scope.motivi = [ "Interesse personale", "Studio",
					"Lavoro", "Altro" ];
				var editingTicket = LocalDataServices.getEditingTicket();
				if(editingTicket.isEditing) {
					$scope.isEditingTicket = true;

					$scope.ticket = {
							ticket : editingTicket.ticket,
							files: []
					}
					$scope.initCat = editingTicket.ticket.id_cat.toString();
					$scope.controller = "edit_ticket";
				} else {
					$scope.isEditingTicket = false;

					$scope.ticket = {
							ticket : {
								id_user_ins : $scope.user.id,
								user_ute_ins : $scope.user.username,
								allegato:false,
								aperto:true,
								cod_biblioteca_ind:"",
								ho_gia_fatto:"",
								id_cat:"",
								motivo_richiesta:"",
								pubblico:false,
								testo:"",
								titolo:"",
								cod_polo: $scope.conf_polo.cod_polo
							},
							files: []
						};
				}
			}
			
			$scope.dismissAlert = function() {
				$("#alert").hide();
				$scope.error_text = "";
			}
			var showAlert = function (text) {
				$scope.error_text = text;
				$("#alert").show();
			/*	setTimeout(function () {
					$("#alert").hide();
				}, LocalDataServices.getHideAlertTime())
				*/
			};
		var uploadFile = function (file, ticketId) {
			$scope.isLoading = true;
			ApiServices
			.uploadFile(
					file,
					"ticket",
					ticketId)
			.then(
					function(
							success) {
						$scope.isLoading = false;
						if(!$scope.isEditingTicket)
							$location.path($scope.conf_polo.cod_polo + "/segnalazioni")
					});
		};		
		var addTicket  = function() {
			$scope.isLoading = true;
			$scope.ticket.file = $scope.ticket.files[0];
			$scope.ticket.ticket.id_cat = parseInt($scope.ticket.ticket.id_cat);
			ApiServices
					.addTicket($scope.ticket.ticket)
					.then(
							function(success) {

								if (isIn(success.data.responseKey, ["OK","MESSAGE_ERROR", "MESSAGE_MAIL_WRONG"])) {
									showAlert(success.data.responseKey)
									LocalDataServices
											.setTickets(success.data.responseObj.tickets)

									if ($scope.ticket.ticket.allegato) {
										uploadFile($scope.ticket.file, success.data.responseObj.newTicket.id)

									} else {
										$location
												.path($scope.conf_polo.cod_polo + "/segnalazioni")
									}
								} else {
									$scope.isLoading = false;
									showAlert(success.data.responseKey)

								}

							}, function(error) {
								$scope.isLoading = false;
							});
		};
		var updateTicket  = function() {
			$scope.isLoading = true;
			$scope.ticket.file = $scope.ticket.files[0];
			$scope.ticket.ticket.id_cat = parseInt($scope.ticket.ticket.id_cat);
			ApiServices
					.updateTicket($scope.ticket.ticket)
					.then(
							function(success) {
								$scope.isLoading = false;
								if (isIn(success.data.responseKey, ["OK","MESSAGE_ERROR", "MESSAGE_MAIL_WRONG"])) {
									showAlert(success.data.responseKey)
									
									if ($scope.ticket.files.length > 0) {
										uploadFile($scope.ticket.file, $scope.ticket.ticket.id)
									} 
								} else {
									$scope.isLoading = false;
									showAlert(success.data.responseKey)

								}

							}, function(error) {
								$scope.isLoading = false;
							});
			$scope.ticket.ticket.id_cat = $scope.ticket.ticket.id_cat.toString();

		};
		$scope.confirm = function() {
			if($scope.isEditingTicket){
				updateTicket();
			} else {
				addTicket();
			}
		};
		
		$scope.dettaglio = function() {
			$scope.isLoading = true;
			var ticket = $scope.ticket.ticket;
			ApiServices
					.getDettaglioTicket(ticket.id,
							$scope.user)
					.then(
							function(success) {
								$scope.isLoading = false
								if (success.data.responseKey == "OK") {

									LocalDataServices
											.setDettaglioTicket(success.data.responseObj);
									LocalDataServices.saveFiltersToDetail($scope.filters);
									$location
											.path($scope.conf_polo.cod_polo + "/dettaglio/"
													+ ticket.id);
								}
							}, function(error) {
								$scope.isLoading = false

							});

		};
		init();
} ]);