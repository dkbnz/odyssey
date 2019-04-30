<template>
    <div>
        <nav-bar-main v-bind:profile="profile"></nav-bar-main>
        <b-navbar variant="light">
            <b-navbar-nav>
                <b-nav-item @click="togglePage(planATrip)">Plan a Trip</b-nav-item>
                <b-nav-item @click="togglePage(yourTrips)">Your Trips</b-nav-item>
            </b-navbar-nav>
        </b-navbar>
        <plan-a-trip v-if="planATrip" v-bind:destinations="destinations"></plan-a-trip>
        <your-trips :trips="trips" v-if="yourTrips" :profile="profile"></your-trips>
        <footer-main></footer-main>

    </div>

</template>

<script>
    import PlanATrip from './planATrip.vue'
    import YourTrips from './yourTrips.vue'
    import NavBarMain from '../helperComponents/navbarMain.vue'
    import FooterMain from '../helperComponents/footerMain.vue'
    export default {
        name: "Trips",
        props: ['profile', 'destinations'],
        created() {
            document.title = "TravelEA - Trips";
        },
        data: function() {
            return {
                planATrip: true,
                yourTrips: false
            }
        },
        mounted() {
            this.getAllTrips(trips => this.trips = trips);
        },
        methods: {
            togglePage: function(viewPage) {
                if(!viewPage) {
                    this.planATrip = !this.planATrip;
                    this.yourTrips = !this.yourTrips;
                }
            },
            getAllTrips(cb) {
                let userId = this.profile.id;
                return fetch(`/v1/trips/all?id=` + userId, {
                    accept: "application/json"
                })
                //.then(this.checkStatus)
                    .then(this.parseJSON)
                    .then(cb);
            },
            parseJSON (response) {
                return response.json();
            },
        },
        components: {
            PlanATrip,
            YourTrips,
            NavBarMain,
            FooterMain
        }
    }
</script>

<style scoped>

</style>