<template>
    <div>
        <destination-gallery
            :destination="destination"
            :profile="profile"
            :userProfile="userProfile">
        </destination-gallery>
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
            destination: Object,
            profile: Object,
            userProfile: {
                default: function() {
                    return this.profile;
                }
            },
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