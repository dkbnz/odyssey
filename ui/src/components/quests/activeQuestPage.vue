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
                            @updated-quest-attempt="updateQuestAttempt">
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
            profile: Object
        },

        mounted() {
            this.queryYourActiveQuests();
        },

        watch: {
            profile() {
                this.queryYourActiveQuests();
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
             *
             * @returns {Promise<Response | never>}
             */
            queryYourActiveQuests() {
                if (this.profile.id !== undefined) {
                    this.loadingResults = true;
                    return fetch(`/v1/quests/profiles/` + this.profile.id, {})
                        .then(response => response.json())
                        .then((data) => {
                            this.questAttempts = data;
                            this.loadingResults = false;
                        })
                }
            },


            /**
             * Updates the list of quest attempts when a selected attempt changes.
             *
             * @param questAttempt  the quest attempt to update.
             */
            updateQuestAttempt(questAttempt) {
                let foundIndex = this.questAttempts.findIndex(x => x.id === questAttempt.id);
                this.questAttempts.splice(foundIndex, 1, questAttempt);
                this.selectedQuestAttempt = questAttempt;
            }
        }
    }
</script>