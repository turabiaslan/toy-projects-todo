angular.module('phinstagram')
    .component('search', {
        templateUrl: 'app/template/search.html',
        controller: function ($scope, $location, AccountApi) {


            let name = location.pathname.split('/').slice(-1)[0]

            $scope.search = function (){
                AccountApi.searchUser({name: name}, function (data) {
                    $scope.result = data;
                    console.log(data);
                });

            };

            $scope.profile = function (x) {
                $location.path("/profile/" + x);
            };






            $scope.init = function () {

                $scope.search();




            };


            $scope.init();
        }
    })

