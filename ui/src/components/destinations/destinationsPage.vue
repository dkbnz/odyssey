<template>
    <div>
        <!--Shows tabs for destination page-->
        <nav-bar-main v-bind:profile="profile"></nav-bar-main>
        <b-navbar variant="light">
            <b-navbar-nav>
                <b-nav-item @click="togglePage(searchDestinations)">Search for a Destination</b-nav-item>
                <b-nav-item @click="togglePage(addDestinations)">Add a Destination</b-nav-item>
            </b-navbar-nav>
        </b-navbar>
        <!--Displays currently selected page-->
        <search-destinations v-if="searchDestinations" v-bind:profile="profile" v-bind:destinationTypes="destinationTypes"></search-destinations>
        <add-destinations v-if="addDestinations" v-bind:profile="profile" v-bind:destinations="destinations" v-bind:destinationTypes="destinationTypes" ></add-destinations>
        <footer-main></footer-main>
    </div>
</template>

<script>
    import SearchDestinations from './searchDestinations.vue'
    import AddDestinations from './addDestinations.vue'
    import NavBarMain from '../helperComponents/navbarMain.vue'
    import FooterMain from '../helperComponents/footerMain.vue'
    export default {
        name: "destinationsPage",
        props: ['profile', 'destinations', 'destinationTypes'],
        created() {
            document.title = "TravelEA - Destinations";
        },
        components: {
            SearchDestinations,
            AddDestinations,
            NavBarMain,
            FooterMain
        },
        mounted () {
        },
        data: function() {
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
        }
    }
</script>

<style scoped>

</style>