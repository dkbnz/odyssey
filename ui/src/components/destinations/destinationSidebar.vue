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
        props: ['profile', 'destinationTypes'],
        components: {DestinationSearchList, AddDestinations}
    }
</script>

<style scoped>

</style>