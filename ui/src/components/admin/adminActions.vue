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
                        <b-alert variant="success" v-model="showTravellerTypeUpdateSuccess">{{alertMessage}}</b-alert>
                        <b-alert variant="danger" v-model="showTravellerTypeUpdateFailure">{{alertMessage}}</b-alert>
                        <!-- Loop through the list of proposals and generate an area to accept/reject for each one -->
                        <!--v-for="destination in travellerTypeProposals"-->
                        <div>
                            <b-row>
                                <b-col>
                                    <h5 class="mb-1">Name</h5>
                                </b-col>
                                <b-col>
                                    <b-button size="sm" class="buttonMarginsBottom"
                                              variant="warning"
                                              @click="showDestinationDetails = !showDestinationDetails">
                                        Show More Details
                                    </b-button>
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
                                        <h6 class="page-title">Current</h6>
                                        <div>
                                            <!--v-for="travellerType in destination.travellerTypes"-->
                                            <p class="mb-1">
                                                <!--{{travellerType.travellerType}}-->
                                            </p>
                                        </div>
                                    </b-card>
                                </b-col>
                                <b-col>
                                    <b-card>
                                        <h6 class="page-title">Proposed Additions</h6>
                                        <div>
                                            <!--v-for="travellerType in destination.proposedTravellerTypesAdd"-->
                                            <p class="mb-1" >
                                                <!--{{travellerType.travellerType}}-->
                                            </p>
                                            <b-button variant="success" size="sm"
                                                      @click="addTravellerTypes(destination, travellerType)">
                                                &#10003;
                                            </b-button>
                                        </div>
                                    </b-card>

                                </b-col>
                                <b-col>
                                    <b-card>
                                        <h6 class="page-title">Proposed Removals</h6>
                                        <div>
                                            <!--v-for="travellerType in destination.proposedTravellerTypesRemove"-->
                                            <p class="mb-1" >
                                                <!--{{travellerType.travellerType}}-->
                                            </p>
                                            <b-button variant="danger" size="sm"
                                                      @click="removeTravellerTypes(destination, travellerType)">
                                                &#10003;
                                            </b-button>
                                        </div>
                                    </b-card>
                                </b-col>
                            </b-row>
                            <b-row>
                                <b-button variant="primary" class="buttonMarginsTop"
                                          @click="sendTravellerTypes(destination)" block>
                                    Submit
                                </b-button>
                            </b-row>
                        </div>
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
                showDestinationDetails: false,
                alertMessage: "",
                showTravellerTypeUpdateSuccess: false,
                showTravellerTypeUpdateFailure: false
            }
        },

        mounted() {
            this.getTravellerTypeProposals(travellerTypeProposals =>
                this.travellerTypeProposals = travellerTypeProposals);
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


            /**
             * Retrieves the list of traveller type proposals to display on the frontend. Admin can then accept/reject
             * proposals.
             *
             *@param updateTravellerTypeProposals   the variable to update the list of traveller type propsals.
             */
            getTravellerTypeProposals(updateTravellerTypeProposals) {
                return fetch(`/v1/destinations/proposals`, {
                    accept: "application/json"
                })
                    .then(response => JSON.parse(JSON.stringify(response)))
                    .then(updateTravellerTypeProposals);
            },


            /**
             * Adds the traveller type to the list of traveller types for said destination.
             *
             * @param travellerType     the traveller type to be added.
             */
            addTravellerTypes(destination, travellerType) {
                destination.travellerTypes.push(travellerType);
            },


            /**
             * Removes the traveller type from the list of traveller types for said destination.
             *
             * @param travellerType     the traveller type to be removed.
             */
            removeTravellerTypes(destination, travellerType) {
                for (let i = 0; i <= destination.travellerTypes.length; i++) {
                    if (destination.travellerTypes[i] === travellerType) {
                        destination.travellerTypes.splice(i, 1);
                    }
                }
            },


            /**
             * Sends a request to the back end, which contains all the traveller types.
             */
            sendTravellerTypes(destination) {
                let self = this;
                fetch(`/v1/destinations/` + destination.id + `/travellerTypes`, {
                    method: 'POST',
                    headers: {'content-type': 'application/json'},
                    body: JSON.stringify(destination.travellerTypes)
                })
                    .then(function(response) {
                        if (response.ok) {
                            self.alertMessage = "Destination traveller types updated.";
                            self.showTravellerTypeUpdateSuccess = true;
                            setTimeout(function () {
                                self.showTravellerTypeUpdateSuccess = false;
                            }, 3000);
                        } else {
                            self.alertMessage = "Cannot update traveller types.";
                            self.showTravellerTypeUpdateFailure = true;
                            setTimeout(function () {
                                self.showTravellerTypeUpdateFailure = false;
                            }, 3000);
                        }
                        return JSON.parse(JSON.stringify(response));
                    });
            }
        },
        components: {
            ProfilesPage,
            SignUp
        }
    }
</script>