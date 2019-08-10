<template>
    <div class="App">
        <div>
            <router-view v-bind:profile="profile" v-bind:destinations="destinations"
                         v-bind:destinationTypes="destinationTypes" v-bind:nationalityOptions="nationalityOptions"
                         v-bind:travTypeOptions="travTypeOptions" @data-changed="refreshData">
            </router-view>
        </div>
    </div>
</template>

<script>
    import assets from './assets'

    export default {
        computed: {
            assets() {
                return assets
            }
        },

        created() {
            document.title = "TravelEA";
        },

        mounted() {
            this.getProfile(profile => this.profile = profile);
            this.getNationalities(nationalities => this.nationalityOptions = nationalities);
            this.getTravTypes(travTypes => this.travTypeOptions = travTypes);
            if (this.profile.id !== undefined) {
                this.getDestinations();
            }
            this.getDestinationTypes(destinationT => this.destinationTypes = destinationT);
        },

        data() {
            return {
                profile: {},
                nationalityOptions: [],
                travTypeOptions: [],
                destinations: [],
                destinationTypes: [],
                trips: []
            }
        },

        methods: {
            /**
             * Retrieves all the destinations.
             */
            getDestinations() {
                let self = this;
                return fetch(`/v1/destinations`, {
                    accept: "application/json"
                })
                    .then(this.parseJSON)
                    .then(function(response) {
                        self.destinations = (response.sort(self.compare));
                    });
            },


            /**
             * Retrieves all the destination types.
             */
            getDestinationTypes(updateDestinationTypes) {
                return fetch(`/v1/destinationTypes`, {
                    accept: "application/json"
                })
                    .then(this.parseJSON)
                    .then(updateDestinationTypes);
            },


            /**
             * Retrieves the current profile.
             */
            getProfile(updateProfile) {
                return fetch(`/v1/profile`, {
                    accept: "application/json"
                })
                    .then(this.parseJSON)
                    .then(updateProfile);
            },


            /**
             * Retrieves all the nationalities.
             */
            getNationalities(updateNationalities) {
                return fetch(`/v1/nationalities`, {
                    accept: "application/json"
                })
                    .then(this.parseJSON)
                    .then(updateNationalities);
            },


            /**
             * Retrieves all the traveller types.
             */
            getTravTypes(updateTravellerTypes) {
                return fetch(`/v1/travtypes`, {
                    accept: "application/json"
                })
                    .then(this.parseJSON)
                    .then(updateTravellerTypes);
            },


            /**
             * Converts the response body to a Json.
             */
            parseJSON(response) {
                return response.json();
            },


            /**
             * Refreshes data when data has been changed.
             */
            refreshData() {
                this.getDestinations();
            }
        }
    }
</script>

<style>
    @import "css/app.css";
</style>
