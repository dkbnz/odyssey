<template>
    <div v-if="destination.owner !== undefined">
        <b-modal id="deleteDestinationModal" ref="deleteDestinationModal" size="xl" title="Delete Destination">
            <b-alert v-model="showError" variant="danger" dismissible>
                Could not delete destination!
            </b-alert>
            <p v-if="destinationUsage.photo_count === 1">
                This destination contains {{destinationUsage.photo_count}} photo.
            </p>
            <p v-else>This destination contains {{destinationUsage.photo_count}} photos.</p>
            <p v-if="destinationUsage.trip_count === 1">
                This destination is used by {{destinationUsage.trip_count}} trip.<br>
                Are you sure you want to delete it?
            </p>
            <p v-else>This destination is used by {{destinationUsage.trip_count}} trips. <br>
                Are you sure you want to delete it?
            </p>
            <b-list-group
                    style="overflow-y: scroll; height: 30vh;" v-if="destinationUsage.trip_count > 0">
                <b-list-group-item class="flex-column align-items-start"
                                   v-for="trip in destinationUsage.matching_trips" :key="trip.id">
                    <div class="d-flex w-100 justify-content-between">
                        <h5 class="mb-1">Name: {{trip.name}}</h5>
                    </div>
                    <div class="d-flex w-100 justify-content-between">
                        <h5 class="mb-1">Created by: {{trip.profile.firstName}} {{trip.profile.lastName}}</h5>
                    </div>
                </b-list-group-item>
            </b-list-group>
            <template slot="modal-footer">
                <b-col>
                    <b-button @click="dismissModal('deleteDestinationModal')" class="mr-2 float-right" variant="primary"
                              block>
                        No
                    </b-button>
                </b-col>
                <b-col>
                    <b-button @click="deleteDestination" class="mr-2 float-right" variant="secondary" block>
                        Yes
                    </b-button>
                </b-col>
            </template>

        </b-modal>

        <b-row>
            <b-col>
                <!--v-if statement prevents error during component load-->
                <p class="mb-1" v-if="destination.type">
                    Type: {{destination.type.destinationType}}
                </p>
                <p class="mb-1">
                    District: {{destination.district}}
                </p>
                <p class="mb-1">
                    Country: {{destination.country}}
                </p>
                <p class="mb-1">
                    Latitude: {{destination.latitude}}
                </p>
                <p class="mb-1">
                    Longitude: {{destination.longitude}}
                </p>
                <p class="mb-1">
                    Traveller Types:
                </p>
                <ul v-if="destination.travellerTypes && destination.travellerTypes.length > 0">
                    <li v-for="travellerType in destination.travellerTypes">
                        {{travellerType.travellerType}}
                    </li>
                </ul>
                <p v-else class="descriptionText">
                    No Traveller Types for this destination!
                </p>
                <b-button variant="outline-primary"
                          @click="calculateCurrentTravellerTypes(); showEditTravellerTypes = !showEditTravellerTypes"
                          block>
                    {{travellerTypeLinkText}}
                </b-button>

                <b-alert variant="success" v-model="showTravellerTypeUpdateSuccess">{{alertMessage}}</b-alert>
                <b-alert variant="danger" v-model="showTravellerTypeUpdateFailure">{{alertMessage}}</b-alert>

                <div v-if="showEditTravellerTypes" class="travellerTypeDiv">
                    <b-form-group label="Add Traveller Types:">
                        <b-form-checkbox-group id="addTravellerTypes" v-model="calculatedTravellerTypes">
                            <b-form-checkbox v-for="travellerType in travTypeOptions" :value="travellerType"
                                             :key="travellerType.id">
                                {{travellerType.travellerType}}
                            </b-form-checkbox>
                        </b-form-checkbox-group>
                    </b-form-group>
                    <b-button variant="primary" @click="requestTravellerTypeChange" block>
                        {{travellerTypeButtonText}}
                    </b-button>
                </div>

                <b-button @click="editDestination" variant="warning"
                          v-if="destination.owner.id === profile.id || profile.isAdmin" block>
                    Edit
                </b-button>
                <b-button @click="confirmDeleteDestination" variant="danger"
                          v-if="destination.owner.id === profile.id || profile.isAdmin" block>
                    Delete
                </b-button>
            </b-col>
            <b-col cols="9">
                <destination-gallery
                        :destination="destination"
                        :profile="profile"
                        :userProfile="userProfile">
                </destination-gallery>
            </b-col>

        </b-row>
    </div>
</template>

