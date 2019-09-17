<template>
    <div>
        <div class="bg-white m-2 pt-3 pl-3 pr-3 pb-3 rounded-lg">
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
                            <desktop-leaderboard :minimal-info="true"
                                           :destinations="destinations"
                                           :destinationTypes="destinationTypes"
                                           :key="refreshProfiles"
                                           :nationalityOptions="nationalityOptions"
                                           :perPage=5
                                           :profile="profile"
                                           :travTypeOptions="travTypeOptions"
                                           @admin-edit="getSingleProfile">
                            </desktop-leaderboard>
                    </b-card>
                </b-col>
                <b-col cols="6">
                    <b-card header="Create a Profile">
                        <b-alert v-model="showSuccess" variant="success" dismissible>Profile successfully created</b-alert>
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
                        <b-alert variant="success" v-model="showTravellerTypeUpdateSuccess" dismissible><p class="wrapWhiteSpace">{{alertMessage}}</p></b-alert>
                        <b-alert variant="danger" v-model="showTravellerTypeUpdateFailure" dismissible><p class="wrapWhiteSpace">{{alertMessage}}</p></b-alert>
                        <!-- Loop through the list of proposals and generate an area to accept/reject for each one -->
                        <div v-if="travellerTypeProposals.length > 0" class="proposalDiv">
                            <b-card v-for="destination in travellerTypeProposals"
                                    class="proposals" :key="destination.id">
                                <h5 class="mb-1">{{destination.name}}</h5>
                                <b-button size="sm" class="buttonMarginsBottom"
                                          variant="warning"
                                          @click="showDestinationDetails = !showDestinationDetails">
                                    Show More Details
                                </b-button>
                                <div v-if="showDestinationDetails">
                                    <p>Type: {{destination.type.destinationType}}</p>
                                    <p>District: {{destination.district}}</p>
                                    <p>Latitude: {{destination.latitude}}</p>
                                    <p>Longitude: {{destination.longitude}}</p>
                                </div>
                                <b-row no-gutters>
                                    <b-col class="pr-1" md="4" sm="12">
                                        <b-card>
                                            <h5 class="page-title">Current</h5>
                                            <div v-for="travellerType in destination.travellerTypes">
                                                <p class="mb-1">
                                                    {{travellerType.travellerType}}
                                                </p>
                                            </div>
                                        </b-card>
                                    </b-col>
                                    <b-col class="pr-1" md="4" sm="12">
                                        <b-card>
                                            <h5 class="page-title">Additions</h5>
                                            <!-- Set no gutters for the following rows, as otherwise they have a negative margin by default -->
                                                <b-row no-gutters v-for="travellerType in destination.proposedTravellerTypesAdd" :key="travellerType.id">
                                                    <b-col cols="10" sm="10">
                                                        {{travellerType.travellerType}}
                                                    </b-col>
                                                    <b-col cols="2" sm="1">
                                                        <b-button variant="success" class="proposalButton"
                                                                  @click="addTravellerTypes(destination, travellerType)">
                                                            &#10003;
                                                        </b-button>
                                                    </b-col>
                                                </b-row>
                                        </b-card>
                                    </b-col>
                                    <b-col class="pr-1" md="4" sm="12">
                                        <b-card>
                                            <h5 class="page-title">Removals</h5>
                                                <b-row no-gutters v-for="travellerType in destination.proposedTravellerTypesRemove" :key="travellerType.id">
                                                    <b-col cols="10" sm="10">
                                                        {{travellerType.travellerType}}
                                                    </b-col>
                                                    <b-col cols="2" sm="1">
                                                        <b-button variant="danger" class="proposalButton"
                                                                  @click="removeTravellerTypes(destination, travellerType)">
                                                            &#10003;
                                                        </b-button>
                                                    </b-col>
                                                </b-row>
                                        </b-card>
                                    </b-col>
                                </b-row>
                                <b-row>
                                    <b-button variant="primary" class="buttonMarginsTop"
                                              @click="sendTravellerTypes(destination)" block :disabled="sendingRequest">
                                        <b-img alt="Loading" class="align-middle loading" v-if="sendingRequest" :src="assets['loadingLogo']" height="20%">
                                        </b-img>
                                        Submit
                                    </b-button>
                                </b-row>
                            </b-card>
                        </div>
                        <div v-else class="text-center my-2">
                            <b-img alt="Loading" class="align-middle loading" v-if="retrievingProposals" :src="assets['loadingLogo']">
                            </b-img>
                            <p v-if="!retrievingProposals && !travellerTypeProposals.length">No proposals could be found.</p>
                        </div>
                    </b-card>
                </b-col>
            </b-row>
        </div>
    </div>
</template>

