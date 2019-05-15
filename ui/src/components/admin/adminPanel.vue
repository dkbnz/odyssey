<template>
    <div>
        <nav-bar-main v-bind:profile="profile"></nav-bar-main>
        <div class="container">
            <h1 class="page_title">Welcome to the Admin Panel</h1>
            <p class="page_title"><i>Because you are an admin, you can achieve all functionality in the application!
                <br/>Start by finding a profile using the form below or make one by clicking the button.</i></p>
            <b-button @click="displaySignupPage" block variant="success">Create a New Profile</b-button>

            <!-- Display the search profiles component -->
            <profiles-page :key="refreshProfiles" :adminView="true" :profile="profile"
                           nationalityOptions="nationalityOptions" travTypeOptions="travTypeOptions">
            </profiles-page>

        </div>
        <div>
            <!-- The modal that uses the signup page to create a new profile -->
            <b-modal
            id="signupModal"
            ref="signupModal"
            hide-footer>
                <template slot="modal-title"><h2>Create a New Profile</h2></template>
                <signup v-bind:nationalityOptions="nationalityOptions" v-bind:travTypeOptions="travTypeOptions"
                        :createdByAdmin="true" @profile-created="showOptions">
                </signup>
            </b-modal>
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
                        <b-button block variant="primary">Yes</b-button>
                    </b-col>
                </b-row>
            </b-modal>
        </div>

    </div>
</template>

<script>
    import NavBarMain from '../helperComponents/navbarMain.vue'
    import ProfilesPage from '../profiles/profilesPage.vue'
    import Signup from '../index/signup.vue'
    export default {
        name: "adminPanel",
        props: ['profile', 'nationalityOptions', 'travTypeOptions'],
        data() {
            return {
                refreshProfiles: 0
            }
        },
        methods: {
            /**
             * Used to display the sign up page modal so the admin can create a new profile.
             */
            displaySignupPage() {
                this.$refs['signupModal'].show();
            },
            /**
             * If a profile is successfully created, then re-render the profiles component (to ensure the new profile
             * is shown). Will then hide the sign up modal and show the what to do next modal.
             */
            showOptions() {
                this.refreshProfiles += 1;
                this.$refs['signupModal'].hide();
                this.$refs['optionModal'].show();
            },
            /**
             * Hides the option modal.
             */
            hideOptionModal() {
                this.$refs['optionModal'].hide();
            }
        },
        components: {
            NavBarMain,
            ProfilesPage,
            Signup
        }
    }
</script>

<style scoped>

</style>