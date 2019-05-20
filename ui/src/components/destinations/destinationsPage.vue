<template>
    <div v-if="profile.length !== 0">
        <!--Shows tabs for destination page-->
        <nav-bar-main v-bind:profile="profile"></nav-bar-main>
        <b-navbar class="stickyMinorNav" variant="light">
            <b-navbar-nav>
                <b-nav-item @click="togglePage(searchDestinations)">Search for a Destination</b-nav-item>
                <b-nav-item @click="togglePage(addDestinations)">Add a Destination</b-nav-item>
            </b-navbar-nav>
        </b-navbar>
        <!--Displays currently selected page-->
        <search-destinations v-bind:destinationTypes="destinationTypes" v-bind:profile="profile"
                             v-if="searchDestinations"></search-destinations>
        <add-destinations v-bind:destinationTypes="destinationTypes" v-bind:destinations="destinations" v-bind:profile="profile"
                          v-if="addDestinations"></add-destinations>
        <footer-main></footer-main>
    </div>
    <div v-else>
        <unauthorised-prompt></unauthorised-prompt>
    </div>
</template>

<script>
    import SearchDestinations from './searchDestinations.vue'
    import AddDestinations from './addDestinations.vue'
    import NavBarMain from '../helperComponents/navbarMain.vue'
    import FooterMain from '../helperComponents/footerMain.vue'
    import UnauthorisedPrompt from '../helperComponents/unauthorisedPromptPage'

    export default {
        name: "destinationsPage",
        props: ['profile', 'destinations', 'destinationTypes'],
        components: {
            SearchDestinations,
            AddDestinations,
            NavBarMain,
            FooterMain,
            UnauthorisedPrompt
        },
        mounted() {
        },
        data: function () {
            return {
                searchDestinations: true,
                addDestinations: false,
            }
        },
        methods: {
            /**
             * Switches between tabs
             * @param viewPage page to be displayed
             */
            togglePage: function (viewPage) {
                if (!viewPage) {
                    this.searchDestinations = !this.searchDestinations;
                    this.addDestinations = !this.addDestinations;
                }
            },
        }
    }
</script>