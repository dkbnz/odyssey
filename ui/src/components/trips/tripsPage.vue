<template>
    <div v-if="profile.length !== 0">
        <nav-bar-main v-bind:profile="profile"></nav-bar-main>
        <b-navbar variant="light">
            <b-navbar-nav>
                <b-nav-item @click="togglePage(planATrip)">Plan a Trip</b-nav-item>
                <b-nav-item @click="togglePage(yourTrips)">Your Trips</b-nav-item>
            </b-navbar-nav>
        </b-navbar>
        <plan-a-trip v-if="planATrip" v-bind:destinations="destinations"></plan-a-trip>
        <your-trips  v-if="yourTrips" :profile="profile"></your-trips>
        <footer-main></footer-main>
    </div>
    <div v-else>
        <unauthorised-prompt></unauthorised-prompt>
    </div>
</template>

<script>
    import PlanATrip from './planATrip.vue'
    import YourTrips from './yourTrips.vue'
    import NavBarMain from '../helperComponents/navbarMain.vue'
    import FooterMain from '../helperComponents/footerMain.vue'
    import UnauthorisedPrompt from '../helperComponents/unauthorisedPromptPage'

    export default {
        name: "Trips",
        props: ['profile', 'destinations'],
        created() {
            document.title = "TravelEA - Trips";
        },
        data: function() {
            return {
                planATrip: true,
                yourTrips: false,
            }
        },
        mounted() {
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
            YourTrips,
            NavBarMain,
            FooterMain,
            UnauthorisedPrompt
        }
    }
</script>

<style scoped>

</style>