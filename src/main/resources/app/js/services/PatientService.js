'use strict';

pointyApp.factory('PatientService', ['$resource',

function($resource) {
    function unmarshalled(patient) {
        var p = _.clone(patient);
        p.visitDate = new Date(patient.visitDate);
        return p;
    }

    function marshalled(patient) {
        var p = _.clone(patient);
        p.visitDate = patient.visitDate.getTime();
        return p;
    }

    return {
        save : function(patient) {
            return $resource('/patients').save(marshalled(patient)).$promise;
        },

        list : function() {
            return $resource('/patients').query().$promise.then(function(patientList) {
                return _.map(patientList, function(patient) {
                    return unmarshalled(patient);
                });
            });
        },

        patientDetail : function(id) {
            return $resource('/patients/:id').get({
                id : id
            }).$promise.then(function(patient) {
                return unmarshalled(patient);
            });
        },

        deletePatient : function(id) {
            return $resource('/patients/:id').remove({
                id : id
            }).$promise;
        }
    };
}

]);
