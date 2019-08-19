<template>
    <div v-if="profile.length !== 0">

        <!--Navigation Bar-->
        <nav-bar-main :profile="profile"></nav-bar-main>
        <b-navbar variant="light" class="d-none d-lg-block">
            <b-navbar-nav>
                <b-nav-item @click="togglePage(viewProfile, 'view')">Profile</b-nav-item>
                <b-nav-item @click="togglePage(editProfile, 'edit')">Edit Profile</b-nav-item>
            </b-navbar-nav>
        </b-navbar>

        <!--Tab Elements-->
        <view-profile
                :destinations="destinations"
                :nationalityOptions="nationalityOptions"
                :profile="profile"
                :travTypeOptions="travTypeOptions"
                :trips="trips"
                v-if="viewProfile">
        </view-profile>
        <edit-profile
                :admin-view="adminView"
                :nationalityOptions="nationalityOptions"
                :profile="profile"
                :showSaved="showSaved"
                :travTypeOptions="travTypeOptions"
                @profile-saved="showSavedProfile"
                v-if="editProfile">
        </edit-profile>
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

        props: ['profile', 'nationalityOptions', 'travTypeOptions', 'trips', 'adminView', 'destinations'],

        data: function () {
            return {
                viewProfile: true,
                editProfile: false,
                photoGallery: false,
                showSaved: false
            }
        },

        methods: {
            /**
             * Switches between tabs.
             *
             * @param viewPage page to be displayed.
             */
            togglePage: function (viewPage) {
                if (!viewPage) {
                    this.viewProfile = !this.viewProfile;
                    this.editProfile = !this.editProfile;
                }
            },


            /**
             * Shows the profile has been successfully saved alert.
             */
            showSavedProfile() {
                this.showSaved = true;
                this.togglePage(this.viewProfile);
            }

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