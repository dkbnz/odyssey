<template>
    <b-col class="w-100 buttonMarginsTop">
        <b-list-group>
            <b-list-group-item v-for="hint in hints"
                               class="flex-column align-items-start"
                               :key="hint.message">
                <b-row>
                    <b-col class="w-100">
                        <p>{{hint.message}}</p>
                    </b-col>
                    <div class="vote-value">
                        {{hint.voteSum}}
                    </div>
                    <b-button @click="upVote"
                              class="mr-3"
                              size="sm">&uarr;
                    </b-button>
                    <b-button @click="downVote"
                              class="mr-3"
                              size="sm">&darr;
                    </b-button>
                </b-row>
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

            upVote() {

            },

            downVote() {

            },

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