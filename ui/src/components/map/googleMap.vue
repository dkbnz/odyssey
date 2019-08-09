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
            <div v-if="destinations === null">
                <img :src="addingMarker"><strong>New Destination</strong>
            </div>
        </div>
    </div>

</template>

<script>

    export default {
        name: "googleMap.vue",

        props: {
            destinationToAdd: Object,
            selectedDestination: Object,
            destinations: {
                default: null
            },
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
                privateMarker: "http://maps.google.com/mapfiles/ms/icons/blue-dot.png",
                addingMarker: "http://maps.google.com/mapfiles/ms/icons/red-dot.png",
                markerToAdd: false
            }
        },

        watch: {
            /**
             * Watches the destinations list for changes and when there are changes calls the create marker function
             */
            destinations: function () {
                // If marker has been instantiated
                if (this.markerToAdd !== false) {
                    if (this.destinations !== null) {
                        // If we click out of the 'add' sideBar tab
                        this.removeMarkerToAdd();
                    } else {
                        // If we click into the 'add' sideBar tab
                        this.placeMarkerToAdd();
                    }
                }

                if (this.destinations !== null) {
                    this.clearMarkers();
                    this.createMarkers();
                }
            },


            /**
             * If selected destination changes, re-center map and calculate markers
             */
            selectedDestination: function (newDestination, oldDestination) {
                if (newDestination.id === oldDestination.id) {
                    // Destination has been edited
                    let newMarker = this.markerArray[newDestination.id];
                    newMarker.setIcon(newDestination.public ? this.publicMarker : this.privateMarker);
                    newMarker.setPosition(this.parseCoordinates(newDestination));
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
            },


            /**
             * If the destination to add changes, add/move a marker for this destination on the map.
             */
            destinationToAdd: {
                handler() {
                    this.addDestinationToAdd()
                },
                // Signals that the watcher needs to check all children for changes.
                deep: true
            }


        },

        methods: {
            /**
             * Initializes the map with given latitude and longitude and zoom
             */
            initMap() {
                let self = this;

                this.$map = new google.maps.Map(this.$refs['map'], {
                    center: this.initial.view,
                    zoom: this.initial.zoom
                });

                this.$map
                    .controls[google.maps.ControlPosition.LEFT_BOTTOM]
                    .push(this.$refs['legend']);

                // Place destination on the map if we are adding it.
                this.addDestinationToAdd();

                google.maps.event.addListener(this.$map, 'click', function (event) {
                    if (self.destinations === null) {
                        //Get the location that the user clicked.
                        let clickedLocation = event.latLng;
                        self.destinationToAdd.latitude = clickedLocation.lat();
                        self.destinationToAdd.longitude = clickedLocation.lng();
                    }
                });
            },


            /**
             * Will place a destination on the map if it is not null.
             * If the marker exists, it will update the position.
             */
            addDestinationToAdd() {
                if (this.destinationToAdd.latitude !== null && this.destinationToAdd.longitude !== null) {
                    if (this.markerToAdd === false) {
                        //Create the marker.
                        this.markerToAdd = this.placeDestinationMarker(this.destinationToAdd);
                    } else {
                        //Marker has already been added, so just change its location.
                        this.updateDestinationMarker(this.destinationToAdd);
                    }
                    this.markerToAdd.setIcon(this.addingMarker);
                    this.$map.panTo(this.parseCoordinates(this.destinationToAdd));
                }
            },


            /**
             * Removes the red marker from the map.
             */
            removeMarkerToAdd() {
                this.markerToAdd.setMap(null);
            },


            /**
             * Adds the red marker to the map.
             */
            placeMarkerToAdd() {
                this.markerToAdd.setMap(this.$map);
            },


            /**
             * Take a destination and return the latitude and longitude of said destination
             * in the format that google requires.
             */
            parseCoordinates(destination) {
                return {
                    lat: parseFloat(destination.latitude),
                    lng: parseFloat(destination.longitude)
                }
            },


            /**
             * Creates all the markers for the map from the destinations array passed in props
             */
            createMarkers() {
                let self = this;
                if (this.destinations === null) {
                    return;
                }

                for (let i = 0; i < this.destinations.length; i++) {
                    let currentDestination = this.destinations[i];

                    let marker = this.placeDestinationMarker(currentDestination);

                    marker.addListener("click", function () {
                        // Emit destination to parent. This will set as selected destination and the transition will be
                        // handled by the watch attribute.
                        self.$emit('destination-click', currentDestination);
                    });
                    this.markerArray[currentDestination.id] = marker;
                }
            },


            /**
             * Given a destination, places a destination marker on the map.
             */
            placeDestinationMarker(destination) {
                return new google.maps.Marker({
                    position: this.parseCoordinates(destination),
                    map: this.$map,
                    title: (destination.name + "\n" + destination.district + "\n"
                        + destination.country),
                    icon: destination.public ? this.publicMarker : this.privateMarker
                });
            },


            /**
             * Given a destination, updates a destination marker on the map.
             */
            updateDestinationMarker(destination) {
                this.markerToAdd.setPosition(this.parseCoordinates(destination));
                this.markerToAdd.setTitle(destination.name + "\n" + destination.district + "\n"
                    + destination.country);
            },


            /**
             * Focuses the map to the currently selected destination.
             */
            focusOnSelectedDestination() {
                this.markerArray[this.selectedDestination.id].setAnimation(google.maps.Animation.BOUNCE);
                this.$map.setZoom(10);
                this.$map.panTo(this.parseCoordinates(this.selectedDestination));
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