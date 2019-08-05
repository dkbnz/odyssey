<template>
    <div>
        <div ref="map" class="mapDiv">
        </div>
        <div ref="legend" class="legend">
            <div>
                <img :src="publicMarker"><strong>Public Destination</strong>
            </div>
            <div>
                <img :src="privateMarker"><strong>Private Destination</strong>
            </div>
        </div>
    </div>

</template>

<script>

    export default {
        name: "googleMap.vue",

        props: {
            selectedDestination: Object,
            destinations: Array,
        },

        mounted() {
            this.initMap();
            this.createMarkers();
        },

        data() {
            return {
                initial: {
                    zoom: 5,
                    view: {
                        lat: -41.272804,
                        lng: 173.299565
                    }
                },
                dragMarkers: false,
                markerArray: [],
                publicMarker: "http://maps.google.com/mapfiles/ms/icons/green-dot.png",
                privateMarker: "http://maps.google.com/mapfiles/ms/icons/blue-dot.png"
            }
        },

        watch: {
            /**
             * Watches the destinations list for changes and when there are changes calls the create marker function
             */
            destinations: function () {
                //If no destinations to show, centre map.
                if (!this.destinations.length) {
                    this.centreMap();
                }
                this.clearMarkers();
                this.createMarkers();
            },


            /**
             * If selected destination changes, re-center map and calculate markers
             */
            selectedDestination: function (newDestination, oldDestination) {
                if (newDestination.id === oldDestination.id) {
                    // Destination has been edited
                    let newMarker = this.markerArray[newDestination.id];
                    newMarker.setIcon(newDestination.public ? this.publicMarker : this.privateMarker);
                    newMarker.setPosition({
                        lat: parseFloat(newDestination.latitude),
                        lng: parseFloat(newDestination.longitude)
                    });
                    newMarker.setTitle(newDestination.name + "\n" + newDestination.district + "\n"
                        + newDestination.country);
                } else if (!newDestination.id) {
                    // Destination has been deleted
                    this.markerArray[oldDestination.id].setMap(null);
                    this.centreMap();
                } else if (oldDestination.id) {
                    // Initial destination selection
                    this.markerArray[oldDestination.id].setAnimation(null);
                }
                this.focusOnSelectedDestination()
            }
        },

        methods: {
            /**
             * Initializes the map with given latitude and longitude and zoom
             */
            initMap() {
                this.$map = new google.maps.Map(this.$refs['map'], {
                    center: this.initial.view,
                    zoom: this.initial.zoom
                });

                this.$map
                    .controls[google.maps.ControlPosition.LEFT_BOTTOM]
                    .push(this.$refs['legend']);
            },


            /**
             * Creates all the markers for the map from the destinations array passed in props
             */
            createMarkers() {
                let self = this;

                for (let i = 0; i < this.destinations.length; i++) {
                    let currentDestination = this.destinations[i];
                    let markerIcon = currentDestination.public ? this.publicMarker : this.privateMarker;

                    let marker = new google.maps.Marker({
                        position: {lat: currentDestination.latitude, lng: currentDestination.longitude},
                        map: this.$map,
                        title: (currentDestination.name + "\n" + currentDestination.district + "\n"
                            + currentDestination.country),
                        icon: markerIcon
                    });

                    marker.addListener("click", function () {
                        // Emit destination to parent. This will set as selected destination and the transition will be
                        // handled by the watch attribute.
                        self.$emit('destination-click', currentDestination);
                    });
                    this.markerArray[currentDestination.id] = marker;
                }
            },


            /**
             * Focuses the map to the currently selected destination.
             */
            focusOnSelectedDestination() {
                this.markerArray[this.selectedDestination.id].setAnimation(google.maps.Animation.BOUNCE);
                this.$map.setZoom(10);
                this.$map.panTo({
                    lat: parseFloat(this.selectedDestination.latitude),
                    lng: parseFloat(this.selectedDestination.longitude)
                });
            },


            /**
             * Clear's the markers currently displayed on the map and clears the marker array
             */
            clearMarkers() {
                for (let key in this.markerArray) {
                    this.markerArray[key].setMap(null);
                }
                this.markerArray = [];
            },


            /**
             * Center's the map based on the initial zoom and view values.
             */
            centreMap() {
                this.$map.setZoom(this.initial.zoom);
                this.$map.panTo(this.initial.view);
            }
        }

    }
</script>

<style scoped>
    @import "../../css/googleMap.css";
</style>