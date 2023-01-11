angular.module('phinstagram')
    .component('profile', {
        templateUrl: 'app/template/profile.html',
        controller: function ($scope, $routeParams, AccountApi) {

            let name = location.pathname.split('/').slice(-1)[0]

            $scope.follow = function () {
                AccountApi.follow({name: name}, function () {
                });
            };

            $scope.unfollow = function () {
                AccountApi.unfollow({name: name}, function () {

                });
            };
            $scope.delete = function (photo) {
                Swal.fire({
                    title: 'Are you sure',
                    text: "You won't be able to revert this!",
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: 'Yes, delete it!'
                }).then((result) => {
                    if (result.isConfirmed) {
                        AccountApi.deletePhoto({id: photo.id}, function () {
                            _.remove($scope.selectedProfile, {id: photo.id});
                        });


                    }
                })
            }




            $scope.init = function () {
                $scope.paging = {
                    size: 9,
                    current: 1
                };
                AccountApi.showProfile({
                    photoUsername: $routeParams.photoUsername,
                    page: $scope.paging.current - 1, size: $scope.paging.size
                }, function (data) {
                    $scope.selectedProfile = data.content;
                });

                AccountApi.userSettings({name: name}, function (data) {
                    $scope.selectedUser = data;
                });
                AccountApi.isAlreadyFollowing({username: name}, function (data) {
                    $scope.alreadyFollowing = data.status;
                });
                AccountApi.showFollowing({name: name}, function (data) {
                    $scope.following = data;
                });
                AccountApi.showFollowers({name: name}, function (data) {
                    $scope.followers = data;
                });
                AccountApi.me(function (data) {
                    $scope.me = data.userSettings;

                });
            }


            $scope.init();
        }
    });