<script>
    import DestinationGallery from "../photos/destinationGallery";
    import AddDestinations from "./addDestinations";
    import YourTrips from "../trips/yourTrips";

    export default {
        name: "singleDestination",

        data: function () {
            return {
                copiedDestination: "",
                destinationUsage: "",
                refreshDestination: null,
                showError: false,
                showEditTravellerTypes: false,
                calculatedTravellerTypes: [],
                alertMessage: "",
                showTravellerTypeUpdateSuccess: false,
                showTravellerTypeUpdateFailure: false
            }
        },

        props: {
            destination: Object,
            profile: Object,
            destinationTypes: Array,
            userProfile: {
                default: function () {
                    return this.profile;
                }
            },
            travTypeOptions: Array
        },

        watch: {
            /**
             * When the destination prop changes, hide the show edit traveller types section, this is so the section
             * can update.
             */
            destination() {
                this.showEditTravellerTypes = false;
            }
        },

        computed: {
            travellerTypeButtonText() {
                if (this.destination.owner !== null &&
                    this.profile.id === this.destination.owner.id ||
                    this.profile.isAdmin) {
                    return "Change Traveller Types"
                }
                return "Propose Traveller Types"
            },

            travellerTypeLinkText() {
                if (this.destination.owner !== undefined &&
                    this.profile !== undefined &&
                    this.profile.id === this.destination.owner.id ||
                    this.profile.isAdmin) {
                    return "Change Traveller Types"
                }
                if (this.showEditTravellerTypes) {
                    return "Close Traveller Types"
                }
                return "Propose Traveller Types"
            }
        },

        components: {
            AddDestinations,
            DestinationGallery,
            YourTrips
        },

        methods: {
            /**
             * Emits destination saved event to the destinations page, this is so data can be re-rendered.
             * @param savedDestination      the destination that is saved and emitted to the parent component.
             */
            destinationSaved(savedDestination) {
                this.$emit('destination-saved', savedDestination);
            },


            /**
             * Used to convert the destination object into a Json object.
             */
            copyDestination() {
                return JSON.parse(JSON.stringify(this.destination))
            },


            /**
             * Copies the selected destination and emits an event to parent component containing the destination.
             */
            editDestination() {
                this.copiedDestination = this.copyDestination();
                this.$emit('destination-edit', this.copiedDestination);
            },


            /**
             * Copies the selected destination, displays the delete destination modal and retrieves all the trips that
             * the destination is used in.
             */
            confirmDeleteDestination() {
                this.copiedDestination = this.copyDestination();
                this.$refs["deleteDestinationModal"].show();
                this.getTripsUsedBy();
            },


            /**
             * Send the Http request to delete the specified destination. If successfully deleted, the delete modal is
             * hidden and the list of destinations refreshed. Otherwise, an error is shown.
             */
            deleteDestination() {
                let self = this;
                fetch(`/v1/destinations/` + this.copiedDestination.id, {
                    method: 'DELETE'
                }).then(function (response) {
                    if (response.ok) {
                        self.dismissModal('deleteDestinationModal');
                        self.$emit('destination-deleted');
                    }
                    else {
                        self.showError = true;
                    }
                });
            },


            /**
             * Sends an Http request to check which trips a destination is used in.
             */
            getTripsUsedBy() {
                fetch(`/v1/destinations/` + this.copiedDestination.id + `/checkDuplicates`, {
                    accept: "application/json"
                })
                    .then(response => response.json())
                    .then(response => this.destinationUsage = response);
            },


            /**
             * Used to dismiss the edit a destination modal.
             *
             * @param modal     the modal that is wanting to be dismissed.
             */
            dismissModal(modal) {
                this.$refs[modal].hide();
            },


            /**
             * Used to calculate the current traveller types for a destination so changes can be made/suggested.
             *
             * @returns {string[]}      the list of traveller types for the current destination.
             */
            calculateCurrentTravellerTypes() {
                this.calculatedTravellerTypes = this.destination.travellerTypes;
            },


            /**
             * Sends a request to the back end, which contains all the traveller types.
             */
            requestTravellerTypeChange() {
                let url = `/v1/destinations/` + this.destination.id + `/travellerTypes`;
                let self = this;
                if (this.destination.owner.id !== this.profile.id && !this.profile.isAdmin) {
                    url += `/propose`;
                }
                fetch(url, {
                    method: 'POST',
                    headers: {'content-type': 'application/json'},
                    body: JSON.stringify(this.calculatedTravellerTypes)
                })
                    .then(function (response) {
                        if (response.ok) {
                            if (self.destination.owner.id === self.profile.id || self.profile.isAdmin) {
                                self.destination.travellerTypes = self.calculatedTravellerTypes;
                                self.alertMessage = "Destination traveller types updated";
                            } else {
                                self.alertMessage = "Update request sent";
                            }

                            self.showTravellerTypeUpdateSuccess = true;
                            setTimeout(function () {
                                self.showTravellerTypeUpdateSuccess = false;
                            }, 3000);
                            self.showEditTravellerTypes = false;
                        } else {
                            self.alertMessage = "Cannot update traveller types";
                            self.showTravellerTypeUpdateFailure = true;
                            setTimeout(function () {
                                self.showTravellerTypeUpdateFailure = false;
                            }, 3000);
                            self.showEditTravellerTypes = false;
                        }
                        self.$emit('data-changed');
                        return JSON.parse(JSON.stringify(response));
                    });
            }
        }
    }
</script>

<style>
    @import "../../css/singleDestination.css";
</style>