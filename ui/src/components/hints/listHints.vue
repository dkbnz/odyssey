<template>
    <b-col class="w-100 buttonMarginsTop">
        <b-list-group>
            <h4 v-if="hints.length > 0">Hints:</h4>
            <p v-if="!hints.length > 0">No Hints for this Objective</p>
            <b-list-group-item v-for="hint in getHints"
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
            <b-row no-gutters class="mt-2" v-if="hints.length > 0">
                <b-col cols="3">
                    <b-form-group
                            id="numItemsPast-field"
                            label-for="perPagePast">
                        <b-form-select :options="optionViews" id="perPage" size="sm" trim v-model="perPage">
                        </b-form-select>
                    </b-form-group>
                </b-col>
                <b-col cols="8">
                    <b-pagination
                            :per-page="perPage"
                            :total-rows="rows"
                            align="center"
                            aria-controls="my-table"
                            first-text="First"
                            last-text="Last"
                            size="sm"
                            v-model="currentPage"
                    ></b-pagination>
                </b-col>
            </b-row>
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

        data: function () {
            return {
                optionViews: [
                    {value: 1, text: "1"},
                    {value: 5, text: "5"},
                    {value: 10, text: "10"},
                    {value: 15, text: "15"}
                ],
                perPage: 5,
                currentPage: 1,
                startingRowNumber: 0,
                endingRowNumber: 5
            }
        },

        computed: {
            /**
             * The amount of rows in total for hints based on if the user is seeing their requested hints or all the
             * hints for that given objective.
             */
            rows() {
              if (this.solved) {
                  return this.objective.numberOfHints;
              }
              return this.hints.length;
            },


            /**
             * Gets the hints for the display list, if the user has solved the objective then they will see the list
             * of hints based on the parent component. Otherwise using a sliced list of all the hints if using the
             * requested hints for solving an objective.
             */
            getHints() {
                if (this.solved) {
                    return this.hints;
                }
                let currentHints = this.hints;
                currentHints = currentHints.slice(this.startingRowNumber, this.endingRowNumber);
                return currentHints;
            }
        },

        watch: {
            /**
             * Calls change hint view when the user changes the current page to view of hints.
             */
            currentPage() {
                this.changeHintView();
            },


            /**
             * Calls change hint view when the user changes the amount of hints per page.
             */
            perPage() {
                this.changeHintView();
            },
        },

        methods: {
            /**
             * Called when per page or current page changes and emits if the user has solved the objective to get new
             * hints from the backend. Or sets the starting and ending row number for the hints list for the hints you
             * have requested.
             */
            changeHintView() {
                if (this.solved) {
                    this.$emit('request-new-hints-page', this.currentPage, this.perPage)
                }
                this.startingRowNumber = this.perPage * (this.currentPage -1);
                this.endingRowNumber = this.startingRowNumber + this.perPage;
            },


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