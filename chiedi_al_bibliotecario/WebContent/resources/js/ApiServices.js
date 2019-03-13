assistenzaApp.factory('ApiServices', [ '$http', '$filter',
		function($http, $filter) {
			return {
				register : function(usr) {
					// var polo = ApiServices.getPolo();

					return $http({
						method : 'post',
						url : 'user/register',
						data : usr,
						headers : {
							'Content-Type' : 'application/json'
						},
						dataType : "json"
					});
				},
				login : function(usr) {
					var url = 'user/login';

					return $http({
						method : 'post',
						url : url,
						data : usr,
						headers : {
							'Content-Type' : 'application/json'
						},
						dataType : "json"
					});
				},
				getAllUsers : function(usr) {
					return $http({
						method : 'post',
						url : 'user/getAll',
						data : usr,
						headers : {
							'Content-Type' : 'application/json'
						},
						dataType : "json"
					});
				},
				getBibliotecari : function(usr) {
					return $http({
						method : 'post',
						url : 'user/getBibliotecari',
						data : usr,
						headers : {
							'Content-Type' : 'application/json'
						},
						dataType : "json"
					});
				},
				editProfile : function(usr) {

					return $http({
						method : 'post',
						url : 'user/update',
						data : usr,
						headers : {
							'Content-Type' : 'application/json'
						},
						dataType : "json"
					});
				},
				deleteProfile : function(usr) {

					return $http({
						method : 'post',
						url : 'user/delete',
						data : usr,
						headers : {
							'Content-Type' : 'application/json'
						},
						dataType : "json"
					});
				},
				resetPsw : function(user) {

					return $http({
						method : 'post',
						url : 'user/resetPassword',
						data : user,
						headers : {
							'Content-Type' : 'application/json'
						},
						dataType : "json"
					});
				},
				resetPswEmail : function(data) {

					return $http({
						method : 'post',
						url : 'user/passwordDimenticata/' ,
						 data : data,
						headers : {
							'Content-Type' : 'application/json'
						},
						dataType : "json"
					});
				},
				changePsw : function(pswModel) {

					return $http({
						method : 'post',
						url : 'user/changePassword',
						data : pswModel,
						headers : {
							'Content-Type' : 'application/json'
						},
						dataType : "json"
					});
				},
				checkiIfNotSbnWeb : function(postObj) {

					return $http({
						method : 'post',
						url : 'user/checkiIfNotSbnWeb',
						 data : postObj,
						headers : {
							'Content-Type' : 'application/json'
						},
						dataType : "json"
					});
				},
				addTicket : function(tkt) {
					return $http({
						method : 'post',
						url : 'tickets/add',
						data : tkt,
						headers : {
							'Content-Type' : 'application/json'
						},
						dataType : "json"
					});
				},
				deleteTicket : function(tkt) {
					return $http({
						method : 'post',
						url : 'tickets/delete',
						data : tkt,
						headers : {
							'Content-Type' : 'application/json'
						},
						dataType : "json"
					});
				},
				updateTicket : function(tkt) {
					return $http({
						method : 'post',
						url : 'tickets/update',
						data : tkt,
						headers : {
							'Content-Type' : 'application/json'
						},
						dataType : "json"
					});
				},
				assignTicket : function(tkt, user) {
					return $http({
						method : 'post',
						url : 'tickets/assign/' + user,
						data : tkt,
						headers : {
							'Content-Type' : 'application/json'
						},
						dataType : "json"
					});
				},
				closeTicket : function(tkt) {
					return $http({
						method : 'post',
						url : 'tickets/close',
						data : tkt,
						headers : {
							'Content-Type' : 'application/json'
						},
						dataType : "json"
					});
				},
				getDettaglioTicket : function(ticket_id) {

					return $http({
						method : 'post',
						url : 'tickets/' + ticket_id,
						headers : {
							'Content-Type' : 'application/json'
						},
						dataType : "json"
					});
				},
				tickets : function(idToStart, filters, username, cod_polo) {
					var url = 'tickets/get/';
					if (idToStart > -1)
						url += idToStart;
					url += "/" + username;
					url += "/" + cod_polo;
					return $http({
						method : 'post',
						url : url,
						data : filters,
						headers : {
							'Content-Type' : 'application/json'
						},
						dataType : "json"
					});
				},
				addTicketMessage : function(msg) {
					// var polo = ApiServices.getPolo();

					return $http({
						method : 'post',
						url : 'messaggi/add',
						data : msg,
						headers : {
							'Content-Type' : 'application/json'
						},
						dataType : "json"
					});
				},
				updateMessage : function(msg) {
					// var polo = ApiServices.getPolo();

					return $http({
						method : 'post',
						url : 'messaggi/update',
						data : msg,
						headers : {
							'Content-Type' : 'application/json'
						},
						dataType : "json"
					});
				},
				deleteMessage : function(msg) {
					// var polo = ApiServices.getPolo();

					return $http({
						method : 'post',
						url : 'messaggi/delete',
						data : msg,
						headers : {
							'Content-Type' : 'application/json'
						},
						dataType : "json"
					});
				},
				updateCategory : function(cat) {

					return $http({
						method : 'post',
						url : 'category/update',
						data : cat,
						headers : {
							'Content-Type' : 'application/json'
						},
						dataType : "json"
					});
				},
				deleteCategory : function(cat) {

					return $http({
						method : 'post',
						url : 'category/delete',
						data : cat,
						headers : {
							'Content-Type' : 'application/json'
						},
						dataType : "json"
					});
				},
				addCategory : function(cat) {

					return $http({
						method : 'post',
						url : 'category/add',
						data : cat,
						headers : {
							'Content-Type' : 'application/json'
						},
						dataType : "json"
					});
				},
				addFaq : function(faq) {

					return $http({
						method : 'post',
						url : 'faq/add',
						data : faq,
						headers : {
							'Content-Type' : 'application/json'
						},
						dataType : "json"
					});
				},
				updateFaq : function(faq) {

					return $http({
						method : 'post',
						url : 'faq/update',
						data : faq,
						headers : {
							'Content-Type' : 'application/json'
						},
						dataType : "json"
					});
				},
				deleteFaq : function(faq) {

					return $http({
						method : 'post',
						url : 'faq/delete',
						data : faq,
						headers : {
							'Content-Type' : 'application/json'
						},
						dataType : "json"
					});
				},
				importaBiblioteca : function(cd_polo,cd_bib) {
					var url = "biblioteche/import/"+cd_polo+"/"+cd_bib;
					return $http({
						method : 'get',
						url : url,
						data : null,
						headers : {
							'Content-Type' : 'application/json'
						},
						dataType : "json"
					});
					
				},
				deleteBiblioteca : function(cd_polo, cd_bib, biblioteca) {
					var url = "biblioteche/delete/"+cd_polo+"/"+cd_bib;
					return $http({
						method : 'post',
						url : url,
						data : biblioteca,
						headers : {
							'Content-Type' : 'application/json'
						},
						dataType : "json"
					});
				},
				addBiblioteca : function(biblioteca) {
					var url = "biblioteche/add";
					return $http({
						method : 'post',
						url : url,
						data : biblioteca,
						headers : {
							'Content-Type' : 'application/json'
						},
						dataType : "json"
					});
				},
				checkTicketBiblioteca : function(biblioteca) {
					var url = "biblioteche/checkTicketsAssociati";
					return $http({
						method : 'post',
						url : url,
						data : biblioteca,
						headers : {
							'Content-Type' : 'application/json'
						},
						dataType : "json"
					});
				},
				getFaq : function(faq) {

					return $http({
						method : 'post',
						url : 'faq/get',
						data : cat,
						headers : {
							'Content-Type' : 'application/json'
						},
						dataType : "json"
					});
				},
				reloadApplicationProps : function() {
					$http({
						method : 'get',
						url : 'restart',
					});
				},
				uploadFile : function(file, tipo, idTicket, idMessaggio) {
					var fd = new FormData();
					fd.append('file', file);
					var apiUrl = 'files/put/allegato/';
					switch (tipo.toLowerCase()) {
					case "ticket":
						apiUrl += "ticket/" + idTicket;
						break;
					case "messaggio":
						apiUrl += "messaggio/" + idTicket + "/" + idMessaggio;
						break;
					default:
						break;
					}
					return $http.post(apiUrl, fd, {
						transformRequest : angular.identity,
						headers : {
							'Content-Type' : undefined
						}
					})
				}
			}
		} ]);
