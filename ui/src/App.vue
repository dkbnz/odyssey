<template>

    <div class="App">
        <div>
            <b-navbar variant="light">
                <b-navbar-brand href="#"><img :src="assets.appLogo"></b-navbar-brand>
                <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>
                <b-collapse id="nav-collapse" is-nav>
                    <b-navbar-nav>
                        <b-nav-item href="/searchProfile">People</b-nav-item>
                        <b-nav-item href="/trips">Trips</b-nav-item>
                        <b-nav-item href="/destinations">Destinations</b-nav-item>
                    </b-navbar-nav>

                    <b-navbar-nav class="ml-auto">
                        <b-nav-item-dropdown right>
                            <!-- Using 'button-content' slot -->
                            <template slot="button-content"><em>{{ username }}</em></template>
                            <b-dropdown-item href="/dash">Profile</b-dropdown-item>
                            <b-dropdown-item href="#">Logout</b-dropdown-item>
                        </b-nav-item-dropdown>
                    </b-navbar-nav>

                </b-collapse>
            </b-navbar>
            <profiles></profiles>
        </div>
    </div>
</template>
<script>

    import Trips from './components/trips/tripsPage.vue'
    import Index from './components/index/indexPage.vue'
    import Dash from './components/dash/dashPage.vue'
    import Profiles from './components/profiles/profilesPage.vue'

    import assets from './assets'
    export default {
        computed: {
            assets () {
                return assets
            },
        },
        mounted () {
            this.getSummary(summary => this.appSummary = summary.content);
        },
        data () {
            return {
                appSummary: '',
                currentComponent: null,
                componentsArray: [Trips,Index],
                username: "Isaac",
                appDestinations: '',

            }
        },
        methods: {
            getSummary (cb) {
                return fetch(`/v1/summary`, {
                    accept: "application/json"
                })
                    .then(this.checkStatus)
                    .then(this.parseJSON)
                    .then(cb)
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
            }
        },
        components: {
            Trips,
            Index,
            Dash,
            Profiles
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
        padding-top: 20px;
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
