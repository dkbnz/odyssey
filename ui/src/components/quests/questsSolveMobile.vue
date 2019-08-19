<template>
    <div class="adminTripsContainer">
        <b-tabs content-class="mt-3" fill>
            <b-tab title="Active Quests" @click="refreshQuests = !refreshQuests" active>
                <quest-list
                        :profile="profile"
                        :active-quests="true"
                        :refresh-quests="refreshQuests"
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
    export default {
        name: "questsSolveMobile",

        props: {
            profile: Object,
        },

        data() {
            return {
                refreshQuests: false
            }
        },

        components: {
            QuestList
        }
    }
</script>