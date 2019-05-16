<template>
    <div>
        <nav-bar-main v-bind:profile="profile"></nav-bar-main>
        <div class="adminPanel">
            <h1 class="page_title">Welcome to the Admin Panel</h1>
            <p class="page_title">
                <i>Because you are an admin, you can achieve all functionality in the application!</i>
            </p>
            <b-row>
                <b-col cols="8">
                    <b-card
                            header="Search for Profiles"
                            header-tag="header">
                        <!-- Display the search profiles component -->
                        <profiles-page :key="refreshProfiles" @admin-edit="navigateToSingleProfile" :perPage=5
                                       :adminView="true" :profile="profile" :nationalityOptions="nationalityOptions"
                                       :travTypeOptions="travTypeOptions">
                        </profiles-page>

                    </b-card>
                </b-col>
                <b-col>
                    <b-card header="Create a Profile">
                        <b-button v-b-toggle.signUpPage block variant="success">Create a New Profile</b-button>
                        <!-- The collapsible that uses the sign up page to create a new profile -->
                        <b-collapse id="signUpPage" class="mt-2">
                            <sign-up :nationalityOptions="nationalityOptions" :travTypeOptions="travTypeOptions"
                                     :createdByAdmin="true" @profile-created="showOptions">
                            </sign-up>
                        </b-collapse>
                    </b-card>
                    <b-card
                    header="Manage Destinations"
                    style="margin-top: 10px">
                    Some content
                    </b-card>
                </b-col>
            </b-row>
        </div>
        <div>
            <!-- The modal to show after a profile has been successfully created -->
            <b-modal
                    id="optionModal"
                    ref="optionModal"
                    hide-footer>
                <template slot="modal-title"><h2>Profile Successfully Created</h2></template>
                Would you like to view this profile?
                <b-row align-h="center" style="margin-top: 10px">
                    <b-col>
                        <b-button block variant="primary" @click="hideOptionModal">No</b-button>
                    </b-col>
                    <b-col>
                        <b-button block variant="primary" @click="navigateToSingleProfile">Yes</b-button>
                    </b-col>
                </b-row>
            </b-modal>
            <single-profile v-if="showSingleProfile"></single-profile>
        </div>

    </div>
</template>

<script>
    import NavBarMain from '../helperComponents/navbarMain.vue'
    import ProfilesPage from '../profiles/profilesPage.vue'
    import SignUp from '../index/signup.vue'
    import SingleProfile from './singleProfile.vue'
    export default {
        name: "adminPanel",
        props: ['profile', 'nationalityOptions', 'travTypeOptions'],
        data() {
            return {
                refreshProfiles: 0,
                showSingleProfile: false
            }
        },
        methods: {
            /**
             * If a profile is successfully created, then re-render the profiles component (to ensure the new profile
             * is shown). Will then hide the sign up modal and show the what to do next modal.
             */
            showOptions() {
                this.refreshProfiles += 1;
                this.$refs['optionModal'].show();
            },

            /**
             * Hides the option modal.
             */
            hideOptionModal() {
                this.$refs['optionModal'].hide();
            },

            /**
             * Navigates to the single profile page where an admin can achieve all functionality of the selected
             * user.
             */
            navigateToSingleProfile(profile) {
                console.log(profile);
                this.showSingleProfile = true;
            }
        },
        components: {
            NavBarMain,
            ProfilesPage,
            SignUp,
            SingleProfile
        }
    }
</script>

<style scoped>

    .adminPanel {
        background: white;
        width: 90vw;
        margin: 2vh 5vw 5vw 5vw;
        border-radius: 5px;
        padding: 2vw;
    }
</style>