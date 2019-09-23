<template>
    <b-col class="w-100 buttonMarginsTop">
        <b-list-group>
            <h4 v-if="hints.length > 0">Hints:</h4>
            <h4 v-if="!hints.length > 0">No Hints for this Objective</h4>
            <b-list-group-item v-for="hint in hints"
                               class="flex-column align-items-start"
                               :key="hint.message">
                <b-col class="w-100">
                    <p>{{hint.message}}</p>
                </b-col>
            </b-list-group-item>
            <b-row no-gutters class="mt-2">
                <b-col>
                    <div class="float-right">
                        <b-button class="no-wrap-text" size="sm" variant="primary" @click="addHint" v-if="solved">Add Hint</b-button>
                        <b-button size="sm" variant="primary" @click="requestHint" v-if="!solved">I need {{hints.length ? "another" : "a"}} hint!</b-button>
                    </div>
                </b-col>
            </b-row>
        </b-list-group>
    </b-col>
</template>

<script>
    import CreateHint from './createHint'
    export default {
        name: "listHints",

        props: {
            objective: Object,
            profile: Object,
            hints: Array,
            solved: false
        },

        methods: {

            /**
             * Emits to the above layer to show the create hint instead of the main group
             */
            addHint() {
                this.$emit('showAddHint');
            },


            /**
             * Emits to the above layer to show the request a hint confirmation modal.
             */
            requestHint() {
                this.$emit('hintRequested');
            }
        },

        components: {CreateHint}
    }
</script>

<style scoped>

</style>