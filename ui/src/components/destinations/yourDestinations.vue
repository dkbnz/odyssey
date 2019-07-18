<template>
    <div>
        <div v-if="addDestinationForm">
            <b-button @click="returnToList" block variant="success">Return</b-button>
            <b-list-group>
                <b-list-group-item class="flex-column align-items-start">
                    <add-destinations v-if="addDestinationForm"
                                      :profile="profile"
                                      :destinationTypes="destinationTypes"
                                      :heading="'Add'"
                                      @data-changed="$emit('data-changed')">
                    </add-destinations>
                </b-list-group-item>
            </b-list-group>
        </div>
        <div v-else>
                <b-button variant="success" @click="addDestinationForm = true" block>Add Destination</b-button>
            <b-list-group class="scroll">
                <b-list-group-item v-for="destination in (yourDestinations)" href="#" class="flex-column align-items-start" @click="$emit('destination-click', destination)">
                    <div class="d-flex w-100 justify-content-between">
                        <h5 class="mb-1">{{destination.name}}</h5>
                        <small v-if="destination.public">Public</small>
                        <small v-else>Private</small>
                    </div>

                    <p class="mb-1">
                        {{destination.district}}
                    </p>
                    <p class="mb-1">
                        {{destination.country}}
                    </p>

                </b-list-group-item>
            </b-list-group>
        </div>
    </div>
</template>

<script>
    import AddDestinations from "./addDestinations";
    export default {
        name: "yourDestinations",
        components: {AddDestinations},
        props: ['profile', 'destinationTypes'],
        data() {
            return {
                yourDestinations: [],
                destinationIndex: 0,
                addDestinationForm: false,
            }
        },
        mounted() {
            this.getYourDestinations();
        },
        methods: {
            /**
             * Make request to the backend to fetch the current users destinations
             */
            getYourDestinations() {
                fetch(`/v1/destinations/` + this.profile.id, {
                    accept: "application/json"
                })
                    .then(response => response.json())
                    .then(response => this.yourDestinations = response)
            },
            returnToList() {
                this.getYourDestinations();
                this.addDestinationForm = false;
            }

        }
    }
</script>

<style scoped>

</style>