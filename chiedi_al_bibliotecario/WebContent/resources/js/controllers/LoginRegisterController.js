assistenzaApp
	.controller(
		'LoginRegisterController',
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
			function ($scope, $translate, $routeParams, $location,
				$filter, $timeout, $route, ApiServices,
				LocalDataServices) {
				console.log("LoginRegisterController");

				if(poli_disponibili.length == 1) {
					conf_polo = poli_disponibili[0];
					$location.path(poli_disponibili[0].cod_polo);
				} else {
					$scope.poli_disponibili = poli_disponibili;
				}
				
				
				$scope.controller = "login";
				$scope.applicationBuild = appBuild;
				LocalDataServices.setTickets([]);
				LocalDataServices.saveFiltersToDetail(null);
				LocalDataServices.setFaqs(faqs);
				$scope.faqs = LocalDataServices.getFaqs();
				$scope.changeLanguage = function (key) {
					$translate.use(key);
				};
				$scope.reset = function () {
					$route.reload();
				}
				$scope.login = {
					usr: '',
					psw: ''
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
				$scope.dismissAlert();
				$scope.isLoading = false;
				$scope.login = function () {
					if ($scope.loginForm.username == ''
						|| $scope.loginForm.password == '') {
						showAlert('USR_PSW_WRONG');
						return;
					}

					$scope.isLoading = true;
					$scope.loginForm.cod_polo = $scope.conf_polo.cod_polo;
					console.log("Login", $scope.loginForm)

					ApiServices
						.login($scope.loginForm)
						.then(
							function (success) {
								console.log(success.data);
								$scope.isLoading = false;
								if (success.data.responseKey != "OK") {

									showAlert(success.data.responseKey);
								} else {
									loginAction(success.data)

								}

							}, function (error) {
								$scope.isLoading = false;

								showAlert('BAD_REQUEST');

							});
				};

				$scope.register = function () {
					console.log("Register", $scope.credential);
					if (!validateUsername($scope.credential.username)) {
						showAlert("FORM_USER_NUM_CHAR");
						return
					}

					if (!valitateEmail($scope.credential.email)) {
						showAlert("FORM_FORMATO_EMAIL");
						return;
					}
					if (!validatePassword($scope.credential.password)) {
						showAlert("FORM_PSW_NON_SICURA");
						return;
					}
					$scope.isLoading = true;
					$scope.credential.cod_polo = $scope.conf_polo.cod_polo;
					
					ApiServices
						.register($scope.credential)
						.then(
							function (success) {
								$scope.isLoading = false;
								console.info(success.data)
								if (success.data.responseKey != "OK") {
									showAlert(success.data.responseKey);
								} else {
									loginAction(success.data)
								}
							}, function (error) {
								$scope.isLoading = false;
								console.error(error)
								showAlert();

							});

				};
				$scope.resetRegister = function () {
					$scope.credential = {
						username: '',
						cognome: "",
						data_nascita_date: null,
						email: "",
						nome: "",
						password: "",
						username: ""
					}
				};
				$scope.resetPassword = function () {
					if (!valitateEmail($scope.resetMail)) {
						showAlert("FORM_FORMATO_EMAIL");
						return;
					}
					$scope.isLoading = true;
					ApiServices.resetPswEmail({email: $scope.resetMail, cod_polo: $scope.conf_polo.cod_polo}).then(function(success) {
						$scope.isLoading = false;
						if(success.data.responseKey == "OK")
							showAlert("label_reimpoasta_psw_correttamente");
						else 
							showAlert(success.data.responseKey);

					
					}, function(error) {
						$scope.isLoading = false;
						showAlert("ERROR");
						console.error(error);
					});
				};
				$scope.checkSbnWebUser = function () {
					$scope.isLoading = true;
					ApiServices.checkiIfNotSbnWeb({email: $scope.resetMail, cod_polo: $scope.conf_polo.cod_polo}).then(function(success) {
						$scope.isLoading = false;
						switch(success.data.responseKey) {
						case "OK":
							debugger
							   window.open($scope.conf_polo.url_sbnweb_reset_psw);
							break;
						default:
							showAlert(success.data.responseKey);
							break;
						
						}
								
					}, function(error) {
						$scope.isLoading = false;
						showAlert("ERROR");
						console.error(error);
					});
				}
				var emailRegex = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
				var pswRegex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[A-Za-z\d!@#$%^&*()_+=]{8,}$/;
				var valitateEmail = function (email) {
					var test = emailRegex.test(email)
					return test;
				}
				var validateUsername = function (user) {
					return (user.length > 3 && user.length < 11);

				}
				var validatePassword = function (password) {
					 return password.length >= 8
				        && !~password.indexOf(" ")
				        && password.match(/[A-Z]/) != null
				        && password.match(/[a-z]/) != null
				        && password.match(/[0-9]/) != null ;
				}
			var initYears = function () {
					$scope.thisYear = new Date().getFullYear();
					$scope.thisYear = parseInt($scope.thisYear);
					$scope.years = [];
					for (i = $scope.thisYear - 18; i != 1900; i--) {
						$scope.years.push(i);
					}
				}
			var loginAction = function(data) {
				LocalDataServices
				.loginSuccess(data);
			LocalDataServices
				.setCategories(categories);
			LocalDataServices
			.setBiblioteche(biblioteche);
			LocalDataServices
				.setBuild(appBuild);
			LocalDataServices.setConfPolo(conf_polo)
			
				$location.path($scope.conf_polo.cod_polo + "/benvenuto")
			};
			$scope.onSelectPolo = function(cod_polo) {
				window.location = cod_polo;
			}
			LocalDataServices.setConfPolo(conf_polo)
			$scope.conf_polo = LocalDataServices.getConfPolo();
			initYears();
			
			
				
		}]);


