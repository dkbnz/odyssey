<template>
    <div v-if="profile.length !== 0" :class="containerClass">
        <!--Shows tabs for destination page-->
        <nav-bar-main :profile="profile" v-if="!adminView"></nav-bar-main>
        <div class="containerMain">
            <h1 class="page-title">Destinations</h1>
            <p class="page-title">
                <i>Here you can add destinations, search destinations or view destinations on a map!</i>
            </p>
            <b-row>
                <b-col cols="8">
                    <b-card ref="maps">
                        <google-map :destinations="destinationsForMap" ref="map"
                                    v-if="showMap"
                                    :selected-destination="selectedDestination"
                                    :destination-to-add="destinationToAdd"
                                    @destination-click="destination => this.selectedDestination = destination">
                        </google-map>
                    </b-card>
                    <b-alert
                            :show="dismissCountDown"
                            @dismiss-count-down="countDownChanged"
                            @dismissed="dismissCountDown=0"
                            dismissible
                            variant="success">
                        <p>Destination Successfully Deleted</p>
                        <b-progress
                                :max="dismissSeconds"
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
                                :travTypeOptions="travTypeOptions"
                                :destination-types="destinationTypes"
                                :profile="profile"
                                @destination-saved="refreshDestinations"
                                @destination-deleted="destinationDeleted"
                                @destination-edit="destinationEdited">
                        </single-destination>
                    </b-card>
                </b-col>
                <b-col>
                    <b-card v-if="!destinationEdit">
                        <destination-sidebar
                                :profile="profile"
                                @destination-click="selectDestination"
                                @destination-search="destinationSearch"
                                :key="refreshDestinationData"
                                :input-destination="destinationToAdd"
                                @data-changed="$emit('data-changed')"
                        ></destination-sidebar>
                    </b-card>
                    <b-card v-else>
                        <add-destinations
                                :inputDestination="destinationToAdd"
                                :profile="profile"
                                :heading="'Edit'"
                                :destination-types="destinationTypes"
                                @destination-saved="destinationSaved">
                        </add-destinations>
                        <b-button @click="closeEditDestination" class="mr-3 buttonMarginsTop" block>Close</b-button>
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
                default: function () {
                    return null;
                }
            },
            adminView: {
                default: function () {
                    return false;
                }
            },
            travTypeOptions: Array
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

        data: function () {
            return {
                searchDestinations: true,
                addDestinations: false,
                selectedDestination: {},
                refreshDestinationData: 0,
                refreshSingleDestination: 0,
                dismissSeconds: 3,
                dismissCountDown: 0,
                destinationsForMap: [],
                showMap: false,
                destinationToAdd: {
                    id: null,
                    name: "",
                    type: {
                        id: null,
                        destinationType: ""
                    },
                    district: "",
                    latitude: null,
                    longitude: null,
                    country: "",
                    public: false
                },
                destinationEdit: false,
                destinationTemplate: {
                    id: null,
                    name: "",
                    type: {
                        id: null,
                        destinationType: ""
                    },
                    district: "",
                    latitude: null,
                    longitude: null,
                    country: "",
                    public: false
                }
            }
        },

        mounted() {
            this.delayMapLoad();
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
                this.refreshDestinationData += 1;
                this.closeEditDestination();
                this.showAlert();
            },


            /**
             * Delays the map loading to let the page load before the map component
             */
            delayMapLoad() {
                let self = this;
                let delay = 100;
                setTimeout(() => {
                    self.showMap = true;
                }, delay)
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
                this.dismissCountDown = this.dismissSeconds
            },


            /**
             * Changes the selected destination or focuses on current destination.
             *
             * @param destinationToSelect   the destination that has been selected on the map.
             */
            selectDestination(destinationToSelect) {
                if (destinationToSelect.id === this.selectedDestination.id) {
                    this.$refs['map'].focusOnSelectedDestination();
                } else {
                    this.selectedDestination = destinationToSelect;
                }
            },


            /**
             * Displays the edit destination form for the given destination.
             *
             * @param destination   the destination that is being edited.
             */
            destinationEdited(destination) {
                this.destinationsForMap = null;
                this.destinationToAdd = destination;
                this.destinationEdit = !this.destinationEdit;
            },


            /**
             * Replaces the edit destination form with the destination side bar if the close button is clicked.
             */
            closeEditDestination() {
                this.destinationEdit = false;
                this.destinationsForMap = [];
                let destination = this.destinationToAdd;
                this.destinationsForMap.push(destination);
                this.destinationToAdd = this.destinationTemplate;

            },


            /**
             * Changes the selected destination on the page to be the newly saved destination, so changes can be seen
             * straight away.
             *
             * @param destination   the recently saved destination.
             */
            destinationSaved(destination) {
                this.selectedDestination = destination;
            },


            /**
             * Resets the destinations to be displayed on the map when destinations are being searched.
             *
             * @param foundDestinations     the list of destinations to display on the map.
             */
            destinationSearch(foundDestinations) {
                this.destinationsForMap = foundDestinations;
                this.selectedDestination = {};
            }
        }
    }
</script>