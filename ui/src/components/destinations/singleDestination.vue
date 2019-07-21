<template>
    <div>
        <b-modal hide-footer id="editDestModal" ref="editDestModal" size="l" title="Edit Destination">
            <add-destinations
                    :inputDestination="copiedDestination"
                    :profile="profile"
                    :heading="'Edit'"
                    :destination-types="destinationTypes"
                    @destination-saved="destinationSaved">
            </add-destinations>
            <b-button @click="dismissModal('editDestModal')" class="mr-3 buttonMargins float-right">Close</b-button>
        </b-modal>

        <b-modal id="deleteDestModal" ref="deleteDestModal" size="xl" title="Delete Destination">
            <b-alert v-model="showError" variant="danger" dismissible>
                Could not delete destination!
            </b-alert>
            <p v-if="tripsUsed.count === 1">This destination is used by {{tripsUsed.count}} trip.<br>
                Are you sure you want to delete it?
            </p>
            <p v-else>This destination is used by {{tripsUsed.count}} trips. <br>
                Are you sure you want to delete it?
            </p>
            <b-list-group
            style="overflow-y: scroll; height: 30vh;" v-if="tripsUsed.count > 0">
                <b-list-group-item class="flex-column align-items-start"
                                   v-for="trip in tripsUsed.matchingTrips">
                    <div class="d-flex w-100 justify-content-between">
                        <h5 class="mb-1">Name: {{trip.tripName}}</h5>
                    </div>
                    <div class="d-flex w-100 justify-content-between">
                        <h5 class="mb-1">Created by: {{trip.firstName}} {{trip.lastName}}</h5>
                    </div>
                </b-list-group-item>
            </b-list-group>
            <template slot="modal-footer">
                <b-col>
                    <b-button @click="dismissModal('deleteDestModal')" class="mr-2 float-right" variant="success" block>
                        No
                    </b-button>
                </b-col>
                <b-col>
                    <b-button @click="deleteDestination" class="mr-2 float-right" variant="danger" block>
                        Yes
                    </b-button>
                </b-col>
            </template>

        </b-modal>

        <b-row>
            <b-col>
                <p class="mb-1">
                    Type: {{viewDestination.type.destinationType}}
                </p>
                <p class="mb-1">
                    District: {{viewDestination.district}}
                </p>
                <p class="mb-1">
                    Country: {{viewDestination.country}}
                </p>
                <p class="mb-1">
                    Latitude: {{viewDestination.latitude}}
                </p>
                <p class="mb-1">
                    Longitude: {{viewDestination.longitude}}
                </p>
                <b-button @click="editDestination" variant="warning"
                          v-if="viewDestination.owner.id === profile.id" block>
                    Edit
                </b-button>
                <b-button @click="confirmDeleteDestination" variant="danger"
                          v-if="viewDestination.owner.id === profile.id" block>
                    Delete
                </b-button>
            </b-col>
            <b-col cols="9">
                <destination-gallery
                        :destination="viewDestination"
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
                tripsUsed: "",
                refreshDestination: null,
                showError: false
            }
        },

        props: {
            destination: Object,
            profile: Object,
            destinationTypes: Array,
            userProfile: {
                default: function() {
                    return this.profile;
                }
            },
        },

        components: {
            AddDestinations,
            DestinationGallery,
            YourTrips
        },

        computed: {
            viewDestination() {
                return this.destination;
            }
        },

        methods: {
            destinationSaved(savedDestination) {
                this.$emit('destination-saved', savedDestination);
            },

            copyDestination() {
                return JSON.parse(JSON.stringify(this.destination))
            },

            editDestination() {
                this.copiedDestination = this.copyDestination();
                this.$refs["editDestModal"].show();
            },

            confirmDeleteDestination() {
                this.copiedDestination = this.copyDestination();
                this.$refs["deleteDestModal"].show();
                this.getTripsUsedBy();
            },

            deleteDestination() {
                let self = this;
                fetch(`/v1/destinations/` + this.copiedDestination.id, {
                    method: 'DELETE'
                }).then(function(response) {
                        if (response.ok) {
                            self.dismissModal('deleteDestModal');
                            self.$emit('destination-deleted');
                        }
                        else {
                            self.showError = true;
                        }
                    });
            },

            getTripsUsedBy() {
                fetch(`/v1/destinationCheck/` + this.copiedDestination.id, {
                    accept: "application/json"
                })
                    .then(response => response.json())
                    .then(response => this.tripsUsed = response);
            },


            /**
             * Used to dismiss the edit a destination modal.
             *
             * @param modal, the modal that is wanting to be dismissed.
             */
            dismissModal(modal) {
                this.$refs[modal].hide();
            }
        }
    }
</script>

<style scoped>

</style>