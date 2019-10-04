<template>
    <div class="App">
        <div>
            <router-view :profile="profile"
                         :destinationTypes="destinationTypes"
                         :nationalityOptions="nationalityOptions"
                         :travTypeOptions="travTypeOptions"
                         @fetch-profile="getProfile"
                         @clear-profile="profile = null"
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
                profile: null,
                nationalityOptions: [],
                travTypeOptions: [],
                destinationTypes: []
            }
        },

        watch: {
            /**
             * Used to re-fetch the profile whenever the route address changes. This is to ensure any user data is
             * updated as the navigate through the application.
             */
            $route () {
                this.getProfile();
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
                    self.handleErrorResponse(response);
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
                    if (self.$router.currentRoute.name !== "index") {
                        self.handleErrorResponse(response);
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
                    self.handleErrorResponse(response);
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
                    self.handleErrorResponse(response);
                });
            }
        }
    }
</script>

<style>
    @import "css/app.css";
</style>
