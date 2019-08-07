<template>
    <div>
        <b-button @click="getCurrentLocation()" variant="info">Get Current Location</b-button>
    </div>
</template>

<script>
    export default {
        name: "getLocationButton",
        methods:{
            /**
             * Gets the current location using geolocation
             */
            getCurrentLocation: function () {
                if(navigator.geolocation){
                    navigator.geolocation.getCurrentPosition(this.showPosition);
                }else{
                    this.$bvToast.toast('Unable to Get Current Location', {
                        title: `Geolocation Error`,
                        variant: "danger",
                        autoHideDelay: "3000",
                        solid: true
                    });
                }
            },

            /**
             * helper function of getCurrentLocation that saves lat/long to appropriate variables
             * @param position
             */
            showPosition:function (position) {
                this.$emit('get-current-location', {
                    lat: position.coords.latitude,
                    lng: position.coords.longitude
                });
            }
        }

    };
</script>

<style scoped>

</style>