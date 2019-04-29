<template>

    <div class="App">
        <div>
            <div v-if="!onIndexPage()">
                <b-navbar variant="light" toggleable="lg">
                    <b-navbar-brand href="/"><img :src="assets.appLogo"></b-navbar-brand>

                    <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

                    <b-collapse id="nav-collapse" is-nav>
                        <b-navbar-nav>
                            <b-nav-item href="/profiles">People</b-nav-item>
                            <b-nav-item href="/trips">Trips</b-nav-item>
                            <b-nav-item href="/destinations">Destinations</b-nav-item>
                        </b-navbar-nav>

                        <b-navbar-nav class="ml-auto">
                            <b-nav-item-dropdown right>
                                <!-- Using 'button-content' slot -->
                                <template slot="button-content"><em>{{ profile.firstName }}</em></template>
                                <b-dropdown-item href="/dash">Profile</b-dropdown-item>
                                <b-dropdown-item @click="logout">Logout</b-dropdown-item>
                            </b-nav-item-dropdown>
                        </b-navbar-nav>

                    </b-collapse>
                </b-navbar>
            </div>
            <router-view></router-view>
            <!--TODO: Pass this information-->
            <!--<dash v-if="profile.length !== 0" v-bind:profile="profile" v-bind:destinations="destinations" v-bind:destinationTypes="destinationTypes" v-bind:nationalityOptions="nationalityOptions" v-bind:travTypeOptions="travTypeOptions"></dash>-->
            <!--<index v-if="profile.length === 0" v-bind:profile="profile" v-bind:destinations="destinations" v-bind:destinationTypes="destinationTypes" v-bind:nationalityOptions="nationalityOptions" v-bind:travTypeOptions="travTypeOptions" ></index>-->
            <footer-main v-if="!onIndexPage()"></footer-main>
        </div>
    </div>
</template>
<script>

    import Trips from './components/trips/tripsPage.vue'
    import Index from './components/index/indexPage.vue'
    import Dash from './components/dash/dashPage.vue'
    import Profiles from './components/profiles/profilesPage.vue'
    import Destinations from './components/destinations/destinationsPage.vue'
    import NavBarMain from './components/helperComponents/navbarMain'
    import FooterMain from './components/helperComponents/footerMain'

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
            onIndexPage() {
                //TODO: find a better way to achieve this!
                return window.location.href === "http://localhost:8080/";
            }

        },
        components: {
            Trips,
            Index,
            Dash,
            Profiles,
            Destinations,
            NavBarMain,
            FooterMain
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
