<template>
    <div>
        <p>Lat = {{lat}} Lon ={{lon}}</p>
        <p>{{error}}</p>
        <v-button @click="getCurrentLocation()">Get Current Location</v-button>
    </div>
</template>

<script>
    export default {
        name: "getLocationButton.vue",

        data: {
            error: '',
            lat:'',
            lon:''
        },

        methods:{
            /**
             * Gets the current location using geolocation
             */
            getCurrentLocation: function () {
                if(navigator.geolocation){
                    navigator.geolocation.getCurrentPosition(this.showPosition);
                }else{
                    this.error = "Geolocation is not supported.";

                }
            },

            /**
             * helper function of getCurrentLocation that saves lat/long to appropriate variables
             * @param position
             */
            showPosition:function (position) {
                this.lat = position.coords.latitude;
                this.lon = position.coords.longitude;
            }
        }

    };
</script>

<style scoped>

</style>