assistenzaApp
		.registerCtrl(
				'ProfiloController',
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
							console.log("ProfiloController");
							$scope.controller = "profilo";
							$scope.user = LocalDataServices.getUser();
							$scope.userEdit = angular.copy($scope.user);
							$scope.applicationBuild = LocalDataServices
									.getBuild();
							LocalDataServices.setTickets([]);
							LocalDataServices.saveFiltersToDetail(null);
							$scope.faqs =  LocalDataServices.getFaqs();
							$scope.conf_polo = LocalDataServices.getConfPolo();
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
								$scope.isLoading = false;

							/*	setTimeout(function () {
									$("#alert").hide();
								}, LocalDataServices.getHideAlertTime())
								*/
							};
							$scope.dismissAlert();
							$scope.today = new Date().toJSON().slice(0, 10);
							var parseDate = function(str) {
								if(str == null)
									return;
								var y = str.substr(0, 4), m = str.substr(4, 2) - 1, d = str
										.substr(6, 2);
								var D = new Date(y, m, d);
								return (D.getFullYear() == y
										&& D.getMonth() == m && D.getDate() == d) ? D
										: 'invalid date';
							}
							$scope.isEditing = false;
							$scope.isLoading = false;
						
							$scope.startEdit = function() {
								$scope.isEditing = true;
								$scope.controller = "modifica_profilo";

							}
							$scope.cancelEdit = function() {
								$route.reload();
							}
							$scope.saveEdit = function() {
								if (!valitateEmail($scope.userEdit.email)) {
									showAlert("FORM_FORMATO_EMAIL");
									return;
								}
								$scope.isLoading = true;

								/* Rimosso nome e cognome ora data è string		$scope.userEdit.data_nascita = $scope.userEdit.data_nascita_date
										.yyyymmdd();*/
								ApiServices
										.editProfile($scope.userEdit)
										.then(
												function(success) {
													console.info(success)
													if (isIn(success.data.responseKey, ["OK","MESSAGE_ERROR", "MESSAGE_MAIL_WRONG"])) {
														LocalDataServices
																.setUser(success.data.responseObj.user);
														$scope.isEditing = false;
														showAlert(success.data.responseKey)

													} else{
														$scope.isLoading = false;

														$scope.isEditing = false;
														showAlert(success.data.responseKey)

													}

												}, function(error) {
													console.error(error);
												});
							};

							$scope.changePsw = function() {
								if (!validatePassword($scope.newPsw.new_psw)) {
									showAlert("FORM_PSW_NON_SICURA");
									return;
								}
								$scope.newPsw.cod_polo = $scope.conf_polo.cod_polo;
								ApiServices
										.changePsw($scope.newPsw)
										.then(
												function(success) {
													console.info(success)
													if (isIn(success.data.responseKey, ["OK","MESSAGE_ERROR", "MESSAGE_MAIL_WRONG"])) {
														LocalDataServices
														.loginSuccess(success.data);
														$scope.isEditing = false;

														showAlert(success.data.responseKey)
													}
													else
														showAlert(success.data.responseKey)

												}, function(error) {
													console.error(error);
												});

							};
							$scope.wantChangePsw = ($scope.user.passwordIsToReset) ;
							$scope.startChangePsw = function() {
								$scope.controller = "change_password";
								$scope.wantChangePsw = true
								$scope.newPsw = {
									old_psw : '',
									new_psw : '',
									repeat_new_psw : '',
									username : $scope.user.username,
									cod_polo : $scope.conf_polo.cod_polo
								}
							};
							if($scope.user.passwordIsToReset)
								$scope.startChangePsw();
							var initYears = function () {
								var thisYear = new Date().getFullYear();
								// alert($scope.today)
								thisYear = parseInt(thisYear);
								$scope.years = [];
								for (i = thisYear - 18; i != 1900; i--) {
									$scope.years.push(i);
								}
							};
							var emailRegex = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
							var valitateEmail = function (email) {
								var test = emailRegex.test(email)
								return test;
							}
							var validatePassword = function (password) {
								 return password.length >= 8
							        && !~password.indexOf(" ")
							        && password.match(/[A-Z]/) != null
							        && password.match(/[a-z]/) != null
							        && password.match(/[0-9]/) != null ;
							}
							initYears();
						} ]);