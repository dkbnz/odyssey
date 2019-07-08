<template>
    <div class="App">
        <div>
            <router-view v-bind:profile="profile" v-bind:destinations="destinations"
                         v-bind:destinationTypes="destinationTypes" v-bind:nationalityOptions="nationalityOptions"
                         v-bind:travTypeOptions="travTypeOptions" @data-changed="refreshData"></router-view>
        </div>
    </div>
</template>
<script>
    import Index from './components/index/indexPage.vue'
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
            this.getDestinations();
            this.getDestinationTypes(destinationT => this.destinationTypes = destinationT);
        },
        data() {
            return {
                profile: "",
                nationalityOptions: [],
                travTypeOptions: [],
                destinations: [],
                destinationTypes: [],
                trips: []
            }
        },
        methods: {
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
             * Sorts the destinations based on the name of the destination.
             * @param first      for each destination, current destination
             * @param next       for each destination, next destination
             * @returns {number} depending on if destinations should be swapped or not -1, 1, 0
             */
            compare(first, next) {
                const nameFirst = first.name.toUpperCase(); // ignore upper and lowercase
                const nameSecond = next.name.toUpperCase(); // ignore upper and lowercase

                if (nameFirst < nameSecond ) {
                    return -1;
                } else if (nameFirst > nameSecond ) {
                    return 1;
                } else {
                    return 0;
                }
            },

            getDestinationTypes(updateDestinationTypes) {
                return fetch(`/v1/destinationTypes`, {
                    accept: "application/json"
                })
                    .then(this.parseJSON)
                    .then(updateDestinationTypes);
            },
            getProfile(updateProfile) {
                return fetch(`/v1/profile`, {
                    accept: "application/json"
                })
                    .then(this.parseJSON)
                    .then(updateProfile);
            },
            getNationalities(updateNationalities) {
                return fetch(`/v1/nationalities`, {
                    accept: "application/json"
                })
                    .then(this.parseJSON)
                    .then(updateNationalities);
            },
            getTravTypes(updateTravellerTypes) {
                return fetch(`/v1/travtypes`, {
                    accept: "application/json"
                })
                    .then(this.parseJSON)
                    .then(updateTravellerTypes);
            },
            parseJSON(response) {
                return response.json();
            },
            /**
             * Refreshes data when data has been changed.
             */
            refreshData() {
                this.getDestinations();
            }
        },
        components: {
            Index
        }
    }
</script>
<style>
    @import "css/app.css";
</style>
