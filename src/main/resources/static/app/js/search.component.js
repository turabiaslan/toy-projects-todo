angular.module('toDoApp')
    .component('search', {
        templateUrl: 'app/template/search.html',
        controller: function ($scope, $location, TodoApi) {


            let param = location.pathname.split('/').slice(-1)[0]

            $scope.search = function (){
                TodoApi.searchTodo({param: param}, function (data) {
                    $scope.todos = data;
                    console.log(data);
                });

            };


            $scope.init = function () {

                $scope.search();


            };


            $scope.init();
        }
    })

