'use strict';

pointyApp.controller('CommonController', ['$scope',

function CommonController($scope) {
    $scope.showFeedback = false;

    $scope.closeAlert = function() {
        $scope.showFeedback = false;
    };

    function showAlert(type, message) {
        $scope.status = message;
        $scope.showFeedback = "true";
        $scope.alertType = type;
    }
}

]);