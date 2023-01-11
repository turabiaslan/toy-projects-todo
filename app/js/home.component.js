angular.module('phinstagram')
    .component('home', {
        templateUrl: 'app/template/home.html',
        controller: function ($scope, $location, AccountApi) {




            $scope.feed = function (){
                AccountApi.personalFeed({page: $scope.paging.current -1, size: $scope.paging.size}, function (data) {
                    $scope.feed = data.content;
                });

            };

            $scope.profile = function (x) {
                $location.path("/profile/" + x.username);

            };

            $scope.paging = {
                size: 9,
                current: 1,

            };


            $scope.init = function () {

                $scope.feed();

            };


            $scope.init();
        }
    })

