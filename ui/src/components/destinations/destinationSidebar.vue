<template>
    <div>
        <b-tabs content-class="mt-3">
            <b-tab title="Your Destinations" active>
                <your-destinations
                        :profile="profile"
                        :destinationTypes="destinationTypes"
                        @destination-click="destination => $emit('destination-click', destination)"
                        @data-changed="$emit('data-changed')"
                ></your-destinations>
            </b-tab>
            <b-tab title="Public Destinations">
                <public-destinations
                        :profile="profile"
                        :destinationTypes="destinationTypes"
                        @destination-click="destination => $emit('destination-click', destination)">
                </public-destinations>
            </b-tab>
        </b-tabs>
    </div>

</template>

<script>
    import YourDestinations from "./yourDestinations";
    import PublicDestinations from "./publicDestinations";
    export default {
        name: "destinationSidebar",
        props: ['profile', 'destinationTypes'],
        components: {PublicDestinations, YourDestinations}
    }
</script>

<style scoped>

</style>