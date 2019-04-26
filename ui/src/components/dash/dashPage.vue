<template>
    <div>
        <b-navbar variant="light">
            <b-navbar-nav>
                <b-nav-item @click="togglePage(viewProfile)">Profile</b-nav-item>
                <b-nav-item @click="togglePage(editProfile)">Edit Profile</b-nav-item>
            </b-navbar-nav>
        </b-navbar>
        <view-profile v-if="viewProfile"></view-profile>
        <edit-profile v-if="editProfile"></edit-profile>

    </div>

</template>

<script>
    import ViewProfile from "./viewProfile.vue"
    import EditProfile from "./editProfile.vue"
    export default {
        name: "dashPage",
        created() {
            document.title = "TravelEA - Dashboard";
        },
        data: function() {
            return {
                viewProfile: true,
                editProfile: false
            }
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