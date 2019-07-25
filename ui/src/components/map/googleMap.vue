<template>
    <div id="map" class="mapDiv">
    </div>

</template>

<script>

    export default {
        name: "googleMap.vue",
        props: {
            latitude: {
                default() {
                    return -41.272804;
                }
            },
            longitude: {
                default: 173.299565
            },
            zoom: {
                default: 5
            },
            dragMarkers: {
                default: false
            },

        },
        components: {

        },
        mounted() {
            this.initMap();

        },
        data() {
            return {
            }
        },
        methods: {
            initMap() {
                this.$map = new google.maps.Map(document.getElementById('map'), {
                    center: {lat: this.latitude, lng: this.longitude},
                    zoom: this.zoom
                })
                this.createMarker();
            },
            createMarker() {
                let marker = new google.maps.Marker({
                    position: {lat: -41.272804, lng: 173.299565},
                    map: this.$map,
                    title: "Centre of NZ"
                });
            }
        }

    }
</script>
<style scoped>

    .mapDiv {
        height: 50vh;
    }

</style>