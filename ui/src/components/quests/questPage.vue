<template>
    <div v-if="profile.length !== 0" :class="containerClass">
        <!--Shows tabs for the quest page-->
        <navbar-main v-bind:profile="profile" v-if="!adminView"></navbar-main>
        <div class="containerMain">
            <h1 class="page-title">Quests</h1>
            <p class="page-title">
                <i>Here you can view and create Quests!</i>
            </p>
            <b-card>
                <b-tabs content-class="mt-3">
                    <b-tab title="Active Quests" @click="">
                        <quest-list
                                :profile="profile"
                                :active-quests="true"
                        ></quest-list>
                    </b-tab>
                    <b-tab title="Available Quests" @click="" active>
                        <quest-list
                                :profile="profile"
                                :available-quests="true"
                        ></quest-list>
                    </b-tab>
                    <b-tab title="Your Quests" @click="">
                        <quest-list
                                :profile="profile"
                                :your-quests="true"
                        ></quest-list>
                    </b-tab>
                </b-tabs>
            </b-card>
            <footer-main></footer-main>
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
            },
        },

        data() {
            return {
                selectedDestination: {},
                showDestinations: false,
                refreshObjectives: false
            }
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
        },


        components: {
            QuestList,
            FooterMain,
            UnauthorisedPromptPage,
            NavbarMain,
        }
    }
</script>