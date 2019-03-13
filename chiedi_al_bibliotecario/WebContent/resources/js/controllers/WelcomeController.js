assistenzaApp
		.registerCtrl(
				'WelcomeController',
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
							console.log("WelcomeController");
							$scope.controller = "welcome_controller";
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
							$("#alert").hide();
							var showAlert = function (text) {
								$scope.error_text = text;
								$("#alert").show();
								setTimeout(function () {
									$("#alert").hide();
								}, LocalDataServices.getHideAlertTime())
							}
							$scope.redirectTo = function(path) {
								$location.path(path);
							}
							} ]);