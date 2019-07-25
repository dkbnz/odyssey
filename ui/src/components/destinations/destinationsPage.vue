<template>
    <div v-if="profile.length !== 0" :class="containerClass">
        <!--Shows tabs for destination page-->
        <nav-bar-main v-bind:profile="profile" v-if="!adminView"></nav-bar-main>
        <div class="containerMain">
            <h1 class="page-title">Destinations</h1>
            <p class="page-title">
                <i>Here you can add destinations, search destinations or view destinations on a map!</i>
            </p>
            <b-row>
                <b-col cols="8">
                    <b-card ref="maps">
                        <google-map></google-map>
                    </b-card>
                    <b-alert
                            :show="dismissCountDown"
                            @dismiss-count-down="countDownChanged"
                            @dismissed="dismissCountDown=0"
                            dismissible
                            variant="success">
                        <p>Destination Successfully Deleted</p>
                        <b-progress
                                :max="dismissSecs"
                                :value="dismissCountDown"
                                height="4px"
                                variant="success"
                        ></b-progress>
                    </b-alert>
                    <b-card
                            :header="selectedDestination.name"
                            style="margin-top: 10px">
                        <div v-if="selectedDestination.name === undefined">
                            No Destination Selected
                        </div>
                        <single-destination
                                :key="refreshSingleDestination"
                                :destination="selectedDestination"
                                :destination-types="destinationTypes"
                                :profile="profile"
                                @destination-saved="refreshDestinations"
                                @destination-deleted="destinationDeleted">
                        </single-destination>
                    </b-card>
                </b-col>
                <b-col>
                    <b-card>
                        <destination-sidebar
                                :profile="profile"
                                @destination-click="destination => this.selectedDestination = destination"
                                :key="refreshDestinationData"
                                @data-changed="$emit('data-changed')"
                        ></destination-sidebar>
                    </b-card>
                </b-col>
            </b-row>
            <footer-main></footer-main>
        </div>
    </div>
    <div v-else>
        <unauthorised-prompt></unauthorised-prompt>
    </div>
</template>

<script>
    import SearchDestinations from './searchDestinationForm.vue'
    import AddDestinations from './addDestinations.vue'
    import NavBarMain from '../helperComponents/navbarMain.vue'
    import FooterMain from '../helperComponents/footerMain.vue'
    import UnauthorisedPrompt from '../helperComponents/unauthorisedPromptPage'
    import DestinationSidebar from "./destinationSidebar";
    import SingleDestination from "./singleDestination";
    import GoogleMap from "../map/googleMap";

    export default {
        name: "destinationsPage",
        props: {
            profile: Object,
            destinations: Array,
            destinationTypes: Array,
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
        components: {
            SingleDestination,
            DestinationSidebar,
            SearchDestinations,
            AddDestinations,
            NavBarMain,
            FooterMain,
            UnauthorisedPrompt,
            GoogleMap
        },
        mounted() {
        },
        data: function () {
            return {
                searchDestinations: true,
                addDestinations: false,
                selectedDestination: {},
                refreshDestinationData: 0,
                refreshSingleDestination: 0,
                dismissSecs: 3,
                dismissCountDown: 0
            }
        },
        methods: {
            /**
             * Switches between tabs.
             *
             * @param viewPage page to be displayed.
             */
            togglePage(viewPage) {
                if (!viewPage) {
                    this.searchDestinations = !this.searchDestinations;
                    this.addDestinations = !this.addDestinations;
                }
            },

            /**
             * Re-renders the destination search side panel and displays the edited destination on the page.
             *
             * @param refreshedDestination  the recently edited destination.
             */
            refreshDestinations(refreshedDestination) {
                this.refreshDestinationData += 1;
                this.selectedDestination = refreshedDestination;
            },


            /**
             * Re-renders the view single destination panel and shows a confirmation of deletion alert.
             */
            destinationDeleted() {
                this.selectedDestination = {};
                this.refreshSingleDestination += 1;
                this.showAlert();
            },


            /**
             * Used to allow an alert to countdown on the successful saving of a destination.
             *
             * @param dismissCountDown      the name of the alert.
             */
            countDownChanged(dismissCountDown) {
                this.dismissCountDown = dismissCountDown
            },


            /**
             * Displays the countdown alert on the successful saving of a destination.
             */
            showAlert() {
                this.dismissCountDown = this.dismissSecs
            }
        }
    }
</script>