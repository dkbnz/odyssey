<template>
    <div v-if="profile.length !== 0">
        <nav-bar-main v-bind:profile="profile"></nav-bar-main>
        <b-navbar variant="light">
            <b-navbar-nav>
                <b-nav-item @click="togglePage(planATrip)">Plan a Trip</b-nav-item>
                <b-nav-item @click="togglePage(yourTrips)">Your Trips</b-nav-item>
            </b-navbar-nav>
        </b-navbar>

        <!-- Displays the plan a trip or your trips page depending on buttons selected by the user -->
        <plan-a-trip :adminView="adminView"
                     :destinations="destinations"
                     :heading="'Plan a Trip'"
                     :profile="profile"
                     :subHeading="'Book your next trip!'"
                     v-if="planATrip">
        </plan-a-trip>
        <your-trips :adminView="adminView"
                    :destinations="destinations"
                    :profile="profile"
                    v-if="yourTrips">
        </your-trips>
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

        props: {
            profile: Object,
            destinations: Array,
            adminView: Boolean,
        },

        data: function () {
            return {
                planATrip: true,
                yourTrips: false
            }
        },

        methods: {
            /**
             * Used to toggle what page is currently being shown.
             *
             * @param viewPage      boolean variable to initiate a component toggle.
             */
            togglePage: function (viewPage) {
                if (!viewPage) {
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