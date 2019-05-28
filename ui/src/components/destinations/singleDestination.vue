<template>
    <div>
        <destination-gallery
            :destination="destination">
        </destination-gallery>
        {{destination}}
    </div>
</template>

<script>
    import DestinationGallery from "../photos/destinationGallery";

    export default {

        name: "singleDestination",
        data: function () {
            return {
            }
        },

        props: {
            destination: Object
        },

        mounted() {
        },

        methods: {
        },

        components: {
            DestinationGallery
        }
    }
</script>

<style scoped>

</style>