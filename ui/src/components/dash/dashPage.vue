<template>
    <div>
        <b-navbar variant="light">
            <b-navbar-nav>
                <b-nav-item @click="togglePage(viewProfile)">Profile</b-nav-item>
                <b-nav-item @click="togglePage(editProfile)">Edit Profile</b-nav-item>
            </b-navbar-nav>
        </b-navbar>
        <view-profile v-if="viewProfile" v-bind:profile="profile" v-bind:nationalityOptions="nationalityOptions" v-bind:travTypeOptions="travTypeOptions"></view-profile>
        <edit-profile v-if="editProfile" v-bind:profile="profile" v-bind:nationalityOptions="nationalityOptions" v-bind:travTypeOptions="travTypeOptions"></edit-profile>

    </div>

</template>

<script>
    import ViewProfile from "./viewProfile.vue"
    import EditProfile from "./editProfile.vue"
    export default {
        name: "dashPage",
        props: ['profile', 'nationalityOptions', 'travTypeOptions'],
        created() {
            document.title = "TravelEA - Dashboard";
        },
        data: function() {
            return {
                viewProfile: true,
                editProfile: false
            }
        },
        mounted () {

        },
        methods: {
            togglePage: function(viewPage) {
                if(!viewPage) {
                    this.viewProfile = !this.viewProfile;
                    this.editProfile = !this.editProfile;
                }
            },
        },
        components: {
            ViewProfile,
            EditProfile
        }
    }
</script>

<style scoped>

</style>