assistenzaApp.registerCtrl('AmministrazioneController', [
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
			console.log("amministrazione Controller ");

			$scope.controller = "amministrazione";
			$scope.user = LocalDataServices.getUser();
			$scope.conf_polo = LocalDataServices.getConfPolo();
			if (LocalDataServices.getUser() == false)
				$location.path($scope.conf_polo.cod_polo + "/segnalazioni")
			$scope.applicationBuild = LocalDataServices.getBuild();
			$scope.categories = LocalDataServices.getCategories();
			$scope.faqs =  LocalDataServices.getFaqs();
			$scope.biblioteche = LocalDataServices.getBiblioteche();
			LocalDataServices.initEditingTicket();

			
			LocalDataServices.saveFiltersToDetail(null);

			$scope.changeLanguage = function(key) {
				$translate.use(key);
			};
			$scope.reload = function() {
				$route.reload();
			}
			$scope.logout = function() {
				LocalDataServices.logout();
				$location.path("/")
			};
			$scope.today = new Date().toJSON().slice(0, 10);

			$scope.isLoading = true;
			$scope.isEditing = false;
			$scope.dismissAlert = function() {
				$("#alert").hide();
				$scope.error_text = "";
			}
			var showAlert = function (text) {
				$scope.isLoading = false;
				$scope.error_text = text;
				$("#alert").show();
			/*	setTimeout(function () {
					$("#alert").hide();
				}, LocalDataServices.getHideAlertTime())
				*/
			};
			$scope.dismissAlert();
			var initUsers = function() {
				$scope.isLoading = true;

				$scope.users = ApiServices.getAllUsers($scope.user).then(
						function(success) {
							console.log(success.data);
							$scope.isLoading = false;
							$scope.users = success.data;
						}, function(error) {
							$scope.users = [];
						});
			}
							var checkAbbinataIsPresent = function(list,
									key_text, key_cod, text_to_check) {
								var isPresent = false;
								text_to_check = text_to_check.toUpperCase();
								list.forEach(function(item) {
									var exist_check_text = item[key_cod]
											+ item[key_text];
									exist_check_text = exist_check_text
											.toUpperCase();
									if (exist_check_text == text_to_check)
										isPresent = true;
								});

								return isPresent;
							};
							var checkElementIsPresent = function(list, key,
									text_to_check) {
								var isPresent = false;
								text_to_check = text_to_check.toUpperCase();
								list.forEach(function(item) {
									var exist_check_text = item[key];
									exist_check_text = exist_check_text
											.toUpperCase();
									if (exist_check_text == text_to_check)
										isPresent = true;
								});
								return isPresent;
							}
			var _edit = function(type, obj) {
				switch (type) {
				case 'categoria':
					if(obj.testo_it == undefined || obj.cd_categoria == undefined || obj.testo_it == ''   || obj.cd_categoria == '') {
						$scope.isLoading = false;

						showAlert("label_campi_asterisco")
						return;
					}
					if(checkAbbinataIsPresent($scope.categories, 'testo_it', 'cd_categoria', obj.cd_categoria+obj.testo_it)) {
						showAlert("GIA_PRESENTE");
						return;
					} 
					
					if(checkElementIsPresent($scope.categories, 'cd_categoria', obj.cd_categoria) 
							&& checkElementIsPresent($scope.categories, 'testo_it', obj.testo_it)) {
						showAlert("GIA_PRESENTE");
						return;
					} 
					
					ApiServices.updateCategory(obj).then(function(success) {
						$scope.onCancelEditing();
						LocalDataServices.setCategories(success.data);
						$scope.categories = LocalDataServices.getCategories();
						ApiServices.reloadApplicationProps();
						$scope.isLoading = false;

					}, function(error) {
						showAlert("ERROR")
					})
					break;
				case 'user':
					ApiServices.editProfile(obj).then(function(success) {
						debugger
						console.info(success)
						$scope.onCancelEditing();

						initUsers();

					}, function(error) {
						console.error(error);
					});
					break;
				case 'faq':
					if(obj.domanda == undefined || obj.risposta == undefined || obj.cd_faq  == undefined 
							|| obj.cd_faq == '' || obj.domanda == '' || obj.risposta == '' ) {
						showAlert("label_campi_asterisco")
						return;
					}
					 if(checkAbbinataIsPresent($scope.faqs, 'domanda', 'cd_faq', obj.cd_faq+obj.domanda)) {
						showAlert("GIA_PRESENTE");
						return;
					} 
					/*if(checkElementIsPresent($scope.faqs, 'cd_faq', obj.cd_faq)) {
						showAlert("GIA_PRESENTE");
						return;
					}*/
					ApiServices.updateFaq(obj).then(function(success) {
						$scope.onCancelEditing();
						LocalDataServices.setFaqs(success.data);
						$scope.faqs = LocalDataServices.getFaqs();
						ApiServices.reloadApplicationProps();
						$scope.isLoading = false;

					}, function(error) {
						showAlert("ERROR")
					})
					break;
				case 'biblioteca':
					break;
				default:
					break;
				}
			}
			var _delete = function(type, obj) {
				switch (type) {
				case 'categoria':
					ApiServices.deleteCategory(obj).then(function(success) {
					
						$scope.isLoading = false;

						if($scope.categories.length == success.data.length) {
							showAlert("TICKETS_CON_CATEGORIA_ASSOCIATA")
						} else {
							LocalDataServices.setCategories(success.data);
							$scope.categories = LocalDataServices.getCategories();
							$scope.onCancelEditing();
							
							ApiServices.reloadApplicationProps();
						}
					
					}, function(error) {
						showAlert("ERROR")
					})
					break;
				case 'user':
					//obj.fl_canc = 's';
					ApiServices.deleteProfile(obj).then(function(success) {
						debugger
						console.info(success)
						$scope.onCancelEditing();

						initUsers();

					}, function(error) {
						console.error(error);
					});
					break;
				case 'faq':
					ApiServices.deleteFaq(obj).then(function(success) {
						$scope.onCancelEditing();

						LocalDataServices.setFaqs(success.data);
						$scope.faqs = LocalDataServices.getFaqs();
						ApiServices.reloadApplicationProps();
						$scope.isLoading = false;

					}, function(error) {
						showAlert("ERROR")
					})
					break;
				case 'biblioteca':
					ApiServices.deleteBiblioteca($scope.applicationBuild.cod_polo, obj.cd_biblioteca, obj).then(function(success) {
						$scope.onCancelEditing();
						
						LocalDataServices.setBiblioteche(success.data);
						$scope.biblioteche = LocalDataServices.getBiblioteche();
						ApiServices.reloadApplicationProps();
						$scope.isLoading = false;

					}, function(error) {
						showAlert("ERROR")
					})
					break;
				default:
					break;
				}
			}
			var _add = function(type, obj) {
				switch (type) {
				case 'categoria':
					if(obj.testo_it == undefined || obj.cd_categoria == undefined || obj.testo_it == ''   || obj.cd_categoria == '') {
						$scope.isLoading = false;

						showAlert("label_campi_asterisco")
						return;
					}
					if(checkAbbinataIsPresent($scope.categories, 'testo_it', 'cd_categoria', obj.cd_categoria+obj.testo_it)) {
						showAlert("GIA_PRESENTE");
						return;
					}
					if(checkElementIsPresent($scope.categories, 'cd_categoria', obj.cd_categoria)) {
						showAlert("GIA_PRESENTE");
						return;
					}
					if(checkElementIsPresent($scope.categories, 'testo_it', obj.testo_it)) {
						showAlert("GIA_PRESENTE");
						return;
					}
						
					ApiServices.addCategory(obj).then(function(success) {
						LocalDataServices.setCategories(success.data);
						$scope.categories = LocalDataServices.getCategories();
						ApiServices.reloadApplicationProps();
						$scope.isLoading = false;
						$scope.onCancelEditing();


					}, function(error) {
						showAlert("ERROR")
						console.log(error)
					})
					break;
				case 'faq':
				if(obj.domanda == undefined || obj.risposta == undefined || obj.cd_faq  == undefined 
						|| obj.cd_faq == '' || obj.domanda == '' || obj.risposta == '' ) {
					showAlert("label_campi_asterisco")
					return;
				}
				if(checkElementIsPresent($scope.faqs, 'cd_faq', obj.cd_faq)) {
					showAlert("GIA_PRESENTE");
					return;
				}
				if(checkAbbinataIsPresent($scope.faqs, 'domanda', 'cd_faq', obj.cd_faq+obj.domanda)) {
					showAlert("GIA_PRESENTE");
					return;
				}
					ApiServices.addFaq(obj).then(function(success) {
						LocalDataServices.setFaqs(success.data);
						$scope.faqs = LocalDataServices.getFaqs();
						ApiServices.reloadApplicationProps();
						$scope.isLoading = false;
						$scope.onCancelEditing();


					}, function(error) {
						showAlert("ERROR")
						console.log(error)
					})
					break;
				case 'biblioteca':
					if(obj.cd_biblioteca == undefined || obj.cd_biblioteca == '') {
						showAlert("label_campi_asterisco")
						return;
					}
					if(obj.nome == undefined || obj.nome == '') {
						showAlert("label_campi_asterisco")
						return;
					}
					if(checkElementIsPresent($scope.biblioteche, 'cod_bib', obj.cod_polo + obj.cd_biblioteca)) {
						showAlert("BIB_GIA_PRESENTE");
						return;
					}
					
					if(obj.cod_polo.length != 3) {
						showAlert("COD_POLO_ERRATO");
						return;
					}
					if(obj.cd_biblioteca.length != 2) {
						showAlert("COD_BIB_ERRATO");
						return;
					}
					ApiServices.addBiblioteca({cod_bib: obj.cd_biblioteca, cod_polo: obj.cod_polo ,nome: obj.nome}).then(function(success) {
						$scope.isLoading = false;

						if(success.data.responseKey == "OK") {
							LocalDataServices.setBiblioteche(success.data.responseObj.biblioteche);
							$scope.biblioteche = LocalDataServices.getBiblioteche();
							ApiServices.reloadApplicationProps();
							$scope.onCancelEditing();
						} else 
							showError(success.data.responseKey)
						

					}, function(error) {
						showAlert("ERROR")
					});
					break;
				default:
					break;
				}
			}
			var _resetPsw = function( obj) {
				ApiServices.resetPsw(obj).then(function(success) {
					debugger
					console.info(success)
					$scope.onCancelEditing();

					initUsers();

				}, function(error) {
					console.error(error);
				});
			}
			var checkTicketsBiblioteca = function (obj) {
				$scope.isLoading = true;
				ApiServices.checkTicketBiblioteca(obj).then(function(success) {
					$scope.isLoading = false;
					if(success.data.length > 0)
					$scope.toEdit.action = 'delete-ticket-presenti';
						

				}, function(error) {
					showAlert("ERROR")
				});
			}
			var toEditObj = null;
			$scope.onStartEdit = function(cat, type, action) {
				toEditObj = angular.copy(cat)
				$scope.dismissAlert();
				if(type == 'biblioteca' && action == 'delete'){
					checkTicketsBiblioteca(toEditObj);
				}
				$scope.toEdit = {
					obj : toEditObj,
					type : type,
					action : action
				};
				if(action == 'add') 
					$scope.toEdit.obj.cod_polo = $scope.conf_polo.cod_polo;
				$scope.isEditing = true;
			}
			$scope.onCancelEditing = function() {
				$scope.toEdit = undefined;
				$scope.isEditing = false;
				$scope.isLoading = false;
				toEditObj = null;
			}
			var _importBiblioteca = function (type, obj) {
				if(obj.cd_biblioteca == undefined || obj.cd_biblioteca == '') {
					showAlert("label_campi_asterisco")
					return;
				}//obj.cod_polo +
				if(checkElementIsPresent($scope.biblioteche, 'cod_bib', obj.cd_biblioteca)) {
					showAlert("BIB_GIA_PRESENTE");
					return;
				}
				if(obj.cod_polo.length != 3) {
					showAlert("COD_POLO_ERRATO");
					return;
				}
				if(obj.cd_biblioteca.length != 2) {
					showAlert("COD_BIB_ERRATO");
					return;
				}
					
				//var polo = obj.cd_biblioteca.substring(0,3)
				//var bib = obj.cd_biblioteca.substring(3)
				debugger
				ApiServices.importaBiblioteca(obj.cod_polo, obj.cd_biblioteca).then(function(success) {
					$scope.isLoading = false;

					if(success.data.responseObj.biblioteche.length == $scope.biblioteche.length) {
						$scope.toEdit.action = 'add';
						showAlert("BIB_NOT_FOUND")
					} else {
						LocalDataServices.setBiblioteche(success.data.responseObj.biblioteche);
						$scope.biblioteche = LocalDataServices.getBiblioteche();
						ApiServices.reloadApplicationProps();
						$scope.onCancelEditing();

					}
					

				}, function(error) {
					showAlert("ERROR")
				})
			}
			$scope.onSave = function() {
				$scope.isLoading = true;
				switch ($scope.toEdit.action) {
				case 'edit':
					
					_edit($scope.toEdit.type, $scope.toEdit.obj)
					break;
				case 'delete-ticket-presenti':
				case 'delete':
					_delete($scope.toEdit.type, $scope.toEdit.obj)

					break;
				case 'add':
					_add($scope.toEdit.type, $scope.toEdit.obj)

					break;
				case 'nominaBib':
					 $scope.toEdit.obj.isBibliotecario = true;
					_edit($scope.toEdit.type, $scope.toEdit.obj)
					break;
				case 'revocaBib':
					 $scope.toEdit.obj.isBibliotecario = false;
					_edit($scope.toEdit.type, $scope.toEdit.obj)
					break;
				case 'importBib':
					_importBiblioteca($scope.toEdit.type, $scope.toEdit.obj);
					break;
				case 'resetPsw':
					_resetPsw($scope.toEdit.obj)
					break;
				default:
					break;
				}
			}
				initUsers();
		} ]);
