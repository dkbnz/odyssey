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
                            <desktop-leaderboard :minimal-info="true"
                                           :containerClass="'adminProfilesContainer'"
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
                        <div v-if="travellerTypeProposals.length > 0" class="proposalDiv">
                            <b-card v-for="destination in travellerTypeProposals"
                                    class="proposals" :key="destination.id">
                                <b-row>
                                    <b-col>
                                        <h5 class="mb-1">{{destination.name}}</h5>
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
                                    <p>Type: {{destination.type.destinationType}}</p>
                                    <p>District: {{destination.district}}</p>
                                    <p>Latitude: {{destination.latitude}}</p>
                                    <p>Longitude: {{destination.longitude}}</p>
                                </div>
                                <b-row>
                                    <b-col>
                                        <b-card>
                                            <h5 class="page-title">Current</h5>
                                            <div v-for="travellerType in destination.travellerTypes">
                                                <p class="mb-1">
                                                    {{travellerType.travellerType}}
                                                </p>
                                            </div>
                                        </b-card>
                                    </b-col>
                                    <b-col>
                                        <b-card>
                                            <h5 class="page-title">Additions</h5>
                                            <div v-for="travellerType in destination.proposedTravellerTypesAdd">
                                                <b-row>
                                                    <b-col cols="16" md="10">
                                                        {{travellerType.travellerType}}
                                                    </b-col>
                                                    <b-col cols="8" md="2">
                                                        <b-button variant="success" class="proposalButton"
                                                                  @click="addTravellerTypes(destination, travellerType)">
                                                            &#10003;
                                                        </b-button>
                                                    </b-col>
                                                </b-row>
                                            </div>
                                        </b-card>
                                    </b-col>
                                    <b-col>
                                        <b-card>
                                            <h5 class="page-title">Removals</h5>
                                            <div v-for="travellerType in destination.proposedTravellerTypesRemove">
                                                <b-row>
                                                    <b-col cols="16" md="10">
                                                        {{travellerType.travellerType}}
                                                    </b-col>
                                                    <b-col cols="8" md="2">
                                                        <b-button variant="danger" class="proposalButton"
                                                                  @click="removeTravellerTypes(destination, travellerType)">
                                                            &#10003;
                                                        </b-button>
                                                    </b-col>
                                                </b-row>
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
                            </b-card>
                        </div>

                        <div v-else>
                            <p>No proposals could be found.</p>
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
             *@param updateTravellerTypeProposals   the variable to update the list of traveller type proposals.
             */
            getTravellerTypeProposals(updateTravellerTypeProposals) {
                return fetch(`/v1/destinations/proposals`, {
                    accept: "application/json"
                })
                    .then(response => response.json())
                    .then(updateTravellerTypeProposals);
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
             * @param destination   the destination to have the traveller types added to.
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
                            self.alertMessage = "Destination traveller types updated";
                            self.showTravellerTypeUpdateSuccess = true;
                            setTimeout(function () {
                                self.showTravellerTypeUpdateSuccess = false;
                            }, 3000);
                            self.removeProposed(destination);
                        } else {
                            self.alertMessage = "Cannot update traveller types";
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
            DesktopLeaderboard,
            SignUp
        }
    }
</script>
<style>
    .proposals {
        margin: 1vh 0 2vh 0;
    }

    .proposalButton {
        height: 20px;
        width: 20px;
        font-size: 12px;
        padding: 0
    }

    .proposalDiv {
        max-height: 100vh;
        overflow: scroll;
    }
</style>