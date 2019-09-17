<template>
    <div v-if="profile.length !== 0">
        <b-row>
            <b-col cols="8">
                <b-card>
                    <objective-list
                            :profile="profile"
                            :adminView="adminView"
                            :yourObjectives="true"
                            :refreshObjectives="refreshObjectives"
                            :selectedDestination="selectedDestination"
                            @destination-select="showDestinationsSidebar"
                            @hide-destinations="hideDestinationsSidebar"
                    ></objective-list>
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
            adminView: {
                default: function () {
                    return false;
                }
            },
            refreshObjectives: Boolean
        },

        data() {
            return {
                selectedDestination: {},
                showDestinations: false
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