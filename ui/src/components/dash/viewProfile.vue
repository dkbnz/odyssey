<template>
    <div class="container">
        <h1>{{ profile_name }}</h1>
        <h2>Personal Details</h2>
        <p> Date of Creation: {{ dateOfCreation }}</p>
        <p> Date of Birth: {{ dateOfBirth }}</p>
        <p> Email: {{ email }}</p>
        <p> Gender: {{ gender }}</p>

        <h2> Nationalities </h2>
        <ul>
            <li v-for="nationality in nationalities">{{ nationality }}</li>
        </ul>

        <h2> Passports </h2>
        <ul>
            <li v-for="passport in passports">{{ passport }}</li>
        </ul>

        <h2> Traveller Types </h2>
        <ul>
            <li v-for="travType in travellerTypes">{{ travType }}</li>
        </ul>

        <your-trips></your-trips>
    </div>
</template>

<script>
    import YourTrips from "../trips/yourTrips.vue"
    export default {
        name: "viewProfile",
        data () {
            return {
                profile_name: "Isaac Taylor Worsley",
                dateOfCreation: "17/04/2019",
                dateOfBirth: "25/09/1998",
                email: "itw21@uclive.ac.nz",
                gender: "male",
                nationalities: ['New Zealand', 'England'],
                passports: ['New Zealand', 'English'],
                travellerTypes: ['Gap Year']
            }
        },
        components: {
            YourTrips
        }
    }
</script>

<style scoped>

</style>