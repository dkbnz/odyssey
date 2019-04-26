<template>
    <div>
        <b-navbar variant="light">
            <b-navbar-nav>
                <b-nav-item @click="togglePage">Plan a Trip</b-nav-item>
                <b-nav-item @click="togglePage">Your Trips</b-nav-item>
            </b-navbar-nav>
        </b-navbar>
        <plan-a-trip v-if="planATrip"></plan-a-trip>
        <your-trips v-if="yourTrips"></your-trips>

    </div>

</template>

<script>
    import PlanATrip from './PlanATrip.vue'
    import YourTrips from './YourTrips.vue'
    export default {
        name: "Trips",
        data: function() {
            return {
                planATrip: true,
                yourTrips: false
            }
        },
        methods: {
            togglePage: function() {
                this.planATrip = !this.planATrip;
                this.yourTrips = !this.yourTrips;
            }
        },
        components: {
            PlanATrip,
            YourTrips
        }
    }
</script>

<style scoped>

</style>