<template>
    <div>
        <b-row>
            <b-col>
                <!-- Nav bar for admin to display the selected page they wish to work on. -->
                <b-navbar toggleable="lg" class="stickyMinorNav">
                    <b-collapse id="nav-collapse-admin" is-nav>
                        <b-nav class="singleProfileNav" vertical>
                            <b-nav-item @click="goBack">Go Back</b-nav-item>
                            <b-navbar-brand @click="currentDisplay = 0" class="nav-bar-brand">
                                <b-img :src="getProfilePictureThumbnail()"
                                       onerror="this.src = '../../../static/default_profile_picture.png'"
                                       fluid rounded="circle" width="50%">
                                </b-img>
                                {{editProfile.firstName}}
                            </b-navbar-brand>
                            <b-nav-item @click="currentDisplay = 1">Edit Profile</b-nav-item>
                            <b-nav-item @click="currentDisplay = 2">View Trips</b-nav-item>
                            <b-nav-item @click="currentDisplay = 3">Add Trips</b-nav-item>
                            <b-nav-item @click="currentDisplay = 4">Destinations</b-nav-item>
                            <b-nav-item @click="currentDisplay = 5">Treasure Hunts</b-nav-item>
                        </b-nav>
                    </b-collapse>
                    <b-navbar-toggle target="nav-collapse-admin"></b-navbar-toggle>
                </b-navbar>

            </b-col>

            <b-col cols="10">
                <view-profile
                        :containerClass="'adminContainer'"
                        :adminView="adminView"
                        :destinations="destinations"
                        :nationalityOptions="nationalityOptions"
                        :profile="editProfile"
                        :showSaved="showSaved"
                        :travTypeOptions="travTypeOptions"
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
                <destinations-page
                        :containerClass="'noBordersContainer'"
                        :destinationTypes="destinationTypes"
                        :adminView="adminView"
                        :destinations="destinations"
                        :travTypeOptions="travTypeOptions"
                        :profile="editProfile"
                        v-if="currentDisplay === 4">
                </destinations-page>
                <treasure-hunt-page
                        :containerClass="'noBordersContainer'"
                        :adminView="adminView"
                        :profile="editProfile"
                        v-if="currentDisplay === 5">
                </treasure-hunt-page>
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
    import DestinationsPage from "./../destinations/destinationsPage.vue"
    import TreasureHuntPage from "../treasureHunt/treasureHuntPage";

    export default {
        name: "singleProfile",

        props: {
            adminView: Boolean,
            profile: Object,
            editProfile: Object,
            nationalityOptions: Array,
            travTypeOptions: Array,
            destinations: Array,
            destinationTypes: Array

        },

        data() {
            return {
                profileImage: {blank: true, width: 75, height: 75, class: 'm1'},
                currentDisplay: 0,
                showSaved: false,
                refreshDestinations: 0
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
            },


            /**
             * Retrieves the user's primary photo thumbnail.
             */
            getProfilePictureThumbnail() {
                if (this.editProfile.profilePicture !== null) {
                    return `/v1/photos/thumb/` + this.editProfile.profilePicture.id;
                }
                return "../../../static/default_profile_picture.png";
            }
        },

        components: {
            TreasureHuntPage,
            ViewProfile,
            PlanATrip,
            YourTrips,
            EditProfile,
            NavBarMain,
            DestinationsPage
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