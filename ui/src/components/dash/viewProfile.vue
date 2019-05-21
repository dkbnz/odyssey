<template>
    <div>
        <b-row>
            <b-col>
                <div :class="containerClass">
                    <b-alert dismissible v-model="showSaved" variant="success">Profile Successfully Saved</b-alert>
                    <!-- Uses the received profile to display the profile's data on a page -->
                    <h1>{{profile.firstName}} {{profile.middleName}} {{profile.lastName}}</h1>
                    <p v-if="profile.isAdmin"><i>Administrator</i></p>
                    <p v-else><i>Regular User</i></p>
                    <h2>Personal Details</h2>
                    <p> Username: {{ profile.username }}</p>
                    <p> Date of Creation: {{ new Date(profile.dateOfCreation).toUTCString()}}</p>
                    <p> Date of Birth: {{new Date(profile.dateOfBirth).toLocaleDateString()}}</p>
                    <p> Gender: {{ profile.gender }}</p>

                    <h2> Nationalities </h2>
                    <ul>
                        <li v-for="nationality in profile.nationalities">{{ nationality.nationality }}</li>
                    </ul>

                    <h2> Passports </h2>
                    <ul>
                        <li v-for="passport in profile.passports">{{ passport.country }}</li>
                    </ul>

                    <h2> Traveller Types </h2>
                    <ul>
                        <li v-for="travType in profile.travellerTypes">{{ travType.travellerType }}</li>
                    </ul>


                </div>
            </b-col>
            <b-col cols="11" class="profileContent">
                <photo-gallery :profile="profile"></photo-gallery>
                <!-- Displays a profile's trips -->
                <your-trips :adminView="adminView"
                            :destinations="destinations"
                            :profile="profile"
                            :userProfile="userProfile">
                </your-trips>
                <your-trips :adminView="adminView"
                            :destinations="destinations"
                            :profile="profile"
                            :userProfile="userProfile">
                </your-trips>
                <your-trips :adminView="adminView"
                            :destinations="destinations"
                            :profile="profile"
                            :userProfile="userProfile">
                </your-trips>

            </b-col>

        </b-row>

    </div>

</template>

<script>
    import YourTrips from "../trips/yourTrips.vue"
    import PhotoGallery from "../photos/photoGallery";

    export default {
        name: "viewProfile",
        props: {
            profile: Object,
            nationalityOptions: Array,
            travTypeOptions: Array,
            trips: Array,
            userProfile: Object,
            adminView: Boolean,
            destinations: Array,
            showSaved: {
                default: function () {
                    return false;
                }
            },
            containerClass: {
                default: function() {
                    return 'sideNavFixed'
                }
            }
        },
        data() {
            return {}
        },
        components: {
            YourTrips,
            PhotoGallery
        }
    }
</script>
<style>
    .sideNavFixed {
        background-color: white;
        border-radius: 5px;
        margin: 8% 0 0 1%;
        padding: 10px;
        position: fixed;
    }
    .sideNavRelative {
        background-color: white;
        border-radius: 5px;
        padding: 10px;
        position: absolute;
    }

    .profileContent {
        margin: 1% 0 0 15%;
    }
</style>