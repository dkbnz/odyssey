<template>
    <div>
        <b-navbar variant="light">
            <b-navbar-nav>
                <b-nav-item @click="togglePage(searchDestinations)">Search for a Destination</b-nav-item>
                <b-nav-item @click="togglePage(addDestinations)">Add a Destination</b-nav-item>
            </b-navbar-nav>
        </b-navbar>
        <search-destinations v-if="searchDestinations" v-bind:profile="profile" v-bind:destinations="destinations" ></search-destinations>
        <add-destinations v-if="addDestinations" v-bind:profile="profile" v-bind:destinations="destinations" ></add-destinations>

    </div>
</template>

<script>
    import SearchDestinations from './searchDestinations.vue'
    import AddDestinations from './addDestinations.vue'
    export default {
        name: "destinationsPage",
        props: ['profile', 'destinations'],
        created() {
            document.title = "TravelEA - Destinations";
        },
        components: {
            SearchDestinations,
            AddDestinations
        },
        data: function() {
            return {
                searchDestinations: true,
                addDestinations: false
            }
        },
        methods: {
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