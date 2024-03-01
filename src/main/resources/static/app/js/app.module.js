let app = angular.module("toDoApp",

    [
        'ngRoute',
        'ngResource',
        'ui.bootstrap',
        'ui.bootstrap.datetimepicker',
        'ngFileUpload'
    ]);


app.config(["$routeProvider", "$locationProvider", "$httpProvider",
    function ($routeProvider, $locationProvider, $httpProvider) {
        $routeProvider
            .when('/login', {
                template: '<login></login>'
            })
            .when('/register', {
                template: '<register></register>'
            })
            .when('/profile', {
                template: '<profile></profile>'
            })
            .when('/search/:param', {
                template: '<search></search>'
            })
            .when('/:todoX', {
                template: '<home></home>'
            })
            .otherwise("/login");

        $locationProvider.html5Mode(true);

        $httpProvider.interceptors.push(function ($q, $location) {
            return {
                'responseError': function (response) {
                    if (response.status == 401 ) {
                        $location.path("/login");
                    }
                    return $q.reject(response);
                }
            };
        });
    }]);
app.factory('UserApi', ['$resource', function ($resource) {
    let baseUrl = "/user";
    return $resource('', {}, {
        register: {
            method: "POST",
            url: baseUrl + "/register"
        },
        login: {
            method: "POST",
            url: baseUrl + "/login",

        },
        logout: {
            method: "GET",
            url: baseUrl + "/logout"
        },
        updateProfile: {
            method: "POST",
            url: baseUrl + "/me",
            transformRequest: function (data) {
                let form = new FormData();
                form.append("name", data.name);
                form.append("surname", data.surname);
                form.append("file", data.avatar);
                return form;
            },
            headers: {"Content-Type": undefined}
        },
        whoami: {
            method: "GET",
            url: baseUrl + "/me"
        },

    });
}]);
app.factory('TodoApi', ['$resource', function ($resource) {
    let baseUrl = "/to-do";
    return $resource('', {}, {
        addToDo: {
            method: "POST",
            url: baseUrl + "/add-to-do"
        },
        showToday: {
            method: "GET",
            url: baseUrl + "/show-today",
            isArray: true
        },
        showMonth: {
            method: "GET",
            url: baseUrl + "/show-month",
            isArray: true
        },
        showFlagged: {
            method: "GET",
            url: baseUrl + "/show-flagged",
            isArray: true
        },
        showAll: {
            method: "GET",
            url: baseUrl + "/show-all",
            isArray: true
        },
        showMonthWithPriority: {
            method: "GET",
            url: baseUrl + "/show-month-with-priority",
            isArray: true
        },
        markAsCompleted: {
            method: "GET",
            url: baseUrl + "/mark-completed",
        },
        showCompleted: {
            method: "GET",
            url: baseUrl + "/show-completed",
            isArray: true
        },
        searchTodo: {
            method: "GET",
            url: baseUrl + "/search/:param",
            isArray: true
        }

    })
}])
app.factory('AuthenticationService', function (UserApi) {

    let loggedIn = false;
    let profile = {};
    let observers = [];

    if (current_user) {
        setLoggedIn(true);
        setProfile(current_user.profile);
    }

    function setLoggedIn(state) {
        loggedIn = state;
        observers.forEach(f => f());

    }

    function addObserver(f) {
        observers.push(f);
    }

    function setProfile(p) {
        profile = p;
        observers.forEach(f => f());
    }

    return {
        getLoggedIn: function () {
            return loggedIn;
        },
        setLoggedIn: setLoggedIn,

        addObserver: addObserver,

        getProfile: function () {
            return profile;
        },

        setProfile: setProfile,

    };

});

app.controller("HeaderController", function ($scope, UserApi, $location, AuthenticationService, $uibModal) {
    $scope.logout = function () {
        UserApi.logout(function (response) {
            AuthenticationService.setLoggedIn(false);
            $location.path("/login")
        });
    };
    $scope.open = function () {
        let modalInstance = $uibModal.open({
            templateUrl: '/app/template/todo-modal.html',
            controller: 'TodoModalController',
            size: 'md',
            resolve: {
                todo: function () {
                    return $scope.todo
                }

            }
        });
        modalInstance.result
            .then(function (todo) {

            });
    };
    $scope.select = function (item) {
        $scope.selected = item;
    };
    $scope.navigate = function (path) {
        $location.path('/' + path);
        $scope.selected = path;
    }
    $scope.search = function (x) {
        $location.path("/search/" + x);

    }
    $scope.loggedInStateChanged = function () {
        $scope.me = AuthenticationService.getProfile();
        $scope.isLoggedIn = AuthenticationService.getLoggedIn();
    };
    $scope.init = function () {
        $scope.selected = 'today';
        $scope.loggedInStateChanged();
        AuthenticationService.addObserver($scope.loggedInStateChanged);
    };

    $scope.init();

});
app.controller("TodoModalController", function ($scope, $uibModalInstance, TodoApi) {

    $scope.ok = function () {
        TodoApi.addToDo($scope.todo, function (response) {
            $uibModalInstance.close(response);
        });
    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
    $scope.init = function () {
        $scope.todo = {};
    };
    $scope.init()
});


app.directive('ngEnter', function () {
    return function (scope, element, attrs) {
        element.bind("keydown keypress", function (event) {
            if (event.which === 13) {
                scope.$apply(function () {
                    scope.$eval(attrs.ngEnter);
                });

                event.preventDefault();
            }
        });
    };
});