<template>
    <div>
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
        components: {
            DestinationGallery
        }
    }
</script>

<style scoped>

</style>