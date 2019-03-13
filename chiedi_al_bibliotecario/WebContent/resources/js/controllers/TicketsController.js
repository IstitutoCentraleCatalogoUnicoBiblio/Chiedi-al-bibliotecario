assistenzaApp
		.registerCtrl(
				'TicketsController',
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
							console.log("TicketsController");
							$scope.controller = "label_nav_segnalazioni_aperte";
							$scope.tickets = LocalDataServices.getTickets();
							$scope.paginationProps = LocalDataServices
									.getPaginationProps();
							$scope.paginationProps.currentPage = 1;
							$scope.user = LocalDataServices.getUser();
							$scope.applicationBuild = LocalDataServices
									.getBuild();
							$scope.categories = LocalDataServices
									.getCategories();
							$scope.faqs =  LocalDataServices.getFaqs();
							$scope.biblioteche = LocalDataServices.getBiblioteche();
							$scope.conf_polo = LocalDataServices.getConfPolo();
							$scope.changeLanguage = function(key) {
								$translate.use(key);
							};
							$scope.reset = function() {
								$route.reload();
							}
							$scope.logout = function() {
								LocalDataServices.logout();
								$location.path("/" + $scope.conf_polo.cod_polo)
							};
							$scope.isExecuted = false;
							$scope.updateEmptyText = function (testo) {
								debugger
								if ($scope.filters.testo == undefined)
									return;
								if(testo == '') {
									$scope.applyFilter('testo', null)
									console.log("Filtri", $scope.filters)
								}
									
							}
							
							$scope.paginationProps.currentPage = 1;
							var createPagine = function(righe, numDocs) {
								var n = numDocs / righe;
								if( n <= 1)
									n = 1;
								else
									n = parseInt(n) + 1;
								var nPag = parseInt(n);
								$scope.pagine = [];
								var pagina = 0;

								for (var i = 0; i < nPag; i++) {
									var c = i;
									var obj = {
										pag : c + 1,
										start : pagina
									};
									$scope.pagine.push(obj);
									pagina = pagina + righe;
								}
								$scope.paginationProps.currentPage = 1;
							}
							var loadDatas = function(idStart, filters,
									isLoadingPage) {
								$scope.isLoading = true;
								ApiServices
										.tickets(idStart, filters, $scope.user.username, $scope.conf_polo.cod_polo )
										.then(
												function(success) {
													console.info(success.data);
													LocalDataServices
															.setTickets(success.data.responseObj.allTickets);
													// LocalDataServices.setPaginationProps(success.data.responseObj.start,success.data.responseObj.numViews,success.data.responseObj.totalTickets);
													$scope.tickets = LocalDataServices
															.getTickets();
													$scope.isLoading = false;

													if (!isLoadingPage) {
														LocalDataServices
																.setPaginationProps(
																		success.data.responseObj.start,
																		success.data.responseObj.numViews,
																		success.data.responseObj.totalTickets);
														createPagine(
																success.data.responseObj.numViews,
																success.data.responseObj.totalTickets);
														$scope.paginationProps = LocalDataServices
																.getPaginationProps();
														$scope.paginationProps.currentPage = 1;

													}

												}, function(error) {
													console.info(error)
													$scope.isLoading = false;

												});
							}

							$scope.filters = LocalDataServices.getFiltersFromDetail();
						
							$scope.applyFilter = function(key, value) {
								
								if (value === undefined || value === null || value === '') {
									delete $scope.filters[key];
									loadDatas(0, $scope.filters, false);
									return;
								}
								else {
									$scope.filters[key] = value;
									loadDatas(0, $scope.filters, false);
									return;
								}
							}

							$scope.loadPage = function(start, page) {

								console
										.info("start: " + start, "page: "
												+ page);
								loadDatas(start, $scope.filters, true)

								$scope.paginationProps.currentPage = page;
							}

							$scope.isLoading = false;
							$scope.dettaglio = function(ticket) {
								$scope.isLoading = true;
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
							if($scope.tickets.length == 0) {
								loadDatas(0, $scope.filters, false)
							} else {
								createPagine($scope.paginationProps.rows,
										$scope.paginationProps.total);
							}
						$scope.findIndex=function(arrayObj, key, value) {
								  var idx = -1;
								  for (var i = 0; i < arrayObj.length; i++) {
								    if (arrayObj[i][key] == value) {
								      idx = i;
								    }
								  }
								  return idx;
								};
						$scope.isShowingFilters = false;
						if(!LocalDataServices.isResetFilters()) {
							$("#buttonBar").slideDown();
							$scope.isShowingFilters = true;
							if($scope.filters.testo != undefined) {
								$scope.testo = angular.copy($scope.filters.testo)
							}
						}
							
						
						$scope.showFilters = function () {
							$("#buttonBar").slideToggle();
							$scope.isShowingFilters = !$scope.isShowingFilters;
						}
						$scope.cancelFilters = function() {
							LocalDataServices.saveFiltersToDetail(null);
							$scope.filters = LocalDataServices
							.getFiltersFromDetail();
							$scope.testo = '';
							//$("#buttonBar").slideUp();
							loadDatas(0, $scope.filters, false)
						}
						LocalDataServices.initEditingTicket();
						} ]);