<template>
    <div class="scroll">
        <div class="wrapper cf">
            <div :class="containerClass">
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
            <div :class="containerClassContent">
                <photo-gallery :profile="profile" :userProfile="userProfile" :adminView="adminView"></photo-gallery>
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
            </div>
        </div>
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
                    return 'sidebar'
                }
            },
            containerClassContent: {
                default: function() {
                    return 'content'
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