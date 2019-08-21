<template>
    <div class="adminTripsContainer">
        <div v-if="showQuestAttempt">
            <active-quest-solve
                    :quest-attempt="selectedQuestAttempt"
                    @updated-quest-attempt="updateQuestAttempts"
                    @show-quest-attempt="showBoolean => this.showQuestAttempt = showBoolean">
            </active-quest-solve>
        </div>
        <b-tabs content-class="mt-3" fill v-model="tabIndex" v-else>
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
                        @start-quest-now="questAttempt => changeToActiveTab(questAttempt)"
                        @start-quest-later="updateQuestAttempts"
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
                tabIndex: 0,
                questAttempts: [],
                loadingResults: false,
                activeQuest: {
                    type: Object,
                    default: null
                }
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
            updateQuestAttempts(questAttempt) {
                let foundIndex = this.questAttempts.findIndex(x => x.id === questAttempt.id);

                // If quest attempt found, replace it in the array, otherwise add to quest attempts.
                if (foundIndex !== -1) {
                    this.questAttempts.splice(foundIndex, 1, questAttempt);
                } else {
                    this.questAttempts.push(questAttempt);
                }

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
            },


            /**
             * Switches to the 'active' page and refreshes the quest list.
             * The 'active' page has an index of 0.
             */
            changeToActiveTab(questAttempt) {
                this.tabIndex = 0;
                this.updateQuestAttempts(questAttempt);
                this.showQuestAttemptPage(questAttempt);
            }
        },

        components: {
            ActiveQuestList,
            ActiveQuestSolve,
            QuestList
        }
    }
</script>