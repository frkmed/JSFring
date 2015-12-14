(function() {
	'use strict';

	/**
	 * Configuration of i18n translations.
	 */
	angular.module('jsangleApp')
		.config(['$translateProvider', function($translateProvider) {
			$translateProvider.translations('en', {
				'right.code': 'Code',
				'right.title': 'Title',
				'profile.id': 'Id',
				'profile.title': 'Title',
				'user.login': 'Login',
				'user.email': 'Email',
				'user.lastConnection': 'Last connection',
			});
			$translateProvider.translations('fr', {
				'right.code': 'Code',
				'right.title': 'Titre',
				'profile.id': 'Id',
				'profile.title': 'Titre',
				'user.login': 'Identifiant',
				'user.email': 'Email',
				'user.lastConnection': 'Derni�re connexion',
			});
			$translateProvider.preferredLanguage('en');
		}]);
})();
