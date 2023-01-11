angular.module('phinstagram')
    .component('userSettings', {
        templateUrl: '/app/template/user_settings.html',
        controller: function ($scope, AccountApi, AuthenticationService) {

            $scope.save = function () {
                AccountApi.saveUserSettings({fullname: $scope.me.userSettings.fullname,
                                            birthDate: $scope.me.userSettings.birthDate,
                                            avatar: $scope.file},  function (response) {
                    if(response.success){
                        toastr.success("Settings successfully updated")
                        AuthenticationService.setUserSettings(response.userSettings);
                    }else {
                        toastr.error("Something went wrong. Please try again.")
                    }

                });
            };



            $scope.init = function () {

                $scope.me = AccountApi.me();
            };

            $scope.init();
        }
    });


    


    