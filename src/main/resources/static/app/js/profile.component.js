angular.module('toDoApp')
    .component('profile' , {
      templateUrl: '/app/template/profile.html',
      controller: function ($scope, UserApi, AuthenticationService) {

          $scope.save = function () {
              UserApi.updateProfile({name: $scope.me.profile.name, surname: $scope.me.profile.surname, avatar: $scope.file}, function (response) {
                  if(response.success) {
                      toastr.success("Your profile successfully updated");
                      AuthenticationService.setProfile(response.profile)
                  }else {
                      toastr.error("Something went wrong!")
                  }

              })

          }
          $scope.init = function () {
              $scope.me = UserApi.whoami();

          };
          $scope.init();

      }
    });