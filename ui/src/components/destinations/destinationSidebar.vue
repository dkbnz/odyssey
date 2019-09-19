<template>
    <div>
        <b-tabs content-class="mt-3">
            <b-tab title="Your Destinations" active @click="$emit('destination-search', yourFoundDestinations)">
                <destination-search-list
                        :search-public="false"
                        :profile="profile"
                        @destination-click="destination => $emit('destination-click', destination)"
                        @destination-search="foundDestinations => foundDestinationUpdate(false, foundDestinations)"
                        @destination-reset="$emit('destination-reset')">
                </destination-search-list>
            </b-tab>
            <b-tab title="Public Destinations" @click="$emit('destination-search', foundPublicDestinations)">
                <destination-search-list
                        :search-public="true"
                        :profile="profile"
                        @destination-click="destination => $emit('destination-click', destination)"
                        @destination-search="foundDestinations => foundDestinationUpdate(true, foundDestinations)"
                        @destination-reset="$emit('destination-reset')">
                </destination-search-list>
            </b-tab>
            <b-tab title="Add" @click="$emit('destination-search', null)">
                <b-list-group>
                    <b-list-group-item class="flex-column align-items-start">
                        <add-destinations :profile="profile"
                                          :destinationTypes="destinationTypes"
                                          :heading="'Add'"
                                          :input-destination="inputDestination">
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

        props: {
            profile: Object,
            inputDestination: Object
        },

        data() {
            return {
                destinationTypes: [],
                foundPublicDestinations: [],
                yourFoundDestinations: []
            }
        },

        methods: {
            /**
             * Updates the destinations to be displayed on the destinations page. These are destinations to be displayed
             * on the map.
             *
             * @param publicDestinations   boolean value that is true if public destinations are to be displayed, false
             *                             otherwise.
             * @param destinations         the destinations found in the search.
             */
            foundDestinationUpdate(publicDestinations, destinations) {
                if (publicDestinations) {
                    this.foundPublicDestinations = destinations;
                    this.$emit('destination-search', this.foundPublicDestinations);
                } else {
                    this.yourFoundDestinations = destinations;
                    this.$emit('destination-search', this.yourFoundDestinations);
                }
            }
        },

        components: {
            DestinationSearchList,
            AddDestinations
        }
    }
</script>