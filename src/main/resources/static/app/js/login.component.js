angular.module('toDoApp')
    .component('login' , {
      templateUrl: '/app/template/login.html',
      controller: function ($scope, UserApi, $location, AuthenticationService) {

          $scope.login=function () {
              UserApi.login($scope.credential, function (response) {
                  if(response.success) {
                      AuthenticationService.setLoggedIn(true);
                      AuthenticationService.setProfile(response.profile);
                      $location.path("/today")
                  } else {
                      toastr.error("Mail Address or password is wrong!")
                  }
              });
          }
          $scope.init = function () {
              $scope.credential = {};
          }
          $scope.init();

      }
    });