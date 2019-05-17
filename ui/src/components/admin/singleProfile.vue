<template>
    <div>
        <b-row>
            <b-col cols="2">
                <b-navbar toggleable="lg">
                    <b-collapse id="nav-collapse-admin" is-nav>
                        <b-nav vertical class="singleProfileNav">
                            <b-nav-item @click="goBack">Go Back</b-nav-item>
                            <b-navbar-brand @click="currentDisplay = 0">
                                <b-img v-bind="profileImage" blank-color="#777" rounded="circle" alt="Circle image">
                                </b-img>
                                {{editProfile.firstName}}
                            </b-navbar-brand>
                            <b-nav-item @click="currentDisplay = 1">Edit Profile</b-nav-item>
                            <b-nav-item @click="currentDisplay = 2">View Trips</b-nav-item>
                            <b-nav-item @click="currentDisplay = 3">Add Trips</b-nav-item>
                        </b-nav>
                    </b-collapse>
                    <b-navbar-toggle target="nav-collapse-admin"></b-navbar-toggle>
                </b-navbar>

            </b-col>

            <b-col cols="9" class="singleProfile">
                <view-profile
                        v-if="currentDisplay === 0"
                        :trips="trips"
                        :profile="editProfile"
                        :destinations="destinations"
                        :nationalityOptions="nationalityOptions"
                        :travTypeOptions="travTypeOptions"
                        :showSaved="showSaved">
                </view-profile>
                <edit-profile
                        v-if="currentDisplay === 1"
                        @profile-saved="redirectToViewProfile"
                        :profile="editProfile"
                        :nationalityOptions="nationalityOptions"
                        :travTypeOptions="travTypeOptions"
                        :adminView="adminView">
                </edit-profile>
                <your-trips
                        v-if="currentDisplay === 2"
                        :destinations="destinations"
                        :profile="editProfile"
                        :userProfile="editProfile"
                        :admin-view="adminView">
                </your-trips>
                <plan-a-trip
                        v-if="currentDisplay === 3"
                        :heading="'Plan a Trip'"
                        :subHeading="'Book your next trip!'"
                        :destinations="destinations"
                        :profile="editProfile"
                        :adminView="adminView">
                </plan-a-trip>
            </b-col>
        </b-row>
    </div>
</template>

<script>
    import NavBarMain from '../helperComponents/navbarMain.vue'
    import ViewProfile from "./../dash/viewProfile.vue"
    import PlanATrip from './../trips/planATrip.vue'
    import YourTrips from './../trips/yourTrips.vue'
    import EditProfile from "./../dash/editProfile.vue"

    export default {
        name: "singleProfile",
        props: {
            adminView: Boolean,
            profile: Object,
            editProfile: Object,
            nationalityOptions: Array,
            travTypeOptions: Array,
            destinations: Array,

        },
        data() {
            return {
                profileImage: {blank: true, width: 75, height: 75, class: 'm1'},
                currentDisplay: 0,
                showSaved: false,
            }
        },
        methods: {
            /**
             * Emits an event to the admin panel page, this will redirect the admin back to the admin dashboard when the
             * go back button is clicked.
             */
            goBack() {
                this.$emit('go-back', null);
            },

            /**
             * If the profile is successfully saved, then redirect to the view profile page.
             */
            redirectToViewProfile(editProfile) {
                this.editProfile = editProfile;
                this.currentDisplay = 0;
                this.showSaved = true;
            }
        },
        components: {
            ViewProfile,
            PlanATrip,
            YourTrips,
            EditProfile,
            NavBarMain,
        }
    }
</script>

<style scoped>
    .singleProfile {
        margin: 10px;
        background: white;
        border-radius: 5px;
    }

    .singleProfileNav {
        background: white;
        padding: 15px;
    }

    a {
        color: grey;
    }
</style>