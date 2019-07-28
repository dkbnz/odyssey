<template>
    <div>
        <div id="map" class="mapDiv">
        </div>
        <div id="legend">

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

                let legend = document.getElementById('legend');
                let div = document.createElement('div');
                div.innerHTML = '<img src="' + this.publicMarker + '"> ' + '<strong>Public Destination</strong>';
                legend.appendChild(div);
                div = document.createElement('div');
                div.innerHTML = '<img src="' + this.privateMarker + '"> ' + '<strong>Private Destination</strong>';
                legend.appendChild(div);

                this.$map.controls[google.maps.ControlPosition.LEFT_BOTTOM].push(legend);
            },

            /**
             * Creates all the markers for the map from the destinations array passed in props
             */
            createMarker() {
                let self = this;
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
                        title: (currentDestination.name + "\n" + currentDestination.district + "\n" + currentDestination.country),
                        icon: colour
                    });
                    marker.addListener("click", function(){
                        self.$map.setZoom(12);
                        self.$map.setCenter(marker.getPosition());
                        self.$emit('destination-click', currentDestination);
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

    #legend {
        font-family: Arial, sans-serif;
        background: #fff;
        padding: 10px;
        margin: 10px;
        border: 3px solid #000;
    }

</style>