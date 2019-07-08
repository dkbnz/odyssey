<template>
    <b-list-group>
        <b-list-group-item v-for="destination in (yourDestinations)" href="#" class="flex-column align-items-start">
            <div class="d-flex w-100 justify-content-between">
                <h5 class="mb-1">{{destination.name}}</h5>
                <small v-if="destination.public">Public</small>
                <small v-else>Private</small>
            </div>

            <p class="mb-1">
                {{destination.district}}
            </p>
            <p class="mb-1">
                {{destination.country}}
            </p>

        </b-list-group-item>
    </b-list-group>
</template>

<script>
    export default {
        name: "yourDestinations",
        props: ['profile'],
        data() {
            return {
                yourDestinations: [],
                destinationIndex: 0
            }
        },
        mounted() {
            this.getYourDestinations();
        },
        methods: {
            getYourDestinations() {
                fetch(`/v1/destinations/` + this.profile.id, {
                    accept: "application/json"
                })
                    .then(response => response.json())
                    .then(response => this.yourDestinations = response)
            }
        }
    }
</script>

<style scoped>

</style>