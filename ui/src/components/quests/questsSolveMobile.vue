<template>
    <div>
        <b-tabs content-class="mt-3" fill>
            <b-tab title="Active Quests" @click="refreshQuests = !refreshQuests" active>
                <quest-list
                        :profile="profile"
                        :active-quests="true"
                ></quest-list>
            </b-tab>
            <b-tab title="Available Quests" @click="refreshQuests = !refreshQuests">
                <quest-list
                        :profile="profile"
                        :available-quests="true"
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

        components: {
            QuestList
        }
    }
</script>