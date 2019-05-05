<template>
    <div class="container">
        <h1>{{ profile.firstName }} {{profile.middleName}} {{profile.lastName}}</h1>
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

        <your-trips :userProfile="userProfile" :profile="profile"></your-trips>
    </div>
</template>

<script>
    import YourTrips from "../trips/yourTrips.vue"

    export default {
        name: "viewProfile",
        props: ['profile', 'nationalityOptions', 'travTypeOptions', 'trips', 'userProfile'],
        data () {
            return {
            }
        },
        components: {
            YourTrips
        },
        mounted() {
        },
        methods: {}
    }
</script>