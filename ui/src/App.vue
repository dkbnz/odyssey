<template>
    <div class="App">
        <div>
            <router-view :profile="profile"
                         :destinationTypes="destinationTypes"
                         :nationalityOptions="nationalityOptions"
                         :travTypeOptions="travTypeOptions"
                         @profile-received="getProfile">
            </router-view>
        </div>
    </div>
</template>

<script>
    export default {
        mounted() {
            this.getProfile();
            this.getNationalities();
            this.getTravellerTypes();
            this.getDestinationTypes();
        },

        data() {
            return {
                profile: {},
                nationalityOptions: [],
                travTypeOptions: [],
                destinationTypes: []
            }
        },

        methods: {
            /**
             * Retrieves all the destination types.
             */
            getDestinationTypes() {
                let self = this;
                fetch(`/v1/destinationTypes`, {
                    accept: "application/json"
                }).then(function (response) {
                    if (!response.ok) {
                        throw response;
                    } else {
                        return response.json();
                    }
                }).then(function (responseBody) {
                    self.destinationTypes = responseBody;
                }).catch(function (response) {
                    if (response.status > 404) {
                        self.showErrorToast(JSON.parse(JSON.stringify([{message: "An unexpected error occurred"}])));
                    } else {
                        response.json().then(function(responseBody) {
                            self.showErrorToast(responseBody);
                        });
                    }
                });
            },


            /**
             * Retrieves the current profile.
             */
            getProfile() {
                let self = this;
                fetch(`/v1/profile`, {
                    accept: "application/json"
                }).then(function (response) {
                    if (!response.ok) {
                        throw response;
                    } else {
                        return response.json();
                    }
                }).then(function (responseBody) {
                    self.profile = responseBody;
                }).catch(function (response) {
                    if (response.status > 404) {
                        self.showErrorToast(JSON.parse(JSON.stringify([{message: "An unexpected error occurred"}])));
                    }
                });
            },


            /**
             * Retrieves all the nationalities.
             */
            getNationalities() {
                let self = this;
                fetch(`/v1/nationalities`, {
                    accept: "application/json"
                }).then(function (response) {
                    if (!response.ok) {
                        throw response;
                    } else {
                        return response.json();
                    }
                }).then(function (responseBody) {
                    self.nationalityOptions = responseBody;
                }).catch(function (response) {
                    if (response.status > 404) {
                        self.showErrorToast(JSON.parse(JSON.stringify([{message: "An unexpected error occurred"}])));
                    } else {
                        response.json().then(function(responseBody) {
                            self.showErrorToast(responseBody);
                        });
                    }
                });
            },


            /**
             * Retrieves all the traveller types.
             */
            getTravellerTypes() {
                let self = this;
                fetch(`/v1/travtypes`, {
                    accept: "application/json"
                }).then(function (response) {
                    if (!response.ok) {
                        throw response;
                    } else {
                        return response.json();
                    }
                }).then(function (responseBody) {
                    self.travTypeOptions = responseBody;
                }).catch(function (response) {
                    if (response.status > 404) {
                        self.showErrorToast(JSON.parse(JSON.stringify([{message: "An unexpected error occurred"}])));
                    } else {
                        response.json().then(function(responseBody) {
                            self.showErrorToast(responseBody);
                        });
                    }
                });
            }
        }
    }
</script>

<style>
    @import "css/app.css";
</style>
