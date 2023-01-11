angular.module('phinstagram')
    .component('register', {
        templateUrl: '/app/template/register.html',
        controller: function ($scope, AccountApi) {

            $scope.register = function () {
                AccountApi.register({
                    username: $scope.regist.username,
                    password: $scope.regist.password,
                    fullname: $scope.regist.fullname,
                    birthDate: $scope.regist.birthDate,
                    avatar: $scope.file
                }, function (response) {
                    if (response.success) {
                        toastr.success("Your profile successfully created!")
                        location.href = "/home";
                    } else {
                        toastr.error("Something went wrong please try again!")
                    }
                });
            }
            $scope.init = function () {
            };

            $scope.init();
        }
    });