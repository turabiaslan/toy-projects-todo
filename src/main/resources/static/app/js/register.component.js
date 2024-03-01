angular.module('toDoApp')
    .component('register', {
        templateUrl: 'app/template/register.html',
        controller: function ($scope, UserApi, $location, AuthenticationService) {

            $scope.register = function () {
                UserApi.register($scope.user, function (response) {
                    if (response.success) {
                        AuthenticationService.setLoggedIn(true);
                        $location.path('/home')
                    } else {
                        toastr.error('Something went wrong. Please try again!')
                    }
                });
            }

            $scope.init = function () {
                $scope.user = {};
            }
            $scope.init();
        }


    })