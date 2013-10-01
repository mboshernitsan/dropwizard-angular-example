'use strict';

pointyApp.factory('PatientService', ['$resource',

function($resource) {
    function fromJSON(patient) {
        var p = _.clone(patient);
        p.visitDate = new Date(patient.visitDate);
        return p;
    }

    function toJSON(patient) {
        var p = _.clone(patient);
        p.visitDate = patient.visitDate.getTime();
        return p;
    }

    return {
        save : function(patient) {
            return $resource('/patients').save(toJSON(patient)).$promise;
        },

        list : function() {
            return $resource('/patients').query().$promise.then(function(patientList) {
                return _.map(patientList, function(patient) {
                    return fromJSON(patient);
                });
            });
        },

        patientDetail : function(id) {
            return $resource('/patients/:id').get({
                id : id
            }).$promise.then(function(patient) {
                return fromJSON(patient);
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