<script>
    import SignUp from '../index/signup.vue'
    import DesktopLeaderboard from "../profiles/desktopLeaderboard";

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
                showTravellerTypeUpdateFailure: false,
                retrievingProposals: false,
                sendingRequest: false
            }
        },

        mounted() {
            this.getTravellerTypeProposals();
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
             *
             * @param editProfile   the selected profile to be modified by an admin.
             */
            getSingleProfile(editProfile) {
                this.$emit('admin-edit', editProfile);
            },


            /**
             * Retrieves the list of traveller type proposals to display on the frontend. Admin can then accept/reject
             * proposals.
             *
             * @param updateTravellerTypeProposals   the variable to update the list of traveller type proposals.
             */
            getTravellerTypeProposals(updateTravellerTypeProposals) {
                let self = this;
                this.retrievingProposals = true;
                return fetch(`/v1/destinations/proposals`, {
                    accept: "application/json"
                }).then(function (response) {
                    if (!response.ok) {
                        throw response;
                    } else {
                        return response.json();
                    }
                }).then(function (responseBody) {
                    self.showTravellerTypeUpdateFailure = false;
                    self.retrievingProposals = false;
                    self.travellerTypeProposals = responseBody;
                }).catch(function (response) {
                    self.alertMessage = "Cannot retrieve traveller type proposals";
                    self.showTravellerTypeUpdateFailure = true;
                    if (response.status > 404) {
                        self.showErrorToast([{message: "An unexpected error occurred"}]);
                    } else {
                        self.showSuccess = false;
                        self.retrievingProposals = false;
                        response.json().then(function(responseBody) {
                            self.showErrorToast(responseBody);
                        });
                    }
                });
            },


            /**
             * Adds the traveller type to the list of traveller types for said destination.
             *
             * @param destination       the destination to have the traveller type added to.
             * @param travellerType     the traveller type to be added.
             */
            addTravellerTypes(destination, travellerType) {
                destination.travellerTypes.push(travellerType);
                for(let j = 0; j <= destination.proposedTravellerTypesAdd.length; j++) {
                    if (JSON.stringify(destination.proposedTravellerTypesAdd[j]) === JSON.stringify(travellerType)) {
                        destination.proposedTravellerTypesAdd.splice(j, 1);
                    }
                }
            },


            /**
             * Removes the traveller type from the list of traveller types for said destination.
             *
             * @param destination       the destination to have the traveller type removed from.
             * @param travellerType     the traveller type to be removed.
             */
            removeTravellerTypes(destination, travellerType) {
                for (let i = 0; i <= destination.travellerTypes.length; i++) {
                    if (JSON.stringify(destination.travellerTypes[i]) === JSON.stringify(travellerType)) {
                        destination.travellerTypes.splice(i, 1);
                    }
                    for(let j = 0; j <= destination.proposedTravellerTypesRemove.length; j++) {
                        if (JSON.stringify(destination.proposedTravellerTypesRemove[j]) === JSON.stringify(travellerType)) {
                            destination.proposedTravellerTypesRemove.splice(j, 1);
                        }
                    }
                }
            },


            /**
             * Deletes the card on a successful submit of the changes for a destination traveller types.
             *
             * @param destination   the destination to be removed from the list of proposed changes.
             */
            removeProposed(destination) {
                for(let j = 0; j <= this.travellerTypeProposals.length; j++) {
                    if (JSON.stringify(this.travellerTypeProposals[j].id) === JSON.stringify(destination.id)) {
                        this.travellerTypeProposals.splice(j, 1);
                    }
                }
            },


            /**
             * Sends a request to the back end, which contains all the traveller types.
             *
             * @param destination   the destination to have the traveller types added to.
             */
            sendTravellerTypes(destination) {
                let self = this;
                this.sendingRequest = true;
                fetch(`/v1/destinations/` + destination.id + `/travellerTypes`, {
                    method: 'POST',
                    headers: {'content-type': 'application/json'},
                    body: JSON.stringify(destination.travellerTypes)
                }).then(function (response) {
                    if (!response.ok) {
                        throw response;
                    } else {
                        return response.json();
                    }
                }).then(function () {
                    self.showTravellerTypeUpdateFailure = false;
                    self.alertMessage = "Destination traveller types updated";
                    self.showTravellerTypeUpdateSuccess = true;
                    setTimeout(function () {
                        self.showTravellerTypeUpdateSuccess = false;
                    }, 3000);
                    self.removeProposed(destination);
                }).catch(function (response) {
                    if (response.status > 404) {
                        self.showErrorToast([{message: "An unexpected error occurred"}]);
                    } else {
                        self.alertMessage = "Cannot update traveller types";
                        self.showTravellerTypeUpdateFailure = true;
                        response.json().then(function(responseBody) {
                            self.showErrorToast(responseBody);
                        });
                    }
                });
                this.sendingRequest = false;
            }
        },

        components: {
            DesktopLeaderboard,
            SignUp
        }
    }
</script>

<style scoped>
    @import "../../css/admin.css";
</style>