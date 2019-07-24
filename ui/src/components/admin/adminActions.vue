<template>
    <div>
        <div class="containerAdminMain">
            <h1 class="page-title">Welcome to the Admin Panel</h1>
            <p class="page-title">
                <i>Because you are an admin, you can achieve all functionality in the application!</i>
            </p>
            <b-row>
                <b-col cols="6">
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
                <b-col cols="6">
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
                    <b-card header="Destination Traveller Types">
                        <b-alert v-model="showSuccess" variant="success"></b-alert>
                        <div>
                            <b-row>
                                <b-col>
                                    <h5 class="mb-1">Name</h5>
                                </b-col>
                                <b-col>
                                    <b-button size="sm" style="margin-bottom: 5px" variant="warning" @click="showDestinationDetails = !showDestinationDetails">Show More Details</b-button>
                                </b-col>
                            </b-row>
                            <div v-if="showDestinationDetails">
                                <p>Type: </p>
                                <p>District: </p>
                                <p>Latitude: </p>
                                <p>Longitude: </p>
                            </div>
                            <b-row>
                                <b-col>
                                    <b-card>
                                        <h7 class="page-title">Current</h7>
                                    </b-card>
                                </b-col>
                                <b-col>
                                    <b-card>
                                        <h7 class="page-title">Proposed Additions</h7>
                                        <div>
                                            <p class="mb-1" v-for="travellerType in travellerTypeDestination">
                                                {{travellerType.travellerType}}
                                            </p>
                                            <b-button variant="success" size="sm" @click="addToTravellerTypes(travellerType)">&#10003;</b-button>
                                        </div>
                                    </b-card>

                                </b-col>
                                <b-col>
                                    <b-card>
                                        <h7 class="page-title">Proposed Removals</h7>
                                        <div>
                                            <p class="mb-1" v-for="travellerType in travellerTypeDestination">
                                                {{travellerType.travellerType}}
                                            </p>
                                            <b-button variant="danger" size="sm" @click="addToTravellerTypes(travellerType)">&#10003;</b-button>
                                        </div>
                                    </b-card>
                                </b-col>
                            </b-row>
                        </div>




                        <b-list-group>
                            <b-list-group-item class="flex-column align-items-start"
                                               v-for="destination in travellerTypeProposals">
                                <div class="d-flex w-100 justify-content-between">
                                    <h5 class="mb-1">Name: {{destination.name}}</h5>
                                </div>
                                <div class="d-flex w-100 justify-content-between">
                                    <h5 class="mb-1">Traveller Types Added:</h5>
                                    <div v-for="travellerTypeDestination in destination.proposedTravellerTypesAdd">
                                        <p class="mb-1" v-for="travellerType in travellerTypeDestination">
                                            {{travellerType.travellerType}}
                                        </p>
                                        <b-button variant="success" @click="addToTravellerTypes(travellerType)">&#10003;</b-button>
                                        <b-button variant="success">&#10007;</b-button>
                                    </div>
                                </div>
                            </b-list-group-item>
                        </b-list-group>
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
                showSuccess: false,
                travellerTypeProposals: [],
                destinationTravellerTypesToAdd: [],
                showDestinationDetails: false
            }
        },

        mounted() {
            this.getTravellerTypeProposals(travellerTypeProposals => this.travellerTypeProposals = travellerTypeProposals);
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
            },

            getTravellerTypeProposals(updateTravellerTypeProposals) {
                return fetch(`/v1/destinations/proposals`, {
                    accept: "application/json"
                })
                    .then(response => response.json())
                    .then(updateTravellerTypeProposals);
            },

            addToTravellerTypes(travellerType) {

            }
        },
        components: {
            ProfilesPage,
            SignUp
        }
    }
</script>

<style>
    .hidden_header {
        display: none;
    }
</style>