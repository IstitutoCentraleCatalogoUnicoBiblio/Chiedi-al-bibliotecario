assistenzaApp.registerCtrl('FaqController', [
		'$scope',
		'$translate',
		'$routeParams',
		'$location',
		'$filter',
		'$timeout',
		"$route",
		"LocalDataServices",
		"ApiServices",
		function($scope, $translate, $routeParams, $location, $filter,
				$timeout, $route, LocalDataServices, ApiServices) {
			console.log("FAQ Controller ");

			$scope.controller = "label_faq";
			$scope.user = LocalDataServices.getUser();
			if (LocalDataServices.getUser() == false)
				$location.path("/segnalazioni")
			$scope.applicationBuild = LocalDataServices.getBuild();
			$scope.categories = LocalDataServices.getCategories();
			$scope.faqs = LocalDataServices.getFaqs();
			$scope.conf_polo = LocalDataServices.getConfPolo();
			LocalDataServices.saveFiltersToDetail(null);
			LocalDataServices.initEditingTicket();

			$scope.changeLanguage = function(key) {
				$translate.use(key);
			};
			$scope.reload = function() {
				$route.reload();
			}
			$scope.logout = function() {
				LocalDataServices.logout();
				$("#logOutModal").dismiss();
				$location.path("/")
			};
			$scope.init = function () {
				for (var i = 0; i < $scope.faqs.length; i++) {
					$scope.faqs[i].icon = "add";
				}
			};
			$scope.risposta_id_name = "#risposta_";
			$scope.domanda_id_name = "#domanda_";
			$scope.setIcon = function (i) {
				var elementId = $scope.risposta_id_name + i;
				$scope.faqs[i].icon = ( $scope.faqs[i].icon == "add") ? "remove" :"add";
			//	$(elementId).collapse('toggle')
			};
		
			var open = function(i, id) {
				// for (var i = 0; i < $scope.faqs.length; i++) {
					 $('#' +id).collapse('show');
					 $scope.faqs[i].icon = 'remove';
				// }
			 }
			var close = function (i, id) {
				$('#' +id).collapse('hide');
				 $scope.faqs[i].icon = 'add';
			}
			$scope.toggle = function (i, id, faq) {
				if($scope.faqs[i].icon == 'add')
					open(i, id);
				else 
					close(i, id);
				$scope.$digest ()
			}
			
			$scope.openAll = function () {
				for (var i = 0; i < $scope.faqs.length; i++) {
					open(i, 'risposta_' + i);
				}
			}
			$scope.closeAll = function() {
				for (var i = 0; i < $scope.faqs.length; i++) {
					close(i, 'risposta_' + i);
				}
			}
			
			$scope.init();
		} ]);