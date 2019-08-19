<template>
    <div>
        <b-list-group-item v-for="destination in (foundDestinations)" href="#"
                           class="flex-column align-items-start"
                           @click="$emit('destination-click', destination)" :key="destination.id">
            <div class="d-flex w-100 justify-content-between">
                <h5 class="mb-1">{{destination.name}}</h5>

                <div v-if="!searchPublic">
                    <small v-if="destination.public">Public</small>
                    <small v-else>Private</small>
                </div>
            </div>

            <p class="mb-1">
                {{destination.district}}
            </p>
            <p class="mb-1">
                {{destination.country}}
            </p>

        </b-list-group-item>
    </div>
</template>

<script>
    export default {
        name: "activeQuestListItem"
    }
</script>

<style scoped>

</style>