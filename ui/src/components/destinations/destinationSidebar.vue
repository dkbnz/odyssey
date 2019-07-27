<template>
    <div>
        <b-tabs content-class="mt-3">
            <b-tab title="Your Destinations" active>
                <destination-search-list
                        :search-public="false"
                        :profile="profile"
                        :destinationTypes="destinationTypes"
                        @destination-click="destination => $emit('destination-click', destination)">
                </destination-search-list>
            </b-tab>
            <b-tab title="Public Destinations">
                <destination-search-list
                        :search-public="true"
                        :profile="profile"
                        :destinationTypes="destinationTypes"
                        @destination-click="destination => $emit('destination-click', destination)">
                </destination-search-list>
            </b-tab>
            <b-tab title="Add">
                <b-list-group>
                    <b-list-group-item class="flex-column align-items-start">
                        <add-destinations :profile="profile"
                                          :destinationTypes="destinationTypes"
                                          :heading="'Add'"
                                          @data-changed="$emit('data-changed')">
                        </add-destinations>
                    </b-list-group-item>
                </b-list-group>
            </b-tab>
        </b-tabs>
    </div>
</template>

<script>
    import DestinationSearchList from "./destinationSearchList";
    import AddDestinations from "./addDestinations";
    export default {
        name: "destinationSidebar",

        props: ['profile'],

        data() {
            return {
                destinationTypes: []
            }
        },

        mounted() {
            this.getDestinationTypes(destinationTypesTemp => this.destinationTypes = destinationTypesTemp);
        },

        methods: {
            /**
             * Retrieves the different destination types from the backend.
             *
             * @param updateDestinationTypes    the list to be updated with the specified destination types.
             * @returns {Promise<any | never>}  the returned promise.
             */
            getDestinationTypes(updateDestinationTypes) {
                return fetch(`/v1/destinationTypes`, {
                    accept: "application/json"
                })
                    .then(response => response.json())
                    .then(updateDestinationTypes);
            },
        },

        components: {
            DestinationSearchList,
            AddDestinations
        }
    }
</script>