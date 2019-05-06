<template>
    <div class="App">
        <div>
            <router-view v-bind:profile="profile" v-bind:destinations="destinations"
                         v-bind:destinationTypes="destinationTypes" v-bind:nationalityOptions="nationalityOptions"
                         v-bind:travTypeOptions="travTypeOptions"></router-view>
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
            },
        },
        mounted() {
            this.getProfile(profile => this.profile = profile);
            this.getNationalities(nationalities => this.nationalityOptions = nationalities);
            this.getTravTypes(travTypes => this.travTypeOptions = travTypes);
            this.getDestinations(destinations => this.destinations = destinations);
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
            getDestinations(updateDestinations) {
                return fetch(`/v1/destinations`, {
                    accept: "application/json"
                })
                    .then(this.parseJSON)
                    .then(updateDestinations);
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
