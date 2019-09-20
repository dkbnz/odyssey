<template>
    <b-col class="w-100 buttonMarginsTop">
        <b-list-group>
            <b-list-group-item v-for="hint in hints"
                               class="flex-column align-items-start"
                               :key="hint.message">
                <b-col class="w-100">
                    <p>{{hints.message}}</p>
                </b-col>
            </b-list-group-item>
            <b-row no-gutters>
                <b-col>
                    <div class="float-right">
                        <b-button class="no-wrap-text" size="sm" variant="primary" @click="addHint" v-if="solved">Add Hint</b-button>
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
            hints: Object,
            solved: false
        },

        methods: {
            /**
             * Emits to the above layer to show the create hint instead of the main group
             */
            addHint() {
                this.$emit('showAddHint');
            }
        },

        components: {CreateHint}
    }
</script>

<style scoped>

</style>