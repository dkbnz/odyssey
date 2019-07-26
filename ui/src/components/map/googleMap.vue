<template>
    <div>
        <div id="map" class="mapDiv">
        </div>
    </div>

</template>

<script>

    export default {
        name: "googleMap.vue",
        props: {
            destinations: {
                type: Array
            },
            latitude: {
                type: Number,
                default: -41.272804
            },
            longitude: {
                type: Number,
                default: 173.299565
            },
            zoom: {
                type: Number,
                default: 5
            },
            dragMarkers: {
                default: false
            }
        },
        components: {
        },
        mounted() {
            this.initMap();
            this.createMarker();
        },
        data() {
            return {
                markerArray: [],
                publicMarker: "http://maps.google.com/mapfiles/ms/icons/green-dot.png",
                privateMarker: "http://maps.google.com/mapfiles/ms/icons/blue-dot.png"
            }
        },
        watch: {
            /**
             * Watches the destinations list for changes and when there are changes calls the create marker function
             */
            destinations: function() {
                this.createMarker();
            }
        },
        methods: {
            /**
             * Initializes the map with given latitude and longitude and zoom
             */
            initMap() {
                this.$map = new google.maps.Map(document.getElementById('map'), {
                    center: {lat: this.latitude, lng: this.longitude},
                    zoom: this.zoom
                });
            },

            /**
             * Creates all the markers for the map from the destinations array passed in props
             */
            createMarker() {
                for (let i = 0; i < this.destinations.length; i++) {

                    let currentDestination = this.destinations[i];
                    let colour;
                    if (currentDestination.public) {
                        colour = this.publicMarker;
                    } else {
                        colour = this.privateMarker;
                    }
                    let marker = new google.maps.Marker({
                        position: {lat: currentDestination.latitude, lng: currentDestination.longitude},
                        map: this.$map,
                        title: currentDestination.name,
                        icon: colour
                    });
                    this.markerArray.push(marker);
                }
            },


            /**
             * Clear's the markers currently displayed ont he map and clears the marker array
             */
            clearMarkers() {
                for (let i = 0; i < this.markerArray.length-1; i++) {
                    this.markerArray[i].setMap(null);
                }
                this.markerArray = [];
            }
        }

    }
</script>
<style scoped>

    .mapDiv {
        height: 50vh;
    }

</style>