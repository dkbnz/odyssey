<template>
    <div>
        <div class="containerMain">
            <h1 class="page-title">Welcome to the Admin Panel</h1>
            <p class="page-title">
                <i>Because you are an admin, you can achieve all functionality in the application!</i>
            </p>
            <b-row>
                <b-col cols="8">
                    <b-card
                            header="Search for Profiles"
                            header-tag="header">
                        <!-- Display the search profiles component -->
                        <profiles-page :adminView="true"
                                       :containerClass="'adminProfilesContainer'"
                                       :destinations="destinations"
                                       :destinationTypes="destinationTypes"
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
                        <b-alert v-model="showSuccess" variant="success">Profile successfully created</b-alert>
                        <b-button @click="showCollapse = !showCollapse" block variant="success">
                            Create a New Profile</b-button>
                        <!-- The collapsible that uses the sign up page to create a new profile -->
                        <b-collapse id="signUpPage" class="mt-2" v-model="showCollapse">
                            <sign-up :createdByAdmin="true"
                                     :nationalityOptions="nationalityOptions"
                                     :travTypeOptions="travTypeOptions"
                                     @profile-created="showOptions">
                            </sign-up>
                        </b-collapse>
                    </b-card>
                </b-col>
            </b-row>
        </div>
    </div>
</template>

<script>
    import ProfilesPage from '../profiles/profilesPage.vue'
    import SignUp from '../index/signup.vue'

    export default {
        name: "adminActions",
        props: ['profile', 'nationalityOptions', 'travTypeOptions', 'destinations', 'destinationTypes'],
        data() {
            return {
                refreshProfiles: 0,
                refreshSignUp: 0,
                showSingleProfile: false,
                showCollapse: false,
                showSuccess: false
            }
        },
        methods: {
            /**
             * If a profile is successfully created, then re-render the profiles component (to ensure the new profile
             * is shown). Will then hide the sign up modal and show the what to do next modal.
             */
            showOptions() {
                this.showCollapse = false;
                this.refreshProfiles += 1;
                this.refreshSignUp += 1;
                this.showSuccess = true;
            },

            /**
             * Emits the selected profile to the adminPanel page, this is so an admin can modify the profile.
             * @param editProfile   the selected profile to be modified by an admin.
             */
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