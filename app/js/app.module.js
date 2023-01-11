let app = angular.module("phinstagram",
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
            .when('/user_settings', {
                template: '<user_settings></user_settings>'
            })
            .when('/register', {
                template: '<register></register>'
            })
            .when('/discover', {
                template: '<discover></discover>'
            })
            .when('/profile/:photoUsername', {
                template: '<profile></profile>'
            })
            .when('/search/:name', {
                template: '<search></search>'
            })
            .when('/home', {
                template: '<home></home>'
            })
            .otherwise("/login");

        $locationProvider.html5Mode(true);

        $httpProvider.interceptors.push(function ($q, $location) {
            return {
                'responseError': function (response) {
                    if (response.status == 401) {
                        $location.path("/login");
                    }
                    return $q.reject(response);
                }
            };
        });
    }]);


app.factory('AccountApi', ['$resource', function ($resource) {
    let baseUrl = "/account";
    return $resource('', {}, {
        register: {
            method: "POST",
            url: baseUrl + "/register"
        },
        login: {
            method: 'POST',
            url: baseUrl + "/login"
        },
        me: {
            method: "GET",
            url: baseUrl + "/me"
        },
        saveUserSettings: {
            method: "POST",
            url: baseUrl + "/me",
            transformRequest: function (data) {
                let form = new FormData();
                form.append("fullname", data.fullname);
                form.append("birthDate", data.birthDate);
                form.append("file", data.avatar);
                return form;
            },
            headers: {'Content-Type': undefined}
        },
        logout: {
            method: "GET",
            url: baseUrl + "/logout"
        },

        post: {
            method: "POST",
            url: baseUrl + "/post",
            transformRequest: function (data) {
                let form = new FormData();
                form.append("caption", data.caption);
                form.append("file", data.file);
                return form;
            },
            headers: {'Content-Type': undefined}
        },

        personalFeed: {
            method: "GET",
            url: baseUrl + "/personal-feed",
        },
        avatar: {
            method: "GET",
            url: baseUrl + "/avatar/img/{path}"
        },

        discover: {
            method: "GET",
            url: baseUrl + "/discover",
        },

        showFollowers: {
            method: "GET",
            url: baseUrl + "/show-followers",
            isArray: true
        },

        showFollowing: {
            method: "GET",
            url: baseUrl + "/show-following",
            isArray: true
        },

        showProfile: {
            method: "GET",
            url: baseUrl + "/profile/:photoUsername",

        },
        follow: {
            method: "GET",
            url: baseUrl + "/follow",
        },
        unfollow: {
            method: "GET",
            url: baseUrl + "/unfollow",
        },
        deletePhoto: {
            method: "GET",
            url: baseUrl + "/delete",
        },
        isAlreadyFollowing: {
            method: "GET",
            url: baseUrl + "/already-following",
        },
        userSettings: {
            method: "GET",
            url: baseUrl + "/user-settings",
        },
        searchUser: {
            method: "GET",
            url: baseUrl + "/search/:name",
            isArray: true,
        },


    });
}]);


app.factory("AuthenticationService", function (AccountApi) {

    let loggedIn = false;
    let userSettings = {}; // manage this Turabi

    let observers = []; // array of functions

    let username = "";

    /**
     AccountApi.me(function (response) {
        setLoggedIn(response.success);
    });*/


    if (current_user) {
        setLoggedIn(true);
        setUserSettings(current_user.userSettings);
        setUsername(current_user.username);
    }


    function setLoggedIn(state) {
        loggedIn = state;
        if (!state) {
            userSettings = {};
            username = "";
        }
        observers.forEach(f => f());
    }

    function addObserver(f) {
        observers.push(f);
    }

    function setUserSettings(p) {
        userSettings = p;
        observers.forEach(f => f());
    }

    function setUsername(u) {
        username = u;
    }


    return {
        getLoggedIn: function () {
            return loggedIn;
        },
        setLoggedIn: setLoggedIn,
        addObserver: addObserver,


        getUserSettings: function () {
            return userSettings;

            // implement this
        },
        getUsername: function () {
            return username;
        },

        setUsername: setUsername,

        setUserSettings: setUserSettings

    };
});


