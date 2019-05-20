<template>
    <div>
        <div class="containerMain">
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
                        <profiles-page :adminView="true"
                                       :containerClass="'adminContainer'"
                                       :destinations="destinations"
                                       :key="refreshProfiles"
                                       :nationalityOptions="nationalityOptions"
                                       :perPage=5
                                       :profile="profile"
                                       :travTypeOptions="travTypeOptions"
                                       @admin-edit="getSingleProfile">
                        </profiles-page>

                    </b-card>
                </b-col>
                <b-col>
                    <b-card header="Create a Profile">
                        <b-button block v-b-toggle.signUpPage variant="success">Create a New Profile</b-button>
                        <!-- The collapsible that uses the sign up page to create a new profile -->
                        <b-collapse class="mt-2" id="signUpPage">
                            <sign-up :createdByAdmin="true"
                                     :nationalityOptions="nationalityOptions"
                                     :travTypeOptions="travTypeOptions"
                                     @profile-created="showOptions">
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
                    hide-footer
                    id="optionModal"
                    ref="optionModal">
                <template slot="modal-title"><h2>Profile Successfully Created</h2></template>
                Would you like to view this profile?
                <b-row align-h="center" style="margin-top: 10px">
                    <b-col>
                        <b-button @click="hideOptionModal" block variant="primary">No</b-button>
                    </b-col>
                    <b-col>
                        <b-button @click="navigateToSingleProfile" block variant="primary">Yes</b-button>
                    </b-col>
                </b-row>
            </b-modal>

        </div>
    </div>
</template>

<script>
    import ProfilesPage from '../profiles/profilesPage.vue'
    import SignUp from '../index/signup.vue'

    export default {
        name: "adminActions",
        props: ['profile', 'nationalityOptions', 'travTypeOptions', 'destinations'],
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
            getSingleProfile(editProfile) {
                this.$emit('admin-edit', editProfile);
            }
        },
        components: {
            ProfilesPage,
            SignUp
        }
    }
</script>