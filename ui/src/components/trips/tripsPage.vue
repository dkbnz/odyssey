<template>
    <div>
        <b-navbar variant="light">
            <b-navbar-nav>
                <b-nav-item @click="togglePage(planATrip)">Plan a Trip</b-nav-item>
                <b-nav-item @click="togglePage(yourTrips)">Your Trips</b-nav-item>
            </b-navbar-nav>
        </b-navbar>
        <plan-a-trip v-if="planATrip"></plan-a-trip>
        <your-trips v-if="yourTrips"></your-trips>

    </div>

</template>

<script>
    import PlanATrip from './planATrip.vue'
    import YourTrips from './yourTrips.vue'
    export default {
        name: "Trips",
        created() {
            document.title = "TravelEA - Trips";
        },
        data: function() {
            return {
                planATrip: true,
                yourTrips: false
            }
        },
        methods: {
            togglePage: function(viewPage) {
                if(!viewPage) {
                    this.planATrip = !this.planATrip;
                    this.yourTrips = !this.yourTrips;
                }
            },
        },
        components: {
            PlanATrip,
            YourTrips
        }
    }
</script>

<style scoped>

</style>