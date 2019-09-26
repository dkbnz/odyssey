<template>
    <div class="w-100 buttonMarginsTop">
        <b-list-group v-if="!loading">
            <p v-if="!(objectiveHints.length > 0) && !loading">No Hints for this Objective</p>
            <b-list-group-item v-for="hint in getHints"
                               class="flex-column align-items-start"
                               draggable="false"
                               :key="hint.message">
                <b-form-row>
                    <b-col md="10" cols="8" class="d-inline text-wrap text-break">
                        {{hint.message}}
                    </b-col>
                    <b-col @click="vote(hint, true)" class="vote greenHover d-inline text-center"
                               :class="{'vote green': hint.vote != null && hint.vote.upVote}" v-if="solved">
                            <b-img
                                    height="15%"
                                    :src="assets['arrowUp']"></b-img>
                        </b-col>
                        <b-col v-if="solved" class="d-inline text-center">
                            {{roundVoteSum(hint.voteSum)}}
                        </b-col>
                        <b-col @click="vote(hint, false)" class="vote redHover d-inline text-center"
                               :class="{'vote red': hint.vote != null && !hint.vote.upVote}" v-if="solved">
                            <b-img
                                    height="15%"
                                    :src="assets['arrowDown']"></b-img>
                        </b-col>
                </b-form-row>

            </b-list-group-item>
            <b-row no-gutters class="mt-2" v-if="objective.numberOfHints > 5">
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
                        <b-button
                                v-if="solved"
                                class="no-wrap-text"
                                size="sm"
                                variant="primary"
                                @click="$emit('add-hint')">
                            Add Hint
                        </b-button>
                        <b-button size="sm"
                                  variant="primary"
                                  @click="showHintAlertModal = true"
                                  v-if="!solved && objective.numberOfHints > objectiveHints.length">
                            I need {{objectiveHints.length ? "another" : "a"}} hint!
                        </b-button>
                    </div>
                </b-col>
            </b-row>
        </b-list-group>
        <div v-else>
            <b-img alt="Loading" class="loading" :src="assets['loadingLogo']" height="30%"></b-img>
        </div>

        <b-modal v-model="showHintAlertModal" title="Are you sure?">
            <div>
                <p>Are you sure you want a hint? <br />
                    You will not gain as many points for solving this objective!</p>
            </div>
            <template v-slot:modal-footer>
                <b-col>
                    <b-button variant="warning" block @click="getAnotherHint">Show Me</b-button>
                </b-col>
                <b-col>
                    <b-button @click="showHintAlertModal = false" block>Cancel</b-button>
                </b-col>
            </template>
        </b-modal>

    </div>
</template>

<script>
    export default {
        name: "listHints",

        props: {
            objective: Object,
            profile: Object,
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
                loading: true,
                perPage: 5,
                currentPage: 1,
                startingRowNumber: 0,
                endingRowNumber: 5,
                showHintAlertModal: false,
                objectiveHints: []
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
              return this.objectiveHints.length;
            },


            /**
             * Gets the hints for the display list, if the user has solved the objective then they will see the list
             * of hints based on the parent component. Otherwise using a sliced list of all the hints if using the
             * requested hints for solving an objective.
             */
            getHints() {
                if (this.solved) {
                    return this.objectiveHints;
                }
                return this.objectiveHints.slice(this.startingRowNumber, this.endingRowNumber);
            }
        },

        watch: {
            /**
             * If per page changes, refetch hints.
             */
            perPage() {
                this.getPageHints();
            },

            /**
             * If current page changes, refetch hints.
             */
            currentPage() {
                this.getPageHints();
            }
        },

        mounted: function () {
            if(this.solved) {
                this.getPageHints();
            } else {
                this.getHintsUserHasSeen();
            }
        },

        methods: {
            /**
             * Uses a fetch method (POST) to downvote a hint. If there is an error for some reason, this is shown to the
             * user.
             * If the vote is changed, updates the hint's vote sum.
             *
             * @param hint      the hint being voted upon.
             * @param upvote    boolean value if a hint is being upvoted.
             */
            vote(hint, upvote) {
                let self = this;
                fetch('/v1/hints/' + hint.id + (upvote ? '/upvote/' : '/downvote/') + this.profile.id, {
                    method: 'POST'
                }).then(function (response) {
                    if (!response.ok) {
                        throw response;
                    } else {
                        return response.json();
                    }
                }).then(function (responseBody) {
                    let hintIndex = self.objectiveHints.findIndex(objectiveHint => objectiveHint.id === responseBody.id);
                    self.objectiveHints.splice(hintIndex, 1, responseBody);
                }).catch(function (response) {
                    self.savingTrip = false;
                    self.handleErrorResponse(response);
                });
            },


            /**
             * Round the number of votes for a given hint to a human readable format.
             *
             * @param votes     the number of votes for a given hint.
             */
            roundVoteSum(votes) {
                return votes >= 1000 ? (votes/1000).toFixed(1) + 'k' : votes;
            },


            /**
             * Gets the hints the user has requested for an unsolved objective from the backend.
             *
             * @returns {[]}        a list of hints.
             */
            getHintsUserHasSeen() {
                this.loading = true;
                let self = this;
                fetch("/v1/objectives/" + this.objective.id + "/hints/" + this.profile.id + "/seen", {
                }).then(function (response) {
                    if (!response.ok) {
                        throw response;
                    } else {
                        return response.json();
                    }
                }).then(function (responseBody) {
                    self.objectiveHints = responseBody;
                }).catch(function (response) {
                    self.handleErrorResponse(response);
                });
                this.loading = false;
            },


            /**
             * Gets the hints to display from the backend for all hints for an objective but paginated based on
             * current page and the per page variables.
             */
            getPageHints() {
                this.loading = true;
                let self = this;
                fetch(`/v1/objectives/` + this.objective.id +
                    `/hints/` + this.profile.id + `?pageNumber=` + (this.currentPage - 1) +
                    `&pageSize=` + this.perPage, {
                }).then(function (response) {
                    if (!response.ok) {
                        throw response;
                    } else {
                        return response.json();
                    }
                }).then(function (responseBody) {
                    self.objectiveHints = responseBody;
                }).catch(function (response) {
                    self.handleErrorResponse(response);
                });

                this.startingRowNumber = this.perPage * (this.currentPage -1);
                this.endingRowNumber = this.startingRowNumber + this.perPage;
                this.loading = false;
            },


            /**
             * Retrieves a hint for the currently viewed objective, is called after the user confirms they wish to
             * retrieve a hint for the given objective in the popup modal.
             */
            getAnotherHint() {
                let self = this;
                fetch("/v1/objectives/" + this.objective.id + "/hints/" + this.profile.id + "/new", {
                    accept: "application/json"
                }).then(function (response) {
                    if (!response.ok) {
                        throw response;
                    } else {
                        return response.json();
                    }
                }).then(function (responseBody) {
                    if (responseBody) {
                        self.objectiveHints.push(responseBody);
                    }
                    self.showHintAlertModal = false;
                }).catch(function (response) {
                    self.handleErrorResponse(response);
                });
            }
        }
    }
</script>