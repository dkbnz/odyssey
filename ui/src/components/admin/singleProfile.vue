<template>
    <div>
        <b-row>
            <b-col cols="2">
                <b-navbar toggleable="lg">
                    <b-collapse id="nav-collapse-admin" is-nav>
                        <b-nav vertical class="singleProfileNav">
                            <b-nav-item @click="goBack">Go Back</b-nav-item>
                            <b-navbar-brand>
                                <b-img v-bind="profileImage" blank-color="#777" rounded="circle" alt="Circle image">
                                </b-img>
                                {{editProfile.firstName}}
                            </b-navbar-brand>
                            <b-nav-item>Add Trips</b-nav-item>
                            <b-nav-item>View Trips</b-nav-item>
                            <b-nav-item>Edit Profile</b-nav-item>
                        </b-nav>
                    </b-collapse>
                    <b-navbar-toggle target="nav-collapse-admin"></b-navbar-toggle>
                </b-navbar>

            </b-col>

            <b-col cols="9" class="singleProfile">
                <view-profile v-if="currentDisplay === 0"
                              :trips="trips"
                              v-bind:profile="profile"
                              v-bind:nationalityOptions="nationalityOptions"
                v-bind:travTypeOptions="travTypeOptions"></view-profile>
                <plan-a-trip v-if="currentDisplay === 1"
                             :heading="'Plan a Trip'"
                             :subHeading="'Book your next trip!'"
                             :destinations="destinations"
                             :profile="profile"
                :adminView="adminView"></plan-a-trip>
                <your-trips v-if="currentDisplay === 2"
                            :destinations="destinations"
                            :profile="profile"
                            :userProfile="profile"
                :admin-view="adminView"></your-trips>
                <edit-profile v-if="editProfile"
                              v-bind:profile="profile"
                              v-bind:nationalityOptions="nationalityOptions"
                              v-bind:travTypeOptions="travTypeOptions"></edit-profile>
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
            editProfile: Object
        },
        components: {
            ViewProfile,
            PlanATrip,
            YourTrips,
            EditProfile,
            NavBarMain,
        },
        data() {
            return {
                profileImage: {blank: true, width: 75, height: 75, class: 'm1'},
                currentDisplay: 0,
            }
        },
        methods: {
            goBack() {
                this.$emit('go-back', null);
            }
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