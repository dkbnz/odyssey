<template>
    <div class="adminTripsContainer">
        <div v-if="showQuestAttempt">
            <b-button @click="showQuestAttempt = false" class="buttonMarginsBottom">Back</b-button>
            <quest-attempt-solve :quest-attempt="selectedQuestAttempt" @updated-quest-attempt="questAttempt => this.selectedQuestAttempt = questAttempt"></quest-attempt-solve>
        </div>
        <b-tabs content-class="mt-3" fill v-else>
            <b-tab title="Active Quests" @click="refreshQuests = !refreshQuests" active>
                <quest-list
                        :profile="profile"
                        :active-quests="true"
                        :refresh-quests="refreshQuests"
                        @quest-attempt-click="showQuestAttemptPage"
                ></quest-list>
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
    import QuestAttemptSolve from "./questAttemptSolve";
    export default {
        name: "questsSolveMobile",

        props: {
            profile: Object,
        },

        data() {
            return {
                refreshQuests: false,
                showQuestAttempt: false,
                selectedQuestAttempt: {}
            }
        },

        methods: {
            showQuestAttemptPage(questAttempt) {
                this.showQuestAttempt = true;
                this.selectedQuestAttempt = questAttempt;
            }
        },

        components: {
            QuestAttemptSolve,
            QuestList
        }
    }
</script>