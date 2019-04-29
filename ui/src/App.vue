<template>

    <div class="App">
        <div>
            <profiles v-if="profile.length !== 0" v-bind:profile="profile" v-bind:destinations="destinations" v-bind:destinationTypes="destinationTypes" v-bind:nationalityOptions="nationalityOptions" v-bind:travTypeOptions="travTypeOptions"></profiles>
            <index v-if="profile.length === 0" v-bind:profile="profile" v-bind:destinations="destinations" v-bind:destinationTypes="destinationTypes" v-bind:nationalityOptions="nationalityOptions" v-bind:travTypeOptions="travTypeOptions" ></index>
        </div>
    </div>
</template>
<script>

    import Trips from './components/trips/tripsPage.vue'
    import Index from './components/index/indexPage.vue'
    import Dash from './components/dash/dashPage.vue'
    import Profiles from './components/profiles/profilesPage.vue'
    import Destinations from './components/destinations/destinationsPage.vue'

    import assets from './assets'
    export default {
        computed: {
            assets () {
                return assets
            },
        },
        mounted () {
            this.getProfile(profile => this.profile = profile);
            this.getNationalities(nationalities => this.nationalityOptions = nationalities);
            this.getTravTypes(travTypes => this.travTypeOptions = travTypes);
            this.getDestinations(destinations => this.destinations = destinations);
            this.getDestinationTypes(destinationT => this.destinationTypes = destinationT);
        },
        data () {
            return {
                profile: "",
                nationalityOptions: [],
                travTypeOptions: [],
                destinations: [],
                destinationTypes: []
            }
        },
        methods: {
            getDestinations (cb) {
                return fetch(`/v1/destinations`, {
                    accept: "application/json"
                })
                    //.then(this.checkStatus)
                    .then(this.parseJSON)
                    .then(cb);
            },
            getDestinationTypes (cb) {
                return fetch(`/v1/destinationTypes`, {
                    accept: "application/json"
                })
                //.then(this.checkStatus)
                    .then(this.parseJSON)
                    .then(cb);
            },


            getProfile (cb) {
                return fetch(`/v1/profile`, {
                    accept: "application/json"
                })
                    //.then(this.checkStatus)
                    .then(this.parseJSON)
                    .then(cb);
            },

            getNationalities (cb) {
                return fetch(`/v1/nationalities`, {
                    accept: "application/json"
                })
                    //.then(this.checkStatus)
                    .then(this.parseJSON)
                    .then(cb);
            },
            getTravTypes (cb) {
                return fetch(`/v1/travtypes`, {
                    accept: "application/json"
                })
                    //.then(this.checkStatus)
                    .then(this.parseJSON)
                    .then(cb);
            },

            checkStatus (response) {
                if (response.status >= 200 && response.status < 300) {
                    return response;
                }
                const error = new Error(`HTTP Error ${response.statusText}`);
                error.status = response.statusText;
                error.response = response;
                console.log(error); // eslint-disable-line no-console
                throw error;
            },
            parseJSON (response) {
                return response.json();
            },

        },
        components: {
            Trips,
            Index,
            Dash,
            Profiles,
            Destinations
        }
    }
</script>
<style>
    html {
        /* The html and body elements cannot have any padding or margin. */
        height: 100%;
    }

    body {
        background-color: #e2e6ea;
        padding-bottom: 50px;

    }
    .container {
        margin-top: 20px;
        padding-top: 20px;
        padding-bottom: 50px;
        background-color: white;
        border-radius: 5px;
    }

    /* Imports the Bauman's Regular Font */
    @font-face
    {
        font-family: Baumans;
        src: url('../static/Baumans-Regular.ttf');
    }

    /* Sets the font styling for headings*/
    h1, h2, h3, h4 {
        font-family: Baumans, sans-serif;
        font-weight: bold;
    }


    #footer {
        background-color: white;
        text-align: center;
        position: fixed;
        left: 0px;
        bottom: 0px;
        height: 40px;
        width: 100%;
        font-size: 0.7em;
    }


    /* Page Titles and Subtitles*/
    .page_title {
        text-align: center;
    }
</style>
