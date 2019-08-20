<template>
    <b-list-group>
        <h3>{{quest.title}}</h3>
        <b-list-group-item v-for="objective in quest.objectives" href="#"
                           class="flex-column align-items-start"
                           :key="objective.id"
                           draggable="false">
            <div class="d-flex w-100 justify-content-between">
                <p class="mb-1 mobile-text font-weight-bold">{{objective.riddle}}</p>
            </div>
            <p class="mb-1 mobile-text">
                {{objective.destination.name}}
            </p>
        </b-list-group-item>

    </b-list-group>
</template>

<script>
    export default {
        name: "completedQuestDetails",

        props: {
            quest: Object,
        },

    }
</script>

<style scoped>

</style>