angular.module('toDoApp')
    .component('home', {
        templateUrl: '/app/template/home.html',
        controller: function ($scope, TodoApi, $routeParams, $q, AuthenticationService) {


            let name = location.pathname.split('/').slice(-1)[0];

            $scope.today = function () {
                TodoApi.showToday(function (data) {
                    $scope.todos = data;
                });
            };
            $scope.headControl = function () {
                if (name === 'today') {
                    return 0;
                } else if (name === 'starred') {
                    return 1;
                } else if (name === 'month') {
                    return 2;
                } else if (name === 'planned') {
                    return 3;
                } else if (name === 'completed') {
                    return 4;
                }
            };

            $scope.month = function () {
                TodoApi.showMonth(function (data) {
                    $scope.todos = data;

                });
            };
            $scope.showCompleted = function () {
                TodoApi.showCompleted(function (data) {
                    $scope.todos = data;
                });
            };
            $scope.flagged = function () {
                TodoApi.showFlagged(function (data) {
                    $scope.todos = data;
                });
            };
            $scope.planned = function () {
                TodoApi.showAll(function (data) {
                    $scope.todos = data;
                    console.log(data);
                });
            };
            $scope.pageChanged = function () {
                let todoX = $routeParams.todoX;
                switch (todoX) {
                    case "today":
                        $scope.today();
                        break;
                    case "month":
                        $scope.month();
                        break;
                    case "planned":
                        $scope.planned();
                        break;
                    case "starred":
                        $scope.flagged();
                        break;
                    case "completed":
                        $scope.showCompleted();
                        break;
                }

            };
            $scope.markAsCompleted = function (todo) {
                TodoApi.markAsCompleted({id: todo.id}, function () {
                });
            };
            $scope.markAsUncompleted = function (todo) {
                TodoApi.markAsUncompleted({id: todo.id}, function () {
                });
            }

            $scope.init = function () {

                $scope.pageChanged();

            }
            $scope.init();

        }
    });

