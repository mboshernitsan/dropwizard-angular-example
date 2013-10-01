'use strict';

var pointyApp =
    angular.module('pointyApp', ['ngRoute', 'ngResource', 'ui.bootstrap'])
        .config(['$routeProvider', '$locationProvider',

        function($routeProvider, $locationProvider) {
            $routeProvider.when('/registerPatient', {
                templateUrl : 'views/Patient.html',
                controller : 'PatientController'
            });
            $routeProvider.when('/patients', {
                templateUrl : 'views/PatientList.html',
                controller : 'PatientController'
            });
            $routeProvider.when('/patientDetail/:patientId', {
                templateUrl : 'views/Patient.html',
                controller : 'PatientController'
            });
            $routeProvider.otherwise({
                redirectTo : '/patients'
            });
        }

        ]);