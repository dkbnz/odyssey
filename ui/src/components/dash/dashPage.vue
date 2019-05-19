<template>
    <div v-if="profile.length !== 0">

        <!--Navigation Bar-->
        <nav-bar-main v-bind:profile="profile"></nav-bar-main>
        <b-navbar variant="light">
            <b-navbar-nav>
                <b-nav-item @click="togglePage(viewProfile, 'view')">Profile</b-nav-item>
                <b-nav-item @click="togglePage(editProfile, 'edit')">Edit Profile</b-nav-item>
                <b-nav-item @click="togglePage(photoGallery, 'photos')">Photo Gallery</b-nav-item>
            </b-navbar-nav>
        </b-navbar>

        <!--Tab Elements-->
        <view-profile
                v-if="viewProfile"
                :trips="trips"
                :profile="profile"
                :nationalityOptions="nationalityOptions"
                :travTypeOptions="travTypeOptions"
                :destinations="destinations">
        </view-profile>
        <edit-profile
                v-if="editProfile"
                :showSaved="showSaved"
                @profile-saved="showSavedProfile"
                :profile="profile"
                :nationalityOptions="nationalityOptions"
                :travTypeOptions="travTypeOptions"
                :admin-view="adminView">
        </edit-profile>
        <photo-gallery v-if="photoGallery" v-bind:profile="profile"></photo-gallery>
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
    import PhotoGallery from "../photos/photoGallery";
    import UnauthorisedPrompt from '../helperComponents/unauthorisedPromptPage'

    export default {
        name: "dashPage",
        props: ['profile', 'nationalityOptions', 'travTypeOptions', 'trips', 'adminView', 'destinations'],
        data: function() {
            return {
                viewProfile: true,
                editProfile: false,
                photoGallery: false,
                editProfile: false,
                showSaved: false
            }
        },
        methods: {
            /**
             * Switches the currently displayed tab on the page
             * @param viewPage the page to be displayed
             */
            togglePage: function(pageState, pageName) {
                if (pageName === 'view') {
                    this.viewProfile = true;
                    this.editProfile = false;
                    this.photoGallery = false;
                } else if (pageName === 'edit') {
                    this.viewProfile = false;
                    this.editProfile = true;
                    this.photoGallery = false;
                } else if (pageName === 'photos') {
                    this.viewProfile = false;
                    this.editProfile = false;
                    this.photoGallery = true;
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
            PhotoGallery,
            ViewProfile,
            EditProfile,
            NavBarMain,
            FooterMain,
            UnauthorisedPrompt
        }
    }
</script>