assistenzaApp
		.registerCtrl(
				'DettaglioTicketController',
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
							console.log("DettaglioTicketController");
							$scope.controller = "dettaglio_ticket";
							$scope.applicationBuild = LocalDataServices
									.getBuild();
							$scope.isLoading = false;
							$scope.ticket = LocalDataServices
									.getDettaglioTicket();
							$scope.faqs =  LocalDataServices.getFaqs();
							$scope.categories = LocalDataServices
									.getCategories();
							$scope.user = LocalDataServices.getUser();
							$scope.biblioteche = LocalDataServices.getBiblioteche();
							$scope.conf_polo = LocalDataServices.getConfPolo();
							LocalDataServices.setTickets([]);
							LocalDataServices.initEditingTicket();

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
						ApiServices.getBibliotecari($scope.user).then(
										function(success) {
											$scope.bibliotecari = success.data;
										}), 
							$scope.logout = function() {
									LocalDataServices.logout();
									$location.path("/")
								};
							$scope.goBack = function() {
								$location.path($scope.conf_polo.cod_polo +"/segnalazioni");
							}
							$scope.goSegnalazioni = function() {
								LocalDataServices.saveFiltersToDetail(null);
								$location.path($scope.conf_polo.cod_polo +"/segnalazioni");
							}
							$scope.isFilled = function (obj) {
								if (obj == undefined)
									return false;
								if (obj == null)
									return false;
								if (obj == '')
									return false;
								return true;
							}
							$scope.findIndex=function(arrayObj, key, value) {
								  var idx = -1;
								  for (var i = 0; i < arrayObj.length; i++) {
								    if (arrayObj[i][key] == value) {
								      idx = i;
								    }
								  }
								  return idx;
								}	
							var initData = function (data) {
								LocalDataServices.setDettaglioTicket({});
								LocalDataServices.setDettaglioTicket(data);
								$route.reload();
							};
							$scope.init = function () {
								$scope.messageToAdd = {
										messaggio : {
											allegato : false,
											testo : '',
											ticket_id : $scope.ticket.ticket.id,
											username_ins : $scope.user.username
										},
										file : []
									}
								$scope.showMessageForm = false;
								$scope.using_form_text = "label_add_message";
								$scope.isAddingMessage = false;
								$("#alert").hide();
								$scope.dismissAlert();
								$scope.filters = LocalDataServices.getFiltersFromDetail();
								$scope.hasSelectetFilters = !jQuery.isEmptyObject($scope.filters);
								$scope.deletingMessage = {
										message: null,
										isDeleting: false
								};
								$scope.deletingTicket = false;
							}
							
							var showAlert = function(text) {
								$scope.error_text = text;
								$("#alert").show();
								setTimeout(function() {
									$("#alert").hide();
								}, LocalDataServices.getHideAlertTime())
							}
							var scrollToForm = function() {
								setTimeout(function() {
									$("html, body").animate(
											{
									scrollTop : $('#messageForm').offset().top
											}, 1000);
								})
							}
							$scope.addFormMessage = function(message) {
								$scope.init();
								$scope.showMessageForm = true;
								$scope.isAddingMessage = true;
								if (message != undefined) {
									$scope.using_form_text = "label_edit_message";

									$scope.messageToAdd = {
											messaggio : {
												allegato : message.allegato,
												testo : message.testo,
												id: message.id,
												ticket_id : $scope.ticket.ticket.id,
												username_ins : $scope.user.username
											},
											file : null,
											files : [],
											isUpdating: true,
											originalMessage: message
										}
								} else {
									$scope.messageToAdd = {
											messaggio : {
												allegato : false,
												testo : '',
												ticket_id : $scope.ticket.ticket.id,
												username_ins : $scope.user.username
											},
											file : null,
											files : [],
											isUpdating: false
										}
								}
								scrollToForm();
							}
							$scope.deleteTicket = function () {
								$scope.using_form_text = "label_delete_ticket";
								$scope.init();
								$scope.showMessageForm = true;
								$scope.deletingTicket = true;
								scrollToForm();
							};
							$scope.confirmDeleteTicket = function() {
								$scope.isLoading = true;
								ApiServices
										.deleteTicket($scope.ticket.ticket)
										.then(
												function(success) {
													$scope.isLoading = false;
													console.log(success.data)
													if (isIn(success.data.responseKey, ["OK","MESSAGE_ERROR", "MESSAGE_MAIL_WRONG"])) {
														LocalDataServices
																.setDettaglioTicket({});
														$location
																.path($scope.conf_polo.cod_polo +"/segnalazioni")
													}

												}, function(error) {
													console.log(error.data)
												});
							}
							$scope.closeTicket = function() {
								$scope.isLoading = true;
								var ticketClose = {
									id_ticket : $scope.ticket.ticket.id,
									username : $scope.user.username
								}
								ApiServices
										.closeTicket(ticketClose)
										.then(
												function(success) {
													$scope.isLoading = false;
													console.log(success.data)
													if (isIn(success.data.responseKey, ["OK","MESSAGE_ERROR", "MESSAGE_MAIL_WRONG"])) {
														initData(success.data.responseObj);
													}

												}, function(error) {
													console.log(error.data)
												});
							}
							$scope.assignTO = function(user) {
								if (user == null || user == '')
									return;
								$scope.isLoading = true;
								ApiServices
										.assignTicket($scope.ticket.ticket, user)
										.then(
												function(success) {
													$scope.isLoading = false;
													console.log(success.data)
													if (isIn(success.data.responseKey, ["OK","MESSAGE_ERROR", "MESSAGE_MAIL_WRONG"])) {
														initData(success.data.responseObj);
													}

												}, function(error) {
													console.log(error.data)
												});
							}
							var updateMessage = function() {
								ApiServices
								.updateMessage(
										$scope.messageToAdd.messaggio)
								.then( function(success) {
									if (isIn(success.data.responseKey, ["OK","MESSAGE_ERROR", "MESSAGE_MAIL_WRONG"])) {
										LocalDataServices.setDettaglioTicket({});
										LocalDataServices
												.setDettaglioTicket(success.data.responseObj);
										if ($scope.messageToAdd.files.length > 0) {
											$scope.isLoading = false;

											ApiServices
													.uploadFile(
															$scope.messageToAdd.file,
															"messaggio",
															success.data.responseObj.ticket.id,
															$scope.messageToAdd.messaggio.id)
													.then(
															function(
																	success) {
																$route.reload()
															});
										} else {
											$route.reload()
										}

									} else {
										showAlert(success.data.responseKey)
										LocalDataServices
												.setDettaglioTicket(success.data.responseObj);
										$scope.ticket = LocalDataServices
										.getDettaglioTicket();
										$scope.showMessageForm = false;
										$scope.isLoading = false;

									}

								}, function (error) {
									$scope.showMessageForm = false;
									$scope.isLoading = false;
								}
							);
								
							}
							$scope.deleteMessage = function(msg) {

								$scope.init();
								$scope.using_form_text = "label_delete_message";

								$scope.showMessageForm = true;
								$scope.deletingMessage = {
										message: msg,
										isDeleting: true
								}
								scrollToForm();
							}
						
							$scope.confirmDeleteMessage = function() {
								$scope.isLoading = true;
								ApiServices
								.deleteMessage($scope.deletingMessage.message)
								.then( function(success) {
									if (isIn(success.data.responseKey, ["OK","MESSAGE_ERROR", "MESSAGE_MAIL_WRONG"])) {
										initData(success.data.responseObj);
									$scope.isLoading = false;
									}
									
								}, function (error) {
									
								}
							);
							}
							$scope.addMessage = function() {
								$scope.isLoading = true;
								$scope.messageToAdd.file = $scope.messageToAdd.files[0];
								if($scope.messageToAdd.isUpdating){
									updateMessage();
									return;
								}
								//LocalDataServices.setDettaglioTicket({});

								ApiServices
										.addTicketMessage(
												$scope.messageToAdd.messaggio)
										.then(
												function(success) {
													if (isIn(success.data.responseKey, ["OK","MESSAGE_ERROR", "MESSAGE_MAIL_WRONG"])) {
														LocalDataServices.setDettaglioTicket({});
														LocalDataServices
																.setDettaglioTicket(success.data.responseObj);
														console.log("addMessage()", success.data.responseObj);
														if ($scope.messageToAdd.messaggio.allegato) {
															$scope.isLoading = false;

															ApiServices
																	.uploadFile(
																			$scope.messageToAdd.file,
																			"messaggio",
																			success.data.responseObj.ticket.id,
																			success.data.responseObj.messaggio.id)
																	.then(
																			function(
																					success) {
																				$route.reload()
																			});
														} else {
															$route.reload()
														}

													} else {
														showAlert(success.data.responseKey)
														LocalDataServices
																.setDettaglioTicket(success.data.responseObj);
														$scope.ticket = LocalDataServices
														.getDettaglioTicket();
														$scope.showMessageForm = false;
														$scope.isLoading = false;

													}

												}, function(error) {
													$scope.isLoading = false;

													console.log(error.data)
												});

							};
							$scope.findIndex = function(arrayObj, key, value) {
								var idx = -1;
								for (var i = 0; i < arrayObj.length; i++) {
									if (arrayObj[i][key] == value) {
										idx = i;
									}
								}
								return idx;
							};
							$scope.downloadAttached = function(url){
								ApiServices.getFile(url).then(function(success){
								$scope.isLoading = true;
								 var text = success.data;
							        var blob = new Blob([text], {
							          type: "octet/stream"
							        });
							        saveAs(blob, "allegato");
									$scope.isLoading = false;

								}, function(error){
									$scope.isLoading = false;

								})
							};
							$scope.ts = function() {
								//ritorna il timestamp per evitare cache allegato img
								return new Date();
							}
							$scope.editTicket = function() {
								LocalDataServices.setEditingTicket($scope.ticket.ticket);
								$location.path($scope.conf_polo.cod_polo +"/modificaSegnalazione")
							}
							$scope.init();
						} ]);
