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
                    <b-card>
                        Destination Map
                    </b-card>
                    <b-card
                            :header="selectedDestination.name"
                            style="margin-top: 10px">
                        <single-destination
                                :destination="selectedDestination"
                                :destination-types="destinationTypes"
                                :profile="profile">
                        </single-destination>
                    </b-card>
                </b-col>
                <b-col>
                    <b-card>
                        <destination-sidebar
                                :profile="profile"
                                :destinationTypes="destinationTypes"
                                @destination-click="destination => this.selectedDestination = destination"
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
    import SearchDestinations from './searchDestinations.vue'
    import AddDestinations from './addDestinations.vue'
    import NavBarMain from '../helperComponents/navbarMain.vue'
    import FooterMain from '../helperComponents/footerMain.vue'
    import UnauthorisedPrompt from '../helperComponents/unauthorisedPromptPage'
    import DestinationSidebar from "./destinationSidebar";
    import SingleDestination from "./singleDestination";

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
        },
        mounted() {
        },
        data: function () {
            return {
                searchDestinations: true,
                addDestinations: false,
                selectedDestination: {}
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
            }
        }
    }
</script>