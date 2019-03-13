 function logout(){
	 
                console.log('Logout');
                location.reload();
            }
 function onInactive(millisecond, callback){
	 
          var wait = setTimeout(callback, millisecond);               
     document.onmousemove = 
     document.mousedown = 
     document.mouseup = 
     document.onkeydown = 
     document.onkeyup = 
     document.focus = function(){
             clearTimeout(wait);
              wait = setTimeout(callback, millisecond);                       
        };

 }
function isIn(value, array) {
	if(array == undefined || array.length == 0)
		return false;
	return (array.indexOf(value) > -1);
}
var assistenzaApp = angular.module("assistenzaApp", [ "ngRoute", "ngSanitize",
		"ngCookies", "ngResource", "pascalprecht.translate", "angular.filter",
		"bw.paging" ]);
var controllersToLoad = [ 'TicketsController',
	'NewEditTicketController',
	'DettaglioTicketController',
	'ProfiloController',
	'AmministrazioneController',
	'FaqController',
	'WelcomeController'];

//crea un TS che serve ad avitare la cache dei file JS
var getVersionRequest = function () {
	
	return "?v=" + new Date();
}
assistenzaApp.config([
		"$routeProvider",
		"$translateProvider",
		"$locationProvider",
		"$controllerProvider",
		function($routeProvider, $translateProvider, $locationProvider,
				$controllerProvider) {

			assistenzaApp.registerCtrl = $controllerProvider.register;
			// dynamically include controllers as needed
			function loadcontrollers(controllers) {
				controllers.forEach(function(controller) {
					var script = document.createElement('script');
					script.type = 'text/javascript';
					script.src = 'js/controllers/' + controller + ".js" + getVersionRequest();
					document.body.appendChild(script);
				});
			}
			;
			$routeProvider.when(
					"/:cod_polo",
					{
						templateUrl : "html/view/LoginRegisterView.html" + getVersionRequest(),
						controller : "LoginRegisterController",
						resolve : {
							load : function() {
								loadcontrollers(controllersToLoad);
							}
						}
					}).when(
							"/",
							{
								templateUrl : "html/view/LoginRegisterView.html" + getVersionRequest(),
								controller : "LoginRegisterController",
								resolve : {
									load : function() {
										loadcontrollers(controllersToLoad);
									}
								}
							}).when(
							"/index",
							{
								templateUrl : "html/view/LoginRegisterView.html" + getVersionRequest(),
								controller : "LoginRegisterController",
								resolve : {
									load : function() {
										loadcontrollers(controllersToLoad)
									}
								}
							})
			.when("/:cod_polo/segnalazioni", {
				templateUrl : "html/view/TicketsView.html" + getVersionRequest(),
				controller : "TicketsController",
			}).when("/:cod_polo/nuovaSegnalazione", {
				templateUrl : "html/view/NewEditTicketView.html" + getVersionRequest(),
				controller : "NewEditTicketController",
			}).when("/:cod_polo/modificaSegnalazione", {
				templateUrl : "html/view/NewEditTicketView.html" + getVersionRequest(),
				controller : "NewEditTicketController",
			}).when("/:cod_polo/dettaglio/:id", {
				templateUrl : "html/view/DettaglioTicketView.html" + getVersionRequest(),
				controller : "DettaglioTicketController"
			}).when("/:cod_polo/profilo/:username", {
				templateUrl : "html/view/ProfiloView.html" + getVersionRequest(),
				controller : "ProfiloController"
			}).when("/:cod_polo/admin", {
				templateUrl : "html/view/AmministrazioneView.html" + getVersionRequest(),
				controller : "AmministrazioneController"
			}).when("/:cod_polo/FAQ", {
				templateUrl : "html/view/FaqView.html" + getVersionRequest(),
				controller : "FaqController"
			}).when("/:cod_polo/benvenuto", {
				templateUrl : "html/view/WelcomeView.html" + getVersionRequest(),
				controller : "WelcomeController"
			}).otherwise({
				redirectTo : "/"
			});
			$locationProvider.html5Mode(true);
			var languages = [ 'it' ]  // [ 'it', 'en' ]
			try {
				languages.forEach(function(lang) {
					$.ajax({
						url : "meta/codici_" + lang + ".json",
						contentType : "application/json; charset=utf-8",
						success : function(success) {
							console.log(lang, success);

							$translateProvider.translations(lang, success);

						},
						error : function(error) {
							console.log("error getting lang: " + lang, error);
						}
					});
				})

			} catch (e) {
				console.log(e);
			}

			$translateProvider.preferredLanguage("it");
			$translateProvider.useSanitizeValueStrategy(null);
			
		} ]);

assistenzaApp.directive("filesInput", function() {
	return {
		require : "ngModel",
		link : function postLink(scope, elem, attrs, ngModel) {
			elem.on("change", function(e) {
				var files = elem[0].files;
				ngModel.$setViewValue(files);
			})
		}
	}
});

Date.prototype.yyyymmdd = function() {
	var mm = this.getMonth() + 1; // getMonth() is zero-based
	var dd = this.getDate();

	return [ this.getFullYear(), (mm > 9 ? '' : '0') + mm,
			(dd > 9 ? '' : '0') + dd ].join('');
};
