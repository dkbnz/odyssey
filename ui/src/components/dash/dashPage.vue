<template>
    <div v-if="profile.length !== 0">

        <!--Navigation Bar-->
        <nav-bar-main v-bind:profile="profile"></nav-bar-main>
        <b-navbar variant="light">
            <b-navbar-nav>
                <b-nav-item @click="togglePage(viewProfile)">Profile</b-nav-item>
                <b-nav-item @click="togglePage(editProfile)">Edit Profile</b-nav-item>
            </b-navbar-nav>
        </b-navbar>

        <!--Tab Elements-->
        <view-profile v-if="viewProfile"
                      :trips="trips"
                      v-bind:profile="profile"
                      v-bind:nationalityOptions="nationalityOptions"
                      v-bind:travTypeOptions="travTypeOptions"></view-profile>
        <edit-profile v-if="editProfile"
                      v-bind:profile="profile"
                      v-bind:nationalityOptions="nationalityOptions"
                      v-bind:travTypeOptions="travTypeOptions"
                      v-bind:admin-view="adminView"></edit-profile>

        <footer-main></footer-main>
    </div>
    <div v-else>
        <unauthorised-prompt></unauthorised-prompt>
    </div>
</template>

<script>

    import ViewProfile from "./viewProfile.vue"
    import EditProfile from "./editProfile.vue"
    import NavBarMain from '../helperComponents/navbarMain.vue'
    import FooterMain from '../helperComponents/footerMain.vue'
    import UnauthorisedPrompt from '../helperComponents/unauthorisedPromptPage'

    export default {
        name: "dashPage",
        props: ['profile', 'nationalityOptions', 'travTypeOptions', 'trips', 'adminView'],
        data: function() {
            return {
                viewProfile: true,
                editProfile: false,
            }
        },
        methods: {
            /**
             * Switches the currently displayed tab on the page
             * @param viewPage the page to be displayed
             */
            togglePage: function (viewPage) {
                if (!viewPage) {
                    this.viewProfile = !this.viewProfile;
                    this.editProfile = !this.editProfile;
                }
            },
        },
        components: {
            ViewProfile,
            EditProfile,
            NavBarMain,
            FooterMain,
            UnauthorisedPrompt
        }
    }
</script>