<template>
    <div v-if="profile.length !== 0" :class="containerClass">
        <!--Shows tabs for the quest page-->
        <navbar-main v-bind:profile="profile" v-if="!adminView"></navbar-main>
        <div class="containerMain d-none d-lg-block">
            <h1 class="page-title">Quests</h1>
            <p class="page-title">
                <i>Here you can view and create Quests!</i>
            </p>
            <b-card>
                <b-tabs content-class="mt-3">
                    <b-tab title="Active Quests" @click="refreshQuests = !refreshQuests" active>
                        <active-quest-page
                                :profile="profile">

                        </active-quest-page>
                    </b-tab>
                    <b-tab title="Available Quests" @click="refreshQuests = !refreshQuests">
                        <quest-list
                                :profile="profile"
                                :available-quests="true"
                                :refresh-quests="refreshQuests"
                        ></quest-list>
                    </b-tab>
                    <b-tab title="Your Quests" @click="refreshQuests = !refreshQuests">
                        <quest-list
                                :profile="profile"
                                :your-quests="true"
                                :refresh-quests="refreshQuests"
                        ></quest-list>
                    </b-tab>
                </b-tabs>
            </b-card>
            <footer-main></footer-main>
        </div>
        <div class="show-only-mobile">
            <quests-solve-mobile
                    :profile="profile">

            </quests-solve-mobile>
        </div>
    </div>
    <div v-else>
        <unauthorised-prompt-page></unauthorised-prompt-page>
    </div>
</template>

<script>
    import NavbarMain from "../helperComponents/navbarMain";
    import UnauthorisedPromptPage from "../helperComponents/unauthorisedPromptPage";
    import FooterMain from "../helperComponents/footerMain";
    import QuestList from "./questList";
    import QuestsSolveMobile from "./activeQuestPageMobile";
    import QuestAttemptSolve from "./activeQuestSolve";
    import ActiveQuestPage from "./activeQuestPage";
    export default {
        name: "questPage",

        props: {
            profile: Object,
            containerClass: {
                default: function() {
                    return null;
                }
            },
            adminView: {
                default: function() {
                    return false;
                }
            }
        },

        data() {
            return {
                selectedDestination: {},
                showDestinations: false,
                refreshObjectives: false,
                refreshQuests: false,
                showQuestAttemptSolve: false,
                selectedQuestAttempt: {}
            }
        },

        mounted() {
            this.testActiveProfiles();
        },

        methods: {
            /**
             * Shows the destination searching sidebar.
             */
            showDestinationsSidebar() {
                this.showDestinations = true;
            },

            /**
             * Hides the destination searching sidebar.
             */
            hideDestinationsSidebar() {
                this.showDestinations = false;
                this.selectedDestination = {};
            },

            //TODO: Cam
            testActiveProfiles() {
                fetch('/v1/quests/9/profiles', {
                    method: 'GET',
                    headers: {'content-type': 'application/json'},
                })
            },


            /**
             * Displays the quest solve page.
             */
            showQuestAttemptPage(questAttempt) {
                this.showQuestAttemptSolve = true;
                this.selectedQuestAttempt = questAttempt;
            }
        },

        components: {
            ActiveQuestPage,
            QuestAttemptSolve,
            QuestsSolveMobile,
            QuestList,
            FooterMain,
            UnauthorisedPromptPage,
            NavbarMain,
        }
    }
</script>