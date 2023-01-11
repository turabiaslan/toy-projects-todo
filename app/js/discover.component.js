angular.module('phinstagram')
    .component('discover', {
        templateUrl: 'app/template/discover.html',
        controller: function ($scope, $location, AccountApi, AuthenticationService, $q, $routeParams) {




            $scope.discover = function (){
                AccountApi.discover({page: $scope.paging.current -1, size: $scope.paging.size}, function (data) {
                    $scope.discoverPhotos =data.content;

                    /**let chunk = function(d, size) {
                        let photos = [];
                        for (let i=0; i<Object.keys(d).length; i+=size) {
                            photos.push(d.slice(i, i+size));
                        }
                        return photos;
                    };

                    $scope.discoverPhotos = chunk($scope.discoverPhotos, 4); */
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

                $scope.discover();

            };


            $scope.init();
        }
    })

