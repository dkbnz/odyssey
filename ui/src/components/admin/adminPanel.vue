<template>
    <div>
        <nav-bar-main :profile="profile"></nav-bar-main>
        <div class="loader" v-if="loadingResults">
            <div class="loader-content">
                <b-img alt="Loading" class="loading" :src="assets['loadingLogoBig']"></b-img>
                <h1>Checking Authentication</h1>
            </div>
        </div>
        <!-- Can only be seen if logged in user is an admin -->
        <div v-if="profile.admin">
            <!-- The admin actions panel, which acts as the Admin Dashboard -->
            <admin-actions
                    v-if="editProfile === null"
                    :profile="profile"
                    :nationalityOptions="nationalityOptions"
                    :travTypeOptions="travTypeOptions"
                    :destinations="destinations"
                    @admin-edit="setProfileToEdit">
            </admin-actions>

            <!-- Once the admin has selected a profile to work on, this page becomes visible -->
            <single-profile
                    v-else
                    :key="refreshSingleProfile"
                    :adminView="adminView"
                    :editProfile="editProfile"
                    :destinationTypes="destinationTypes"
                    :profile="profile"
                    :nationalityOptions="nationalityOptions"
                    :travTypeOptions="travTypeOptions"
                    :destinations="destinations"
                    @go-back="setProfileToEdit">
            </single-profile>
        </div>

        <!-- If logged in user is not an admin, then display an error -->
        <div v-else-if="!profile.admin && !loadingResults" class="d-flex justify-content-center">
            You do not have permission to access this page!
        </div>

    </div>
</template>

<script>
    import NavBarMain from '../helperComponents/navbarMain.vue'
    import SingleProfile from './singleProfile.vue'
    import AdminActions from './adminActions.vue'

    export default {
        name: "adminPanel",

        props: ['nationalityOptions', 'travTypeOptions', 'destinations', 'destinationTypes'],

        data() {
            return {
                showSingleProfile: false,
                editProfile: null,
                viewSingleProfile: false,
                adminView: true,
                refreshSingleProfile: 0,
                loadingResults: false,
                profile: {}
            }
        },

        mounted() {

                this.loadingResults = false;            this.getProfile();
        },

        methods: {
            /**
             * Navigates to the single profile page where an admin can achieve all functionality of the selected
             * user.
             */
            setProfileToEdit(editProfile) {
                this.editProfile = editProfile;
                this.viewSingleProfile = true;
                window.scrollTo(0, 0);
            },


            /**
             * Retrieves the current profile as needs to check if admin or not.
             */
            getProfile() {
                let self = this;
                this.loadingResults = true;
                fetch(`/v1/profile`, {
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
                    self.loadingResults = false;
                    self.handleErrorResponse(response);
                });
            }
        },

        components: {
            NavBarMain,
            SingleProfile,
            AdminActions
        }
    }
</script>