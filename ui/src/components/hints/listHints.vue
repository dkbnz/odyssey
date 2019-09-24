<template>
    <b-col class="w-100 buttonMarginsTop">
        <b-list-group>
            <h4 v-if="hints.length > 0">Hints:</h4>
            <p v-if="!hints.length > 0">No Hints for this Objective</p>
            <b-list-group-item v-for="hint in hints"
                               class="flex-column align-items-start"
                               :key="hint.message">
                <b-row>
                    <b-col class="w-100">
                        <p>{{hint.message}}</p>
                    </b-col>
                    <b-row v-if="solved" class="vote" style="vertical-align: middle;">
                        <b-col cols="4">
                            <div>
                                {{hint.voteSum}}
                            </div>
                        </b-col>
                        <b-col cols="2">
                            <b-img class="vote blue" height="30%" :src="assets['arrowUp']" @click="upVote(hint)"></b-img>
                        </b-col>
                        <b-col cols="2">
                            <b-img class="vote red" height="30%" :src="assets['arrowDown']" @click="downVote(hint)"></b-img>
                        </b-col>
                    </b-row>
                </b-row>
            </b-list-group-item>
            <b-row no-gutters class="mt-2">
                <b-col>
                    <div class="float-right">
                        <b-button class="no-wrap-text" size="sm" variant="primary" @click="addHint" v-if="solved">Add Hint</b-button>
                        <b-button size="sm" variant="primary"
                                  @click="requestHint"
                                  v-if="!solved && objective.numberOfHints > objective.hints.length">
                            I need {{hints.length ? "another" : "a"}} hint!
                        </b-button>
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
             * Uses a fetch method (POST) to upvote a hint. If there is an error for some reason, this is shown to the
             * user.
             * If the vote is changed, updates the hint's vote sum
             *
             * @param hint      the hint being voted upon
             */
            upVote(hint) {
                let hintId = hint.id;
                let self = this;
                fetch('/v1/hints/' + hintId + '/upvote/' + this.profile.id, {
                    method: 'POST'
                }).then(function (response) {
                    if (!response.ok) {
                        throw response;
                    } else {
                        return response.json();
                    }
                }).then(function (responseBody) {
                    hint.voteSum = responseBody.voteSum;
                }).catch(function (response) {
                    self.savingTrip = false;
                    self.handleErrorResponse(response);
                });
            },


            /**
             * Uses a fetch method (POST) to downvote a hint. If there is an error for some reason, this is shown to the
             * user.
             * If the vote is changed, updates the hint's vote sum
             *
             * @param hint      the hint being voted upon
             */
            downVote(hint) {
                let hintId = hint.id;
                let self = this;
                fetch('/v1/hints/' + hintId + '/downvote/' + this.profile.id, {
                    method: 'POST'
                }).then(function (response) {
                    if (!response.ok) {
                        throw response;
                    } else {
                        return response.json();
                    }
                }).then(function (responseBody) {
                    hint.voteSum = responseBody.voteSum;
                }).catch(function (response) {
                    self.savingTrip = false;
                    self.handleErrorResponse(response);
                });
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