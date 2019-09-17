<template>
    <div>
        <b-row>
            <b-col cols="5">
                <active-quest-list
                        :quest-attempts="questAttempts"
                        :loading-results="loadingResults"
                        @quest-attempt-clicked="questAttempt => this.selectedQuestAttempt = questAttempt">
                </active-quest-list>
            </b-col>
            <b-col cols="7">
                <b-card class="w-100 h-100">
                    <quest-attempt-solve
                            :quest-attempt="selectedQuestAttempt"
                            @updated-quest-attempt="updateQuestAttempts">
                    </quest-attempt-solve>
                </b-card>
            </b-col>
        </b-row>
    </div>
</template>

<script>
    import QuestAttemptSolve from "./activeQuestSolve";
    import ActiveQuestList from "./activeQuestList";
    export default {
        name: "activeQuestPage",

        props: {
            profile: Object,
            refreshQuests: Boolean,
            newQuestAttempt: Object
        },

        mounted() {
            this.queryYourActiveQuests();
        },

        watch: {
            profile() {
                this.queryYourActiveQuests();
            },

            refreshQuests() {
                this.queryYourActiveQuests();
            },

            newQuestAttempt() {
                //If new quest attempt changes, user has selected a new quest attempt, add to list and select it
                this.updateQuestAttempts(this.newQuestAttempt)
            }
        },

        data() {
            return {
                selectedQuestAttempt: {},
                questAttempts: [],
                loadingResults: false
            }
        },

        components: {
            QuestAttemptSolve,
            ActiveQuestList
        },

        methods: {
            /**
             * Runs a query which searches through the quests in the database and returns only
             * quests started by the profile.
             */
            queryYourActiveQuests() {
                let self = this;
                if (this.profile.id !== undefined) {
                    this.loadingResults = true;
                    fetch(`/v1/quests/profiles/` + this.profile.id, {})
                        .then(function (response) {
                            if (!response.ok) {
                                throw response;
                            } else {
                                return response.json();
                            }
                        }).then(function (responseBody) {
                            self.questAttempts = responseBody;
                        }).catch(function (response) {
                            if (response.status > 404) {
                                self.showErrorToast([{message: "An unexpected error occurred"}]);
                            } else {
                                response.json().then(function(responseBody) {
                                    self.showErrorToast(responseBody);
                                });
                            }
                        });
                    this.loadingResults = false;
                }
            },


            /**
             * Updates the list of quest attempts when a selected attempt changes.
             *
             * @param questAttempt  the quest attempt to update.
             * @return              the id number of the quest attempt in the list of quest attempts.
             */
            updateQuestAttempts(questAttempt) {
                let foundIndex = this.questAttempts.findIndex(item => {
                    return item.id === questAttempt.id;
                });

                // If quest attempt found, replace it in the array, otherwise add to quest attempts.
                if (foundIndex !== -1) {
                    this.questAttempts.splice(foundIndex, 1, questAttempt);
                } else {
                    this.questAttempts.push(questAttempt);
                }

                this.selectedQuestAttempt = questAttempt;
            }
        }
    }
</script>