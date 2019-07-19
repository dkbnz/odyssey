<template>
    <div>
        <b-modal hide-footer id="editDestModal" ref="editDestModal" size="l" title="Edit Destination">
            <add-destinations
                    :inputDestination="copiedDestination"
                    :profile="profile"
                    :heading="'Edit'"
                    :destination-types="destinationTypes"
                    @destinationSaved="refreshDestination()">
            </add-destinations>
            <b-button @click="dismissModal('editDestModal')" class="mr-2 float-right">Close</b-button>
        </b-modal>

        <b-modal hide-footer id="deleteDestModal" ref="deleteDestModal" size="l" title="Delete Destination">
            <p v-if="tripsUsedCount.count === 1">This destination is used by {{tripsUsedCount.count}} trip.<br>
                Are you sure you want to delete it?
            </p>
            <p v-else>This destination is used by {{tripsUsedCount.count}} trips. <br>
                Are you sure you want to delete it?
            </p>
            <b-row>
                <b-col>
                    <b-button @click="dismissModal('deleteDestModal')" class="mr-2 float-right" variant="success" block>
                        No
                    </b-button>
                </b-col>
                <b-col>
                    <b-button @click="dismissModal('deleteDestModal')" class="mr-2 float-right" variant="danger" block>
                        Yes
                    </b-button>
                </b-col>
            </b-row>
        </b-modal>

        <b-row>
            <b-col>
                <p class="mb-1">
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
                <b-button @click="editDestination" variant="warning"
                          v-if="destination.owner.id === profile.id" block>
                    Edit
                </b-button>
                <b-button @click="deleteDestination" variant="danger"
                          v-if="destination.owner.id === profile.id" block>
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

    export default {

        name: "singleDestination",
        data: function () {
            return {
                copiedDestination: null,
                tripsUsedCount: 0
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
            DestinationGallery
        },
        methods: {
            refreshDestination() {
            },

            copyDestination() {
                return JSON.parse(JSON.stringify(this.destination))
            },

            editDestination() {
                this.copiedDestination = this.copyDestination();
                this.$refs["editDestModal"].show();
            },

            deleteDestination() {
                this.copiedDestination = this.copyDestination();
                this.$refs["deleteDestModal"].show();
                this.getTripsUsedBy();
            },

            getTripsUsedBy() {
                fetch(`/v1/destinationCheck/` + this.copiedDestination.id, {
                    accept: "application/json"
                })
                    .then(response => response.json())
                    .then(response => this.tripsUsedCount = response);
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