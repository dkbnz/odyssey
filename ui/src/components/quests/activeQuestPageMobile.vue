<template>
    <div class="adminTripsContainer">
        <div v-if="showQuestAttempt">
            <active-quest-solve
                    :quest-attempt="selectedQuestAttempt"
                    @updated-quest-attempt="updateQuestAttempt"
                    @show-quest-attempt="showQuestAttempt => this.showQuestAttempt = showQuestAttempt">
            </active-quest-solve>
        </div>
        <b-tabs content-class="mt-3" fill v-else>
            <b-tab title="Active Quests" @click="refreshQuests = !refreshQuests" active>
                <active-quest-list
                        :quest-attempts="questAttempts"
                        :loading-results="loadingResults"
                        @quest-attempt-clicked="showQuestAttemptPage"
                ></active-quest-list>
            </b-tab>
            <b-tab title="Available Quests" @click="refreshQuests = !refreshQuests">
                <quest-list
                        :profile="profile"
                        :available-quests="true"
                        :refresh-quests="refreshQuests"
                ></quest-list>
            </b-tab>
        </b-tabs>

    </div>
</template>

<script>
    import QuestList from "./questList";
    import ActiveQuestSolve from "./activeQuestSolve";
    import ActiveQuestList from "./activeQuestList";

    export default {
        name: "activeQuestPageMobile",

        props: {
            profile: Object,
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
                refreshQuests: false,
                showQuestAttempt: false,
                selectedQuestAttempt: {},
                questAttempts: [],
                loadingResults: false
            }
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
            },


            /**
             * Displays the quest attempt page and sets the required quest attempt to the emitted one.
             *
             * @param questAttempt  the specified quest attempt.
             */
            showQuestAttemptPage(questAttempt) {
                this.showQuestAttempt = true;
                this.selectedQuestAttempt = questAttempt;
            }
        },

        components: {
            ActiveQuestList,
            ActiveQuestSolve,
            QuestList
        }
    }
</script>