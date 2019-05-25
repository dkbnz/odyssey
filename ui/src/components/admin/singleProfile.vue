<template>
    <div>
        <b-row>
            <b-col>
                <b-navbar toggleable="lg" class="stickyMinorNav">
                    <b-collapse id="nav-collapse-admin" is-nav>
                        <b-nav class="singleProfileNav" vertical>
                            <b-nav-item @click="goBack">Go Back</b-nav-item>
                            <b-navbar-brand @click="currentDisplay = 0" class="nav-bar-brand">
                                <b-img alt="Circle image" blank-color="#777" rounded="circle" v-bind="profileImage">
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

            <b-col cols="10">
                <view-profile
                        :adminView="adminView"
                        :destinations="destinations"
                        :nationalityOptions="nationalityOptions"
                        :profile="editProfile"
                        :showSaved="showSaved"
                        :travTypeOptions="travTypeOptions"
                        :trips="trips"
                        v-if="currentDisplay === 0">
                </view-profile>
                <edit-profile
                        :containerClass="'adminContainer'"
                        :adminView="adminView"
                        :nationalityOptions="nationalityOptions"
                        :profile="editProfile"
                        :travTypeOptions="travTypeOptions"
                        @profile-saved="redirectToViewProfile"
                        v-if="currentDisplay === 1">
                </edit-profile>
                <your-trips
                        :containerClass="'adminContainer'"
                        :admin-view="adminView"
                        :destinations="destinations"
                        :profile="editProfile"
                        :userProfile="editProfile"
                        v-if="currentDisplay === 2">
                </your-trips>
                <plan-a-trip
                        :containerClass="'adminContainer'"
                        :adminView="adminView"
                        :destinations="destinations"
                        :heading="'Plan a Trip'"
                        :profile="editProfile"
                        :subHeading="'Book your next trip!'"
                        v-if="currentDisplay === 3">
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