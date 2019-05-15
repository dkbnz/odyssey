<template>
    <div>
        <nav-bar-main v-bind:profile="profile"></nav-bar-main>
        <div class="container">
            <h1 class="page_title">Welcome to the Admin Panel</h1>
            <p class="page_title"><i>Because you are an admin, you can achieve all functionality in the application!
                <br/>Start by finding a profile using the form below or make one by clicking the button.</i></p>
            <b-button @click="displaySignupPage" block variant="success">Create a New Profile</b-button>
            <profiles-page :adminView="true" :profile="profile" nationalityOptions="nationalityOptions" travTypeOptions="travTypeOptions"></profiles-page>

        </div>
        <div>
            <b-modal
            id="signupModal"
            ref="signupModal">
                <signup></signup>
            </b-modal>
        </div>

    </div>
</template>

<script>
    import NavBarMain from '../helperComponents/navbarMain.vue'
    import ProfilesPage from '../profiles/profilesPage.vue'
    import Signup from '../index/signup,vue'
    export default {
        name: "adminPanel",
        props: ['profile', 'nationalityOptions', 'travTypeOptions'],
        components: {
            NavBarMain,
            ProfilesPage,
            Signup
        },
        methods: {
            displaySignupPage() {
                this.$refs['signupModal'].show();
            }
        }
    }
</script>

<style scoped>

</style>