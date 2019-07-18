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
                <b-button @click="editDestination" variant="warning"  block>Edit</b-button>
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