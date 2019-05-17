<template>
    <div class="container">
        <b-alert v-model="showSaved" variant="success" dismissible>Profile Successfully Saved</b-alert>
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

        <!-- Displays a profile's trips -->
        <your-trips :userProfile="userProfile"
                    :profile="profile"
                    :adminView="adminView"
                    :destinations="destinations">
        </your-trips>
    </div>
</template>

<script>
    import YourTrips from "../trips/yourTrips.vue"

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
                default: function() {
                    return false;
                }
            }
        },
        data () {
            return {
            }
        },
        components: {
            YourTrips
        }
    }
</script>