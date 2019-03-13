assistenzaApp.factory('LocalDataServices',
		[
				'$filter',
				'$q',
				'$location',
				'$cookies',
				'$http',
				'$compile',
				'$controller',
				function($filter, $q, $location, $cookies, $http, $compile,
						$controller) {
					var hideAlertTime = 20000;
					var localVar = {
						error : null,
						user : null,
						isLogged : false,
						tickets : [],
						categories : [],
						faqs : [],
						biblioteche: [],
						paginationProps : null,
						build: null,
						searchFilters: null,
						conf_polo: null
					};
					var editingTicket = {
							ticket: null,
							isEditing: false
					}
					return {
						setUser : function(user) {
							localVar.user = user;
						},
						getUser : function() {
							return localVar.user;
						},
						loginSuccess : function(obj) {
							if (obj == null) {
								localVar.isLogged = false;
								return;
							}
							if (obj.responseObj.user) {
								// this.addControllers();
								this.setUser(obj.responseObj.user)
								this.setTickets(obj.responseObj.allTickets);
								this.setPaginationProps(obj.responseObj.start, obj.responseObj.numViews, obj.responseObj.totalTickets);
								localVar.isLogged = true
							} else {
								localVar.isLogged = false;
							}

						},
						isLogged : function() {
							return localVar.isLogged;
						},
						logout : function() {
							var cod_polo = localVar.conf_polo.cod_polo;
						localVar = {
								error : null,
								user : null,
								isLogged : false,
								tickets : [],
								categories : [],
								faqs : [],
								biblioteche: [],
								paginationProps : null,
								build: null,
								searchFilters: null,
								conf_polo: null
							};
						//location.reload();
						location.href = cod_polo;
						},
						setTickets : function(tickets) {
							//localVar.detail = undefined;
							localVar.tickets = tickets;
						},
						getTickets : function() {
							return localVar.tickets;
						},
						setCategories : function(categories) {
							localVar.categories = categories
						},
						getCategories : function() {
							return localVar.categories;
						},
						setFaqs : function(faqs) {
							localVar.faqs = faqs
						},
						getFaqs : function() {
							return localVar.faqs;
						},
						setDettaglioTicket : function(detail) {
							localVar.detail = detail;
						},
						getDettaglioTicket : function() {
							return localVar.detail;
						},
						setPaginationProps : function(start, rows, total) {
							localVar.paginationProps = {
								start : start,
								 rows : rows,
								total : total
							}
						},
						getPaginationProps : function() {
							return localVar.paginationProps;
						}, 
						getBuild : function() {
							return localVar.build;
						}, 
						setBuild : function(buildObj) {
							localVar.build = buildObj;
						}, 
						getApplicationName: function() {
							return localVar.build.applicationName;
						}, 
						saveFiltersToDetail: function(filters){
							localVar.searchFilters = filters;
						},
						getFiltersFromDetail: function(){
								
							return ( localVar.searchFilters == null) ? {} : localVar.searchFilters ;
						}, 
						isResetFilters: function(){
							
							return (localVar.searchFilters == null || angular.equals(localVar.searchFilters, {}));
						}, 
						setBiblioteche: function(bibs) {
							localVar.biblioteche = bibs;
						},
						getBiblioteche: function(){
							return localVar.biblioteche;
						}, 
						getHideAlertTime: function() {
							return hideAlertTime;
						},
						setEditingTicket: function(ticket) {
							editingTicket = {
									ticket: ticket,
									isEditing: true
							}
						},
						getEditingTicket: function () {
							return editingTicket;
						},
						initEditingTicket: function() {
							editingTicket = {
									ticket: null,
									isEditing: false
							}
						},
						setConfPolo(conf) {
							localVar.conf_polo = conf;
						},
						getConfPolo(){
							return localVar.conf_polo;
						}
						

					}
				} ]);