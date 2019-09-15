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
        <div class="loader" v-if="loadingResults">
            <div class="loader-content">
                <b-img alt="Loading" class="loading" :src="assets['loadingLogoBig']"></b-img>
                <h1>Loading Profile Data</h1>
            </div>
        </div>
        <div :class='{opacity: loadingResults}'>
            <!--Tab Elements-->
            <view-profile
                    :destinations="destinations"
                    :nationalityOptions="nationalityOptions"
                    :profile="profile"
                    :userProfile="profile"
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
        </div>

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

        props: ['nationalityOptions', 'travTypeOptions', 'trips', 'adminView', 'destinations'],

        data: function () {
            return {
                viewProfile: true,
                editProfile: false,
                showSaved: false,
                profile: {},
                loadingResults: false
            }
        },

        mounted() {
            this.getProfile();
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
            },


            /**
             * Retrieves the current profile in case any changes have been made.
             */
            getProfile() {
                let self = this;
                this.loadingResults = true;
                return fetch(`/v1/profile`, {
                    accept: "application/json"
                }).then(function (response) {
                    if (!response.ok) {
                        throw response;
                    } else {
                        return response.json();
                    }
                }).then(function (responseBody) {
                    self.loadingResults = false;
                    self.profile = responseBody;
                }).catch(function (response) {
                    if (response.status > 404) {
                        self.showErrorToast(JSON.parse(JSON.stringify([{message: "An unexpected error occurred"}])));
                    } else {
                        self.loadingResults = false;
                        response.json().then(function(responseBody) {
                            self.showErrorToast(responseBody);
                        });
                    }
                });
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