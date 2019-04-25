<template>

    <div class="App">
        <div>
            <b-navbar>
                <b-navbar-brand href="#"><img :src="assets.appLogo"></b-navbar-brand>
            </b-navbar>
        </div>

        <trips></trips>

    </div>
</template>
<script>

    import Trips from './components/Trips.vue'
    import assets from './assets'
    export default {
        computed: {
            assets () {
                return assets
            },
        },
        mounted () {
            this.getSummary(summary => this.appSummary = summary.content)
        },
        data () {
            return {
                appSummary: ''
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
            Trips
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
