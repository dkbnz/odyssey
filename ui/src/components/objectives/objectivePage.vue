<template>
    <div v-if="profile.length !== 0" :class="containerClass">
        <!--Shows tabs for objective page-->
        <navbar-main v-bind:profile="profile" v-if="!adminView"></navbar-main>
        <div class="containerMain">
            <h1 class="page-title">Objectives</h1>
            <p class="page-title">
                <i>Here you can view and create Objectives!</i>
            </p>
            <b-row>
                <b-col cols="8">
                    <b-card>
                        <b-tabs content-class="mt-3">
                            <b-tab title="Available Objectives" @click="refreshObjectives = !refreshObjectives" active>
                                <objective-list
                                        :profile="profile"
                                        :adminView="adminView"
                                        :yourObjectives="false"
                                        :refreshObjectives="refreshObjectives"
                                        :selectedDestination="selectedDestination"
                                        @destination-select="showDestinationsSidebar"
                                        @hide-destinations="hideDestinationsSidebar"
                                ></objective-list>
                            </b-tab>
                            <b-tab title="Your Objectives" @click="refreshObjectives = !refreshObjectives">
                                <objective-list
                                        :profile="profile"
                                        :adminView="adminView"
                                        :yourObjectives="true"
                                        :refreshObjectives="refreshObjectives"
                                        :selectedDestination="selectedDestination"
                                        @destination-select="showDestinationsSidebar"
                                        @hide-destinations="hideDestinationsSidebar"
                                ></objective-list>
                            </b-tab>
                        </b-tabs>
                    </b-card>
                </b-col>
                <b-col>
                    <b-card>
                        <found-destinations
                                v-if="showDestinations"
                                :search-public="true"
                                :profile="profile"
                                @destination-click="destination => this.selectedDestination = destination">
                        </found-destinations>
                    </b-card>
                </b-col>
            </b-row>
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
    import FoundDestinations from "../destinations/destinationSearchList";
    import ObjectiveList from "./objectiveList";
    import FooterMain from "../helperComponents/footerMain";
    export default {
        name: "objectivePage",

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
            FooterMain,
            ObjectiveList,
            FoundDestinations,
            UnauthorisedPromptPage,
            NavbarMain,
        }
    }
</script>