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
            return {}
        },
        watch: {
            destinations: function() {
                this.createMarker();
            }
        },
        methods: {
            initMap() {
                this.$map = new google.maps.Map(document.getElementById('map'), {
                    center: {lat: this.latitude, lng: this.longitude},
                    zoom: this.zoom
                });
            },
            createMarker() {
                for (let i = 0; i < this.destinations.length-1; i++) {
                    let marker = new google.maps.Marker({
                        position: {lat: this.destinations[i].latitude, lng: this.destinations[i].longitude},
                        map: this.$map,
                        title: this.destinations[i].name});
                }
            }
        }

    }
</script>
<style scoped>

    .mapDiv {
        height: 50vh;
    }

</style>