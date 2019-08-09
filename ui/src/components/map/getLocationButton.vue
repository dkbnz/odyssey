<template>
    <div>
        <b-button @click="getCurrentLocation()"
                  variant="outline-primary"
                  block
                  size="sm"
                  class="buttonMarginsBottom">
            Use Current Location
        </b-button>
    </div>
</template>

<script>
    export default {
        name: "getLocationButton",

        methods: {
            /**
             * Gets the current location using geolocation.
             */
            getCurrentLocation: function () {
                if (navigator.geolocation) {
                    navigator.geolocation.getCurrentPosition(this.showPosition);
                } else {
                    this.$bvToast.toast('Unable to Get Current Location', {
                        title: `Geolocation Error`,
                        variant: "danger",
                        autoHideDelay: "3000",
                        solid: true
                    });
                }
            },


            /**
             * Helper function of getCurrentLocation that saves lat/long to appropriate variables.
             *
             * @param position  the current position on the map.
             */
            showPosition: function (position) {
                this.$emit('get-current-location', {
                    latitude: position.coords.latitude,
                    longitude: position.coords.longitude
                });
            }
        }
    }
</script>