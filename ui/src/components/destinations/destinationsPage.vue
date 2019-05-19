<template>
    <div v-if="profile.length !== 0">
        <!--Shows tabs for destination page-->
        <nav-bar-main v-bind:profile="profile"></nav-bar-main>
        <b-navbar variant="light">
            <b-navbar-nav>
                <b-nav-item @click="togglePage(searchDestinations)">Search for a Destination</b-nav-item>
                <b-nav-item @click="togglePage(addDestinations)">Add a Destination</b-nav-item>
            </b-navbar-nav>
        </b-navbar>
        <!--Displays currently selected page-->
        <search-destinations v-if="searchDestinations" :profile="profile"
                             :destinationTypes="destinationTypes"></search-destinations>
        <add-destinations @data-changed="emitDataChanged" v-if="addDestinations" :profile="profile"
                          :destinations="destinations" :destinationTypes="destinationTypes"></add-destinations>
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
            togglePage: function(viewPage) {
                if(!viewPage) {
                    this.searchDestinations = !this.searchDestinations;
                    this.addDestinations = !this.addDestinations;
                }
            },

            /**
             * Emits to the App Vue component to refresh the data when data has been changed.
             */
            emitDataChanged() {
                this.emit('data-changed', true);
            }
        }
    }
</script>