app.controller("HeaderController", function ($scope, AccountApi, $location, AuthenticationService, $uibModal) {

    $scope.logout = function () {
        AccountApi.logout(function (response) {
            AuthenticationService.setLoggedIn(false);
            $location.path('/login');
        });
    }

    $scope.select = function (item) {
        $scope.selected = item;
    };

    $scope.loggedInStateChanged = function () {
        $scope.me = AuthenticationService.getUserSettings();
        $scope.profile = AuthenticationService.getUsername();
        $scope.isLoggedIn = AuthenticationService.getLoggedIn();
    };

    $scope.navigate = function (path) {
        $location.path('/' + path);
        $scope.selected = path;
    };
    $scope.open = function () {
        let modalInstance = $uibModal.open({
            templateUrl: '/app/template/post-modal.html',
            controller: 'PostModalController',
            size: 'md',
            resolve: {
                photo: function () {
                    return $scope.photo;
                }
            }
        });
        modalInstance.result
            .then(function (photo) {
                $scope.feed.push(photo);
            });
    };
    $scope.search = function (x) {
        $location.path("/search/" + x);

    }


    $scope.init = function () {
        // SSP, FEAT, NEW
        $scope.selected = 'home';
        $scope.loggedInStateChanged();
        AuthenticationService.addObserver($scope.loggedInStateChanged);
    };

    $scope.init();
});

app.controller("PostModalController", function ($scope, $uibModalInstance, AccountApi) {

    $scope.ok = function () {
        AccountApi.post({file: $scope.file, caption: $scope.photo.caption}, function (response) {
            $uibModalInstance.close(response);
        });
    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
    $scope.init = function () {
        $scope.photo = {};
    };
    $scope.init()
});

app.filter("timeago", function () {
    //time: the time
    //local: compared to what time? default: now
    //raw: wheter you want in a format of "5 minutes ago", or "5 minutes"
    return function (time, local, raw) {
        if (!time) return "never";

        if (!local) {
            (local = Date.now())
        }

        if (angular.isDate(time)) {
            time = time.getTime();
        } else if (typeof time === "string") {
            time = new Date(time).getTime();
        }

        if (angular.isDate(local)) {
            local = local.getTime();
        } else if (typeof local === "string") {
            local = new Date(local).getTime();
        }

        if (typeof time !== 'number' || typeof local !== 'number') {
            return;
        }

        var
            offset = Math.abs((local - time) / 1000),
            span = [],
            MINUTE = 60,
            HOUR = 3600,
            DAY = 86400,
            WEEK = 604800,
            MONTH = 2629744,
            YEAR = 31556926,
            DECADE = 315569260;

        if (offset <= MINUTE) span = ['', raw ? 'now' : 'less than a minute'];
        else if (offset < (MINUTE * 60)) span = [Math.round(Math.abs(offset / MINUTE)), 'min'];
        else if (offset < (HOUR * 24)) span = [Math.round(Math.abs(offset / HOUR)), 'hr'];
        else if (offset < (DAY * 7)) span = [Math.round(Math.abs(offset / DAY)), 'day'];
        else if (offset < (WEEK * 52)) span = [Math.round(Math.abs(offset / WEEK)), 'week'];
        else if (offset < (YEAR * 10)) span = [Math.round(Math.abs(offset / YEAR)), 'year'];
        else if (offset < (DECADE * 100)) span = [Math.round(Math.abs(offset / DECADE)), 'decade'];
        else span = ['', 'a long time'];

        span[1] += (span[0] === 0 || span[0] > 1) ? 's' : '';
        span = span.join(' ');

        if (raw === true) {
            return span;
        }
        return (time <= local) ? span + ' ago' : 'in ' + span;
    }

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
